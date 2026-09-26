using System;
using System.Collections.Generic;

public class AThMadaraFunc
{
	private const int TELEPORT_MAX_TILES = 6;

	private static Dictionary<MainObject, int[]> gomSavedGlobal = null;

	private static bool gomFollowPlayer = false;

	public static bool gomUseMapCenter = false;

	public static int gomOffsetX = 0;

	public static int gomOffsetY = 0;

	public static int gomFixedX = 0;

	public static int gomFixedY = 0;

	public static long lastUserInteractionTime = 0L;

	public static bool isUserInteracting(Player p)
	{
		bool interacting = false;
		if (GameCanvas.isPointerDown || GameCanvas.isPointerSelect || GameCanvas.isPointerMove)
		{
			interacting = true;
		}
		if (GameCanvas.keyMove(0) || GameCanvas.keyMove(1) || GameCanvas.keyMove(2) || GameCanvas.keyMove(3)
			|| GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(1) || GameCanvas.isKeyPressed(2) || GameCanvas.isKeyPressed(3))
		{
			interacting = true;
		}
		if (GameCanvas.keyMyHold != null)
		{
			if (GameCanvas.keyMyHold[2] || GameCanvas.keyMyHold[4] || GameCanvas.keyMyHold[6] || GameCanvas.keyMyHold[8]
				|| GameCanvas.keyMyHold[22] || GameCanvas.keyMyHold[24] || GameCanvas.keyMyHold[26] || GameCanvas.keyMyHold[28])
			{
				interacting = true;
			}
		}
		if (p != null && (p.vx != 0 || p.vy != 0))
		{
			interacting = true;
		}

		if (interacting)
		{
			lastUserInteractionTime = GameCanvas.timeNow;
			return true;
		}
		return (GameCanvas.timeNow - lastUserInteractionTime < 2000L);
	}

	public static bool isSkillEnabledInAuto(short skillId)
	{
		return Player.isSkillEnabledForAuto(skillId);
	}

	public static bool isSkillUsable(Player p, Skill_Info sk, bool isAttackSkill)
	{
		if (sk == null) return false;
		if (sk.Lv_RQ < 0) return false;
		if (GameCanvas.loadmap != null && GameCanvas.loadmap.mapLang()) return false;
		if (sk.typeSkill == 3 || sk.typeSkill == 6) return false;

		bool isSeaMap = (LoadMap.specMap == 4);
		if (isSeaMap)
		{
			if (sk.typeSkill == 1) return false;
		}
		else
		{
			if (sk.typeSkill == 4) return false;
		}

		if (isAttackSkill)
		{
			if (isSeaMap)
			{
				if (sk.typeSkill != 4 && sk.typeSkill != 0) return false;
			}
			else
			{
				if (sk.typeSkill != 1 && sk.typeSkill != 0) return false;
			}
		}
		else
		{
			if (sk.typeSkill != 2) return false;
			if (sk.typeBuff == 3)
			{
				if (p == null || GameScreen.objFocus == null || !p.setFightPk(GameScreen.objFocus))
				{
					return false;
				}
			}
		}

		if (!isAttackSkill)
		{
			if (!isSkillEnabledInAuto(sk.ID)) return false;
		}

		DelaySkill d = DelaySkill.getDelay(sk.indexHotKey);
		if (d != null && !d.isCoolDown()) return false;

		if (p != null)
		{
			int manaNeed = Math.Max((int)sk.manaLost, p.getManaNeedUse((int)sk.manaLost));
			if (p.Mp < manaNeed)
			{
				updateAutoPotion(p);
				if (p.Mp < manaNeed) return false;
			}
		}

		if (p != null)
		{
			if (p.Hp <= 0 || p.isDie || p.Action == 4) return false;
			if (p.check_Fire_EffSpec()) return false;
		}

		return true;
	}

