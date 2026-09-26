using System;

public class MsgAutoFire : MsgDialog
{
	private int valueRevice;
	private int valueAutoGetItem;
	public static short[][] value;
	private int indexBuff;
	private iCommand cmdOK;
	private int numCols = 5;

	public override void commandPointer(int index, int subIndex)
	{
		if (index != 9)
		{
			return;
		}
		isClose = true;

		// 1. Che do danh chieu: Tu dong dung cac chieu da tick chon
		Player.typeAutoFireMain = 1;
		if (Player.AutoFireCur != 2)
		{
			Player.AutoFireCur = 1;
		}

		// 2. Luu ky nang buff ho tro
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

		// 3. Luu tu hoi sinh
		Player.AutoRevice = (sbyte)valueRevice;

		// 4. Luu tu loc / nhat do
		Player.isAutoFilterItems = (valueAutoGetItem == 1);

		// 5. Luu tu bom HP/MP
		Player.isMPHP = (MsgAutoMPHP.hp > 0 || MsgAutoMPHP.mp > 0);
		if (GameScreen.player != null)
		{
			GameScreen.player.hpPoi = null;
			GameScreen.player.mpPoi = null;
		}

		// Ghi toan bo xuong RMS
		GameCanvas.saveRms.SaveAutoFire();
		GameCanvas.saveRms.SaveAutoMp_Hp();
		GameCanvas.saveRms.SaveAutoGetItem();
	}

