//
// iOSPluginsBridge.mm
// Native iOS implementation bridge for HaiTac TiHon
// Fixes 'Undefined symbols for architecture arm64' during Xcode linking
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <MessageUI/MessageUI.h>

extern "C" {

    void _SMSsend(const char* tophone, const char* withtext, int n)
    {
        NSString *phone = tophone ? [NSString stringWithUTF8String:tophone] : @"";
        NSString *body = withtext ? [NSString stringWithUTF8String:withtext] : @"";
        NSLog(@"[iOSPlugins] _SMSsend called: phone=%@, body=%@, n=%d", phone, body, n);
        
        dispatch_async(dispatch_get_main_queue(), ^{
            if ([MFMessageComposeViewController canSendText]) {
                MFMessageComposeViewController *controller = [[MFMessageComposeViewController alloc] init];
                controller.body = body;
                if (phone.length > 0) {
                    controller.recipients = @[phone];
                }
                UIViewController *rootVC = [UIApplication sharedApplication].keyWindow.rootViewController;
                if (rootVC) {
                    [rootVC presentViewController:controller animated:YES completion:nil];
                }
            } else {
                NSLog(@"[iOSPlugins] Device cannot send SMS");
            }
        });
    }

    int _unpause()
    {
        NSLog(@"[iOSPlugins] _unpause called");
        return 0;
    }

    int _checkRotation()
    {
        UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
        if (UIInterfaceOrientationIsLandscape(orientation)) {
            return 1;
        }
        return 0;
    }

    int _back()
    {
        NSLog(@"[iOSPlugins] _back called");
        return 0;
    }

    int _Send()
    {
        NSLog(@"[iOSPlugins] _Send called");
        return 0;
    }

    void _purchaseItem(const char* itemID, const char* userName, const char* gameID)
    {
        NSString *item = itemID ? [NSString stringWithUTF8String:itemID] : @"";
        NSString *user = userName ? [NSString stringWithUTF8String:userName] : @"";
        NSString *game = gameID ? [NSString stringWithUTF8String:gameID] : @"";
        NSLog(@"[iOSPlugins] _purchaseItem called: itemID=%@, userName=%@, gameID=%@", item, user, game);
    }

}
