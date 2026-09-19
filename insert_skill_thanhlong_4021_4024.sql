-- =====================================================================
-- SQL SCRIPT: CHUAN HOA KY NANG TRAI AC QUY THANH LONG (KAIDO SEIRYU)
-- Danh cho he thong co so du lieu game Hai Tac Ti Hon
-- Skill IDs: 4021, 4022, 4023, 4024
-- Index: 813, 814, 815, 816 | ID_2: 5028, 5029, 5030, 5031
-- Icons: 446, 447, 448, 449 | Effects: 4021, 4022, 4023
-- Item: 913 (Trai Thanh Long, Icon 769, Type 7)
-- =====================================================================

-- 1. XOA DU LIEU CU TRANH XUNG DOT
DELETE FROM `skill` WHERE `id` BETWEEN 4021 AND 4024 OR `id_index` BETWEEN 813 AND 816 OR `id_2` BETWEEN 5028 AND 5031;

-- 2. INSERT 4 SKILL TRAI THANH LONG KAIDO
INSERT INTO `skill` (`id`, `id_index`, `id_2`, `icon`, `typeSkill`, `typeBuff`, `name`, `typeEffSkill`, `range`, `rangeLan`, `nTarget`, `damage`, `manaLost`, `timeDelay`, `nKick`, `info`, `Lv_RQ`, `percentLv`, `typeDevil`, `option`, `EffSpec`, `LvDevilSkill`, `phanTramDevilSkill`) VALUES
(
    4021, 813, 5028, 446, 1, 0,
    'Cổ Long Hoàng Kim', 4021, 220, 130, 4,
    1650, 90, 15000, 1,
    'Khai mở Ma Trận Chân Hoàng Kim triệu hoán Cổ Long Châu xé gió lao vào mục tiêu, bệ trụ thần long trỗi dậy ngoạm nát mặt đất. Kèm 45% tỷ lệ gây Choáng trong 3.0s. 800% sát thương của chiêu thức Quả đấm tốc độ.',
    1, 0, 1,
    '[[13,450],[10,400],[57,350],[58,300],[28,1],[29,450],[30,30]]',
    '[1,450,30]',
    0, 0
),
(
    4022, 814, 5029, 447, 1, 0,
    'Thần Long Giáng Lôi', 4022, 220, 150, 5,
    2500, 120, 45000, 3,
    'Hóa thân Thần Long Kaido ngự thiên xé toạc mây giông, há miệng phóng liên hoàn Lôi Cầu Tím Đen tạo đại bộc phá sấm sét rung chuyển trời đất. Kèm 50% tỷ lệ gây Bộc Phá Lôi Đình trong 4.0s. 1200% sát thương của chiêu thức Quả đấm tốc độ.',
    1, 0, 1,
    '[[10,750],[11,650],[13,700],[46,400],[28,6],[29,500],[30,40]]',
    '[6,500,40]',
    0, 0
),
(
    4023, 815, 5030, 448, 2, 1,
    'Long Thần Hộ Thể', 4023, 0, 0, 1,
    0, 100, 18000, 0,
    'Vận khí Long Thần Hộ Thể kích phát luồng hào quang rồng vàng bọc quanh toàn thân, gia hộ Kim Thân Bất Hoại: tăng 35% Công Lôi Đình, 30% Haki Bá Vương, 30% Giáp Long Lân, 25% Phản Sát Thương và 35% Miễn Thương trong 18.0s.',
    1, 0, 1,
    '[[32,180],[53,350],[1,350],[10,300],[14,300],[12,350]]',
    '[0,-1,-1]',
    0, 0
),
(
    4024, 816, 5031, 449, 3, 0,
    'Vảy Rồng Thần', 0, 0, 0, 0,
    0, 0, 0, 0,
    'Huyết mạch Zoan Thần Thoại vĩnh viễn cường hóa nhục thân: tăng 40% Công Lôi Đình, 35% HP tối đa, 30% Giáp Long Lân, 30% Haki Bá Vương, 25% Miễn Thương và 35% Kháng Mọi Hiệu Ứng Khống Chế.',
    1, 0, 1,
    '[[1,400],[17,350],[10,100],[11,450],[53,250],[71,350]]',
    '[0,-1,-1]',
    0, 0
);

-- 3. XOA VA INSERT ITEM TRAI THANH LONG (ITEM 913)
DELETE FROM `item4` WHERE `id` = 913;
INSERT INTO `item4` (`id`, `name`, `icon`, `price`, `priceruby`, `istrade`, `hpmpother`, `timedelay`, `value`, `timeactive`, `nameuse`, `indexInfoPotion`) VALUES (
    913, 'Trái Thanh Long', 769, 0, 0,
    1, 7, 0, 0,
    0, 'Sử dụng', 0
);

-- 4. XOA VA INSERT MO TA VAT PHAM ITEM4_INFO
DELETE FROM `item4_info` WHERE `id` = 913;
INSERT INTO `item4_info` (`id`, `info`) VALUES (
    913, 'Trái Ác Quỷ Hệ Zoan Thần Thoại Uo Uo no Mi, Model: Seiryu (Thần Long Kaido). Khi sử dụng sẽ sở hữu toàn bộ 4 tuyệt kỹ Cổ Long Hoàng Kim, Thần Long Giáng Lôi, Long Thần Hộ Thể và Vảy Rồng Thần.'
);