	public void setinfoAuto_Fire()
	{
		fontDia = mFont.tahoma_7b_black;
		beginDia();
		cmdList = new mVector();
		cmdOK = new iCommand(T.xong, 9, this);
		cmdList.addElement(cmdOK);

		wDia = MotherCanvas.w - 16;
		if (wDia > 210)
		{
			wDia = 210;
		}
		if (wDia < 170)
		{
			wDia = 170;
		}
		maxWShow = wDia;
		wShowPaper = 5;
		wItem = 22;

		int num = 0;
		short[] array = new short[Player.vecListSkill != null ? Player.vecListSkill.size() : 0];
		if (Player.vecListSkill != null)
		{
			for (int i = 0; i < Player.vecListSkill.size(); i++)
			{
				Skill_Info skill_Info = (Skill_Info)Player.vecListSkill.elementAt(i);
				if (skill_Info != null && skill_Info.Lv_RQ >= 0 && (skill_Info.typeSkill == 2 || skill_Info.typeSkill == 1 || skill_Info.typeSkill == 4 || skill_Info.typeSkill == 0))
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

		numCols = (wDia - 28) / 26;
		if (numCols < 4) numCols = 4;
		if (numCols > 5) numCols = 5;

		int numRows = (num > 0) ? ((num + numCols - 1) / numCols) : 0;
		hDia = 175 + numRows * 26;
		if (hDia > MotherCanvas.h - 10)
		{
			hDia = MotherCanvas.h - 10;
		}

		xDia = MotherCanvas.hw - wDia / 2;
		yDia = MotherCanvas.hh - hDia / 2 - 4;
		indexBuff = 0;
		valueRevice = Player.AutoRevice;
		valueAutoGetItem = Player.isAutoFilterItems ? 1 : 0;

		setPosCmdNew(-2, isLast: false);
		updateHelpText();
	}

	public override void paint(mGraphics g)
	{
		GameCanvas.resetTrans(g);
		int num = yDia;
		int num2 = xDia + 14;
		paintPaper(g, MotherCanvas.hw - wShowPaper / 2, num, wShowPaper, hDia, maxWShow, AvMain.PAPER_NORMAL);
		g.setClip(MotherCanvas.hw - wShowPaper / 2, 0, wShowPaper, MotherCanvas.h);
		g.saveCanvas();
		g.ClipRec(MotherCanvas.hw - wShowPaper / 2, 0, wShowPaper, MotherCanvas.h);

		num += 10;
		g.setColor(15972174);
		g.fillRoundRect(xDia + 10, num, wDia - 20, 16, 4, 4);
		num += 2;
		AvMain.FontBorderColor(g, "CÀI ĐẶT TỰ ĐỘNG", xDia + wDia / 2, num, 2, 6, 5);

		num += 18;
		int barW = 68;

		// 1. Thanh Bơm HP
		mImage imgIcon = (GameScreen.player != null && GameScreen.player.Lv >= 100) ? Interface_Game.imgIconMPHP2 : Interface_Game.imgIconMPHP;
		if (imgIcon != null)
		{
			g.drawRegion(imgIcon, 0, 0, 10, 10, 0, num2, num + 2, 0);
		}
		mFont.tahoma_7b_brown.drawString(g, "HP:", num2 + 13, num, 0);
		Interface_Game.PaintHPMP(g, 1, MsgAutoMPHP.hp * 10, 100, num2 + 38, num + 1, 0, 10, barW, 1, isflip: false, 0, isUpdateEff: false, 0);
		string hpText = (MsgAutoMPHP.hp > 0) ? (MsgAutoMPHP.hp + "%") : "Tắt";
		mFont.tahoma_7b_black.drawString(g, hpText, num2 + 38 + barW + 5, num, 0);

		num += 18;
		// 2. Thanh Bơm MP
		if (imgIcon != null)
		{
			g.drawRegion(imgIcon, 0, 10, 10, 10, 0, num2, num + 2, 0);
		}
		mFont.tahoma_7b_brown.drawString(g, "MP:", num2 + 13, num, 0);
		Interface_Game.PaintHPMP(g, 2, MsgAutoMPHP.mp * 10, 100, num2 + 38, num + 1, 0, 10, barW, 1, isflip: false, 0, isUpdateEff: false, 0);
		string mpText = (MsgAutoMPHP.mp > 0) ? (MsgAutoMPHP.mp + "%") : "Tắt";
		mFont.tahoma_7b_black.drawString(g, mpText, num2 + 38 + barW + 5, num, 0);

		num += 18;
		// 3. Ưu tiên Dược phẩm
		mFont.tahoma_7b_brown.drawString(g, T.uutien + ":", num2, num, 0);
		string uuTienStr = (MsgAutoMPHP.typeUu == 0) ? "Bình nhỏ" : "Bình lớn";
		AvMain.Font3dColor(g, " " + uuTienStr, num2 + 45, num, 0, 0, 7);

		num += 18;
		// 4. Kỹ năng tự đánh & buff (Lưới toàn bộ skill chủ động/buff/cơ bản)
		mFont.tahoma_7b_brown.drawString(g, "Kỹ năng tự đánh & buff:", num2, num, 0);
		num += 14;
		if (value == null || value.Length == 0)
		{
			mFont.tahoma_7_black.drawString(g, " Không có kỹ năng", num2, num, 0);
			num += 18;
		}
		else
		{
			int skSize = 22;
			int skCols = (numCols > 0) ? numCols : 4;
			int skGridX = num2 + (wDia - 28 - (skCols * (skSize + 4))) / 2;
			if (skGridX < num2) skGridX = num2;
			int skRows = (value.Length + skCols - 1) / skCols;

			for (int i = 0; i < value.Length; i++)
			{
				Skill_Info skillFromID = Skill_Info.getSkillFromID(value[i][0]);
				if (skillFromID == null) continue;

				int col = i % skCols;
				int row = i / skCols;
				int iconX = skGridX + col * (skSize + 4);
				int iconY = num + row * (skSize + 4);

				if (idSelect == 3 && indexBuff == i)
				{
					g.setColor(0xFFFF00);
					g.drawRect(iconX - 2, iconY - 2, skSize + 3, skSize + 3);
				}

				Skill_Info.paintIcon(g, iconX + skSize / 2, iconY + skSize / 2, skillFromID.idIcon, skillFromID.LvDevilSkill);
				if (value[i][1] == 0)
				{
					AvMain.fraDelay2.drawFrame(0, iconX + skSize / 2, iconY + skSize / 2, 0, 3, g);
				}
				else
				{
					g.setColor(0x00FF00);
					g.fillRect(iconX + skSize - 4, iconY + skSize - 4, 4, 4);
				}
			}
			num += skRows * (skSize + 4) + 4;
		}

		// 5. Checkbox Tự dùng hồi sinh
		if (idSelect == 4)
		{
			g.setColor(14342874);
			g.fillRect(num2 - 2, num - 2, wDia - 24, 16);
		}
		g.drawImage(AvMain.imgBorderCombo, num2 + 5, num + 6, 3);
		if (valueRevice == 1)
		{
			AvMain.fraCheck.drawFrame(2, num2 + 5, num + 6, 0, 3, g);
		}
		mFont.tahoma_7b_brown.drawString(g, T.autoRevice, num2 + 15, num, 0);

		num += 18;
		// 6. Checkbox Tự động nhặt đồ
		if (idSelect == 5)
		{
			g.setColor(14342874);
			g.fillRect(num2 - 2, num - 2, wDia - 24, 16);
		}
		g.drawImage(AvMain.imgBorderCombo, num2 + 5, num + 6, 3);
		if (valueAutoGetItem == 1)
		{
			AvMain.fraCheck.drawFrame(2, num2 + 5, num + 6, 0, 3, g);
		}
		mFont.tahoma_7b_brown.drawString(g, "Tự động nhặt đồ", num2 + 15, num, 0);

		paintInfoHelp(g);
		if (cmdList != null)
		{
			for (int j = 0; j < cmdList.size(); j++)
			{
				iCommand iCommand2 = (iCommand)cmdList.elementAt(j);
				iCommand2.paint(g, iCommand2.xCmd, iCommand2.yCmd);
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
	}

	public override void updatekey()
	{
		if (GameCanvas.keyMove(1)) // Lên
		{
			if (idSelect > 0)
			{
				idSelect--;
			}
			GameCanvas.ClearkeyMove(1);
			updateHelpText();
		}
		else if (GameCanvas.keyMove(3)) // Xuống
		{
			if (idSelect < 5)
			{
				idSelect++;
			}
			GameCanvas.ClearkeyMove(3);
			updateHelpText();
		}
		else if (GameCanvas.keyMove(0)) // Trái
		{
			setSelect(-1);
			GameCanvas.ClearkeyMove(0);
		}
		else if (GameCanvas.keyMove(2)) // Phải
		{
			setSelect(1);
			GameCanvas.ClearkeyMove(2);
		}
		else if (GameCanvas.isKeyPressed(5) || (GameCanvas.keyMyHold != null && GameCanvas.keyMyHold[5]))
		{
			GameCanvas.clearKeyHold(5);
			GameCanvas.clearKeyPressed();
			if (idSelect == 3 && value != null && indexBuff >= 0 && indexBuff < value.Length)
			{
				value[indexBuff][1] = (short)(value[indexBuff][1] == 1 ? 0 : 1);
				updateHelpText();
			}
			else if (idSelect == 4)
			{
				valueRevice = (valueRevice == 1) ? 0 : 1;
				setInfoHelp(T.helpAutoRevice);
			}
			else if (idSelect == 5)
			{
				valueAutoGetItem = (valueAutoGetItem == 1) ? 0 : 1;
			}
			else if (cmdList != null && idCommand < cmdList.size())
			{
				((iCommand)cmdList.elementAt(idCommand)).perform();
			}
		}
		updatekeyPC();
	}

	public void setSelect(int plus)
	{
		if (idSelect == 0) // HP
		{
			MsgAutoMPHP.hp += plus * 10;
			if (MsgAutoMPHP.hp > 90) MsgAutoMPHP.hp = 90;
			if (MsgAutoMPHP.hp < 0) MsgAutoMPHP.hp = 0;
			setInfoHelp(T.mHelpAutoMPHP[0] + MsgAutoMPHP.hp + "%");
		}
		else if (idSelect == 1) // MP
		{
			MsgAutoMPHP.mp += plus * 10;
			if (MsgAutoMPHP.mp > 90) MsgAutoMPHP.mp = 90;
			if (MsgAutoMPHP.mp < 0) MsgAutoMPHP.mp = 0;
			setInfoHelp(T.mHelpAutoMPHP[1] + MsgAutoMPHP.mp + "%");
		}
		else if (idSelect == 2) // Ưu tiên
		{
			MsgAutoMPHP.typeUu = (MsgAutoMPHP.typeUu == 0) ? 1 : 0;
			setInfoHelp(T.mHelpAutoMPHP[2 + MsgAutoMPHP.typeUu]);
		}
		else if (idSelect == 3) // Lưới kỹ năng
		{
			if (value != null && value.Length > 0)
			{
				indexBuff += plus;
				if (indexBuff < 0) indexBuff = 0;
				if (indexBuff >= value.Length) indexBuff = value.Length - 1;
				updateHelpText();
			}
		}
		else if (idSelect == 4) // Tự hồi sinh
		{
			valueRevice = (valueRevice == 0) ? 1 : 0;
			setInfoHelp(T.helpAutoRevice);
		}
		else if (idSelect == 5) // Tự nhặt đồ
		{
			valueAutoGetItem = (valueAutoGetItem == 0) ? 1 : 0;
		}
	}

	private void updateHelpText()
	{
		if (idSelect == 0)
		{
			setInfoHelp(T.mHelpAutoMPHP[0] + MsgAutoMPHP.hp + "%");
		}
		else if (idSelect == 1)
		{
			setInfoHelp(T.mHelpAutoMPHP[1] + MsgAutoMPHP.mp + "%");
		}
		else if (idSelect == 2)
		{
			setInfoHelp(T.mHelpAutoMPHP[2 + MsgAutoMPHP.typeUu]);
		}
		else if (idSelect == 3)
		{
			if (value != null && indexBuff >= 0 && indexBuff < value.Length)
			{
				Skill_Info sk = Skill_Info.getSkillFromID(value[indexBuff][0]);
				if (sk != null)
				{
					setInfoHelp((value[indexBuff][1] == 1 ? "[Dùng] " : "[Tắt] ") + sk.name);
				}
			}
		}
		else if (idSelect == 4)
		{
			setInfoHelp(T.helpAutoRevice);
		}
		else if (idSelect == 5)
		{
			setInfoHelp("Tự động nhặt vật phẩm rơi trên mặt đất.");
		}
	}

	public override void updatePointer()
	{
		int num = yDia + 10 + 18;
		int num2 = xDia + 14;
		int barW = 68;

		if (GameCanvas.isPointerSelect || GameCanvas.isPointerDown || GameCanvas.isPointerMove)
		{
			// 1. Touch Bơm HP Bar
			if (GameCanvas.isPoint(num2 + 35, num - 4, barW + 35, 18))
			{
				int val = (GameCanvas.px - (num2 + 38)) * 100 / barW;
				val = (val / 10) * 10;
				if (val < 0) val = 0;
				if (val > 90) val = 90;
				MsgAutoMPHP.hp = val;
				setInfoHelp(T.mHelpAutoMPHP[0] + MsgAutoMPHP.hp + "%");
			}

			// 2. Touch Bơm MP Bar
			if (GameCanvas.isPoint(num2 + 35, num + 18 - 4, barW + 35, 18))
			{
				int val = (GameCanvas.px - (num2 + 38)) * 100 / barW;
				val = (val / 10) * 10;
				if (val < 0) val = 0;
				if (val > 90) val = 90;
				MsgAutoMPHP.mp = val;
				setInfoHelp(T.mHelpAutoMPHP[1] + MsgAutoMPHP.mp + "%");
			}
		}

		if (GameCanvas.isPointerSelect)
		{
			// 3. Touch Ưu tiên
			if (GameCanvas.isPoint(num2, num + 36 - 2, wDia - 28, 16))
			{
				MsgAutoMPHP.typeUu = (MsgAutoMPHP.typeUu == 0) ? 1 : 0;
				setInfoHelp(T.mHelpAutoMPHP[2 + MsgAutoMPHP.typeUu]);
				GameCanvas.isPointerSelect = false;
			}

			// 4. Touch Kỹ năng tự đánh & buff grid
			int curSkillY = num + 54 + 14;
			if (value != null && value.Length > 0)
			{
				int skSize = 22;
				int skCols = (numCols > 0) ? numCols : 4;
				int skGridX = num2 + (wDia - 28 - (skCols * (skSize + 4))) / 2;
				if (skGridX < num2) skGridX = num2;

				for (int i = 0; i < value.Length; i++)
				{
					int col = i % skCols;
					int row = i / skCols;
					int iconX = skGridX + col * (skSize + 4);
					int iconY = curSkillY + row * (skSize + 4);

					if (GameCanvas.isPoint(iconX - 2, iconY - 2, skSize + 4, skSize + 4))
					{
						value[i][1] = (short)(value[i][1] == 1 ? 0 : 1);
						idSelect = 3;
						indexBuff = i;
						Skill_Info sk = Skill_Info.getSkillFromID(value[i][0]);
						if (sk != null)
						{
							setInfoHelp((value[i][1] == 1 ? "[Dùng] " : "[Tắt] ") + sk.name);
						}
						GameCanvas.isPointerSelect = false;
						break;
					}
				}
				int skRows = (value.Length + skCols - 1) / skCols;
				curSkillY += skRows * (skSize + 4) + 4;
			}
			else
			{
				curSkillY += 18;
			}

			// 5. Touch Checkbox Tự dùng hồi sinh
			if (GameCanvas.isPoint(num2, curSkillY - 2, wDia - 28, 18))
			{
				idSelect = 4;
				valueRevice = (valueRevice == 1) ? 0 : 1;
				setInfoHelp(T.helpAutoRevice);
				GameCanvas.isPointerSelect = false;
			}

			// 6. Touch Checkbox Tự động nhặt đồ
			if (GameCanvas.isPoint(num2, curSkillY + 18 - 2, wDia - 28, 18))
			{
				idSelect = 5;
				valueAutoGetItem = (valueAutoGetItem == 1) ? 0 : 1;
				setInfoHelp("Tự động nhặt vật phẩm rơi trên mặt đất.");
				GameCanvas.isPointerSelect = false;
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
