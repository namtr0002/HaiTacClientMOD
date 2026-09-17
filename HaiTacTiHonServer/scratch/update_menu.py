import os

path = r'c:\DepLor\HTTH\Team\HaiTacTiHonServer\src\main\java\core\MenuController.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content_norm = content.replace('\r\n', '\n')

old_part = """                case 993: {

               switch (index) {

                   case 0: {

                       if (p.getConnStatus() != 1) {

                           p.getService().send_box_ThongBao_OK("Chưa Kích hoạt không thể đổi ");

                           return;

                       }

                       p.setyesNoDialog(new YesNoDialog(10, "Thông báo",

                               "Bạn muốn đổi 3000 ruby sang 2.000.000 extol?", new String[]{"Đồng ý", "Hủy"},

                               new byte[]{2, 1}, value -> {

                                   if (value == 0) { // Đồng ý

                                       if (p.get_ngoc() < 3_000) {

                                           p.getService().send_box_ThongBao_OK("Bạn không đủ 3.000 Ruby");

                                           return;

                                       }

                                       p.update_ngoc_ex(-3_000);

                                       p.updateVnd(2_000_000);

                                       p.updateMoney();

                                       p.getService().send_box_ThongBao_OK("Bạn đã đổi thành công 2.000.000 Extol.");

                                       zLog.gI().add_log(p, "Đổi extol");

                                   }

                               }));

                       p.getService().startYesNo();

                       break;

                   }"""

new_part = """                case 993: {
               switch (index) {
                   case 0: {
                       if (p.getConnStatus() != 1) {
                           p.getService().send_box_ThongBao_OK("Chưa Kích hoạt không thể đổi");
                           return;
                       }
                       p.sendInput("Đổi Coin ra Extol", new String[]{"Nhập số lượng Coin muốn đổi:"}, textInputs -> {
                           try {
                               long coinAmt = Long.parseLong(textInputs[0].trim());
                               if (coinAmt <= 0) {
                                   p.getService().send_box_ThongBao_OK("Số lượng Coin nhập phải lớn hơn 0!");
                                   return;
                               }
                               if (p.get_coin() < coinAmt) {
                                   p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(coinAmt) + " Coin (Hiện có: " + ZUtil.number_format(p.get_coin()) + ")!");
                                   return;
                               }
                               p.update_coin(-coinAmt);
                               p.updateVnd(coinAmt);
                               p.updateMoney();
                               p.getService().send_box_ThongBao_OK("Bạn đã đổi thành công " + ZUtil.number_format(coinAmt) + " Coin sang " + ZUtil.number_format(coinAmt) + " Extol (Tỉ giá: 1 Coin = 1 Extol).");
                               zLog.gI().add_log(p, "Đổi Coin sang Extol", "Đổi " + coinAmt + " Coin -> " + coinAmt + " Extol");
                           } catch (Exception e) {
                               p.getService().send_box_ThongBao_OK("Số nhập vào không hợp lệ!");
                           }
                       });
                       break;
                   }"""

if old_part in content_norm:
    content_norm = content_norm.replace(old_part, new_part)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content_norm.replace('\n', '\r\n'))
    print("SUCCESS: Updated case 993 in MenuController.java")
else:
    print("WARNING: old_part not found directly. Searching partial...")
    idx = content_norm.find("case 993:")
    if idx != -1:
        print(content_norm[idx:idx+400])