	public static void autoFireMoveToTarget(Player p)
	{
		if (p == null || p.Hp <= 0 || p.isDie || p.Action == 4)
		{
			return;
		}
		if (GameCanvas.loadmap != null && GameCanvas.loadmap.mapLang())
		{
			return;
		}
		if (p.skillCurrent != null)
		{
			return;
		}

		bool isAutoActive = p.isAutoFireNew108 || (Player.AutoFireCur > 0 && Interface_Game.isAutoFireInterface);
		if (!isAutoActive)
		{
			return;
		}

		if (isUserInteracting(p))
		{
			try
			{
				p.posTransRoad = null;
			}
			catch (Exception) {}
			return;
		}

		try
		{
			if (Player.typeAutoBuff == 1 && MsgAutoFire.value != null && !isUserInteracting(p))
			{
				if (GameCanvas.gameTick % 5 == 1)
				{
					for (int i = 0; i < MsgAutoFire.value.Length; i++)
					{
						short id = MsgAutoFire.value[i][0];
						short enabled = MsgAutoFire.value[i][1];
						if (enabled != 1)
						{
							continue;
						}
						Skill_Info sk = Skill_Info.getSkillFromID(id);
						if (sk == null)
						{
							continue;
						}
						if (!isSkillUsable(p, sk, false))
						{
							continue;
						}
						try
						{
							bool started = p.beginPlayerFire(sk);
							if (started)
							{
								p.timeFristSkill = GameCanvas.timeNow;
								return;
							}
						}
						catch (Exception)
						{
						}
					}
				}
			}
		}
		catch (Exception)
		{
		}

		if (gomSavedGlobal != null && gomUseMapCenter && !isUserInteracting(p) && gomFixedX > 0 && gomFixedY > 0)
		{
			try
			{
				p.x = gomFixedX;
				p.y = gomFixedY;
				p.toX = gomFixedX;
				p.toY = gomFixedY;
				p.vx = 0;
				p.vy = 0;
				p.posTransRoad = null;
			}
			catch (Exception)
			{
			}
		}

		MainObject tgt = GameScreen.objFocus;
		if (tgt == null || !isAllowedTargetForAuto(p, tgt) || tgt.isRemove || (tgt.Hp <= 0 && tgt.maxHp > 0) || tgt.isDie || tgt.Action == 4)
		{
			GameScreen.objFocus = null;
			int best = int.MaxValue;
			MainObject bestObj = null;
			if (GameScreen.vecPlayers != null)
			{
				for (int i = 0; i < GameScreen.vecPlayers.size(); i++)
				{
					MainObject o = (MainObject)GameScreen.vecPlayers.elementAt(i);
					if (o == null)
					{
						continue;
					}
					if (!isAllowedTargetForAuto(p, o))
					{
						continue;
					}
					if (o.isRemove || o.isDie || (o.Hp <= 0 && o.maxHp > 0) || o.Action == 4)
					{
						continue;
					}
					int d = MainObject.getDistance(p.x, p.y, o.x, o.y);
					if (d < best)
					{
						best = d;
						bestObj = o;
					}
				}
			}
			if (bestObj != null)
			{
				GameScreen.objFocus = bestObj;
				tgt = bestObj;
				try
				{
					GameScreen.addEffectEnd_ObjTo(24, 0, tgt.x, tgt.y, tgt.ID, tgt.typeObject, 0, null);
					Interface_Game.isPaintInfoFocus = true;
				}
				catch (Exception)
				{
				}
			}
			else
			{
				return;
			}
		}

		Hotkey[] row = (Player.hotkeyPlayer != null) ? Player.hotkeyPlayer[Player.currentTab] : null;
		int n = (row != null) ? row.Length : 0;
		if (n > 0)
		{
			if (Player.IndexFire < 0 || Player.IndexFire >= n)
			{
				Player.IndexFire = 0;
			}
		}

		int tileSize = (GameCanvas.loadmap != null && GameCanvas.loadmap.mapW > 0) ? LoadMap.wTile : 24;
		bool hasFired = false;

		if (row != null && n > 0)
		{
			for (int iter = 0; iter < n; ++iter)
			{
				int slot = (Player.IndexFire + iter) % n;
				Hotkey hk = row[slot];
				if (hk == null) continue;

				if (hk.skill != null)
				{
					Skill_Info sk = Skill_Info.getSkillFromID(hk.skill.ID);
					if (sk == null) continue;

					if (sk.typeSkill == 2)
					{
						if (!isSkillUsable(p, sk, false)) continue;
						try
						{
							bool started = p.beginPlayerFire(sk);
							if (started)
							{
								p.timeFristSkill = GameCanvas.timeNow;
								Player.IndexFire = (slot + 1) % n;
								hasFired = true;
								break;
							}
						}
						catch (Exception) {}
						continue;
					}

					if (!isSkillUsable(p, sk, true)) continue;

					int skillRange = sk.range > 0 ? (int)sk.range : 60;
					double dx = p.x - tgt.x;
					double dy = p.y - tgt.y;
					double dist = System.Math.Sqrt(dx * dx + dy * dy);

					if (dist > skillRange)
					{
						if (AThMadaraMOD.autoCombatMoveMode == 0)
						{
							int tx = (tgt.x / tileSize) * tileSize + tileSize / 2;
							int ty = (tgt.y / tileSize) * tileSize + tileSize / 2;
							sendTeleport(p, tx, ty);
							p.posTransRoad = null;
							try { p.vx = 0; p.vy = 0; } catch (Exception) {}
						}
						else
						{
							int targetTileX = tgt.x / tileSize;
							int targetTileY = tgt.y / tileSize;
							int playerTileX = p.x / tileSize;
							int playerTileY = p.y / tileSize;
							if (p.posTransRoad == null && GameCanvas.loadmap != null)
							{
								p.posTransRoad = GameCanvas.loadmap.updateFindRoad(targetTileX, targetTileY, playerTileX, playerTileY, 30, p);
							}
							if (p.posTransRoad == null)
							{
								p.toX = tgt.x;
								p.toY = tgt.y;
							}
							return;
						}
					}

					try
					{
						bool started = p.beginPlayerFire(sk);
						if (started)
						{
							p.timeFristSkill = GameCanvas.timeNow;
							Player.IndexFire = (slot + 1) % n;
							hasFired = true;
							break;
						}
					}
					catch (Exception) {}
				}
			}
		}

		// Fallback: Neu cac o hotkey trong hoac chua san sang / bi chan, tu dung skill co ban danh quai
		if (!hasFired && Player.vecListSkill != null && Player.vecListSkill.size() > 0)
		{
			for (int k = 0; k < Player.vecListSkill.size(); k++)
			{
				Skill_Info skNorm = (Skill_Info)Player.vecListSkill.elementAt(k);
				if (skNorm == null) continue;
				if (!isSkillUsable(p, skNorm, true)) continue;

				int skillRange = skNorm.range > 0 ? (int)skNorm.range : 60;
				double dx = p.x - tgt.x;
				double dy = p.y - tgt.y;
				double dist = System.Math.Sqrt(dx * dx + dy * dy);

				if (dist > skillRange)
				{
					if (AThMadaraMOD.autoCombatMoveMode == 0)
					{
						int tx = (tgt.x / tileSize) * tileSize + tileSize / 2;
						int ty = (tgt.y / tileSize) * tileSize + tileSize / 2;
						sendTeleport(p, tx, ty);
						p.posTransRoad = null;
						try { p.vx = 0; p.vy = 0; } catch (Exception) {}
					}
					else
					{
						int targetTileX = tgt.x / tileSize;
						int targetTileY = tgt.y / tileSize;
						int playerTileX = p.x / tileSize;
						int playerTileY = p.y / tileSize;
						if (p.posTransRoad == null && GameCanvas.loadmap != null)
						{
							p.posTransRoad = GameCanvas.loadmap.updateFindRoad(targetTileX, targetTileY, playerTileX, playerTileY, 30, p);
						}
						if (p.posTransRoad == null)
						{
							p.toX = tgt.x;
							p.toY = tgt.y;
						}
						return;
					}
				}

				try
				{
					bool started = p.beginPlayerFire(skNorm);
					if (started)
					{
						p.timeFristSkill = GameCanvas.timeNow;
						hasFired = true;
						break;
					}
				}
				catch (Exception) {}
			}
		}
	}

