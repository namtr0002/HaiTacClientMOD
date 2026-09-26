/**
 * ClientConfig - Quản lý định danh và phiên bản Client độc lập (J2ME)
 * Người dùng có thể tùy ý cấu hình CLIENT_ID, CLIENT_VERSION cho từng client riêng biệt.
 */
public class ClientConfig {

    // Mã định danh client riêng (Client ID) - có thể chỉnh theo từng client
    public static String CLIENT_ID = "CLIENT_HTTH_01";

    // Phiên bản client riêng (Client Version)
    public static String CLIENT_VERSION = "1.2.9";

    // Số hiệu bản build / No
    public static String CLIENT_BUILD_NO = "1";

    // Tên dòng client
    public static String CLIENT_LINE = "J2ME_CLIENT";

    // URL cấu hình danh sách máy chủ từ xa
    public static String SERVER_LIST_URL = "https://raw.githubusercontent.com/thachdeptrai/listIP/main/iphtth.txt";

    // Thời gian timeout kết nối nạp server (mili-giây)
    public static int CONNECTION_TIMEOUT = 5000;
}
