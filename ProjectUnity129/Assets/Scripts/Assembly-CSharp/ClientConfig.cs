/**
 * ClientConfig - Quản lý định danh và phiên bản Client độc lập (Unity C#)
 * Người dùng có thể tùy ý cấu hình CLIENT_ID, CLIENT_VERSION cho từng client riêng biệt.
 */
public class ClientConfig
{
    // Mã định danh client riêng (Client ID) - có thể chỉnh theo từng client
    public static string CLIENT_ID = "HaiTacZ";

    // Phiên bản client riêng (Client Version)
    public static string CLIENT_VERSION = "1.2.9";

    // Số hiệu bản build / No
    public static string CLIENT_BUILD_NO = "1";

    // Tên dòng client
    public static string CLIENT_LINE = "UNITY_CLIENT";

    // URL cấu hình danh sách máy chủ từ xa
    public static string SERVER_LIST_URL = "https://raw.githubusercontent.com/namtr0002/HaiTacClientMOD/main/data/ip_servers_unity.txt";
    public static string FALLBACK_SERVER_LIST_URL = "https://raw.githubusercontent.com/namtr0002/HaiTacClientMOD/main/data/ip_servers.txt";

    // Thời gian timeout kết nối nạp server (mili-giây)
    public static int CONNECTION_TIMEOUT = 5000;
}
