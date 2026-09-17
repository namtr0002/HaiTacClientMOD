package model;

import network.Message;
import java.io.IOException;
import event.EventManager;
import event.Event;
import event.SuKienHe;

public class Pokemon {

    public static void process(Player p, Message m2) throws IOException {
        short ballItemId = m2.reader().readShort();
        short mobIndex = m2.reader().readShort();
        
        Event ev = EventManager.gI().getEvent(SuKienHe.ID);
        if (ev instanceof SuKienHe && EventManager.isActive(SuKienHe.ID)) {
            ((SuKienHe) ev).catchPokemon(p, ballItemId, mobIndex);
        }
    }
    
    public static void update_pokemon_0(Player p, int id) throws IOException {
        Message m = new Message(-12);
        m.writer().writeByte(2);
        m.writer().writeByte(4);
        m.writer().writeShort(id);
        p.conn.addmsg(m);
        m.cleanup();
    }
}
