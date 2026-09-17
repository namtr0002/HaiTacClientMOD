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
