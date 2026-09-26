using System;

public class MsgAutoFire : MsgDialog
{
	private int itemStep = 28;
	private int valueRevice = 0;
	public static short[][] value;
	private int indexBuff = 0;
	private iCommand cmdOK;
	private int numCols = 4;

	public override void commandPointer(int index, int subIndex)
	{
		if (index == 9)
		{
			isClose = true;
			Player.typeAutoFireMain = 1;
			if (Player.AutoFireCur != 2)
			{
				Player.AutoFireCur = 1;
			}

			if (value != null)
			{
				Player.typeAutoBuff = 0;
				for (int i = 0; i < value.Length; i++)
				{
					if (value[i][1] == 1)
					{
						Skill_Info sk = Skill_Info.getSkillFromID(value[i][0]);
						if (sk != null && sk.typeSkill == 2)
						{
							Player.typeAutoBuff = 1;
							break;
						}
					}
				}
			}

			Player.AutoRevice = (sbyte)valueRevice;
			Player.isAutoRevice = (valueRevice == 1);
			GameCanvas.saveRms.SaveAutoFire();
		}
	}

	public void setinfoAuto_Fire()
	{
		fontDia = mFont.tahoma_7b_black;
		beginDia();
		cmdList = new mVector();
		cmdOK = new iCommand((T.xong != null) ? T.xong : "Xong", 9, this);
		cmdList.addElement(cmdOK);

		wDia = 200;
		if (wDia > MotherCanvas.w - 10)
		{
			wDia = MotherCanvas.w - 10;
		}

		maxWShow = wDia;
		wShowPaper = 5;
		itemStep = 28;
		wItem = 24;

		int num = 0;
		short[] array = new short[50];

		if (Player.vecListSkill != null)
		{
			for (int i = 0; i < Player.vecListSkill.size(); i++)
			{
				Skill_Info skill_Info = (Skill_Info)Player.vecListSkill.elementAt(i);
				if (skill_Info != null && skill_Info.Lv_RQ >= 0 && (skill_Info.typeSkill == 1 || skill_Info.typeSkill == 4 || skill_Info.typeSkill == 0 || skill_Info.typeSkill == 2))
				{
					if (num < array.Length)
					{
						array[num] = skill_Info.ID;
						num++;
					}
				}
			}
		}

		short[][] oldValue = value;
		if (num > 0)
		{
			value = new short[num][];
			for (int j = 0; j < value.Length; j++)
			{
				value[j] = new short[2];
				value[j][0] = array[j];
				value[j][1] = 1;
				if (oldValue != null)
				{
					for (int k = 0; k < oldValue.Length; k++)
					{
						if (oldValue[k][0] == array[j])
						{
							value[j][1] = oldValue[k][1];
							break;
						}
					}
				}
			}
		}

		numCols = (wDia - 24) / itemStep;
		if (numCols < 3) numCols = 3;
		if (numCols > 5) numCols = 5;

		int rows = (num > 0) ? ((num + numCols - 1) / numCols) : 1;
		hDia = 65 + rows * itemStep + 24;
		if (hDia > MotherCanvas.h - 10)
		{
			hDia = MotherCanvas.h - 10;
		}

		xDia = MotherCanvas.hw - wDia / 2;
		yDia = MotherCanvas.hh - hDia / 2 - 2;
		indexBuff = 0;
		valueRevice = Player.AutoRevice;
		setPosCmdNew(-2, isLast: false);

		if (value != null && value.Length > 0)
		{
			Skill_Info firstSk = Skill_Info.getSkillFromID(value[0][0]);
			if (firstSk != null)
			{
				setInfoHelp((value[0][1] == 1 ? "[Dùng] " : "[Tắt] ") + firstSk.name);
			}
		}
	}