	public static void updateAutoPotion(Player p)
	{
		if (p == null || p.Hp <= 0 || p.isDie) return;
		if (p.maxHp > 0 && (p.Hp * 100 / p.maxHp < 35 || (p.maxMp > 0 && p.Mp * 100 / p.maxMp < 20)))
		{
			if (GameCanvas.gameTick % 10 == 0 && Player.vecInventory != null)
			{
				for (int inv = 0; inv < Player.vecInventory.size(); inv++)
				{
					MainItem mi = (MainItem)Player.vecInventory.elementAt(inv);
					if (mi != null && mi.typeObject == 3)
					{
						GlobalService.gI().Use_Potion((short)mi.ID);
						break;
					}
				}
			}
		}
	}

	public static void updateHutItem(Player p)
	{
		if (p == null || p.isDie || p.Hp <= 0) return;
		if (GameCanvas.gameTick % 6 != 0) return;
		int maxDist = AThMadaraMOD.isHutItem ? 1000 : 140;

		if (GameScreen.vecPlayers != null)
		{
			for (int i = 0; i < GameScreen.vecPlayers.size(); i++)
			{
				MainObject obj = (MainObject)GameScreen.vecPlayers.elementAt(i);
				if (obj != null && !obj.isRemove)
				{
					if (obj.typeObject == 3 || obj.typeObject == 4 || obj.typeObject == 5 || obj.typeObject == 7)
					{
						int d = MainObject.getDistance(p.x, p.y, obj.x, obj.y);
						if (d <= maxDist)
						{
							GlobalService.gI().Get_Item_Map(obj.ID, obj.typeObject);
							break;
						}
					}
				}
			}
		}

		if (LoadMap.mItemMap != null && LoadMap.mItemMap.Length > 3 && LoadMap.mItemMap[3] != null && LoadMap.mItemMap[3].size() > 0)
		{
			for (int im = 0; im < LoadMap.mItemMap[3].size(); im++)
			{
				MainItemMap item = (MainItemMap)LoadMap.mItemMap[3].elementAt(im);
				if (item != null && !item.isRemove)
				{
					int distItem = MainObject.getDistance(p.x, p.y, item.x, item.y);
					if (distItem <= maxDist)
					{
						GlobalService.gI().Get_Item_Map(item.IDItem, item.layer);
						break;
					}
				}
			}
		}
	}

