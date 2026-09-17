import sys

unity_file = r'ProjectUnity129/Assets/Scripts/Assembly-CSharp/AThMadaraMOD.cs'

with open(unity_file, 'r', encoding='utf-8') as f:
    lines = f.readlines()

print('Original line count:', len(lines))
assert lines[3042].strip().endswith('117,118,1056,288, 118,117,24,288,'), f"Line 3043 mismatch: {lines[3042]}"
assert 'findPathBFS' in lines[3043], f"Line 3044 mismatch: {lines[3043]}"
assert lines[3456].strip() == 'if (targetPortal == null)', f"Line 3457 mismatch: {lines[3456]}"

replacement_text = """\t\t118,124,1032,300, 124,118,24,300, 124,125,936,300, 125,126,1032,300, 125,124,24,276, 126,125,24,288,
\t\t126,127,1032,300, 127,126,216,288, 167,168,24,240, 167,169,348,168, 167,170,696,240, 168,167,1572,12,
\t\t168,173,132,168, 168,171,732,168, 169,167,36,12, 169,172,1452,168, 169,171,132,168, 170,167,36,12,
\t\t170,172,852,168, 170,174,1452,168, 171,168,1500,12, 171,173,24,252, 171,169,1548,12, 171,176,1404,192,
\t\t172,169,84,12, 172,170,36,12, 172,174,1416,252, 172,176,1308,192, 173,168,36,12, 173,175,1584,240,
\t\t173,171,84,12, 174,170,84,12, 174,172,36,12, 174,175,1416,228, 175,173,36,12, 175,174,1548,12,
\t\t175,176,1500,12, 176,171,36,12, 176,172,1428,12, 176,175,780,192, 178,179,24,276, 179,180,155,50,
\t\t180,179,20,310, 181,179,24,276, 182,179,24,276, 183,179,24,276, 189,113,24,288, 189,190,1344,168,
\t\t190,191,1656,276, 191,190,192,444, 191,192,1056,312, 192,191,24,240, 192,193,1080,240, 193,194,1128,276,
\t\t193,192,24,276, 194,193,24,276, 194,195,588,192, 195,194,24,276, 195,196,1128,264, 196,195,24,264,
\t\t196,197,1176,264, 197,196,24,312, 197,198,1176,300, 198,197,24,264, 254,261,157,180, 254,261,349,180,
\t\t254,261,541,180, 254,25,349,345, 255,262,157,180, 255,262,349,180, 255,262,541,180, 255,33,349,345,
\t\t256,263,157,180, 256,263,349,180, 256,263,541,180, 256,49,349,345, 257,264,157,180, 257,264,349,180,
\t\t257,264,541,180, 257,69,349,345, 258,265,157,180, 258,265,349,180, 258,265,541,180, 258,83,349,345,
\t\t267,206,1032,280, 267,204,900,170, 267,205,900,370, 268,203,24,288, 268,207,1032,300, 268,206,140,370,
\t\t268,206,900,370, 269,203,24,300, 269,206,140,170, 269,207,936,300, 269,206,800,170, 270,207,1032,300,
\t\t270,204,900,170, 270,205,900,370, 270,203,24,276, 270,204,140,170, 270,205,140,370, 271,206,24,266,
\t\t271,204,140,170, 271,205,140,370
\t};

\tpublic static void initDefaultMapGraph()
\t{
\t\ttry
\t\t{
\t\t\tfor (int i = 0; i < STATIC_MAP_EDGES.Length; i += 4)
\t\t\t{
\t\t\tMapTransition t = new MapTransition();
\t\t\tt.fromMapId = STATIC_MAP_EDGES[i];
\t\t\tt.toMapId = STATIC_MAP_EDGES[i + 1];
\t\t\tt.portalX = STATIC_MAP_EDGES[i + 2];
\t\t\tt.portalY = STATIC_MAP_EDGES[i + 3];
\t\t\tt.type = TRANS_TYPE_VGO;
\t\t\taddOrUpdateGraphTransition(t);
\t\t\t}
\t\t}
\t\tcatch (Exception) {}
\t}

\tpublic static void loadMapGraphRMS()
\t{
\t\ttry
\t\t{
\t\t\tsbyte[] data = CRes.loadRMS("MOD_MAP_GRAPH");
\t\t\tif (data != null && data.Length > 0)
\t\t\t{
\t\t\t\tByteArrayInputStream bais = new ByteArrayInputStream(data);
\t\t\t\tDataInputStream dis = new DataInputStream(bais);
\t\t\t\tshort count = dis.readShort();
\t\t\t\tfor (int i = 0; i < count; i++)
\t\t\t\t{
\t\t\t\t\tMapTransition t = new MapTransition();
\t\t\t\t\tt.fromMapId = dis.readInt();
\t\t\t\t\tt.toMapId = dis.readInt();
\t\t\t\t\tt.type = dis.readByte();
\t\t\t\t\tt.portalName = dis.readUTF();
\t\t\t\t\tt.portalX = dis.readInt();
\t\t\t\t\tt.portalY = dis.readInt();
\t\t\t\t\tt.npcId = dis.readShort();
\t\t\t\t\tt.npcName = dis.readUTF();
\t\t\t\t\tt.npcX = dis.readInt();
\t\t\t\t\tt.npcY = dis.readInt();
\t\t\t\t\tt.npcCategory = dis.readByte();
\t\t\t\t\tt.npcMenuIndex = dis.readByte();
\t\t\t\t\tt.dialogId = dis.readShort();
\t\t\t\t\tt.dialogChoice = dis.readByte();
\t\t\t\t\tt.itemId = dis.readShort();
\t\t\t\t\tt.nextMapId = dis.readShort();
\t\t\t\t\taddOrUpdateGraphTransition(t);
\t\t\t\t}
\t\t\t\tdis.close();
\t\t\t}
\t\t}
\t\tcatch (Exception) {}
\t\tinitDefaultMapGraph();
\t}

\tpublic static mVector findPathBFS(int startMap, int targetMap)
\t{
\t\tif (startMap == targetMap || startMap < 0 || targetMap < 0) return null;

\t\tmVector queue = new mVector();
\t\tqueue.addElement(startMap);

\t\tmVector visited = new mVector();
\t\tvisited.addElement(startMap);

\t\tSystem.Collections.Generic.Dictionary<int, MapTransition> edgeTo = new System.Collections.Generic.Dictionary<int, MapTransition>();

\t\tbool found = false;
\t\twhile (queue.size() > 0)
\t\t{
\t\t\tint curMap = (int)queue.elementAt(0);
\t\t\tqueue.removeElementAt(0);

\t\t\tif (curMap == targetMap)
\t\t\t{
\t\t\t\tfound = true;
\t\t\t\tbreak;
\t\t\t}

\t\t\tfor (int i = 0; i < mapGraphTransitions.size(); i++)
\t\t\t{
\t\t\t\tMapTransition t = (MapTransition)mapGraphTransitions.elementAt(i);
\t\t\t\tif (t != null && t.fromMapId == curMap)
\t\t\t\t{
\t\t\t\t\tint nextMap = t.toMapId;
\t\t\t\t\tif (!visited.contains(nextMap))
\t\t\t\t\t{
\t\t\t\t\t\tvisited.addElement(nextMap);
\t\t\t\t\t\tedgeTo[nextMap] = t;
\t\t\t\t\t\tqueue.addElement(nextMap);
\t\t\t\t\t}
\t\t\t\t}
\t\t\t}
\t\t}

\t\tif (!found) return null;

\t\tmVector path = new mVector();
\t\tint curr = targetMap;
\t\tint maxHops = 100;
\t\twhile (curr != startMap && --maxHops > 0)
\t\t{
\t\t\tif (!edgeTo.ContainsKey(curr)) break;
\t\t\tMapTransition t = edgeTo[curr];
\t\t\tif (t == null) break;
\t\t\tpath.insertElementAt(t, 0);
\t\t\tcurr = t.fromMapId;
\t\t}
\t\treturn path;
\t}

\tpublic static MapTransition findNextTransition(int currentMap, int targetMap)
\t{
\t\tif (currentMap < 0 || targetMap < 0 || currentMap == targetMap) return null;

\t\t// 1. Kiểm tra nếu chuỗi recordedRoute có đường nối từ currentMap đến targetMap
\t\tif (recordedRoute != null && recordedRoute.size() > 0)
\t\t{
\t\t\tMapTransition firstStep = null;
\t\t\tint curr = currentMap;
\t\t\tint visitedCount = 0;
\t\t\twhile (curr != targetMap && visitedCount <= recordedRoute.size())
\t\t\t{
\t\t\t\tMapTransition nextInRoute = null;
\t\t\t\tfor (int i = 0; i < recordedRoute.size(); i++)
\t\t\t\t{
\t\t\t\t\tMapTransition t = (MapTransition)recordedRoute.elementAt(i);
\t\t\t\tif (t != null && t.fromMapId == curr)
\t\t\t\t{
\t\t\t\t\tnextInRoute = t;
\t\t\t\t\tbreak;
\t\t\t\t}
\t\t\t\t}
\t\t\t\tif (nextInRoute == null) break;
\t\t\t\tif (firstStep == null) firstStep = nextInRoute;
\t\t\t\tcurr = nextInRoute.toMapId;
\t\t\t\tvisitedCount++;
\t\t\t}
\t\t\tif (curr == targetMap && firstStep != null)
\t\t\t{
\t\t\t\treturn firstStep;
\t\t\t}
\t\t}

\t\t// 2. Tìm đường BFS trên toàn bộ đồ thị chuyển map (cổng tĩnh STATIC_MAP_EDGES, NPC thuyền, vật phẩm)
\t\tmVector bfsPath = findPathBFS(currentMap, targetMap);
\t\tif (bfsPath != null && bfsPath.size() > 0)
\t\t{
\t\t\treturn (MapTransition)bfsPath.elementAt(0);
\t\t}

\t\t// 3. Cổng trực tiếp từ map hiện tại tới targetMap
\t\tif (LoadMap.vecPointChange != null && LoadMap.vecPointChange.size() > 0)
\t\t{
\t\t\tstring targetMapName = LoadMap.getNameMap(targetMap);
\t\t\tif (!string.IsNullOrEmpty(targetMapName))
\t\t\t{
\t\t\t\tstring cleanTarget = targetMapName.Trim().ToLower();
\t\t\t\tfor (int i = 0; i < LoadMap.vecPointChange.size(); i++)
\t\t\t\t{
\t\t\t\t\tPoint pt = (Point)LoadMap.vecPointChange.elementAt(i);
\t\t\t\t\tif (pt != null && !string.IsNullOrEmpty(pt.name))
\t\t\t\t\t{
\t\t\t\t\t\tstring cleanP = pt.name.Trim().ToLower();
\t\t\t\t\t\tif (cleanP.Equals(cleanTarget) || cleanTarget.IndexOf(cleanP) >= 0 || cleanP.IndexOf(cleanTarget) >= 0)
\t\t\t\t\t\t{
\t\t\t\t\t\t\tMapTransition directTrans = new MapTransition();
\t\t\t\t\t\t\tdirectTrans.fromMapId = currentMap;
\t\t\t\t\t\t\tdirectTrans.toMapId = targetMap;
\t\t\t\t\t\t\tdirectTrans.type = TRANS_TYPE_VGO;
\t\t\t\t\t\t\tdirectTrans.portalName = pt.name;
\t\t\t\t\t\t\tdirectTrans.portalX = pt.x;
\t\t\t\t\t\t\tdirectTrans.portalY = pt.y;
\t\t\t\t\t\t\treturn directTrans;
\t\t\t\t\t\t}
\t\t\t\t\t}
\t\t\t\t}
\t\t\t}
\t\t}

\t\t// 4. Heuristic theo hướng map trên cùng đảo
\t\tif (LoadMap.vecPointChange != null && LoadMap.vecPointChange.size() > 0)
\t\t{
\t\t\tPoint bestPortal = null;
\t\t\tint bestToMap = -1;
\t\t\tint minDistanceInMapId = int.MaxValue;

\t\t\tfor (int i = 0; i < LoadMap.vecPointChange.size(); i++)
\t\t\t{
\t\t\t\tPoint pt = (Point)LoadMap.vecPointChange.elementAt(i);
\t\t\t\tif (pt == null || pt.name == null) continue;
\t\t\t\tstring pName = pt.name.Trim().ToLower();

\t\t\t\tint pToMap = -1;
\t\t\t\tfor (int m = 0; m < 500; m++)
\t\t\t\t{
\t\t\t\t\tstring mName = LoadMap.getNameMap(m);
\t\t\t\t\tif (!string.IsNullOrEmpty(mName) && mName.Trim().ToLower().Equals(pName))
\t\t\t\t\t{
\t\t\t\t\t\tpToMap = m;
\t\t\t\t\t\tbreak;
\t\t\t\t\t}
\t\t\t\t}

\t\t\t\tif (pToMap >= 0)
\t\t\t\t{
\t\t\t\t\tint dist = Math.Abs(pToMap - targetMap);
\t\t\t\t\tbool isCorrectDirection = (targetMap > currentMap && pToMap > currentMap) || (targetMap < currentMap && pToMap < currentMap);
\t\t\t\t\tif (isCorrectDirection && dist < minDistanceInMapId)
\t\t\t\t\t{
\t\t\t\t\t\tminDistanceInMapId = dist;
\t\t\t\t\t\tbestPortal = pt;
\t\t\t\t\t\tbestToMap = pToMap;
\t\t\t\t\t}
\t\t\t\t}
\t\t\t}

\t\t\tif (bestPortal != null)
\t\t\t{
\t\t\t\tMapTransition heuristicTrans = new MapTransition();
\t\t\t\theuristicTrans.fromMapId = currentMap;
\t\t\t\theuristicTrans.toMapId = bestToMap;
\t\t\t\theuristicTrans.type = TRANS_TYPE_VGO;
\t\t\t\theuristicTrans.portalName = bestPortal.name;
\t\t\t\theuristicTrans.portalX = bestPortal.x;
\t\t\t\theuristicTrans.portalY = bestPortal.y;
\t\t\t\treturn heuristicTrans;
\t\t\t}
\t\t}

\t\treturn null;
\t}

\tpublic static void executeTransitionStep(Player p, MapTransition step, long now)
\t{
\t\tif (step.type == TRANS_TYPE_VGO)
\t\t{
\t\t\tPoint targetPortal = null;
\t\t\tint currentMap = (GameCanvas.loadmap != null) ? GameCanvas.loadmap.idMap : -1;
\t\t\tif (LoadMap.vecPointChange != null && LoadMap.vecPointChange.size() > 0)
\t\t\t{
\t\t\t\t// 1. Check by closest coordinates if step has portalX, portalY from STATIC_MAP_EDGES or graph
\t\t\t\tif (step.portalX > 0 && step.portalY > 0)
\t\t\t\t{
\t\t\t\t\tint minDist = int.MaxValue;
\t\t\t\t\tfor (int i = 0; i < LoadMap.vecPointChange.size(); i++)
\t\t\t\t\t{
\t\t\t\t\t\tPoint pt = (Point)LoadMap.vecPointChange.elementAt(i);
\t\t\t\t\t\tif (pt != null)
\t\t\t\t\t\t{
\t\t\t\t\t\t\tint d = MainObject.getDistance(step.portalX, step.portalY, pt.x, pt.y);
\t\t\t\t\t\t\tif (d < minDist)
\t\t\t\t\t\t\t{
\t\t\t\t\t\t\t\tminDist = d;
\t\t\t\t\t\t\t\ttargetPortal = pt;
\t\t\t\t\t\t\t}
\t\t\t\t\t\t}
\t\t\t\t\t}
\t\t\t\t\tif (minDist > 80)
\t\t\t\t\t{
\t\t\t\t\t\ttargetPortal = null;
\t\t\t\t\t}
\t\t\t\t}

\t\t\t\t// 2. Check by explicit step.portalName
\t\t\t\tif (targetPortal == null && !string.IsNullOrEmpty(step.portalName))
\t\t\t\t{
\t\t\t\t\tfor (int i = 0; i < LoadMap.vecPointChange.size(); i++)
\t\t\t\t\t{
\t\t\t\t\t\tPoint pt = (Point)LoadMap.vecPointChange.elementAt(i);
\t\t\t\t\t\tif (pt != null && pt.name != null && pt.name.Trim().ToLower().Equals(step.portalName.Trim().ToLower()))
\t\t\t\t\t\t{
\t\t\t\t\t\t\ttargetPortal = pt;
\t\t\t\t\t\t\tbreak;
\t\t\t\t\t\t}
\t\t\t\t\t}
\t\t\t\t}

\t\t\t\t// 3. Check by resolving destination map name from step.toMapId
\t\t\t\tif (targetPortal == null && step.toMapId >= 0)
\t\t\t\t{
\t\t\t\t\tstring toMapName = LoadMap.getNameMap(step.toMapId);
\t\t\t\t\tif (!string.IsNullOrEmpty(toMapName))
\t\t\t\t\t{
\t\t\t\t\t\tfor (int i = 0; i < LoadMap.vecPointChange.size(); i++)
\t\t\t\t\t{
\t\t\t\t\t\t\tPoint pt = (Point)LoadMap.vecPointChange.elementAt(i);
\t\t\t\t\t\t\tif (pt != null && pt.name != null && pt.name.Trim().ToLower().Equals(toMapName.Trim().ToLower()))
\t\t\t\t\t\t\t{
\t\t\t\t\t\t\t\ttargetPortal = pt;
\t\t\t\t\t\t\t\tstep.portalName = pt.name;
\t\t\t\t\t\t\t\tstep.portalX = pt.x;
\t\t\t\t\t\t\t\tstep.portalY = pt.y;
\t\t\t\t\t\t\t\taddOrUpdateGraphTransition(step);
\t\t\t\t\t\t\t\tbreak;
\t\t\t\t\t\t\t}
\t\t\t\t\t\t}
\t\t\t\t\t}
\t\t\t\t}

\t\t\t\t// 4. Directional heuristic among portals on current map
\t\t\t\tif (targetPortal == null && LoadMap.vecPointChange.size() > 1 && currentMap >= 0)
\t\t\t\t{
\t\t\t\t\tPoint bestPt = null;
\t\t\t\t\tint minDiff = int.MaxValue;
\t\t\t\t\tfor (int i = 0; i < LoadMap.vecPointChange.size(); i++)
\t\t\t\t\t{
\t\t\t\t\t\tPoint pt = (Point)LoadMap.vecPointChange.elementAt(i);
\t\t\t\t\t\tif (pt == null || pt.name == null) continue;
\t\t\t\t\t\tstring pName = pt.name.Trim().ToLower();
\t\t\t\t\t\tint pToMap = -1;
\t\t\t\t\t\tfor (int m = 0; m < 500; m++)
\t\t\t\t\t\t{
\t\t\t\t\t\t\tstring mName = LoadMap.getNameMap(m);
\t\t\t\t\t\t\tif (!string.IsNullOrEmpty(mName) && mName.Trim().ToLower().Equals(pName))
\t\t\t\t\t\t\t{
\t\t\t\t\t\t\t\tpToMap = m;
\t\t\t\t\t\t\t\tbreak;
\t\t\t\t\t\t\t}
\t\t\t\t\t\t}
\t\t\t\t\t\tif (pToMap >= 0)
\t\t\t\t\t\t{
\t\t\t\t\t\t\tif (pToMap == step.fromMapId) continue; // Skip portal leading backward
\t\t\t\t\t\t\tint diff = Math.Abs(pToMap - step.toMapId);
\t\t\t\t\t\t\tbool sameDir = (step.toMapId > currentMap && pToMap > currentMap) || (step.toMapId < currentMap && pToMap < currentMap);
\t\t\t\t\t\t\tif (sameDir && diff < minDiff)
\t\t\t\t\t\t\t{
\t\t\t\t\t\t\t\tminDiff = diff;
\t\t\t\t\t\t\t\tbestPt = pt;
\t\t\t\t\t\t\t}
\t\t\t\t\t\t}
\t\t\t\t\t}
\t\t\t\t\tif (bestPt != null)
\t\t\t\t\t{
\t\t\t\t\t\ttargetPortal = bestPt;
\t\t\t\t\t\tstep.portalName = bestPt.name;
\t\t\t\t\t\tstep.portalX = bestPt.x;
\t\t\t\t\t\tstep.portalY = bestPt.y;
\t\t\t\t\t\taddOrUpdateGraphTransition(step);
\t\t\t\t\t}
\t\t\t\t}

\t\t\t\t// 5. Fallback: single portal or default
\t\t\t\tif (targetPortal == null)
\t\t\t\t{
\t\t\t\t\ttargetPortal = (Point)LoadMap.vecPointChange.elementAt(0);
\t\t\t\t}
\t\t\t}
"""