	public override void paint(mGraphics g)
	{
		GameCanvas.resetTrans(g);
		int curY = yDia;
		int startX = xDia + 12;

		paintPaper(g, MotherCanvas.hw - wShowPaper / 2, curY, wShowPaper, hDia, maxWShow, AvMain.PAPER_NORMAL);
		g.setClip(MotherCanvas.hw - wShowPaper / 2, 0, wShowPaper, MotherCanvas.h);
		g.saveCanvas();
		g.ClipRec(MotherCanvas.hw - wShowPaper / 2, 0, wShowPaper, MotherCanvas.h);

		curY += 10;
		g.setColor(15972174);
		g.fillRoundRect(xDia + 10, curY, wDia - 20, 16, 4, 4);
		curY += 2;
		AvMain.FontBorderColor(g, "KỸ NĂNG AUTO", xDia + wDia / 2, curY, 2, 6, 5);

		curY += 20;
		mFont.tahoma_7b_brown.drawString(g, "Chọn chiêu thức tự đánh & buff:", startX, curY, 0);
		curY += 14;

		if (value != null && value.Length > 0)
		{
			int gridStartX = xDia + (wDia - numCols * itemStep) / 2;
			for (int i = 0; i < value.Length; i++)
			{
				Skill_Info sk = Skill_Info.getSkillFromID(value[i][0]);
				if (sk == null) continue;

				int col = i % numCols;
				int row = i / numCols;
				int iconCenterX = gridStartX + col * itemStep + itemStep / 2;
				int iconCenterY = curY + row * itemStep + itemStep / 2;

				if (idSelect == 0 && indexBuff == i)
				{
					g.setColor(0xFFFF00);
					g.drawRect(iconCenterX - 13, iconCenterY - 13, 25, 25);
					g.drawRect(iconCenterX - 14, iconCenterY - 14, 27, 27);
				}

				Skill_Info.paintIcon(g, iconCenterX, iconCenterY, sk.idIcon, sk.LvDevilSkill);

				if (value[i][1] == 0)
				{
					AvMain.fraDelay2.drawFrame(0, iconCenterX, iconCenterY, 0, 3, g);
				}
				else
				{
					g.setColor(0x00FF00);
					g.fillRect(iconCenterX + 4, iconCenterY + 4, 4, 4);
				}
			}
			int rows = (value.Length + numCols - 1) / numCols;
			curY += rows * itemStep + 4;
		}
		else
		{
			mFont.tahoma_7_black.drawString(g, "Không có kỹ năng", startX, curY, 0);
			curY += 28;
		}

		// Checkbox Tự hồi sinh
		if (idSelect == 1)
		{
			g.setColor(14342874);
			g.fillRect(xDia + 8, curY - 2, wDia - 16, 16);
		}
		g.drawImage(AvMain.imgBorderCombo, startX + 5, curY + 5, 3);
		if (valueRevice == 1)
		{
			AvMain.fraCheck.drawFrame(2, startX + 5, curY + 5, 0, 3, g);
		}
		mFont.tahoma_7b_brown.drawString(g, (T.autoRevice != null) ? T.autoRevice : "Tự dùng hồi sinh", startX + 16, curY, 0);

		paintInfoHelp(g);

		if (cmdList != null)
		{
			for (int i = 0; i < cmdList.size(); i++)
			{
				iCommand cmd = (iCommand)cmdList.elementAt(i);
				cmd.paint(g, cmd.xCmd, cmd.yCmd);
			}
		}

		g.restoreCanvas();
	}

	public override void update()
	{
		updateInfoHelp();
		if (isClose)
		{
			updateClose();
			return;
		}
		updateOpen();
		if (GameCanvas.isTouchNoOrPC())
		{
			updatekey();
		}
		updatePointer();
		if (cmdOK != null)
		{
			cmdOK.isSelect = (idSelect == 2);
		}
	}

	public override void updatekey()
	{
		if (GameCanvas.keyMove(1)) // Lên
		{
			if (idSelect == 2)
			{
				idSelect = 1;
			}
			else if (idSelect == 1)
			{
				idSelect = 0;
				if (value != null && value.Length > 0)
				{
					indexBuff = value.Length - 1;
				}
			}
			else if (idSelect == 0)
			{
				if (indexBuff >= numCols)
				{
					indexBuff -= numCols;
				}
			}
			GameCanvas.ClearkeyMove(1);
			updateHelpText();
		}
		else if (GameCanvas.keyMove(3)) // Xuống
		{
			if (idSelect == 0)
			{
				if (value != null && indexBuff + numCols < value.Length)
				{
					indexBuff += numCols;
				}
				else
				{
					idSelect = 1;
				}
			}
			else if (idSelect == 1)
			{
				idSelect = 2;
			}
			GameCanvas.ClearkeyMove(3);
			updateHelpText();
		}
		else if (GameCanvas.keyMove(0)) // Trái
		{
			if (idSelect == 0)
			{
				if (indexBuff > 0) indexBuff--;
			}
			else if (idSelect == 1)
			{
				valueRevice = (valueRevice == 1) ? 0 : 1;
			}
			GameCanvas.ClearkeyMove(0);
			updateHelpText();
		}
		else if (GameCanvas.keyMove(2)) // Phải
		{
			if (idSelect == 0)
			{
				if (value != null && indexBuff < value.Length - 1) indexBuff++;
			}
			else if (idSelect == 1)
			{
				valueRevice = (valueRevice == 1) ? 0 : 1;
			}
			GameCanvas.ClearkeyMove(2);
			updateHelpText();
		}
		else if (GameCanvas.isKeyPressed(5) || (GameCanvas.keyMyHold != null && GameCanvas.keyMyHold[5]) || GameCanvas.isKeyPressed(12) || (GameCanvas.keyMyHold != null && GameCanvas.keyMyHold[12]))
		{
			GameCanvas.clearKeyHold(5);
			GameCanvas.clearKeyHold(12);
			GameCanvas.clearKeyPressed();
			if (idSelect == 0)
			{
				if (value != null && indexBuff >= 0 && indexBuff < value.Length)
				{
					value[indexBuff][1] = (short)(value[indexBuff][1] == 1 ? 0 : 1);
					updateHelpText();
				}
			}
			else if (idSelect == 1)
			{
				valueRevice = (valueRevice == 1) ? 0 : 1;
				setInfoHelp((T.helpAutoRevice != null) ? T.helpAutoRevice : "Sử dụng vật phẩm hồi sinh khi chết.");
			}
			else if (idSelect == 2)
			{
				if (cmdOK != null)
				{
					cmdOK.perform();
				}
			}
		}
		updatekeyPC();
	}