	public static bool isPvpMap()
	{
		if (GameScreen.isPvPNew) return true;
		if (GameCanvas.loadmap == null) return false;
		int id = GameCanvas.loadmap.idMap;
		if (LoadMap.specMap == 1 || LoadMap.specMap == 7) return true;
		return id == 58 || id == 120 || id == 122 || id == 123 || id == 260 
			|| (id >= 70 && id <= 74 && id != 73) // Dau Truong Tu Do
			|| id == 81 // Pho Ban Khong Lo (Little Garden)
			|| (id >= 254 && id <= 258) || (id >= 261 && id <= 265) // Chiem Dao
			|| (id >= 267 && id <= 271) // Bao Ve Phao Dai
			|| (id >= 272 && id <= 275) // Dai Chien The Gioi / World War
			|| (id >= 280 && id <= 290);
	}

	// Cho phep danh: quai/boss (typeObject==1), vat the (typeObject==5, 7), nguoi choi trong PvP (typeObject==0)
	private static bool isAllowedTargetForAuto(Player p, MainObject t)
	{
		if (t == null) return false;
		if (GameCanvas.loadmap != null && GameCanvas.loadmap.mapLang()) return false;
		if (t.isDie || (t.Hp <= 0 && t.maxHp > 0) || t.isRemove || t.Action == 4) return false;

		// Quai / Boss (typeObject == 1)
		if (t.typeObject == 1)
		{
			if (p != null && !p.setFightPk(t)) return false;
			if (AThMadaraMOD.isSlaughterActive && AThMadaraMOD.slaughterTargetName != null)
			{
				if (!AThMadaraMOD.slaughterTargetName.Equals(t.name)) return false;
				if (AThMadaraMOD.slaughterTargetLevel > 0 && t.Lv != AThMadaraMOD.slaughterTargetLevel) return false;
			}
			return true;
		}

		// Vat the pha huy duoc (thung go, thap, cot, tru...)
		if (t.typeObject == 5 || t.typeObject == 7)
		{
			if (p != null && !p.setFightPk(t)) return false;
			return true;
		}

		// Nguoi choi trong map PvP hoac co doi dich
		if (t.typeObject == 0 && t != p)
		{
			if (isPvpMap()) return true;
			if (p != null && p.setFightPk(t)) return true;
		}

		return false;
	}

