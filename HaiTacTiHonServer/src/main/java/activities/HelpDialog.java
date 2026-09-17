package activities;

import model.Player;
import network.Service;

import java.io.IOException;

public class HelpDialog {
    public static void show_LangCoiXayGio(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Đăng ký tài khoản\r\n"
                        + "Nếu bạn là tài khoản chơi mới, hãy luyện đến Level 4 và đến gặp Trưởng làng để đăng ký tài khoản.\b"
                        + "Nhập vào Tên tài khoản ( Email hoặc là số điện thoại) và mật khẩu từ 6 đến 12 ký tự.\b"
                        + "Chúng tôi sẽ trả về thông báo xác nhận việc đăng ký của bạn đã hoàn tất hay chưa.";
                break;
            }
            case 1: {
                text = "Làm nhiệm vụ tân thủ\r\n"
                        + "Đến gặp Trưởng làng để làm nhiệm vụ tân thủ. Sau đó hãy đến gặp từng người trong làng để xem họ cần giúp đỡ gì không.\b"
                        + "Những người có dấu ! Trên đầu nghĩa là họ đang cần bạn giúp đỡ đấy.\b"
                        + "Hoàn thành nhiệm vụ là cách nhanh nhất giúp bạn thăng tiến sức mạnh.";
                break;
            }
            case 2: {
                text = "Cửa hàng thực phẩm\r\n"
                        + "Hãy đến gặp Machiko để mua HP, MP, các loại Rương Báu và các vật phẩm cần thiết.\b"
                        + "Shop thực phẩm có mặt hầu hết tất cả các làng hãy chú ý đến biểu tượng trên đầu của Machiko đó là dấu hiệu nhận "
                        + "ra người bán thực phẩm.\b"
                        + "Nhớ là phải luôn chuẩn bị đủ HP và MP trong mọi chuyến phiêu lưu. Rương báu là cách dễ nhất giúp bạn có được"
                        + "các trang bị tốt hãy nhớ lấy.";
                break;
            }
            case 3: {
                text = "Vận chuyển hàng\r\n"
                        + "Bạn muốn có nhiều Beri, hãy đến gặp Machiko và giúp cô ấy vận chuyển hàng khi đạt cấp 15.\b"
                        + "Bạn cần đăng ký chức năng Lái buôn để vận chuyển hàng. Sau khi đăng ký, hãy Lấy hàng và dẫn"
                        + "Lạc đà đi qua các Làng khác.\b"
                        + "Bạn càng đi qua nhiều Làng thì số tiền nhận được sẽ nhiều hơn. Hãy chú ý các Hải tặc luôn sẵn sàng cướp hàng của bạn.";
                break;
            }
            case 4: {
                text = "Trang bị\r\n"
                        + "Hãy đến gặp Guru để mua sắm trang bị. Cũng như shop thực phẩm thì cửa hàng trang bị có ở hầu hết các làng bạn đi đến.";
                break;
            }
            case 5: {
                text = "Kỹ năng \r\n"
                        + "Hãy đến gặp Gap, ông ta sẽ dạy cho bạn thêm nhiều kỹ năng mới và giúp bạn tẩy điểm tiềm năng.\b"
                        + "Gap cũng sẽ theo bạn trong các chuyến phiêu lưu và lưu ở tại các làng bạn đến.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_ThiTranVoSo(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Bảng xếp hạng\n" +
                        "Cậu bé Cobi đang giữ danh sách những cao thủ, hãy đến xem nếu bạn có hứng thú.\b" +
                        "Ngoài bảng xếp hạng cao thủ còn có bảng xếp hạng của các băng hải tặc nữa đấy.";
                break;
            }
            case 1: {
                text = "Nhiệm vụ hàng ngày\n" +
                        "Namie luôn có những thử thách cho bạn mỗi ngày, hãy đến gặp cô ấy để xem.\b" +
                        "Thành tích sẽ luôn được tính và bạn chỉ cần đến gặp cô ấy để báo khi đã hoàn thành thôi.\b" +
                        "Phần thưởng cũng vô cùng hấp dẫn, đừng bỏ lỡ.";
                break;
            }
            case 2: {
                text = "Cường hoá trang bị\n" +
                        "Hãy đến gặp Johny, anh ta sẽ dạy bạn cách thực hiện.\b" +
                        "Trang bị sau khi cường hoá sẽ mạnh hơn, cấp cường hoá càng cao thì sức mạnh của trang bị càng cao.\b" +
                        "Để cường hoá bạn cần có Nguyên liệu, bạn có thể mua tại Cửa hàng của Johny hoặc tách những trang bị không dùng đến để lấy Nguyên liệu.\b" +
                        "Ngôi sao may mắn sẽ tăng tỉ lệ thành công khi bạn Cường hoá. Mai rùa sẽ giúp bạn giữ nguyên cấp Cường hoá nếu như bạn Cường hoá thất bại.";
                break;
            }
            case 3: {
                text = "Khảm đá\n" +
                        "Johny sẽ dạy bạn cách Khảm đá, hãy đến gặp anh ta nếu như bạn muốn mạnh hơn. " +
                        "Đá khảm có nhiều loại, mỗi loại lại có những đặc trưng riêng.\b" +
                        "Để có đá khảm, bạn có thể mua tại Cửa hàng của Johny, làm nhiệm vụ lặp hoặc mở Rương huyền bí.\b" +
                        "Đá khảm có 6 cấp độ, cần 3 viên cùng loại để ghép thành 1 viên có cấp cao hơn. Mỗi trang bị sẽ có tối đa 4 lỗ khảm, " +
                        "hãy đục thêm lỗ khảm nếu như bạn muốn khảm nhiều đá hơn vào trang bị.";
                break;
            }
            case 4: {
                text = "Chuyển hoá\n" +
                        "Bạn có những trang bị có cấp cường hoá cao, muốn chuyển cấp cường hoá cho những trang bị khác có Level cao hơn, " +
                        "hãy đến gặp Johny, anh ấy sẽ giúp bạn.\b" +
                        "Trang bị muốn chuyển phải có cấp cường hoá lớn hơn hoặc bằng 6.\n" +
                        " Trang bị nhận phải có cấp cường hoá nhỏ hơn hoặc bằng 5.\b" +
                        "Khi chuyển hoá vẫn sẽ có tỉ lệ rớt cấp cường hoá, bạn nên chú ý. Trang bị sau khi chuyển hoá sẽ không bị mất, " +
                        "chỉ chuyển hoá cấp cường hoá từ trang bị này sang trang bị khác.";
                break;
            }
            case 5: {
                text = "Phó bản săn boss\n" +
                        "Hãy đến gặp Zosaku, anh ta sẽ đưa bạn đến những phó bản vô cùng cam go, tuy nhiên phần thưởng nhận được cũng không hề nhỏ.\b" +
                        "Để có thể vào Phó bản săn boss, bạn cần tạo nhóm ít nhất 2 người. Mỗi người cần có 1 Chia khoá phó bản.\b" +
                        "Sau khi tạo nhóm, bạn hãy chọn cho mình 1 Boss vừa sức và đánh bại nó. Khi hạ được Boss, bạn sẽ nhận được phần thưởng hấp dẫn.";
                break;
            }
            case 6: {
                text = "Đấu Boss liên tầng\n" +
                        "Để có thể vào Đấu Boss liên tầng, bạn cần tạo nhóm ít nhất 2 người. Mỗi người cần có 2 Chìa khoá phó bản.\b" +
                        "Sau khi tạo nhóm, hãy đến gặp Zosaku, anh ta sẽ đưa bạn và đồng đội vào.\b" +
                        "Tại đây sẽ có 7 tầng, mỗi tầng sẽ có 2 phút để chinh phục, bạn cần đánh chết hết quái và Boss để có thể lên tầng " +
                        "tiếp theo. Sau khi qua mỗi tầng đều có phần thưởng.";
                break;
            }
            case 7: {
                text = "PvP\n" +
                        "Những cuộc chiến nãy lửa sẽ diễn ra tại đấu trường PvP này. Zosaku sẽ đưa bạn vào. " +
                        "Để có thể đăng ký PvP bạn cần tốn 1 vé PvP.\bCả 2 sẽ có 3 phút để phân thắng bại, người thắng trước 5 hiệp đấu sẽ thắng. " +
                        "Sau 3 phút, ai có số bàn thắng nhiều hơn sẽ thắng cuộc.\b" +
                        "Sau mỗi tháng sẽ tổng kết người có số điểm PvP cao nhất để trao giải và phần thưởng thì cũng không hề nhỏ đâu.";
                break;
            }
            case 8: {
                text = "Khoá bảo vệ\n" +
                        " Hãy đảm bảo đồ đạc, trang bị ... của bạn được bảo quản an toàn bằng cách đến gặp các NPC Bán trang bị trong các " +
                        "Làng để tiến hành đặt Khoá bảo vệ\b Khi đăng ký Khoá bảo vệ bạn cần nhập mật khẩu bảo vệ từ 4 đến 6 kí tự\b" +
                        "Bước nhập ngày sinh rất quan trọng, bạn nên nhập đúng ngày sinh của mình để có thể lấy lại mật khẩu bảo vệ trong " +
                        "trường hợp quên mật khẩu bảo vệ\b Trong trường hợp bạn quên mật khẩu bảo vệ và ngày sinh thì hãy dùng chức năng " +
                        "Xoá mật khẩu bảo vệ, hệ thống sẽ xoá mật khẩu bảo vệ sau 5 ngày";
                break;
            }
            case 9: {
                text = "Nạp tiền\n" +
                        "Khi bạn cần có thêm nhiều Ruby, hãy đến gặp Namie, cô ấy sẽ giúp bạn.\b Khi nạp tiền bạn sẽ nhận được " +
                        "1 phần ruby để mua các vật phẩm trong trò chơi\n" +
                        " 1 phần Extol để mua bán đồ trong chợ.\b Bạn có thể đổi từ Extol ra ruby tại Namie với tỉ giá là 1000Extol = 1 Ruby.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_ThiTranOrange(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Chợ mua bán\n" +
                        "Chỉ duy nhất tại thị trấn này sẽ diễn ra những phiên chợ mua bán vật phẩm.\b " +
                        "Hãy đến gặp Namie, cô ấy sẽ đưa bạn đến tham quan khu vực chợ và các vật phẩm được bày bán. " +
                        "Bạn có thể bán những trang bị không dùng đến để kiếm thêm thu nhập.\b " +
                        "Để có thể bày bán cũng như mua các vật phẩm được bày bán tại chợ, bạn cần có Extol. " +
                        "Extol chỉ có được khi bạn nạp vào từ thẻ cào với 1000vnd bạn sẽ nhận được 1000 Extol.\b " +
                        "Các trang bị sau khi mặc lên người sẽ bị Khoá, và bạn không thể đăng bán trang bị khoá .\b" +
                        " Bạn cũng có thể đổi Extol sang Ruby với giá trị quy đổi: 1000 Extol = 1 Ruby ở chổ Namie để dùng cho các chức năng khác.";
                break;
            }
            case 1: {
                text = "Ṿng xoay kho báu\n" +
                        " Kho báu của biển Đông này chỉ có mình Buggi là người có bản đồ thôi.\b Hãy mua thẻ vòng quay tại các cửa hàng bán thực phẩm để có thể trải nghiệm vòng quay. Ngoài ra bạn cũng có thể mua vé tại chính Buggi với gói 3 vé.\b Khi tham gia vòng xoay bạn sẽ ngẫu nhiên nhận được các vật phẩm giá trị mà không thể mua được ở bất cứ đâu.\n" +
                        "Đá hải thạch\n" +
                        " Rương siêu ma thuật\n" +
                        " Rương cam";
                break;
            }
            case 2: {
                text = "Hoàn mỹ\n" +
                        " Trang bị chưa được hoàn mỹ cũng giống như ngọc quý chưa qua mài dũa không thể sáng bóng được.\b " +
                        "Hãy đến Buggi chọn vào Hoàn mỹ - Kích Ẩn để có thể hoàn mỹ món đồ bạn cần. " +
                        "Để có thể thực hiện thao tác hoàn mỹ bạn cần có đá Hải thạch và mỗi cấp đá lại có tỉ lệ thành công khác nhau.\b " +
                        "Nếu hoàn mỹ thành công món đồ của bạn sẽ tăng 10% giá trị gốc. " +
                        "Mỗi lần đập hoàn mỹ món đồ sẽ mất đi 1 số điểm chế tác nhất định phụ thuộc vào cấp đá hải thạch bạn bỏ vào.";
                break;
            }
            case 3:
            case 4:
            case 5:
            case 6: {
                text = "Kích ẩn\n" +
                        " Mỗi trang bị điều ẩn chứa trong mình một thuộc tính ẩn cần được khai mở.\b " +
                        "Hãy đến  để ông ấy giúp bạn làm việc đó. Đá Hải thạch là nguyên liệu không thể thiếu để kích ẩn. " +
                        "Ứng với mỗi cấp đá bạn sẽ có tỉ lệ thành công khác nhau cũng như số điểm chế tác hao tồn mỗi lần kích cũng khác nhau.\b " +
                        "Khi kích ẩn thành công thì món trang bị đó sẽ mạnh hơn rất nhiều đấy. " +
                        "Các thuộc tính ẩn khi thành công bạn hãy xem trong mục Thuộc tính kích ẩn nha. " +
                        "Mỗi thuộc tính chỉ cộng dồn tối đa 3 lần trong tất cả trang bị.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_LangSiRup(Player p, byte index) throws IOException {
        String text = "Cường hoá ác quỷ\n" +
                "Đây là ngôi làng duy nhất bạn có thể Cường hoá ác quỷ.\b " +
                "Khi bạn đạt đến Level 30, tham gia các hoạt động, phó bản trên game sẽ nhận được Tinh thể ác quỷ. " +
                "Bạn cũng có thể mua Đá ác quỷ từ Johny trong cửa hàng cường hoá.\b " +
                "Bạn có thể Cường hoá Rương ác quỷ lên Rương đại ác quỷ để có cơ hội mở ra được trái ác quỷ cao cấp.\b " +
                "Bạn có thể Cường hoá chiêu thức tấn công để mạnh hơn, kể cả chiêu thức từ trái ác quỷ.";
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_Barate(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_ThuyenBarati(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Băng hải tặc\n" +
                        " Ra biển một mình tất sự không phải là ý hay và cuộc phiêu lưu sẽ thêm phần thú vị nếu có bạn bè bên cạnh.\b " +
                        "Hãy đến gặp Mihao nếu như bạn có dự định thành lập 1 Băng hải tặc, làm nhiệm vụ Băng hay tham gia phó bản Băng. \b " +
                        "Khi tham gia vào Băng bạn có thể nhận nhiệm vụ Băng để giúp bản thân và các đồng đội mạnh hơn..\b " +
                        "Có rất nhiều phó bảng mà chỉ dành riêng cho các Băng thứ tài với nhau. " +
                        "Đồng thời Băng hải tặc còn giúp bạn tăng thêm diểm tiềm năng cùng rất nhiều vật phẩm hổ trợ nữa.";
                break;
            }
            case 1: {
                text = "Phó bảng băng\n" +
                        " Nơi tranh tài của các băng hải tắc để khẳng định sức mạnh đồng đội.\b " +
                        "Hãy tham gia Băng rồi sau đó củng đồng đội mình tham gia phó bảng. " +
                        "Với yêu cầu có ít nhất 5 người trong băng đồng ý tham gia. Thời gian mở cửa là từ 11h đến 15h và 19h đến 23h hằng ngày.\b " +
                        "Sẽ có nhiều chế độ thi đấu khác nhau cũng như nhiều khu vực thi đấu khác nhau thay đổi mỗi ngày. \b " +
                        "Hãy cùng đồng đội mình tham gia thôi nào.";
                break;
            }
            case 2: {
                text = "Phó bảng khổng lồ Lv 5x trở xuống\n" +
                        " Cuộc phiêu lưu nào cũng có rất nhiều sự bất ngờ.\b " +
                        "Và cuộc gặp gỡ 2 người khổng lồ sứ Elbaf là 1 điều bất ngờ hơn cả những bất ngờ. \b" +
                        "Cuộc chiến giữa 2 người bọn họ mãi không có hồi kết nên bạn và các đồng đội của mình hãy giúp 1 tay đi nào.\b " +
                        "Cùng ít nhất 4 người đồng đội trong Băng tham gia phó bảng, hạ gục các con quái để mang về bình rượu hoặc mảng thịt " +
                        "cho các người khổng lồ. \b Có 1 con quái đặt biệt mang trên nó 1 vật phẩm đặt biệt không kém mà bạn sẽ có được khi hạ gục nó.  " +
                        "Vậy còn chờ gì nữa mà không cùng tham gia nào.";
                break;
            }
            case 3: {
                text = "Bảo vệ pháo đài\n" +
                        "Thuyền trưởng hoặc Thuyền phó đến gặp NPC Mihao để tiến hành đăng ký và  " +
                        "hệ thống sẽ tìm Băng đối thủ xứng tầm trong chóc lát.\bVào trận  tất cả thành viên của cả 2 Băng sẽ đều tập trung " +
                        "ở khu căn cứ riêng của Băng đó (còn được gọi là Làng đỏ và Làng xanh)\b" +
                        "Hãy chuẩn bị chiến thuật hợp lý để thành viên trong Băng tiến hành di chuyển sang Đường trên, " +
                        "Đường dưới và Đường giữa để phá huỷ các Trụ\bCứ sau 2 phút thì HP của Trụ sẽ giảm 20 HP\n" +
                        "Khi phá huỷ trụ trên sẽ tăng 20% dame cho tất cả đồng đội\n" +
                        "Khi phá huỷ trụ dưới sẽ tăng 10% hồi máu cho tất cả đồng đội\b" +
                        "Lưu ý: Khi cả 2 Trụ thường đều bị phá huỷ thì mới có thể đánh Trụ chính\n" +
                        "Trụ thường sẽ hồi sinh sau 2 phút\bỞ phút thứ 5 và thứ 10 sẽ xuất hiện các Siêu Boss ở gần vị trí các Trụ, " +
                        "hãy tiêu diệt chúng sẽ nhận thêm hỗ trợ cho toàn đội\bBăng nào phá huỷ được Trụ chính của đối phương trước sẽ thắng cuộc\n" +
                        "Nếu cả 2 Trụ chính đều không bị phá huỷ thì Băng nào phá huỷ được nhiều Trụ thường nhất sẽ thắng cuộc .";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_LangHatDe(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Bảo vệ kho báu Namie\n" +
                        "Đây là ngôi làng mà Namie đã sinh ra và lớn lên, tại đây cô ta có cất giữ rất nhiều kho báu, " +
                        "hãy giúp cô ấy bảo vệ kho báu của mình khỏi tay bọn hải quân.\b Hãy đến gặp Zosaku, " +
                        "anh ta sẽ đưa bạn đến Kho báu Namie. Nhưng nhớ là hãy tạo nhóm trước khi vào.\b " +
                        "Phó bản gồm có 20 tầng, độ khó sẽ tăng dần sau mỗi tầng. Những phần thưởng vô cùng hấp dẫn sẽ thuộc " +
                        "về bạn nếu bạn bảo vệ kho báu thành công.";
                break;
            }
            case 1: {
                text = "Siêu Boss\n" +
                        "Vào lúc 18h đến 20h hằng ngày sẽ xuất hiện Siêu Boss tại khu 5, map 6-1 Chợ dừa.\b" +
                        "Hãy chung tay tiêu diệt Siêu Boss để nhận về những phần thưởng cực kỳ giá trị.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_ThiTranKhoiDau(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Lệnh truy nã\n" +
                        "Đây là thị trấn duy nhất bạn có thể tham gia truy nã.\b " +
                        "Hãy đến gặp Zosaku, anh ta sẽ đưa bạn đến đấu trường truy nã. Hãy thử tìm kiếm và truy nã một ai đó, " +
                        "nếu thắng bạn sẽ nhận được 10.000 Beri truy nã + 1% số tiền truy nã của đối phương.\b " +
                        "Ngược lại, nếu thất bại bạn sẽ bị trừ 5.000 Beri truy nã + 1% số tiền truy nã của bản thân.\b " +
                        "Sau khi thắng liên tiếp 3 trận hoặc  sau khi thua 1 trận,,bạn phải chờ 20phút để tiếp tục Truy nã, " +
                        "có thể dùng Ruby để huỷ thời gian chờ\b Cứ mỗi khi bạn chiến thắng, bạn sẽ nhận ngẫu nhiên 1 Rương Truy nã. " +
                        "Mở Rương Truy nã sẽ nhận nhiều phần thưởng vô cùng hấp dẫn.\b " +
                        "Lưu ý nếu bạn thuộc top truy nã, nhân vật của bạn 3 ngày không đăng nhập vào game thì mỗi ngày sẽ bị trừ 1% số điểm Truy nã " +
                        "đến khi bạn đăng nhập lại.";
                break;
            }
            case 1: {
                text = "Siêu Boss\n" +
                        "Vào lúc 18h đến 20h hằng ngày sẽ xuất hiện Siêu Boss tại khu 5, map 6-1 Chợ dừa.\b" +
                        "Hãy chung tay tiêu diệt Siêu Boss để nhận về những phần thưởng cực kỳ giá trị.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_MomSinhDoi(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Vượt Redline\n" +
                        "Núi đảo nghịch nơi dòng nước biển chạy thẳng lên núi thật kì diệu.\b " +
                        "Việc của bạn cần làm là vượt Redline 50 lần, sau khi hoàn thành bạn sẽ nhận được 1 trái ác quỷ\b " +
                        "Hãy đến gặp Crocus để nhận nhiệm vụ. Mỗi người chỉ nhận được 1 lần thôi nhé.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_ThiTranWhiskey(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Trái ác quỷ\n" +
                        "Loại quả bí ẩn nhất trong thế giới hải tặc. Nó được xem là lời nguyền của biển cả. " +
                        "Bởi nếu ai ăn nó rồi sẽ không thể bơi được nữa.\b Cũng như mất hết sức lực nếu chạm đến mọi thử của biển. " +
                        "Nhưng lời nguyền hay phép màu thì chỉ có bạn mới có thể khẳng định thôi.\b" +
                        "Tại thị trấn này có bán những Rương ác quỷ vô cùng quý báu. Hãy đến gặp Mr Acrobatic để mua những Rương ác quỷ. " +
                        "Mở rương ác quỷ bạn sẽ nhận được trái ác quỷ ngẫu nhiên. Mỗi trái ác quỷ đều ẩn chứa bên trong sức mạnh vô cùng to lớn.";
                break;
            }
            case 1: {
                text = "Đấu trường tự do\n" +
                        " Vào lúc 20h30 đến 22h hằng ngày, các khu luyện quái từ khu 1 đến khu 9 sẽ trở thành 1 đấu trường thực thụ.\b " +
                        "Khi bạn ra ngoài các khu vực luyện quái sẽ được chọn ngẫu nhiên 1 trong 2 màu cờ PK là xanh lục và đỏ. Hãy hạ thật nhiều đối " +
                        "thủ để trở thành người chiến thắng.";
                break;
            }
            case 2: {
                text = "Siêu Boss\n" +
                        "Vào lúc 18h đến 20h hằng ngày sẽ xuất hiện Siêu Boss tại khu 5, map 6-1 Chợ dừa.\b" +
                        "Hãy chung tay tiêu diệt Siêu Boss để nhận về những phần thưởng cực kỳ giá trị.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_DaoLittleGrand(Player p, byte index) throws IOException {
        String text = "Mục này chưa có hướng dẫn, hãy hỏi admin nhé";
        switch (index) {
            case 0: {
                text = "Phó bản Mr Candle\n" +
                        "Nhận được lệnh của Mr Sand, bộ đôi Mr Candle và Miss Paint đã đến hòn đảo Little Garden. " +
                        "Hãy cùng nhau đánh bại họ.\b Hãy đến gặp Miss All Sunday, cô ta sẽ đưa bạn gặp Mr Candle và Miss Paint. " +
                        "Với sức mạnh của bộ đôi Mr Candle, bạn cần phải tạo nhóm ít nhất 3 người mới có thể vào tham chiến.\b " +
                        "Rất nhiều điểm kinh nghiệm và Beri sẽ được tặng sau khi bạn đánh bại cả 2 người họ.";
                break;
            }
            case 1: {
                text = "Phó bảng khổng lồ Lv 6x trở lên\n" +
                        " Cuộc phiêu lưu nào cũng có rất nhiều sự bất ngờ.\b Và cuộc gặp gỡ 2 người khổng lồ sứ Elbaf là 1 " +
                        "điều bất ngờ hơn cả những bất ngờ. \bCuộc chiến giữa 2 người bọn họ mãi không có hồi kết nên bạn và " +
                        "các đồng đội của mình hãy giúp 1 tay đi nào.\b Cùng ít nhất 4 người đồng đội trong Băng tham gia phó bảng, " +
                        "hạ gục các con quái để mang về bình rượu hoặc mảng thịt cho các người khổng lồ. \b " +
                        "Có 1 con quái đặt biệt mang trên nó 1 vật phẩm đặt biệt không kém mà bạn sẽ có được khi hạ gục nó.  " +
                        "Vậy còn chờ gì nữa mà không cùng tham gia nào.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_LinhDanhThue(Player p, byte index) throws IOException {
        String text = "";
        switch (index) {
            case 0: {
                text = "Tổng quan Lính Đánh Thuê\n" +
                        "Lính Đánh Thuê là các chiến binh dũng mãnh gồm 3 Phe: Hải Quân, Hải Tặc, Quân Cách Mạng.\b" +
                        "Lính sở hữu trang bị Hoàn Mỹ, khảm đá cao cấp, kích ẩn và Trái Ác Quỷ cực mạnh.\b" +
                        "Bạn có thể thuê lính tại NPC Johny ở Thị Trấn Vỏ Sò hoặc qua menu Lính Đánh Thuê.";
                break;
            }
            case 1: {
                text = "Hợp đồng & Xuất chiến\n" +
                        "Mỗi người chơi có thể ký tối đa 10 Hợp đồng lính đánh thuê cùng lúc.\b" +
                        "Tối đa 3 lính có thể Xuất chiến cùng lúc để bảo vệ hoặc tàn sát quái theo bạn.\b" +
                        "Dữ liệu lính được lưu bền vững vào máy chủ, không lo mất khi rời mạng hay đổi máy.";
                break;
            }
            case 2: {
                text = "Chế độ hành vi của Lính\n" +
                        "Có 4 chế độ hành vi linh hoạt:\n" +
                        "- Đi theo: Chỉ đi theo sau chủ nhân, không tấn công quái.\n" +
                        "- Bảo vệ: Bảo vệ chủ nhân trong phạm vi 150px, phản công quái áp sát.\n" +
                        "- Tấn công: Tự động di chuyển săn lùng và tiêu diệt quái trên toàn bản đồ.\n" +
                        "- Về nhà / Rút về: Ẩn toàn bộ lính khỏi bản đồ khi không cần thiết.";
                break;
            }
            case 3: {
                text = "Kinh nghiệm & Vật phẩm rơi\n" +
                        "Khi lính đánh thuê tiêu diệt quái, chủ nhân sẽ nhận được 60% EXP chiến đấu.\b" +
                        "Tất cả Beri và trang bị rơi ra từ quái đều tự động thuộc quyền sở hữu của người thuê.\b" +
                        "Lính là trợ thủ đắc lực giúp bạn cày cấp và farm đồ nhanh chóng vượt trội!";
                break;
            }
            case 4: {
                text = "Trái Ác Quỷ & Phẩm cấp\n" +
                        "- Thuê bằng Extol: Trang bị Trái Ác Quỷ Bá Vương (Magma, Băng, Nika, Rồng) cấp quỷ 5, thời gian 3-8 tiếng.\n" +
                        "- Thuê bằng Ruby: Trang bị Trái Ác Quỷ Thượng cấp (Lửa, Khói, Cát, Sét, Mochi) cấp quỷ 3, thời gian 2-6 tiếng.\n" +
                        "- Thuê bằng Beri: Trang bị Trái Ác Quỷ Phổ thông (Lò xo, Trọng lượng, Bộc phá) cấp quỷ 1, thời gian 1.5-4 tiếng.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_DaoDrum(Player p, byte index) throws IOException {
        String text = "";
        switch (index) {
            case 0: {
                text = "Vương Quốc Mùa Đông Drum\n" +
                        "Vương quốc tuyết trắng vĩnh cửu, nơi sinh ra bác sĩ tuần lộc Chopper huyền thoại.\b" +
                        "Tại đây quái vật có sức chống chịu băng giá cao, người chơi cần chuẩn bị trang bị kháng phép tốt.";
                break;
            }
            case 1: {
                text = "Boss Wapol Phàm Ăn\n" +
                        "Tên vua bạo chúa Wapol sở hữu năng lực Trái Baku Baku no Mi, có khả năng nuốt chửng mọi thứ để biến đổi cơ thể.\b" +
                        "Hãy tập hợp đồng đội để hạ gục hắn và giải cứu vương quốc tuyết!";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_Alabasta(Player p, byte index) throws IOException {
        String text = "";
        switch (index) {
            case 0: {
                text = "Vương Quốc Sa Mạc Alabasta\n" +
                        "Vùng đất sa mạc rộng lớn thuộc Đại Hải Trình với khí hậu khắc nghiệt.\b" +
                        "Cẩn thận với những cơn bão cát dữ dội và quái vật sa mạc hung hãn!";
                break;
            }
            case 1: {
                text = "Đại Trùm Cá Sấu Crocodile\n" +
                        "Cựu Thất Vũ Hải Crocodile - Thống lĩnh tổ chức Baroque Works sở hữu Trái Cát Suna Suna no Mi.\b" +
                        "Để đánh bại hắn, hãy chú ý tránh những đòn bão cát diện rộng và phối hợp kỹ năng nước/băng!";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_DaoTrenTroi(Player p, byte index) throws IOException {
        String text = "";
        switch (index) {
            case 0: {
                text = "Đảo Trên Trời Skypiea\n" +
                        "Hòn đảo huyền thoại ngự trị trên những tầng mây trắng xóa Bạch Hải.\b" +
                        "Nơi đây lưu giữ chuông vàng Shandora cổ đại và những kho báu vô giá từ ngàn xưa.";
                break;
            }
            case 1: {
                text = "Chúa Tể Sấm Sét Enel\n" +
                        "Enel tự xưng là Thần với sức mạnh hủy diệt của Trái Goro Goro no Mi.\b" +
                        "Các đòn sấm sét có tầm đánh cực xa và sát thương diện rộng khủng khiếp, hãy luôn di chuyển né tránh!";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_Water7(Player p, byte index) throws IOException {
        String text = "";
        switch (index) {
            case 0: {
                text = "Thành Phố Nước Water 7\n" +
                        "Thủ phủ của những thợ đóng tàu tài ba bậc nhất thế giới Galley-La.\b" +
                        "Nơi đóng nên những con tàu huyền thoại vươn ra mọi vùng biển nguy hiểm.";
                break;
            }
            case 1: {
                text = "Tổ chức ngầm CP9\n" +
                        "Các điệp viên sát thủ CP9 nắm giữ bí kỹ Lục Thức (Rokushiki) thượng thừa.\b" +
                        "Hãy rèn luyện kỹ năng chiến đấu đạt cấp độ cao để đối đầu cùng chúng!";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_Sabaody(Player p, byte index) throws IOException {
        String text = "";
        switch (index) {
            case 0: {
                text = "Quần Đảo Bong Bóng Sabaody\n" +
                        "Quần đảo tạo nên từ rễ của những cây đước khổng lồ Yarukiman Mangrove.\b" +
                        "Cửa ngõ quan trọng trước khi tiến vào Đảo Người Cá và Tân Thế Giới.";
                break;
            }
            case 1: {
                text = "Đấu Trường & Siêu Tân Tinh\n" +
                        "Nơi quy tụ của 11 Siêu Tân Tinh (Worst Generation) lừng danh.\b" +
                        "Cẩn trọng trước các trận chiến khốc liệt và sự truy quét gắt gao của Hải Quân Hoàng Gia!";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }

    public static void show_TongQuanTinhNang(Player p, byte index) throws IOException {
        String text = "";
        switch (index) {
            case 0: {
                text = "Hệ thống Cường hóa & Khảm đá\n" +
                        "- Cường hóa trang bị tại Johny để gia tăng chỉ số cơ bản.\n" +
                        "- Khảm đá vào lỗ trang bị để nhận thêm công, thủ, bạo kích, chính xác.\n" +
                        "- Ghép 3 viên đá cùng cấp để nâng cấp đá khảm lên cấp cao hơn.";
                break;
            }
            case 1: {
                text = "Hệ thống Hoàn Mỹ & Kích Ẩn\n" +
                        "- Dùng Đá Hải Thạch tại Buggi để Hoàn Mỹ trang bị tăng 10% chỉ số gốc.\n" +
                        "- Kích hoạt các dòng thuộc tính ẩn mạnh mẽ cho trang bị.\n" +
                        "- Mỗi thuộc tính ẩn cộng dồn tối đa 3 lần trên toàn bộ trang bị.";
                break;
            }
            case 2: {
                text = "Hệ thống Lính Đánh Thuê\n" +
                        "- Thuê trợ thủ Hải Quân, Hải Tặc, Cách Mạng tại NPC Johny.\n" +
                        "- Tối đa 10 hợp đồng, 3 lính xuất chiến cùng lúc.\n" +
                        "- Hưởng 60% EXP và toàn bộ vật phẩm quái rơi ra.";
                break;
            }
            case 3: {
                text = "Hệ thống Băng Hải Tặc & Hoạt động\n" +
                        "- Lập băng cùng bạn bè tại Mihao để cùng làm nhiệm vụ bang hội.\n" +
                        "- Tham gia Bảo Vệ Pháo Đài, Phó bản Khổng Lồ nhận quà giá trị.\n" +
                        "- Tham gia Đấu Trường Truy Nã và Săn Siêu Boss mỗi ngày.";
                break;
            }
        }
        p.getService().Help_From_Server(-997, text);
    }
}