	private void updateHelpText()
	{
		if (idSelect == 0 && value != null && indexBuff >= 0 && indexBuff < value.Length)
		{
			Skill_Info sk = Skill_Info.getSkillFromID(value[indexBuff][0]);
			if (sk != null)
			{
				setInfoHelp((value[indexBuff][1] == 1 ? "[Dùng] " : "[Tắt] ") + sk.name);
			}
		}
		else if (idSelect == 1)
		{
			setInfoHelp((T.helpAutoRevice != null) ? T.helpAutoRevice : "Sử dụng vật phẩm hồi sinh khi chết.");
		}
	}

	public override void updatePointer()
	{
		if (GameCanvas.isPointerSelect)
		{
			int curY = yDia + 10 + 16 + 2 + 20 + 14;
			int startX = xDia + 12;

			if (value != null && value.Length > 0)
			{
				int gridStartX = xDia + (wDia - numCols * itemStep) / 2;
				for (int i = 0; i < value.Length; i++)
				{
					int col = i % numCols;
					int row = i / numCols;
					int iconBoxX = gridStartX + col * itemStep;
					int iconBoxY = curY + row * itemStep;

					if (GameCanvas.isPoint(iconBoxX, iconBoxY, itemStep, itemStep))
					{
						indexBuff = i;
						idSelect = 0;
						value[i][1] = (short)(value[i][1] == 1 ? 0 : 1);
						updateHelpText();
						GameCanvas.isPointerSelect = false;
						return;
					}
				}
				int rows = (value.Length + numCols - 1) / numCols;
				curY += rows * itemStep + 4;
			}
			else
			{
				curY += 28;
			}

			// Checkbox Tự hồi sinh
			if (GameCanvas.isPoint(startX, curY - 2, wDia - 24, 18))
			{
				idSelect = 1;
				valueRevice = (valueRevice == 1) ? 0 : 1;
				setInfoHelp((T.helpAutoRevice != null) ? T.helpAutoRevice : "Sử dụng vật phẩm hồi sinh khi chết.");
				GameCanvas.isPointerSelect = false;
				return;
			}
		}

		base.updatePointer();
	}

	/// <summary>
	/// Rebuild MsgAutoFire.value từ vecListSkill hiện tại, giữ nguyên trạng thái bật/tắt của từng skill.
	/// </summary>
	public static void syncValue()
	{
		try
		{
			if (Player.vecListSkill == null || Player.vecListSkill.size() == 0) return;
			int count = 0;
			short[] ids = new short[Player.vecListSkill.size()];
			for (int i = 0; i < Player.vecListSkill.size(); i++)
			{
				Skill_Info sk = (Skill_Info)Player.vecListSkill.elementAt(i);
				if (sk != null && sk.Lv_RQ >= 0
					&& (sk.typeSkill == 1 || sk.typeSkill == 2 || sk.typeSkill == 4 || sk.typeSkill == 0))
				{
					ids[count++] = sk.ID;
				}
			}
			if (count == 0) return;
			short[][] oldValue = value;
			value = new short[count][];
			for (int i = 0; i < count; i++)
			{
				value[i] = new short[2];
				value[i][0] = ids[i];
				value[i][1] = 1; // mặc định bật
				if (oldValue != null)
				{
					for (int j = 0; j < oldValue.Length; j++)
					{
						if (oldValue[j][0] == ids[i])
						{
							value[i][1] = oldValue[j][1];
							break;
						}
					}
				}
			}
			Player.typeAutoBuff = 0;
			for (int i = 0; i < value.Length; i++)
			{
				if (value[i][1] == 1)
				{
					Skill_Info sk = Skill_Info.getSkillFromID(value[i][0]);
					if (sk != null && sk.typeSkill == 2)
					{
						Player.typeAutoBuff = 1;
						break;
					}
				}
			}
			if (Player.typeAutoFireMain != 1 && Player.typeAutoFireMain != 2 && Player.typeAutoFireMain != -1)
			{
				Player.typeAutoFireMain = 1;
			}
		}
		catch (System.Exception) { }
	}
}