	public static int[] findSafeGomPosition(Player p, int prefX, int prefY)
	{
		if (GameCanvas.loadmap == null) return new int[] { prefX, prefY };
		int wTile = LoadMap.wTile > 0 ? LoadMap.wTile : 24;
		int minX = 80;
		int maxX = Math.Max(minX + 50, GameCanvas.loadmap.maxWMap - 80);
		int minY = 80;
		int maxY = Math.Max(minY + 50, GameCanvas.loadmap.maxHMap - 80);

		for (int r = 0; r <= 15; r++)
		{
			for (int dx = -r; dx <= r; dx++)
			{
				for (int dy = -r; dy <= r; dy++)
				{
					if (Math.Abs(dx) != r && Math.Abs(dy) != r) continue;
					int cx = prefX + dx * wTile;
					int cy = prefY + dy * wTile;

					if (cx < minX || cx > maxX || cy < minY || cy > maxY) continue;
					int tile = GameCanvas.loadmap.getTile(cx, cy);
					if (tile == 1 || tile == -1) continue;

					bool nearVgo = false;
					if (LoadMap.vecPointChange != null)
					{
						for (int i = 0; i < LoadMap.vecPointChange.size(); i++)
						{
							Point pt = (Point)LoadMap.vecPointChange.elementAt(i);
							if (pt != null)
							{
								int distVgo = MainObject.getDistance(cx, cy, pt.x, pt.y);
								if (distVgo < 130)
								{
									nearVgo = true;
									break;
								}
							}
						}
					}
					if (nearVgo) continue;

					return new int[] { cx, cy };
				}
			}
		}
		return new int[] { prefX, prefY };
	}

	public static Dictionary<MainObject, int[]> startGomAllFollow(Player p, int offsetX, int offsetY, bool follow, bool useMapCenter)
	{
		Dictionary<MainObject, int[]> saved = new Dictionary<MainObject, int[]>();
		if (p == null)
		{
			return saved;
		}

		try
		{
			mVector vec = GameScreen.vecPlayers;
			if (vec == null)
			{
				return saved;
			}

			int prefX;
			int prefY;
			if (useMapCenter && GameCanvas.loadmap != null)
			{
				prefX = GameCanvas.loadmap.maxWMap / 2;
				prefY = GameCanvas.loadmap.maxHMap / 2;
			}
			else
			{
				prefX = p.x + offsetX;
				prefY = p.y + offsetY;
			}

			int[] safePos = findSafeGomPosition(p, prefX, prefY);
			int targetX = safePos[0];
			int targetY = safePos[1];
			gomFixedX = targetX;
			gomFixedY = targetY;
			gomUseMapCenter = useMapCenter;

			for (int i = 0; i < vec.size(); ++i)
			{
				object o = vec.elementAt(i);
				if (o is MainObject)
				{
					MainObject m = (MainObject)o;
					if (m == null || m.isRemove || m.typeObject != 1)
					{
						continue;
					}
					try
					{
						saved.Add(m, new int[8] { m.x, m.y, m.toX, m.toY, m.toXNew, m.toYNew, m.vx, m.vy });
					}
					catch (Exception)
					{
					}
					m.gomX = targetX;
					m.gomY = targetY;
					m.x = targetX;
					m.y = targetY;
					m.toX = targetX;
					m.toY = targetY;
					m.toXNew = targetX;
					m.toYNew = targetY;
					m.vx = 0;
					m.vy = 0;
					m.isGom = true;
				}
			}

			gomSavedGlobal = saved;
			gomFollowPlayer = follow;
			gomOffsetX = offsetX;
			gomOffsetY = offsetY;
		}
		catch (Exception)
		{
		}
		return saved;
	}

	public static void updateGomPositions(Player p)
	{
		if (p == null || gomSavedGlobal == null)
		{
			return;
		}
		try
		{
			int targetX;
			int targetY;
			if (gomUseMapCenter)
			{
				if (gomFixedX == 0 && gomFixedY == 0 && GameCanvas.loadmap != null)
				{
					int[] safePos = findSafeGomPosition(p, GameCanvas.loadmap.maxWMap / 2, GameCanvas.loadmap.maxHMap / 2);
					gomFixedX = safePos[0];
					gomFixedY = safePos[1];
				}
				targetX = gomFixedX;
				targetY = gomFixedY;
			}
			else if (gomFollowPlayer)
			{
				int[] safePos = findSafeGomPosition(p, p.x + gomOffsetX, p.y + gomOffsetY);
				targetX = safePos[0];
				targetY = safePos[1];
			}
			else
			{
				var it = gomSavedGlobal.GetEnumerator();
				if (!it.MoveNext())
				{
					return;
				}
				MainObject first = it.Current.Key;
				if (first == null)
				{
					return;
				}
				targetX = first.gomX;
				targetY = first.gomY;
			}

			if (GameScreen.vecPlayers != null)
			{
				for (int i = 0; i < GameScreen.vecPlayers.size(); ++i)
				{
					object o = GameScreen.vecPlayers.elementAt(i);
					if (o is MainObject)
					{
						MainObject m = (MainObject)o;
						if (m == null || m.isRemove || m.isDie || m.Hp <= 0 || m.typeObject != 1) continue;
						if (!gomSavedGlobal.ContainsKey(m))
						{
							try { gomSavedGlobal.Add(m, new int[8] { m.x, m.y, m.toX, m.toY, m.toXNew, m.toYNew, m.vx, m.vy }); } catch (Exception) {}
						}
						m.gomX = targetX;
						m.gomY = targetY;
						m.x = targetX;
						m.y = targetY;
						m.toX = targetX;
						m.toY = targetY;
						m.toXNew = targetX;
						m.toYNew = targetY;
						m.vx = 0;
						m.vy = 0;
						m.isGom = true;
					}
				}
			}
		}
		catch (Exception)
		{
		}
	}