replacement_lines = [line + '\n' for line in replacement_text.split('\n')]
# Note: split('\n') may create an extra empty line at end if ends with \n
if replacement_lines and replacement_lines[-1] == '\n':
    replacement_lines.pop()

new_lines = lines[:3043] + replacement_lines + lines[3455:]

# Also update updateAutoReviveFallback
content = "".join(new_lines)
target_fallback = """\tpublic static void updateAutoReviveFallback(Player p)
\t{
\t\tif (p == null || (p.Hp > 0 && !p.isDie && p.Action != 4))
\t\t{
\t\t\treviveFallbackTime = 0;
\t\t\treturn;
\t\t}"""

replacement_fallback = """\tpublic static void updateAutoReviveFallback(Player p)
\t{
\t\tif (p == null || (p.Hp > 0 && !p.isDie && p.Action != 4))
\t\t{
\t\t\treviveFallbackTime = 0;
\t\t\tif (p == GameScreen.player && (wasSlaughterActive || wasTuDanhActive) && !shouldReturnToLastMap)
\t\t\t{
\t\t\t\tAThMadaraFunc.onPlayerRevived();
\t\t\t}
\t\t\treturn;
\t\t}"""

assert target_fallback in content, "target_fallback not found!"
content = content.replace(target_fallback, replacement_fallback, 1)

with open(unity_file, 'w', encoding='utf-8') as f:
    f.write(content)

print("AThMadaraMOD.cs successfully updated!")
