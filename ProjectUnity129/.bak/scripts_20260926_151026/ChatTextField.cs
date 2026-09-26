public class ChatTextField : AvMain
{
	public TField tfChat;

	public static ChatTextField instance;

	public static bool isShow;

	public static ChatTextField gI()
	{
		if (instance != null)
		{
			return instance;
		}
		return instance = new ChatTextField();
	}

	public void setChat()
	{
		isShow = !isShow;
		GameCanvas.clearKeyHold();
		if (isShow)
		{
			tfChat.setPoiter();
		}
	}

	public override void commandTab(int index, int subIndex)
	{
		switch (index)
		{
		case 0:
			GameCanvas.clearAll();
			tfChat.setText("");
			isShow = false;
			if (!GameCanvas.isTouch)
			{
				tfChat.setFocus(isFocus: true);
			}
			break;
		case 1:
			sendChat();
			break;
		}
	}

	protected ChatTextField()
	{
		tfChat = new TField();
		tfChat.isChangeFocus = false;
		tfChat.setFocus(isFocus: true);
		init();
		tfChat.x = (MotherCanvas.w - tfChat.width) / 2;
		if (GameMidlet.DEVICE == 2)
		{
			tfChat.x = 10;
		}
		tfChat.setMaxTextLenght(70);
		tfChat.setStringNull(T.chat);
		if (!GameCanvas.isTouch)
		{
			left = new iCommand(T.close, 0);
			center = new iCommand(T.chat, 1);
			right = tfChat.setCmdClear();
		}
	}

	public void init()
	{
		tfChat.y = MotherCanvas.h - iCommand.hButtonCmdNor - tfChat.height - 5;
		tfChat.width = MotherCanvas.w - TField.xDu * 2 - 20;
		if (GameMidlet.DEVICE == 2)
		{
			tfChat.y = MotherCanvas.h - tfChat.height - 10;
			tfChat.width = MotherCanvas.w / 2 - 10;
		}
	}

	public void keyPressed(int keyCode)
	{
		tfChat.keyPressed(keyCode);
	}

	public override void updatekey()
	{
		tfChat.update();
		base.updatekey();
	}

	public override void paint(mGraphics g)
	{
		base.paint(g);
		tfChat.paint(g);
	}

	public override void updatePointer()
	{
		tfChat.updatePointer();
		base.updatePointer();
	}

	public void sendChat()
	{
		if (tfChat.getText().Length > 0)
		{
			string chatStr = tfChat.getText().Trim();
			if (chatStr.Equals("/lv5", System.StringComparison.OrdinalIgnoreCase) || chatStr.Equals("/cap5", System.StringComparison.OrdinalIgnoreCase) || chatStr.Equals("lv5", System.StringComparison.OrdinalIgnoreCase) || chatStr.Equals("cap 5", System.StringComparison.OrdinalIgnoreCase))
			{
				Effect_Skill.s_phoenixLevelOverride = 5;
				Interface_Game.addInfoPlayerNormal("Trái Phượng Hoàng: CẤP = 5 (Hoàng Kim Thần Thoại)", mFont.tahoma_7_yellow);
				tfChat.setText("");
				if (GameCanvas.isTouch) isShow = false;
				return;
			}
			if (chatStr.Equals("/lv1", System.StringComparison.OrdinalIgnoreCase) || chatStr.Equals("/cap1", System.StringComparison.OrdinalIgnoreCase) || chatStr.Equals("lv1", System.StringComparison.OrdinalIgnoreCase) || chatStr.Equals("cap 1", System.StringComparison.OrdinalIgnoreCase))
			{
				Effect_Skill.s_phoenixLevelOverride = 1;
				Interface_Game.addInfoPlayerNormal("Trái Phượng Hoàng: CẤP < 5 (Lam Hỏa Tiêu Chuẩn)", mFont.tahoma_7_yellow);
				tfChat.setText("");
				if (GameCanvas.isTouch) isShow = false;
				return;
			}
			if (chatStr.Equals("/auto", System.StringComparison.OrdinalIgnoreCase) || chatStr.Equals("/lv0", System.StringComparison.OrdinalIgnoreCase) || chatStr.Equals("auto", System.StringComparison.OrdinalIgnoreCase))
			{
				Effect_Skill.s_phoenixLevelOverride = 0;
				Interface_Game.addInfoPlayerNormal("Trái Phượng Hoàng: TỰ ĐỘNG THEO SKILL", mFont.tahoma_7_yellow);
				tfChat.setText("");
				if (GameCanvas.isTouch) isShow = false;
				return;
			}

			GameScreen.player.strChatPopup = tfChat.getText();
			GlobalService.gI().chatPopup(tfChat.getText());
			tfChat.setText("");
		}
		if (GameCanvas.isTouch)
		{
			isShow = false;
		}
	}
}
