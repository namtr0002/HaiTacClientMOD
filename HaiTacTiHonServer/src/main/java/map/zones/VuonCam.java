package map.zones;

import event.EventManager;

import model.Party;
import model.Player;
import model.Quest;
import core.ZUtil;
import map.Zone;
import mob.Mob;
import network.Message;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.MobTemplate;

public class VuonCam extends zabstracts.AbsTreasureDungeon implements zinterfaces.iMob {
  private static List<Zone> ENTRY = new CopyOnWriteArrayList<>();

  private Set<String> nameP = new HashSet<>();
  public int level;
  public int hp, hpMax, indexMob;
  public byte dir;
  public int state;
  public long time_state;
  public int mobCanInit;
  public long timeHpDecrease;
  public long timeChat;
  public HashMap<Mob, Long> listRemoveMap;

  public int hpMob;
  public int lvMob;

  public static VuonCam createDungeon(List<Player> listP) {
      long maxDame = 0;
      long totalDame = 0;
      int lvMax = 1;
      String creator = (listP != null && !listP.isEmpty() && listP.get(0) != null) ? listP.get(0).name : "Unknown";
      for (Player p0 : listP) {
          if (p0 == null) continue;
          if (!p0.isBot && !p0.isDe && !(p0 instanceof model.DeTu) && !(p0 instanceof bot.mercenary.MercenaryBot)) {
              try {
                  p0.updateMoney();
                  if (p0.getService() != null) {
                      p0.getService().CountDown_Ticket();
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
          long dame = (p0.ability != null) ? p0.ability.get_dame(true) : 1000L;
          if (p0.ability != null) {
              dame = (dame * p0.ability.get_dame_devil_percent()) / 100;
          }
          totalDame += dame;
          maxDame = Math.max(maxDame, dame);
          lvMax = Math.max(lvMax, p0.level);
      }
      long effectiveDame = Math.max(maxDame, (long) (maxDame + (totalDame - maxDame) * 0.5));
      if (effectiveDame < 1000) effectiveDame = 1000;
      VuonCam dungeon = new VuonCam(effectiveDame, lvMax);
      dungeon.creatorName = creator;
      dungeon.create();
      database.DungeonSessionCache.gI().registerInstance(dungeon, creator, listP, 62, dungeon.time_state, lvMax, (int) Math.min(effectiveDame, Integer.MAX_VALUE), (byte) 0, dungeon.level);
      return dungeon;
  }

  public VuonCam() {
      this.maps = new ArrayList<>();
      this.listRemoveMap = new HashMap<>();
  }

  public VuonCam(long dameMax, int lvMax) {
      this.hpMax = 1000;
      this.hp = this.hpMax;
      // Chuẩn hóa HP quái Vườn Cam: Nerf chuẩn, 2x dame, tối thiểu 15.000 HP
      long baseHp = Math.max(dameMax * 2L, 15_000L);
      this.hpMob = (int) Math.min(baseHp, Integer.MAX_VALUE);
      this.lvMob = lvMax;
      this.level = 1;
      this.state = 0;
      this.time_state = System.currentTimeMillis() + 11_000;
      this.time = this.time_state;
      this.indexMob = -2;
      this.maps = new ArrayList<>();
      this.listRemoveMap = new HashMap<>();
  }

  public synchronized static void add_map(Zone map_boss) {
    ENTRY.add(map_boss);
  }

  public synchronized static Zone get_map(Player p) {
    List<Zone> listRemove = new ArrayList<>();
    for (int i = 0; i < ENTRY.size(); i++) {
      Zone map = ENTRY.get(i);
      if (map.vuonCam != null && map.vuonCam.nameP.contains(p.name)) {
        return map;
      }
      if (!map.isRun()) {
        listRemove.add(map);
      }
    }
    ENTRY.removeAll(listRemove);
    return null;
  }

  public void add_name(Party party) {
    if (party != null && party.list != null) {
      for (int i = 0; i < party.list.size(); i++) {
        if (party.list.get(i) != null) {
          nameP.add(party.list.get(i).name);
        }
      }
    }
  }

  @Override
  public void create() {
      this.mobs = new CopyOnWriteArrayList<>();
      this.maps = new CopyOnWriteArrayList<>();
      
      Zone mapTemplate = Zone.getMapByID(62)[0];
      Zone map_boss = new Zone();
      map_boss.template = mapTemplate.template;
      map_boss.zone_id = (byte) 0;
      map_boss.list_mob = new int[0];
      map_boss.vuonCam = this;
      map_boss.map_dungeon = this;
      
      map_boss.start_map();
      Zone.add_map_plus(map_boss);
      VuonCam.add_map(map_boss);
      this.maps.add(map_boss);
  }

  @Override
  public void join(Player p) throws IOException {
      if (maps != null && !maps.isEmpty()) {
          p.save_previous_map();
          p.map = maps.get(0);
          p.x = 469;
          p.y = 210;
          p.xold = p.x;
          p.yold = p.y;
          if (!p.isBot && !p.isDe && !(p instanceof model.DeTu) && !(p instanceof bot.mercenary.MercenaryBot)) {
              p.namiMax--;
              p.updateArchiDaily(4);
              p.updateArchiDaily(12);
              p.update_key_boss(-2);
          }
          p.tableTickOption = null;
          p.map.goto_map(p);
          p.dungeon = this;
          p.addDungeon(this);
          if (p.getService() != null) {
              p.getService().send_time_cool_down(this.time_state > 0 ? this.time_state : this.time, "Đợt " + this.level, 0);
              p.getService().update_PK(p, true);
              p.getService().pet(p, true);
          }
          Quest.update_map_have_side_quest(p, true);
      }
  }

  public void join(List<Player> list, Party party) {
      if (list != null) {
          list.forEach(p0 -> {
              if (p0 != null) {
                  p0.save_previous_map();
                  if (p0.map != null) {
                      p0.map.leave_map(p0, 2);
                  }
                  nameP.add(p0.name);
              }
          });
          list.forEach(p0 -> {
              if (p0 != null) {
                  try {
                      this.join(p0);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });
      }
      if (party != null) {
          this.add_name(party);
      }
  }

  @Override
  public void update(Zone zone) throws IOException {
      database.DungeonSessionCache.gI().updateInstanceState(this);
      if (this.state == 0) {
          long time_remain = (this.time_state - System.currentTimeMillis()) / 1000;
          if (this.time_state < System.currentTimeMillis()) {
              this.time_state = System.currentTimeMillis() + (60_000L * 2);
              this.state = 1;
              Message m = new Message(-73);
              m.writer().writeByte(1);
              m.writer().writeInt(this.hpMax);
              m.writer().writeInt(this.hp);
              zone.send_msg_all_p(m, null, true);
              m.cleanup();
          } else if (time_remain > 0) {
              if (this.timeChat < System.currentTimeMillis()) {
                  this.timeChat = System.currentTimeMillis() + 4_000;
                  Message m = new Message(17);
                  m.writer().writeShort(-72);
                  m.writer().writeByte(2);
                  m.writer().writeUTF("Đợt tấn công tiếp theo sẽ bắt đầu sau " + time_remain + "s nữa");
                  zone.send_msg_all_p(m, null, true);
                  m.cleanup();
              }
          }
           for (int i = 0; i < zone.players.size(); i++) {
               Player p0 = zone.players.get(i);
               if (p0.isdie) {
                   p0.isdie = false;
                   p0.hp = p0.ability.get_hp_max(true);
                   p0.mp = p0.ability.get_mp_max(true);
                   try {
                       int hp_max = p0.ability.get_hp_max(true);
                       int mp_max = p0.ability.get_mp_max(true);
                       
                       // 1. Gửi gói tin cập nhật HP/MP (-83) cho mọi người chơi trong map
                       Message mHp = new Message(-83);
                       mHp.writer().writeShort(p0.index_map);
                       mHp.writer().writeByte(0); // type
                       mHp.writer().writeInt(hp_max); // maxhp
                       mHp.writer().writeInt(p0.hp); // hp remain
                       mHp.writer().writeInt(hp_max); // hp gain
                       mHp.writer().writeInt(mp_max); // maxmp
                       mHp.writer().writeInt(p0.mp); // mp remain
                       mHp.writer().writeInt(mp_max); // mp gain
                       zone.send_msg_all_p(mHp, null, true);
                       mHp.cleanup();
                       
                       // 2. Gửi gói tin hồi sinh (-71) để xóa trạng thái chết trên client
                       Message mRevive = new Message(-71);
                       mRevive.writer().writeByte(1);
                       mRevive.writer().writeShort(p0.index_map);
                       mRevive.writer().writeByte(0);
                       mRevive.writer().writeInt(1); 
                       zone.send_msg_all_p(mRevive, null, true);
                       mRevive.cleanup();
                   } catch (Exception e) {}
               }
           }
      } else if (this.state == 1) {
          Message m = new Message(-73);
          m.writer().writeByte(0);
          long time_remain = this.time_state - System.currentTimeMillis();
          m.writer().writeShort((short) (time_remain / 1000));
          m.writer().writeUTF("Đợt " + this.level);
          zone.send_msg_all_p(m, null, true);
          m.cleanup();
          this.state = 2;
          this.mobCanInit = 5;
          this.hpMob = (this.hpMob * 102) / 100;
          this.dir = (byte) ZUtil.random(4);
          m = new Message(-31);
          m.writer().writeByte(0);
          switch (this.dir) {
              case 0:
                  m.writer().writeUTF("Đường trên bên trái");
                  break;
              case 1:
                  m.writer().writeUTF("Đường dưới bên trái");
                  break;
              case 2:
                  m.writer().writeUTF("Đường trên bên phải");
                  break;
              default:
                  m.writer().writeUTF("Đường dưới bên phải");
                  break;
          }
          m.writer().writeByte(0);
          m.writer().writeShort(-1);
          zone.send_msg_all_p(m, null, true);
      } else if (this.state == 2) {
          if (this.hp <= 0) {
              this.state = 4;
              return;
          }
          synchronized (this.mobs) {
              for (int i = 0; i < this.mobs.size(); i++) {
                  Mob mob = this.mobs.get(i);
                  if (mob.id_target == -1) {
                      if (mob.x != 580 && (mob.x == 500 || this.dir < 2)) {
                          if (mob.x < 500) {
                              mob.x += 40;
                          }
                          if (mob.x > 500) {
                              mob.x = 500;
                          }
                          if (mob.x == 500) {
                              mob.y = (short) ZUtil.random(238, 287);
                              if (this.timeHpDecrease < System.currentTimeMillis()) {
                                  this.timeHpDecrease = System.currentTimeMillis() + 2_500;
                                  this.hp -= this.mobs.size();
                              }
                          }
                      } else {
                          if (mob.x > 580) {
                              mob.x -= 40;
                          }
                          if (mob.x < 580) {
                              mob.x = 580;
                          }
                          if (mob.x == 580) {
                              mob.y = (short) ZUtil.random(238, 287);
                              if (this.timeHpDecrease < System.currentTimeMillis()) {
                                  this.timeHpDecrease = System.currentTimeMillis() + 2_500;
                                  this.hp -= this.mobs.size();
                              }
                          }
                      }
                      Message m_local = new Message(1);
                      m_local.writer().writeByte(1);
                      m_local.writer().writeShort(mob.index);
                      m_local.writer().writeShort(mob.x);
                      m_local.writer().writeShort(mob.y);
                      zone.send_msg_all_p(m_local, null, true);
                      m_local.cleanup();
                  } else {
                      zone.mob_fire(mob, mob.id_target);
                  }
              }
              if (this.mobCanInit > 0) {
                  for (int i = 0; i < 1; i++) {
                      this.mobCanInit--;
                      Mob mob_add = new Mob();
                      mob_add.mtemplate = MobTemplate.ENTRYS.get(5);
                      if (this.level == 20) {
                          mob_add.mtemplate = MobTemplate.ENTRYS.get(36);
                      }
                      switch (this.dir) {
                          case 0:
                              mob_add.x = 60;
                              mob_add.y = 205;
                              break;
                          case 1:
                              mob_add.x = 60;
                              mob_add.y = 335;
                              break;
                          case 2:
                              mob_add.x = 1050;
                              mob_add.y = 205;
                              break;
                          default:
                              mob_add.x = 1050;
                              mob_add.y = 335;
                              break;
                      }
                      mob_add.hp_max = (this.level == 20) ? (int) Math.min(this.hpMob * 25L / 10L, Integer.MAX_VALUE) : this.hpMob;
                      mob_add.hp = mob_add.hp_max;
                      mob_add.level = this.lvMob;
                      mob_add.isdie = false;
                      mob_add.id_target = -1;
                      mob_add.index = this.indexMob--;
                      mob_add.map = zone;
                      mob_add.is_boss = (this.level == 20);
                      mob_add.boss_inf = null;
                      mob_add.iMob = this;
                      this.mobs.add(mob_add);
                      zone.mobs.put(mob_add.index, mob_add);
                      Message m_local = new Message(1);
                      m_local.writer().writeByte(1);
                      m_local.writer().writeShort(mob_add.index);
                      m_local.writer().writeShort(mob_add.x);
                      m_local.writer().writeShort(mob_add.y);
                      zone.send_msg_all_p(m_local, null, true);
                      m_local.cleanup();
                      for (int pIdx = 0; pIdx < zone.players.size(); pIdx++) {
                          Player pInMap = zone.players.get(pIdx);
                          if (pInMap != null && pInMap.getService() != null) {
                              try {
                                  pInMap.getService().send_mob_info(mob_add);
                              } catch (Exception ignored) {}
                          }
                      }
                  }
               } else if (this.time_state < System.currentTimeMillis()) {
                   if (this.mobs.isEmpty()) {
                       this.time_state = System.currentTimeMillis() + 11_000;
                       this.time = this.time_state;
                       this.state = 0;
                       this.level++;
                       database.DungeonSessionCache.gI().updateInstanceState(this);
                   } else {
                       this.state = 3;
                       this.time_state = System.currentTimeMillis() + 1_000;
                       this.time = this.time_state;
                   }
               } else {
                  if (this.mobs.isEmpty()) {
                      Message m = new Message(-31);
                      m.writer().writeByte(0);
                      m.writer().writeUTF("Bạn đã vượt qua đợt " + this.level);
                      m.writer().writeByte(0);
                      m.writer().writeShort(-1);
                      zone.send_msg_all_p(m, null, true);
                      this.time_state = 0;
                      m = new Message(-73);
                      m.writer().writeByte(0);
                      m.writer().writeShort(0);
                      m.writer().writeUTF("Đợt " + this.level);
                      zone.send_msg_all_p(m, null, true);
                      m.cleanup();
                       if (this.level > 4 && this.level % 5 == 0) {
                           List<GiftBox> listGift = new ArrayList<>();
                           {
                               GiftBox gb = new GiftBox();
                               ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                               gb.id = it_temp4.id;
                               gb.type = 4;
                               gb.name = it_temp4.name;
                               gb.icon = it_temp4.icon;
                               gb.num = (this.level == 20) ? 250000 : 100000;
                               gb.color = 0;
                               listGift.add(gb);
                           }
                           if (this.level == 20) {
                               GiftBox gbRuby = new GiftBox();
                               ItemTemplate4 it_ruby = ItemTemplate4.get_it_by_id(1);
                               if (it_ruby != null) {
                                   gbRuby.id = it_ruby.id;
                                   gbRuby.type = 4;
                                   gbRuby.name = it_ruby.name;
                                   gbRuby.icon = it_ruby.icon;
                                   gbRuby.num = 30;
                                   gbRuby.color = 0;
                                   listGift.add(gbRuby);
                               }
                           }
                          {
                              GiftBox gb_ = new GiftBox();
                              ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(221);
                              gb_.id = it4.id;
                              gb_.type = 4;
                              gb_.name = it4.name;
                              gb_.icon = it4.icon;
                              gb_.num = ZUtil.random(4, 10);
                              gb_.color = 0;
                              listGift.add(gb_);
                          }
                          {
                              GiftBox gb_ = new GiftBox();
                              ItemTemplate7 it4 = ItemTemplate7.get_it_by_id(5);
                              gb_.id = it4.id;
                              gb_.type = 7;
                              gb_.name = it4.name;
                              gb_.icon = it4.icon;
                              gb_.num = ZUtil.random(3, 5);
                              gb_.color = 0;
                              listGift.add(gb_);
                          }
                          if (15 > ZUtil.random(50)) {
                              GiftBox gb_ = new GiftBox();
                              ItemTemplate7 it4 = ItemTemplate7.get_it_by_id(6);
                              gb_.id = it4.id;
                              gb_.type = 7;
                              gb_.name = it4.name;
                              gb_.icon = it4.icon;
                              gb_.num = ZUtil.random(3, 5);
                              gb_.color = 0;
                              listGift.add(gb_);
                          }
                          if (this.level == 20) {
                              this.state = 3;
                              this.time_state = System.currentTimeMillis() + 10_000;
                              if (50 > ZUtil.random(120)) {
                                  GiftBox gb = new GiftBox();
                                  ItemTemplate4 it_temp7 = ItemTemplate4.get_it_by_id(158);
                                  gb.id = it_temp7.id;
                                  gb.type = 4;
                                  gb.name = it_temp7.name;
                                  gb.icon = it_temp7.icon;
                                  gb.num = 1;
                                  gb.color = 0;
                                  listGift.add(gb);
                              }
                              event.EventManager.dispatchMapDrop(zone, listGift, this.level);
                              for (int i = 0; i < zone.players.size(); i++) {
                                  Player p0 = zone.players.get(i);
                                  if (p0 == null || p0.isBot || p0.conn == null) continue;
                                  core.RewardService.sendGiftOrMail(p0, 1, "Vườn cam",
                                          "Hoàn thành Vườn Cam Nami", listGift, true);
                                  event.EventManager.dispatchOnVuonCam(p0, this.level);
                                  if (event.EventManager.isActive(9)) {
                                      event.EventData temp_select = null;
                                      for (int i2 = 0; i2 < p0.eventData.size(); i2++) {
                                          if (p0.eventData.get(i2).eventID == 9) {
                                              temp_select = p0.eventData.get(i2);
                                              break;
                                          }
                                      }
                                      if (temp_select != null && temp_select.data[event.SuKienTrongCay.IDX_VUON_CAM] < 8) {
                                          temp_select.data[event.SuKienTrongCay.IDX_GIO_TRAI_CAY]++;
                                      }
                                  }
                              }
                          } else {
                               for (int i = 0; i < zone.players.size(); i++) {
                                   Player p0 = zone.players.get(i);
                                   if (p0 == null || p0.isBot || p0.conn == null) continue;
                                   core.RewardService.sendGiftOrMail(p0, 1, "Vườn cam",
                                           "Hoàn thành Đợt " + this.level, listGift, true);
                                   event.EventManager.dispatchOnVuonCam(p0, this.level);
                               }
                           }
                      }
                  }
              }
          }
      } else if (this.state == 3) {
          if (this.time_state < System.currentTimeMillis()) {
              this.state = 4;
          } else {
              if (this.timeChat < System.currentTimeMillis()) {
                  this.timeChat = System.currentTimeMillis() + 4_000;
                  Message m = new Message(17);
                  m.writer().writeShort(-72);
                  m.writer().writeByte(2);
                  m.writer().writeUTF("Kết thúc sau "
                          + ((this.time_state - System.currentTimeMillis()) / 1000) + "s nữa");
                  zone.send_msg_all_p(m, null, true);
                  m.cleanup();
              }
          }
       } else if (this.state == 4) {
            if (!zone.players.isEmpty()) {
                List<Player> playerList = new ArrayList<>(zone.players);
                notifyDungeonEnd();
                playerList.forEach(l -> {
                    if (l != null) {
                        l.dungeon = null;
                        l.return_to_previous_map();
                    }
                });
            } else {
                notifyDungeonEnd();
            }
            zone.setRunning(false);
            zone.map_dungeon = null;
      }
      List<Mob> listRemove = new ArrayList<>();
      try {
          for (java.util.Map.Entry<Mob, Long> en : this.listRemoveMap.entrySet()) {
              if (en.getValue() < System.currentTimeMillis()) {
                  zone.remove_obj(en.getKey().index, 1);
                  listRemove.add(en.getKey());
              }
          }
      } catch (Exception e) {
      }
      for (int i = 0; i < listRemove.size(); i++) {
          this.listRemoveMap.remove(listRemove.get(i));
      }
    }

    @Override
    public void onDeath(Player pKill, Mob mob) {
        this.mobs.remove(mob);
        mob.map.mobs.remove(mob.index);
        this.listRemoveMap.put(mob, System.currentTimeMillis() + 1_500);
        database.DungeonSessionCache.gI().updateInstanceState(this);
    }

    @Override
    public void update(Mob mob) {
    }
}
