-- ============================================================================
-- SQL SCRIPT: CHUẨN HÓA VẬT PHẨM TRÁI ÁC QUỶ THANH LONG (ITEM 913)
-- Item ID: 913 | Name: 'Trái Thanh Long' | Icon: 769 | Type: 7 (Ăn Trái Ác Quỷ)
-- ============================================================================

DELETE FROM `item4` WHERE `id` = 913;

INSERT INTO `item4` (
    `id`, `name`, `icon`, `price`, `priceruby`, 
    `istrade`, `hpmpother`, `timedelay`, `value`, 
    `timeactive`, `nameuse`, `indexInfoPotion`
) VALUES (
    913, 'Trái Thanh Long', 769, 0, 0, 
    1, 7, 0, 0, 
    0, 'Sử dụng', 0
);

DELETE FROM `item4_info` WHERE `id` = 913;

INSERT INTO `item4_info` (`id`, `info`) VALUES (
    913, 'Trái Ác Quỷ Hệ Zoan Thần Thoại Uo Uo no Mi, Model: Seiryu (Thần Long Kaido). Khi sử dụng sẽ sở hữu toàn bộ 4 tuyệt kỹ Cổ Long Hoàng Kim, Thần Long Giáng Lôi, Long Thần Hộ Thể và Vảy Rồng Thần.'
);