	public static void stopGomAll(Dictionary<MainObject, int[]> saved)
	{
		Dictionary<MainObject, int[]> toRestore = (saved != null) ? saved : gomSavedGlobal;
		if (toRestore == null)
		{
			return;
		}
		try
		{
			foreach (var pair in toRestore)
			{
				MainObject m = pair.Key;
				int[] s = pair.Value;
				if (m == null || s == null || s.Length < 8)
				{
					continue;
				}
				try
				{
					m.x = s[0];
					m.y = s[1];
					m.toX = s[2];
					m.toY = s[3];
					m.toXNew = s[4];
					m.toYNew = s[5];
					m.vx = s[6];
					m.vy = s[7];
				}
				catch (Exception)
				{
				}
				m.isGom = false;
				m.gomX = 0;
				m.gomY = 0;
			}
		}
		catch (Exception)
		{
		}
		finally
		{
			if (gomSavedGlobal != null)
			{
				gomSavedGlobal.Clear();
			}
			gomSavedGlobal = null;
			gomFollowPlayer = false;
			gomUseMapCenter = false;
			gomOffsetX = (gomOffsetY = 0);
			gomFixedX = (gomFixedY = 0);
		}
	}

	public static void resetGomData()
	{
		if (gomSavedGlobal != null)
		{
			gomSavedGlobal.Clear();
		}
		gomSavedGlobal = null;
		gomFollowPlayer = false;
		gomUseMapCenter = false;
		gomOffsetX = (gomOffsetY = 0);
		gomFixedX = (gomFixedY = 0);
	}

	private static bool isAttackable(MainObject t)
	{
		return t != null && !t.returnAction() && !t.isDie && t.Hp > 0 && !t.isRemove;
	}

	public static void sendTeleport(Player p, int x, int y)
	{
		try
		{
			GlobalService.gI().Obj_Move((short)x, (short)y);
			p.x = x;
			p.y = y;
			p.xLast = x;
			p.yLast = y;
			Player.isSendMove = true;
		}
		catch (Exception)
		{
		}
	}

	public static void paintShowDateTime(mGraphics g)
	{
		try
		{
			// Ve ten HaiTacZet o giua man hinh ben tren cung
			if (AThMadaraMOD.isShowTopTitle)
			{
				mFont.tahoma_7b_black.drawString(g, "HaiTacZet", MotherCanvas.hw + 1, 3, 2);
				mFont.tahoma_7b_yellow.drawString(g, "HaiTacZet", MotherCanvas.hw, 2, 2);
			}

			DateTime now = DateTime.Now;
			string timeStr = string.Format(
				"{0:D2}:{1:D2}:{2:D2} {3:D2}-{4:D2}-{5:D4}",
				now.Hour,
				now.Minute,
				now.Second,
				now.Day,
				now.Month,
				now.Year
			);
			int imgW = 22, imgH = 16;
			int chatX = Interface_Game.xNumMess;
			int chatY = Interface_Game.yNumMess;

			int textY = chatY + imgH + 2;
			int textW = mFont.tahoma_7_white.getWidth(timeStr);
			int drawX = chatX + (imgW / 2) - (textW / 2);
			if (drawX < 2)
			{
				drawX = 2;
			}
			if (drawX + textW > MotherCanvas.w - 2)
			{
				drawX = MotherCanvas.w - 2 - textW;
			}
			mFont.tahoma_7_white.drawString(g, timeStr, drawX, textY, 0);

			// Hien thi FPS va Toc do game ngay duoi thoi gian
			string fpsSpeedStr = "FPS: " + MotherCanvas.currentFPS + " | Speed: x" + AThMadaraMOD.gameSpeed;
			int textW2 = mFont.tahoma_7_yellow.getWidth(fpsSpeedStr);
			int drawX2 = chatX + (imgW / 2) - (textW2 / 2);
			if (drawX2 < 2)
			{
				drawX2 = 2;
			}
			if (drawX2 + textW2 > MotherCanvas.w - 2)
			{
				drawX2 = MotherCanvas.w - 2 - textW2;
			}
			mFont.tahoma_7_yellow.drawString(g, fpsSpeedStr, drawX2, textY + 11, 0);
		}
		catch (Exception)
		{
		}
	}

