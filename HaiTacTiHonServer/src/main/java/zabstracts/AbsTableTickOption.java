package zabstracts;

import model.Player;
import network.Message;
import map.Zone;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbsTableTickOption {
    private static final AtomicInteger nextFakeId = new AtomicInteger(1);
    private static final AtomicInteger nextRealId = new AtomicInteger(100);
    private static final Map<Short, AbsTableTickOption> activeDialogs = new ConcurrentHashMap<>();
    private static final Map<Integer, AbsTableTickOption> activeFakeDialogs = new ConcurrentHashMap<>();

    public int fakeId;
    public short idDialog;
    public List<Player> listP;
    public byte[] list_check;
    public boolean is_finish = false;
    public Player player;
    protected String key;

    public String getSeasonKey() {
        String baseKey = (key != null && !key.trim().isEmpty()) ? key : ("TableTickOption_" + idDialog);
        event.Event activeEvent = event.EventManager.gI().getActiveEvent();
        if (activeEvent != null && activeEvent.getSeasonKey() != null && !activeEvent.getSeasonKey().trim().isEmpty()) {
            return historys.HistoryManager.formatKey(baseKey, activeEvent.getSeasonKey());
        }
        return baseKey;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }
    
//    public AbsTableTickOption() {
//        this.fakeId = nextFakeId.getAndIncrement();
//        int realId = nextRealId.getAndIncrement();
//        if (realId > Short.MAX_VALUE) {
//            nextRealId.set(100);
//            realId = 100;
//        }
//        this.idDialog = (short) realId;
//    }
    
    public AbsTableTickOption(Player player) {
        this.fakeId = nextFakeId.getAndIncrement();
        int realId = nextRealId.getAndIncrement();
        if (realId > Short.MAX_VALUE) {
            nextRealId.set(100);
            realId = 100;
        }
        this.idDialog = (short) realId;
        this.player = player;
    }

    public static void register(AbsTableTickOption option) {
        activeDialogs.put(option.idDialog, option);
        activeFakeDialogs.put(option.fakeId, option);
    }

    public static void unregister(AbsTableTickOption option) {
        if (option != null) {
            activeDialogs.remove(option.idDialog);
            activeFakeDialogs.remove(option.fakeId);
        }
    }

    public static AbsTableTickOption get(short idDialog) {
        return activeDialogs.get(idDialog);
    }

    public static AbsTableTickOption getByFake(int fakeId) {
        return activeFakeDialogs.get(fakeId);
    }

    public void show(String title) throws IOException {
        register(this);

        // Tự động thêm các Bot Lính Đánh Thuê của tất cả người chơi thật trong danh sách vào bảng đăng ký phó bản
        if (this.listP == null) {
            this.listP = new ArrayList<>();
        }
        List<Player> realPlayers = new ArrayList<>();
        for (Player pMember : this.listP) {
            if (pMember != null && !pMember.isBot) {
                realPlayers.add(pMember);
            }
        }
        if (player != null && !player.isBot && !realPlayers.contains(player)) {
            realPlayers.add(player);
        }

        for (Player realP : realPlayers) {
            // 1. Tự động thêm Đệ tử chính thức của người chơi
            if (realP.detu != null && !realP.detu.isdie && realP.detu.detuStatus != model.DeTu.STATUS_HOME && realP.detu.detuStatus != model.DeTu.STATUS_FUSION) {
                model.DeTu dt = realP.detu;
                dt.clan = realP.clan;
                dt.typePirate = realP.typePirate;
                dt.type_pk = realP.type_pk;
                boolean alreadyAdded = false;
                for (Player pInList : this.listP) {
                    if (pInList != null && pInList.name != null && pInList.name.equals(dt.name)) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (!alreadyAdded) {
                    this.listP.add(dt);
                }
            }

            // 2. Tự động thêm Lính Đánh Thuê của người chơi
            List<bot.mercenary.MercenaryManager.HiredRecord> contracts = bot.mercenary.MercenaryManager.gI().getPlayerContracts(realP);
            if (contracts != null && !contracts.isEmpty()) {
                int activeCount = bot.mercenary.MercenaryManager.gI().countActive(contracts);
                if (activeCount == 0) {
                    int toActivate = Math.min(3, contracts.size());
                    for (int i = 0; i < toActivate; i++) {
                        bot.mercenary.MercenaryManager.HiredRecord r = contracts.get(i);
                        if (r != null && !r.isExpired()) {
                            r.isActive = true;
                        }
                    }
                }

                for (bot.mercenary.MercenaryManager.HiredRecord r : contracts) {
                    if (r != null && r.isActive && !r.isExpired()) {
                        if (r.bot == null) {
                            r.bot = bot.mercenary.MercenaryBot.create(realP, r.entry, r.currency);
                        }
                        if (r.bot != null && !r.bot.isdie) {
                            r.bot.clan = realP.clan;
                            r.bot.typePirate = realP.typePirate;
                            r.bot.type_pk = realP.type_pk;
                            boolean alreadyAdded = false;
                            for (Player pInList : this.listP) {
                                if (pInList != null && pInList.name != null && pInList.name.equals(r.bot.name)) {
                                    alreadyAdded = true;
                                    break;
                                }
                            }
                            if (!alreadyAdded) {
                                this.listP.add(r.bot);
                            }
                        }
                    }
                }
            }
        }

        if (this.listP != null) {
            if (this.list_check == null || this.list_check.length < this.listP.size()) {
                byte[] newCheck = new byte[this.listP.size()];
                if (this.list_check != null) {
                    System.arraycopy(this.list_check, 0, newCheck, 0, this.list_check.length);
                }
                this.list_check = newCheck;
            }
            // Tự động tick chấp nhận (auto accept) cho tất cả Bot, Đệ Tử và Lính Đánh Thuê trong danh sách
            for (int i = 0; i < this.listP.size(); i++) {
                Player pMember = this.listP.get(i);
                if (pMember != null && (pMember.isBot || pMember.isDe || pMember instanceof model.DeTu || pMember instanceof bot.mercenary.MercenaryBot)) {
                    this.list_check[i] = 1;
                    pMember.tableTickOption = this;
                }
            }
        }
        
        Message m = new Message(-74);
        m.writer().writeByte(0);
        m.writer().writeShort(this.idDialog);
        m.writer().writeUTF(title);
        m.writer().writeByte(this.listP.size());
        for (int i = 0; i < this.listP.size(); i++) {
            m.writer().writeShort(this.listP.get(i).index_map);
            m.writer().writeUTF(this.listP.get(i).name);
            m.writer().writeShort((short) (this.listP.get(i).map != null ? this.listP.get(i).map.template.id : 0));
        }
        for (int i = 0; i < this.listP.size(); i++) {
            Player p0 = Zone.get_player_by_name_allmap(this.listP.get(i).name);
            if (p0 == null) {
                p0 = this.listP.get(i);
            }
            if (p0 != null && !p0.isBot && !(p0 instanceof bot.mercenary.MercenaryBot) && p0.getService() != null) {
                p0.tableTickOption = this;
                p0.addmsg(m);
            }
        }
        m.cleanup();

        // Gửi gói tin đã chấp nhận (byte 1) cho từng Bot / Đệ Tử / Lính Đánh Thuê để client cập nhật tích xanh
        for (int i = 0; i < this.listP.size(); i++) {
            Player pMember = this.listP.get(i);
            if (pMember != null && (pMember.isBot || pMember.isDe || pMember instanceof model.DeTu || pMember instanceof bot.mercenary.MercenaryBot)) {
                Message mTick = new Message(-74);
                mTick.writer().writeByte(1);
                mTick.writer().writeShort(this.idDialog);
                mTick.writer().writeShort(pMember.index_map);
                for (Player recipient : this.listP) {
                    Player pRec = Zone.get_player_by_name_allmap(recipient.name);
                    if (pRec == null) pRec = recipient;
                    if (pRec != null && !pRec.isBot && !(pRec instanceof bot.mercenary.MercenaryBot) && pRec.getService() != null) {
                        pRec.addmsg(mTick);
                    }
                }
                mTick.cleanup();
            }
        }
    }

    public void processResponse(Player p, byte type) throws IOException {
        if (this.is_finish) return;
        
        if (type == 1) { // accept
            if (this.listP.get(0).name.equals(p.name)) {
                // Leader clicked Start! Thu thập tất cả người chơi và bot đã xác nhận (list_check == 1)
                List<Player> warpPlayers = new ArrayList<>();
                for (int i = 0; i < this.listP.size(); i++) {
                    if (this.list_check[i] == 1) {
                        Player p0 = this.listP.get(i);
                        if (p0 != null && (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot || p0.conn != null)) {
                            warpPlayers.add(p0);
                        }
                    }
                }
                
                if (warpPlayers.isEmpty()) {
                    p.getService().send_box_ThongBao_OK("Chưa có thành viên nào xác nhận tham gia!");
                    return;
                }
                
                this.is_finish = true;
                for (Player p0 : this.listP) {
                    p0.tableTickOption = null;
                }
                unregister(this);
                
                onAccept(p, warpPlayers);
            } else {
                // Member checked ready
                Message m = new Message(-74);
                m.writer().writeByte(1);
                m.writer().writeShort(this.idDialog);
                m.writer().writeShort(p.index_map);
                
                for (int i = 0; i < this.listP.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(this.listP.get(i).name);
                    if (p0 != null) {
                        p0.addmsg(m);
                    }
                    if (p.name.equals(this.listP.get(i).name)) {
                        this.list_check[i] = 1;
                    }
                }
                m.cleanup();
            }
        } else if (type == 2) { // huy
            if (this.listP.get(0).name.equals(p.name)) {
                // Trưởng nhóm huỷ -> Huỷ toàn bộ phòng chờ
                Message m = new Message(-74);
                m.writer().writeByte(3);
                m.writer().writeShort(this.idDialog);
                m.writer().writeShort(p.index_map);
                
                for (int i = 0; i < this.listP.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(this.listP.get(i).name);
                    if (p0 != null) {
                        p0.addmsg(m);
                    }
                    if (p.name.equals(this.listP.get(i).name)) {
                        this.list_check[i] = -1;
                    }
                }
                m.cleanup();
                
                this.is_finish = true;
                for (Player p0 : this.listP) {
                    p0.tableTickOption = null;
                }
                unregister(this);
                
                onDecline(p);
            } else {
                // Thành viên huỷ -> Bỏ qua thành viên đó, phòng chờ vẫn tiếp tục cho những người khác
                Message m = new Message(-74);
                m.writer().writeByte(3);
                m.writer().writeShort(this.idDialog);
                m.writer().writeShort(p.index_map);
                
                for (int i = 0; i < this.listP.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(this.listP.get(i).name);
                    if (p0 != null) {
                        p0.addmsg(m);
                    }
                    if (p.name.equals(this.listP.get(i).name)) {
                        this.list_check[i] = -1;
                    }
                }
                m.cleanup();
                p.tableTickOption = null;
                p.getService().send_box_ThongBao_OK("Bạn đã từ chối tham gia phó bản.");
            }
        }
    }

    public abstract void onAccept(Player leader, List<Player> readyPlayers) throws IOException;
    public abstract void onDecline(Player p) throws IOException;
}