	public static void autoSellItem(Player p)
	{
	}

	public static void autoReviceV1(Player p)
	{
		if (p == null || !p.isDie)
		{
			return;
		}
		if (AThMadaraMOD.autoReviveMode == 0 && !Player.isAutoRevice)
		{
			return;
		}
		try
		{
			if (GameCanvas.loadmap != null)
			{
				AThMadaraMOD.lastMapId = GameCanvas.loadmap.idMap;
			}
			if (GameScreen.player != null)
			{
				AThMadaraMOD.lastX = GameScreen.player.x;
				AThMadaraMOD.lastY = GameScreen.player.y;
			}
			try
			{
				saveCurrentZone();
			}
			catch (Exception)
			{
			}

			int mode = AThMadaraMOD.autoReviveMode;
			if (mode == 0 && Player.isAutoRevice)
			{
				mode = 4;
			}

			if (mode == 4)
			{
				GlobalService.gI().Player_Revice(0);
				if (AThMadaraMOD.isAutoReMap)
				{
					AThMadaraMOD.shouldReturnToLastMap = true;
					Interface_Game.addInfoPlayerNormal("Đã hồi sinh về làng. Sẽ quay lại map cũ...", mFont.tahoma_7_yellow);
				}
				else
				{
					AThMadaraMOD.shouldReturnToLastMap = false;
					Interface_Game.addInfoPlayerNormal("Đã hồi sinh về làng.", mFont.tahoma_7_yellow);
				}
			}
			else if (mode == 1 || mode == 2)
			{
				GlobalService.gI().Player_Revice(1);
				AThMadaraMOD.shouldReturnToLastMap = false;
				Interface_Game.addInfoPlayerNormal("Đã hồi sinh tại chỗ.", mFont.tahoma_7_yellow);
			}
			else if (mode == 3)
			{
				if (AThMadaraMOD.reviveFallbackTime == 0)
				{
					AThMadaraMOD.reviveFallbackTime = mSystem.currentTimeMillis();
					GlobalService.gI().Player_Revice(1);
					AThMadaraMOD.shouldReturnToLastMap = false;
					Interface_Game.addInfoPlayerNormal("Đang hồi sinh tại chỗ...", mFont.tahoma_7_yellow);
				}
				else if (mSystem.currentTimeMillis() - AThMadaraMOD.reviveFallbackTime > 4000)
				{
					GlobalService.gI().Player_Revice(0);
					AThMadaraMOD.reviveFallbackTime = 0;
					if (AThMadaraMOD.isAutoReMap)
					{
						AThMadaraMOD.shouldReturnToLastMap = true;
						Interface_Game.addInfoPlayerNormal("Hồi sinh tại chỗ thất bại, về làng & quay lại map...", mFont.tahoma_7_yellow);
					}
				}
			}
			GameCanvas.end_Dialog();
		}
		catch (Exception)
		{
		}
	}

	private static sbyte savedZone = -1;

	public static void saveCurrentZone()
	{
		if (GameCanvas.loadMapScr != null)
		{
			savedZone = GameCanvas.loadMapScr.area;
		}
	}

	public static void restoreSavedZoneIfAny()
	{
		if (savedZone >= 0 && savedZone <= 20)
		{
			try
			{
				GlobalService.gI().Select_Area((sbyte)0, savedZone);
				Interface_Game.addInfoPlayerNormal("Đang khôi phục khu " + savedZone + "...", mFont.tahoma_7_yellow);
			}
			catch (Exception)
			{
			}
		}
	}

	public static bool isPlayerFullyRevived(Player p)
	{
		if (p == null)
		{
			return false;
		}
		return p.Hp > 0 && !p.isDie && GameCanvas.loadmap != null;
	}
}
