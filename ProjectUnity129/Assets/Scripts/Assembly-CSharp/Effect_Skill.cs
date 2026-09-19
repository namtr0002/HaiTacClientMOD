using System;

public class Effect_Skill : MainEffect
{
	public const int TYPE_NIKA_ACTIVE_1_LEVEL1 = 3100;
	public const int TYPE_NIKA_ACTIVE_1_LEVEL5 = 3101;
	public const int TYPE_NIKA_ACTIVE_2 = 3102;
	public const int TYPE_NIKA_BUFF = 3103;
	public const int TYPE_LIGHT_ACTIVE_1_LEVEL5 = 3104;
	public const int TYPE_LIGHT_ACTIVE_2_LEVEL5 = 3105;
	public const int TYPE_LOVE_ACTIVE_1_LEVEL5 = 3106;
	public const int TYPE_LOVE_ACTIVE_2_LEVEL5 = 3107;
	public const int TYPE_NIKYU_ACTIVE_1 = 3120;
	public const int TYPE_NIKYU_ACTIVE_2 = 3121;
	public const int TYPE_NIKYU_BUFF = 3122;

	private const short NIKYU_PROJECTILE = 3120;
	private const short NIKYU_IMPACT = 3121;
	private const short NIKYU_DASH = 3122;
	private const short NIKYU_REPULSION = 3123;
	private const short NIKYU_BUFF_EFF = 3124;
	private const short NIKYU_PASSIVE_EFF = 3125;

	private const short LIGHT_LEVEL5_CAST = 37;
	private const short LIGHT_LEVEL5_PROJECTILE = 38;
	private const short LIGHT_LEVEL5_IMPACT = 39;
	private const short LIGHT_LEVEL5_ACTIVE_2_START = 40;
	private const short LOVE_LEVEL5_FINISH_ATTACHED = 20;
	private const short LOVE_LEVEL5_FINISH_IMPACT = 36;

	private const int NIKA_VARIANT_ACTIVE_1_LEVEL1 = 0;
	private const int NIKA_VARIANT_ACTIVE_1_LEVEL5 = 1;
	private const int NIKA_VARIANT_ACTIVE_2 = 2;

	private const short NIKA_DATA_LEVEL1_START = 61;
	private const short NIKA_DATA_LEVEL1_JUMP = 62;
	private const short NIKA_DATA_LEVEL1_LANDING_LEFT = 63;
	private const short NIKA_DATA_LEVEL1_LANDING_RIGHT = 64;
	private const short NIKA_DATA_LEVEL5_START = 65;
	private const short NIKA_DATA_LEVEL5_JUMP = 66;
	private const short NIKA_DATA_LEVEL5_LANDING_LEFT = 67;
	private const short NIKA_DATA_LEVEL5_LANDING_RIGHT = 68;
	private const short NIKA_DATA_BUFF = 69;
	private const short NIKA_DATA_ACTIVE_2 = 70;

	public const sbyte EFF_NORMAL = 0;

	public const short EFF_ZORO_1 = 154;

	public const short EFF_TASHIGI_2 = 155;

	public const sbyte EFF_LUFFY_S1_L1 = 21;

	public const sbyte EFF_LUFFY_S1_L2 = 33;

	public const short EFF_LUFFY_S1_L3 = 83;

	public const short EFF_LUFFY_S1_L4 = 180;

	public const short EFF_LUFFY_S1_L5 = 212;

	public const short EFF_LUFFY_S1_L6 = 271;

	public const short EFF_LUFFY_S1_L7 = 471;

	public const sbyte EFF_LUFFY_S2_L1 = 34;

	public const sbyte EFF_LUFFY_S2_L2 = 35;

	public const short EFF_LUFFY_S2_L3 = 84;

	public const short EFF_LUFFY_S2_L4 = 181;

	public const short EFF_LUFFY_S2_L5 = 213;

	public const short EFF_LUFFY_S2_L6 = 272;

	public const short EFF_LUFFY_S2_L7 = 472;

	public const sbyte EFF_LUFFY_S3_L1 = 1;

	public const sbyte EFF_LUFFY_S3_L2 = 37;

	public const short EFF_LUFFY_S3_L3 = 85;

	public const short EFF_LUFFY_S3_L4 = 182;

	public const short EFF_LUFFY_S3_L5 = 214;

	public const short EFF_LUFFY_S3_L6 = 273;

	public const short EFF_LUFFY_S3_L7 = 473;

	public const short EFF_LUFFY_SEA_L1 = 133;

	public const short EFF_LUFFY_SEA_L2 = 134;

	public const short EFF_LUFFY_SEA_L3 = 135;

	public const sbyte EFF_ZORO_S1_L1 = 38;

	public const sbyte EFF_ZORO_S1_L2 = 15;

	public const short EFF_ZORO_S1_L3 = 86;

	public const short EFF_ZORO_S1_L4 = 183;

	public const short EFF_ZORO_S1_L5 = 215;

	public const short EFF_ZORO_S1_L6 = 281;

	public const short EFF_ZORO_S1_L7 = 481;

	public const sbyte EFF_ZORO_S2_L1 = 41;

	public const sbyte EFF_ZORO_S2_L2 = 29;

	public const short EFF_ZORO_S2_L3 = 87;

	public const short EFF_ZORO_S2_L4 = 184;

	public const short EFF_ZORO_S2_L5 = 216;

	public const short EFF_ZORO_S2_L6 = 282;

	public const short EFF_ZORO_S2_L7 = 482;

	public const sbyte EFF_ZORO_S3_L1 = 121;

	public const sbyte EFF_ZORO_S3_L2 = 122;

	public const short EFF_ZORO_S3_L3 = 123;

	public const short EFF_ZORO_S3_L4 = 185;

	public const short EFF_ZORO_S3_L5 = 217;

	public const short EFF_ZORO_S3_L6 = 283;

	public const short EFF_ZORO_S3_L7 = 483;

	public const sbyte EFF_ZORO_SEA_L1 = 42;

	public const sbyte EFF_ZORO_SEA_L2 = 43;

	public const sbyte EFF_ZORO_SEA_L3 = 19;

	public const sbyte EFF_SANJI_S1_L1 = 14;

	public const sbyte EFF_SANJI_S1_L2 = 44;

	public const short EFF_SANJI_S1_L3 = 124;

	public const short EFF_SANJI_S1_L4 = 186;

	public const short EFF_SANJI_S1_L5 = 218;

	public const short EFF_SANJI_S1_L6 = 291;

	public const short EFF_SANJI_S1_L7 = 491;

	public const sbyte EFF_SANJI_S2_L1 = 47;

	public const sbyte EFF_SANJI_S2_L2 = 48;

	public const short EFF_SANJI_S2_L3 = 125;

	public const short EFF_SANJI_S2_L4 = 187;

	public const short EFF_SANJI_S2_L5 = 219;

	public const short EFF_SANJI_S2_L6 = 292;

	public const short EFF_SANJI_S2_L7 = 492;

	public const sbyte EFF_SANJI_S3_L1 = 49;

	public const sbyte EFF_SANJI_S3_L2 = 50;

	public const short EFF_SANJI_S3_L3 = 12;

	public const short EFF_SANJI_S3_L4 = 188;

	public const short EFF_SANJI_S3_L5 = 220;

	public const short EFF_SANJI_S3_L6 = 293;

	public const short EFF_SANJI_S3_L7 = 493;

	public const short EFF_SANJI_SEA_L1 = 136;

	public const short EFF_SANJI_SEA_L2 = 137;

	public const short EFF_SANJI_SEA_L3 = 138;

	public const sbyte EFF_NAMI_S1_L1 = 16;

	public const sbyte EFF_NAMI_S1_L2 = 51;

	public const short EFF_NAMI_S1_L3 = 52;

	public const short EFF_NAMI_S1_L4 = 189;

	public const short EFF_NAMI_S1_L5 = 221;

	public const short EFF_NAMI_S1_L6 = 311;

	public const short EFF_NAMI_S1_L7 = 511;

	public const sbyte EFF_NAMI_S2_L1 = 9;

	public const sbyte EFF_NAMI_S2_L2 = 53;

	public const short EFF_NAMI_S2_L3 = 63;

	public const short EFF_NAMI_S2_L4 = 190;

	public const short EFF_NAMI_S2_L5 = 222;

	public const short EFF_NAMI_S2_L6 = 312;

	public const short EFF_NAMI_S2_L7 = 512;

	public const sbyte EFF_NAMI_S3_L1 = 31;

	public const sbyte EFF_NAMI_S3_L2 = 55;

	public const short EFF_NAMI_S3_L3 = 56;

	public const short EFF_NAMI_S3_L4 = 191;

	public const short EFF_NAMI_S3_L5 = 223;

	public const short EFF_NAMI_S3_L6 = 313;

	public const short EFF_NAMI_S3_L7 = 513;

	public const sbyte EFF_NAMI_SEA_L1 = 11;

	public const short EFF_NAMI_SEA_L2 = 139;

	public const short EFF_NAMI_SEA_L3 = 140;

	public const sbyte EFF_USSOP_S1_L1 = 57;

	public const sbyte EFF_USSOP_S1_L2 = 58;

	public const short EFF_USSOP_S1_L3 = 126;

	public const short EFF_USSOP_S1_L4 = 192;

	public const short EFF_USSOP_S1_L5 = 224;

	public const short EFF_USSOP_S1_L6 = 301;

	public const short EFF_USSOP_S1_L7 = 501;

	public const sbyte EFF_USSOP_S2_L1 = 64;

	public const sbyte EFF_USSOP_S2_L2 = 66;

	public const short EFF_USSOP_S2_L3 = 127;

	public const short EFF_USSOP_S2_L4 = 193;

	public const short EFF_USSOP_S2_L5 = 225;

	public const short EFF_USSOP_S2_L6 = 302;

	public const short EFF_USSOP_S2_L7 = 502;

	public const sbyte EFF_USSOP_S3_L1 = 67;

	public const sbyte EFF_USSOP_S3_L2 = 68;

	public const short EFF_USSOP_S3_L3 = 69;

	public const short EFF_USSOP_S3_L4 = 194;

	public const short EFF_USSOP_S3_L5 = 226;

	public const short EFF_USSOP_S3_L6 = 303;

	public const short EFF_USSOP_S3_L7 = 503;

	public const sbyte EFF_USSOP_SEA_L1 = 7;

	public const short EFF_USSOP_SEA_L2 = 141;

	public const short EFF_USSOP_SEA_L3 = 142;

	public const short EFF_LUFFY_S1_L3_OLD = 156;

	public const short EFF_LUFFY_S2_L3_OLD = 160;

	public const short EFF_ZORO_S1_L3_OLD = 157;

	public const short EFF_ZORO_S2_L3_SHORT_OLD = 161;

	public const short EFF_SANJI_S1_L3_OLD = 158;

	public const short EFF_SANJI_S2_L3_OLD = 162;

	public const short EFF_NAMI_S2_L3_OLD = 163;

	public const short EFF_USSOP_S1_L3_OLD = 159;

	public const sbyte EFF_BUFF = 46;

	public const short EFF_BUFF_2 = 165;

	public const short EFF_END_BUFF_2 = 166;

	public const short EFF_GET_MONEY = 17;

	public const short EFF_CAUSU_1 = 164;

	public const short EFF_CAUSU_1_L2 = 227;

	public const sbyte EFF_ACE_1 = 2;

	public const short EFF_ACE_1_L2 = 228;

	public const short EFF_ACE_1_L2_SUPER_1 = 259;

	public const short EFF_ACE_1_L2_SUPER_2 = 260;

	public const short EFF_ACE_1_L2_SUPER_3 = 261;

	public const sbyte EFF_ACE_2 = 3;

	public const short EFF_ACE_2_L2 = 229;

	public const short EFF_ACE_2_L2_SUPER_1 = 262;

	public const short EFF_ACE_2_L2_SUPER_2 = 263;

	public const short EFF_ACE_2_L2_SUPER_3 = 264;

	public const sbyte EFF_AOKIJI_1 = 4;

	public const short EFF_AOKIJI_1_L2 = 230;

	public const sbyte EFF_AOKIJI_2 = 5;

	public const short EFF_AOKIJI_2_L2 = 231;

	public const sbyte EFF_SMOKER_1 = 6;

	public const short EFF_SMOKER_1_L2 = 232;

	public const sbyte EFF_SMOKER_2 = 10;

	public const short EFF_SMOKER_2_L2 = 234;

	public const sbyte EFF_CROCODILE_1 = 25;

	public const short EFF_CROCODILE_1_L2 = 235;

	public const sbyte EFF_CROCODILE_2 = 26;

	public const short EFF_CROCODILE_2_L2 = 236;

	public const short EFF_SET_1 = 169;

	public const short EFF_SET_1_L2 = 237;

	public const short EFF_SET_2 = 170;

	public const short EFF_SET_2_L2 = 238;

	public const short EFF_NHAM_THACH_1 = 171;

	public const short EFF_NHAM_THACH_1_L2 = 239;

	public const short EFF_NHAM_THACH_2 = 172;

	public const short EFF_NHAM_THACH_2_L2 = 240;

	public const short EFF_PELL_1 = 179;

	public const short EFF_PELL_1_L2 = 241;

	public const short EFF_LUCCI_1 = 209;

	public const short EFF_LUCCI_1_L2 = 242;

	public const short EFF_DONG_DAT_1 = 210;

	public const short EFF_DONG_DAT_1_L2 = 243;

	public const short EFF_DONG_DAT_2 = 211;

	public const short EFF_DONG_DAT_2_L2 = 244;

	public const short EFF_MR5_1 = 233;

	public const short EFF_DAO_1 = 245;

	public const short EFF_DAO_1_L2 = 251;

	public const short EFF_SAP_1 = 246;

	public const short EFF_SAP_1_L2 = 253;

	public const short EFF_SAP_2 = 247;

	public const short EFF_SAP_2_L2 = 254;

	public const short EFF_KILO_1 = 248;

	public const short EFF_KILO_1_L2 = 255;

	public const short EFF_DAO_2 = 249;

	public const short EFF_DAO_2_L2 = 252;

	public const short EFF_RANKYAKU = 266;

	public const short EFF_SHIGAN = 267;

	public const short EFF_DOOR = 268;

	public const short EFF_DOOR_L2 = 269;

	public const short EFF_KUMADORI = 270;

	public const short EFF_XA_PHONG = 274;

	public const short EFF_XA_PHONG_L2 = 275;

	public const short EFF_SOI = 276;

	public const short EFF_SOI_L2 = 277;

	public const short EFF_HUOU = 278;

	public const short EFF_HUOU_L2 = 279;

	public const short EFF_GOAL = 280;

	public const short EFF_THUNDER_FALLS_1 = 1998;

	public const short EFF_THUNDER_FALLS_2 = 1999;

	public const short EFF_FIRE_EXPLORE = 2000;

	public const sbyte EFF_MON_1 = 30;

	public const sbyte EFF_MON_2 = 71;

	public const sbyte EFF_MON_3 = 72;

	public const sbyte EFF_MON_4 = 73;

	public const sbyte EFF_MON_5 = 74;

	public const sbyte EFF_MON_6 = 75;

	public const sbyte EFF_ALVIDA_1 = 76;

	public const sbyte EFF_ALVIDA_2 = 77;

	public const sbyte EFF_MON_7 = 78;

	public const sbyte EFF_MON_8 = 79;

	public const sbyte EFF_MON_9 = 80;

	public const sbyte EFF_MON_10 = 81;

	public const sbyte EFF_MON_11 = 82;

	public const sbyte EFF_MORGAN_1 = 88;

	public const sbyte EFF_MORGAN_2 = 89;

	public const sbyte EFF_HELMEPO_1 = 90;

	public const sbyte EFF_HELMEPO_2 = 91;

	public const sbyte EFF_MON_12 = 92;

	public const sbyte EFF_MOHJI_1 = 93;

	public const sbyte EFF_MOHJI_2 = 94;

	public const sbyte EFF_BUGGY_1 = 95;

	public const sbyte EFF_BUGGY_2 = 96;

	public const sbyte EFF_CABAJI_1 = 97;

	public const sbyte EFF_CABAJI_2 = 98;

	public const sbyte EFF_NYABAN_1 = 99;

	public const sbyte EFF_NYABAN_2 = 100;

	public const sbyte EFF_NYABAN_3 = 101;

	public const sbyte EFF_JANGO_1 = 102;

	public const sbyte EFF_KURO_1 = 103;

	public const sbyte EFF_KURO_2 = 104;

	public const sbyte EFF_PEARL_1 = 105;

	public const sbyte EFF_PEARL_2 = 106;

	public const sbyte EFF_GHIN_1 = 107;

	public const sbyte EFF_GHIN_2 = 108;

	public const sbyte EFF_DON_KRIEG_1 = 109;

	public const sbyte EFF_DON_KRIEG_2 = 110;

	public const sbyte EFF_DON_KRIEG_3 = 111;

	public const sbyte EFF_HACHI_1 = 112;

	public const sbyte EFF_HACHI_2 = 113;

	public const sbyte EFF_CHU_1 = 114;

	public const sbyte EFF_CHU_2 = 115;

	public const sbyte EFF_KUROBI_1 = 116;

	public const sbyte EFF_KUROBI_2 = 117;

	public const sbyte EFF_ARLONG_1 = 118;

	public const sbyte EFF_ARLONG_2 = 119;

	public const sbyte EFF_ARLONG_3 = 120;

	public const short EFF_MON_13 = 128;

	public const short EFF_MON_14 = 129;

	public const short EFF_MON_15 = 130;

	public const short EFF_MON_16 = 131;

	public const short EFF_MON_17 = 132;

	public const short EFF_MON_18 = 143;

	public const short EFF_MON_19 = 144;

	public const short EFF_MON_20 = 145;

	public const short EFF_MON_21 = 146;

	public const short EFF_MON_22 = 147;

	public const short EFF_MON_23 = 148;

	public const short EFF_MON_24 = 149;

	public const short EFF_MON_25 = 150;

	public const short EFF_MON_26 = 151;

	public const short EFF_MON_27 = 152;

	public const short EFF_MON_28 = 153;

	public const short EFF_MON_SMOKER_1 = 13;

	public const short EFF_MON_SMOKER_2 = 18;

	public const short EFF_MON_VALENTINE = 20;

	public const short EFF_MON_VALENTINE_2 = 22;

	public const short EFF_MON_MR5 = 23;

	public const short EFF_MON_MR5_2 = 24;

	public const short EFF_MON_CHESS = 27;

	public const short EFF_MON_KUROMARIMO = 28;

	public const short EFF_WAPOL_1 = 32;

	public const short EFF_WAPOL_2 = 36;

	public const short EFF_WAPOL_3 = 39;

	public const short EFF_WAPOL_4 = 40;

	public const short EFF_MR3_1 = 45;

	public const short EFF_MR3_2 = 54;

	public const short EFF_MISS_GOLDEN_WEEKEND_1 = 59;

	public const short EFF_MISS_GOLDEN_WEEKEND_2 = 60;

	public const short EFF_LAPIN = 61;

	public const short EFF_MON_29 = 62;

	public const sbyte EFF_MR4_1 = 65;

	public const sbyte EFF_MR4_2 = 70;

	public const short EFF_MISS_MS_1 = 167;

	public const short EFF_MR1_1 = 168;

	public const short EFF_MR1_2 = 173;

	public const short EFF_DF_1 = 174;

	public const short EFF_DF_2 = 175;

	public const short EFF_MR2_1 = 176;

	public const short EFF_MR2_2 = 177;

	public const short EFF_MR0_1 = 178;

	public const short EFF_ENEL_1 = 195;

	public const short EFF_ENEL_2 = 196;

	public const short EFF_ENEL_3 = 197;

	public const short EFF_SATORI_1 = 198;

	public const short EFF_SATORI_2 = 199;

	public const short EFF_OHM_1 = 200;

	public const short EFF_OHM_2 = 201;

	public const short EFF_GEDATSU_1 = 202;

	public const short EFF_GEDATSU_2 = 203;

	public const short EFF_SHURA_1 = 204;

	public const short EFF_SHURA_2 = 205;

	public const short EFF_LINH_TROI_1 = 206;

	public const short EFF_LINH_TROI_2 = 207;

	public const short EFF_TRU_1 = 208;

	public const short EFF_TRU_2_LAN = 250;

	public const short EFF_THA_DEN = 256;

	public const short EFF_THA_PHAO_HOA = 257;

	public const short EFF_SNOW_DOWN = 258;

	public const short EFF_LAW_HEART = 265;

	public const short EFF_PANTHEONG_1 = 10001;

	public const short EFF_PANTHEONG_2 = 10002;

	public const short EFF_GALIO_1 = 10003;

	public const short EFF_GALIO_2 = 10004;

	public const short EFF_NO_NANG_LUONG_1 = 10005;

	public const short EFF_NO_NANG_LUONG_2 = 10006;

	public const short EFF_NO_NANG_LUONG_3 = 10007;

	public const short EFF_NO_THEO_HUONG_1 = 10008;

	public const short EFF_NO_THEO_HUONG_2 = 10009;

	public const short EFF_XERATH_1 = 10010;

	public const short EFF_XERATH_2 = 10011;

	public const short EFF_XERATH_3 = 10012;

	public const short EFF_URGOT_1 = 10013;

	public const short EFF_URGOT_2 = 10014;

	public const short EFF_URGOT_3 = 10015;

	public const short EFF_URGOT_4 = 10016;

	public const short EFF_MONSTER_HUT_MAU = 10017;

	public const short EFF_MONSTER_CHAY_THANG_1 = 10018;

	public const short EFF_MONSTER_CHAY_THANG_2 = 10019;

	public const short EFF_MONSTER_GIAP_GAI = 10020;

	public const short EFF_MONSTER_HUT_MANA = 10021;

	public const short EFF_MONSTER_DANH_TRON_1 = 10022;

	public const short EFF_MONSTER_DANH_TRON_2 = 10023;

	public const short EFF_MONSTER_NEM_BOOM_1 = 10024;

	public const short EFF_MONSTER_NEM_BOOM_2 = 10025;

	public const short EFF_MONSTER_KHONG_DANH_1 = 10026;

	public const short EFF_MONSTER_KHONG_DANH_2 = 10027;

	public const short EFF_TRAI_AC_QUY_HO_DEN_VU_TRU = 10028;

	public const short EFF_BLACK_HOLE_1 = 400;

	public const short EFF_TRAI_AC_QUY_HO_DEN_VU_TRU_3 = 10030;

	public const short EFF_BLACK_HOLE_2 = 401;

	public const short EFF_BLACK_HOLE_1_LV5 = 402;

	public const short EFF_BLACK_HOLE_2_LV5 = 403;

	public const sbyte EFF_DAN_FOCUS = -1;

	// Trái Ánh Sáng (Kizaru - Light Fruit)
	public const short EFF_KIZURA_1           = 404; // Kizaru Skill1 Lv<5
	public const short EFF_KIZURA_2           = 405; // Kizaru Skill2 Lv<5
	public const short EFF_KIZURA_1_LV5       = 406; // Kizaru Skill1 Lv=5 (upgrade từ 404)
	public const short EFF_KIZURA_2_LV5       = 407; // Kizaru Skill2 Lv=5 (upgrade từ 405)

	// Trái Tình Yêu (Hancock - Love Fruit)
	public const short EFF_BOAHANDCOCK_1      = 408; // Hancock Skill1 Lv<5
	public const short EFF_BOAHANDCOCK_2      = 409; // Hancock Skill2 Lv<5
	public const short EFF_BOAHANDCOCK_1_LV5  = 410; // Hancock Skill1 Lv=5 (upgrade từ 408)
	public const short EFF_BOAHANDCOCK_2_LV5  = 411; // Hancock Skill2 Lv=5 (upgrade từ 409)

	public int[][][] skillZoro3 = new int[4][][]
	{
		new int[2][]
		{
			new int[3] { 4, -26, -23 },
			new int[3] { 1, -10, 20 }
		},
		new int[4][]
		{
			new int[3] { 3, -15, -43 },
			new int[3] { 1, 5, -26 },
			new int[3] { 3, -28, -4 },
			new int[3] { 4, -28, 10 }
		},
		new int[5][]
		{
			new int[3] { 0, -27, -90 },
			new int[3] { 3, -27, -76 },
			new int[3] { 4, -22, -58 },
			new int[3] { 2, -10, -30 },
			new int[3] { 1, 0, -14 }
		},
		new int[4][]
		{
			new int[3] { 3, -44, -70 },
			new int[3] { 0, -44, -45 },
			new int[3] { 4, -36, -21 },
			new int[3] { 3, -24, 0 }
		}
	};

	public int[][] Mr1 = new int[6][]
	{
		new int[3] { 3, -15, -35 },
		new int[3] { 4, -25, -32 },
		new int[3] { 5, -25, -27 },
		new int[3] { 0, -30, -20 },
		new int[3] { 1, -30, -20 },
		new int[3] { 2, -30, -20 }
	};

	public int[][] speedEff;

	public int subType;

	public int waitTick;

	public int indexObjBefire;

	public int xArchor;

	public int yArchor;

	public int[][] plusxy;

	public int[][] mframeSuper;

	public int fPlayFrameSuper;

	public int fPre;

	public FrameImage[] mImgframe;

	public bool isAddSound;

	public bool isaddEff;

	public MainObject objBeFireMain;

	public MainSkill skill;

	public mVector VecEff = new mVector();

	public mVector VecSubEff = new mVector();

	public mVector vecPos = new mVector();

	public mVector vectargetPos = new mVector();

	public int[] xLech;

	public int[] yLech;

	public short[] posSmock = new short[7] { 0, 50, 75, 100, 20, 110, 30 };

	private int vXTam;

	private int vYTam;

	private new int x1000;

	private new int y1000;

	private int vX1000;

	private int vY1000;

	private int xEff;

	private int yEff;

	private int angle;

	private int R;

	private int size1;

	private int[][] mTamgiac;

	private int lT_Arc;

	private int gocT_Arc;

	private int r;

	public int[] radian = new int[12]
	{
		0, 30, 60, 90, 120, 150, 180, 210, 240, 270,
		300, 330
	};

	public int CR = 15;

	public int Ctick;

	public int t2;

	private int disHard;

	private int tickadd;

	private Point_Focus rocket1;

	private Point_Focus rocket2;

	private int frame1;

	private int frame2;

	private int xLight1;

	private int xLight2;

	private int giatocFly;

	private int fSpeedTest;

	public const short EFF_SPEC_CRI = 1010;

	public const short EFF_SPEC_GIAP = 1013;

	public const short EFF_SPEC_PHAN = 1014;

	public const short EFF_SPEC_HUT_HP = 1021;

	public const short EFF_SPEC_HUT_MP = 1022;

	public const short EFF_SPEC_HAP_THU = 1058;
	public int enelOrigX;
	public int enelOrigY;
	public int phoenixOrigX;
	public int phoenixOrigY;
	public int phoenixTargetX;
	public int phoenixTargetY;
	public int marcoWaveHitMask;
	public int daibutsuCasterX;
	public int daibutsuCasterY;
	public int daibutsuImpactX;
	public int daibutsuImpactY;
	public int kidCannonImpactX;
	public int kidCannonImpactY;


	public Effect_Skill(MainSkill skill, MainObject objEffFire, int x, int y, mVector vec)
	{
		indexObjBefire = 0;
		valueEffect = 0;
		if (LoadMapScreen.isNextMap)
		{
			base.x = x;
			base.y = y;
			vecPos = vec;
			Dir = skill.Dirbuff;
			if (vecPos != null && vecPos.size() > 0)
			{
				Point_Focus point_Focus = (Point_Focus)vecPos.elementAt(0);
				toX = point_Focus.x;
				toY = point_Focus.y;
			}
			else
			{
				toX = x;
				toY = y;
			}
			timeAddNum = -1;
			objBeFireMain = objEffFire;
			isStop = false;
			isRemove = false;
			f = -1;
			ysai = 0;
			Skill_Info _sk = (skill != null) ? Skill_Info.getSkillFromID(skill.ID) : null;
			int _skillIdx = (_sk != null) ? _sk.indexSkillInServer : -1;
			typeEffect = (skill != null) ? getUpgradedEffSkill(skill.typeEffSkill, skill.lvDevil, _skillIdx) : (short)0;
			
			subType = skill.typeEffBuff;
			timeBegin = skill.timeBegin;
			timeEnd = skill.timebuff;
			this.skill = skill;
			objFireMain = objEffFire;
			isEff = true;
			numNextFrame = 1;
		}
	}

	public static short getUpgradedEffSkill(short baseEff, sbyte lvDevil)
	{
		return getUpgradedEffSkill(baseEff, lvDevil, -1);
	}

	public static short getUpgradedEffSkill(short baseEff, sbyte lvDevil, int skillIndex)
	{
		if (lvDevil < 5) return baseEff;
		// Trái Bóng Tối (Teach - Dark): Skill1 400->402, Skill2 401->403
		if (baseEff == 400) return 402;
		if (baseEff == 401) return 403;
		// Trái Ánh Sáng (Kizaru - Light): Skill1 404->406, Skill2 405->407
		if (baseEff == 404) return 406;
		if (baseEff == 405) return 407;
		// Trái Tình Yêu (Hancock - Love): Skill1 408->410, Skill2 409->411
		if (baseEff == 408) return 410;
		if (baseEff == 409) return 411;
		// Trái Nika: Skill1 3100->3101, Skill2 3102->3102, Buff 3103->3103
		if (baseEff == 3100) return 3101;
		if (baseEff == 3102) return 3102;
		if (baseEff == 3103) return 3103;
		switch (baseEff)
		{
			case 2: return 228;
			case 3: return 229;
			case 4: return 230;
			case 5: return 231;
			case 6: return 232;
			case 10: return 234;
			case 25: return 235;
			case 164: return 227;
			case 245: return 251;
			case 246: return 253;
			case 247: return 254;
			case 248: return 255;
			case 249: return 252;
			default: return baseEff;
		}
	}

	public Effect_Skill(MainSkill skill, MainObject objEffFire)
	{
		indexObjBefire = 0;
		valueEffect = 0;
		if (LoadMapScreen.isNextMap)
		{
			timeAddNum = -1;
			objBeFireMain = objEffFire;
			isStop = false;
			isRemove = false;
			f = -1;
			ysai = 0;
			Skill_Info _sk = (skill != null) ? Skill_Info.getSkillFromID(skill.ID) : null;
			int _skillIdx = (_sk != null) ? _sk.indexSkillInServer : -1;
			typeEffect = (skill != null) ? getUpgradedEffSkill(skill.typeEffSkill, skill.lvDevil, _skillIdx) : (short)0;
			
			subType = skill.typeEffBuff;
			timeBegin = skill.timeBegin;
			this.skill = skill;
			timeEnd = (short)skill.timebuff;
			objFireMain = objEffFire;
			isEff = true;
			numNextFrame = 1;
		}
	}

	public Effect_Skill()
	{
	}

	public Effect_Skill(int typeKill, int subtype, MainObject objEffFire, mVector vec)
	{
		indexObjBefire = 0;
		valueEffect = 0;
		if (!LoadMapScreen.isNextMap)
		{
			return;
		}
		timeAddNum = -1;
		objBeFireMain = null;
		subType = subtype;
		isStop = false;
		isRemove = false;
		if (vec == null || vec.size() == 0)
		{
			return;
		}
		vecObjsBeFire = vec;
		f = -1;
		ysai = 0;
		typeEffect = typeKill;
		timeBegin = GameCanvas.timeNow;
		objFireMain = objEffFire;
		Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
		if (object_Effect_Skill != null)
		{
			objBeFireMain = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
		}
		if (objBeFireMain != null && objFireMain != null)
		{
			isEff = false;
			if (objFireMain == GameScreen.player && LoadMap.specMap != 3)
			{
				isEff = true;
			}
			numNextFrame = 1;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			toX = objBeFireMain.x;
			toY = objBeFireMain.y - objBeFireMain.hOne / 2;
			if (objFireMain != objBeFireMain)
			{
				setAngle();
				objFireMain.type_left_right = Dir;
				objFireMain.Dir = Dir;
			}
		}
	}

	public Effect_Skill(int typeKill, int subtype, MainObject objEffFire, mVector vec, int x, int y)
	{
		indexObjBefire = 0;
		valueEffect = 0;
		if (!LoadMapScreen.isNextMap)
		{
			return;
		}
		timeAddNum = -1;
		objBeFireMain = null;
		subType = subtype;
		isStop = false;
		isRemove = false;
		if (vec == null || vec.size() == 0)
		{
			return;
		}
		vecObjsBeFire = vec;
		f = -1;
		ysai = 0;
		typeEffect = typeKill;
		timeBegin = GameCanvas.timeNow;
		objFireMain = objEffFire;
		Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
		if (object_Effect_Skill != null)
		{
			objBeFireMain = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
		}
		if (objBeFireMain != null && objFireMain != null)
		{
			isEff = false;
			if (objFireMain == GameScreen.player && LoadMap.specMap != 3)
			{
				isEff = true;
			}
			numNextFrame = 1;
			base.x = x;
			base.y = y;
			toX = objBeFireMain.x;
			toY = objBeFireMain.y - objBeFireMain.hOne / 2;
			if (objFireMain != objBeFireMain)
			{
				setAngle();
				objFireMain.type_left_right = Dir;
				objFireMain.Dir = Dir;
			}
		}
	}

	private void createEffFireExplore()
	{
		if (isAddSound) return;
		indexImg = 0;
		fraImgEff = new FrameImage(mImage.createImage("/test_eff/skill2/eff.png"), 18);
		x = GameScreen.player.x;
		y = GameScreen.player.y;
		Player.isBlock = true;
	}

	private void createEffThunderFalls()
	{
		indexImg = 0;
		fImg = -1;
		fSub = (fSub2 = (fSub3 = -1));
		fRemove = 24;
		fraImgEff = new FrameImage(mImage.createImage("/test_eff/skill1/eff1.png"), 4);
		fraImgSubEff = new FrameImage(mImage.createImage("/test_eff/skill1/eff2.png"), 4);
		fraImgSub2Eff = new FrameImage(mImage.createImage("/test_eff/skill1/eff3.png"), 4);
		x = objBeFireMain.x;
		y = objBeFireMain.y;
		xLech = new int[4] { -50, 70, 55, -30 };
		yLech = new int[4] { -30, -20, 30, 20 };
		if (isAddSound)
		{
			mSound.playSound(17, mSound.volumeSound);
		}
	}

	private void updateEffFireExplore()
	{
		if (f % 3 == 0 && !isPauseImgEff)
		{
			fImg++;
		}
		if (fImg > 18)
		{
			fImg = 0;
			indexImg++;
		}
		if (indexImg > 0)
		{
			Player.isBlock = false;
			removeEff();
		}
	}

	private void updateEffThunderFall()
	{
		x = objBeFireMain.x;
		y = objBeFireMain.y;
		for (int i = 0; i < xLech.Length; i++)
		{
			if (GameCanvas.loadmap.getTile(x + xLech[i], y + yLech[i]) == 1)
			{
				yLech[i] = -yLech[i] + CRes.random(10, 20);
			}
		}
		if (indexImg < 5)
		{
			if (f % 3 == 0 && !isPauseImgEff)
			{
				fImg++;
			}
			if (fImg > 3)
			{
				timeStartPauseImgEff = mSystem.currentTimeMillis();
				isPauseImgEff = true;
				fImg = 0;
				indexImg++;
			}
			if (isPauseImgEff)
			{
				GameScreen.addEffectEnd(63, 0, x, y, Dir, objMainEff);
				GameScreen.addEffectEnd(59, 0, x, y, Dir, objMainEff);
				if (mSystem.currentTimeMillis() - timeStartPauseImgEff >= 300)
				{
					isPauseImgEff = false;
				}
			}
		}
		if (indexSub < 4)
		{
			if (f % 3 == 0 && !isPauseSubEff && indexImg > 0)
			{
				fSub++;
			}
			if (fSub > 3)
			{
				timeStartPauseSubEff = mSystem.currentTimeMillis();
				isPauseSubEff = true;
				fSub = 0;
				indexSub++;
			}
			if (isPauseSubEff && mSystem.currentTimeMillis() - timeStartPauseSubEff >= 300)
			{
				isPauseSubEff = false;
			}
		}
		if (indexSub2 < 4)
		{
			if (f % 3 == 0 && !isPauseSub2Eff && indexImg > 0)
			{
				fSub2++;
			}
			if (fSub2 > 3)
			{
				timeStartPauseSub2Eff = mSystem.currentTimeMillis();
				isPauseSub2Eff = true;
				fSub2 = 0;
				indexSub2++;
			}
			if (isPauseSub2Eff && mSystem.currentTimeMillis() - timeStartPauseSub2Eff >= 300)
			{
				isPauseSub2Eff = false;
			}
		}
		if (indexImg == 5 && indexSub == 4 && indexSub2 == 4)
		{
			removeEff();
		}
	}

	public override bool CreateEffectSkill()
	{
		if (objFireMain != null && objBeFireMain == null)
		{
			objBeFireMain = objFireMain;
			toX = objFireMain.x;
			toY = objFireMain.y;
		}
		if (objFireMain == null || objBeFireMain == null || objFireMain.returnAction() || objBeFireMain.returnAction())
		{
			return false;
		}
		if (objFireMain == GameScreen.player || CRes.random(3) == 0)
		{
			isAddSound = true;
		}
		objMainEff = objFireMain;
		am_duong = -1;
		if (GameCanvas.lowGraphic && objFireMain != GameScreen.player)
		{
			if (MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, objFireMain.x, objFireMain.y) > 120)
			{
				removeEff();
				return true;
			}
			if (GameMidlet.DEVICE == 0 && GameScreen.vecObjFire.size() > 30)
			{
				removeEff();
				return true;
			}
		}
		if (Dir == 2)
		{
			am_duong = 1;
		}
		if (typeEffect == 4017 || typeEffect == 4010)
		{
			createThanTrangSkill(4010);
			return true;
		}
		if (typeEffect == 4018)
		{
			createVenomRain4018();
			return true;
		}
		if (typeEffect == 4019)
		{
			createVenomBuff4019();
			return true;
		}
		if (typeEffect == 4021)
		{
			createThanhLongActive4021();
			return true;
		}
		if (typeEffect == 4022)
		{
			createThanhLongActive4022();
			return true;
		}
		if (typeEffect == 4023)
		{
			createThanhLongBuff4023();
			return true;
		}
		if ((typeEffect >= 4001 && typeEffect <= 4080) || (typeEffect >= 4201 && typeEffect <= 4216) || (typeEffect >= 4501 && typeEffect <= 4516))
		{
			createThanTrangSkill(typeEffect);
			return true;
		}
		switch (typeEffect)
		{
		case TYPE_NIKA_ACTIVE_1_LEVEL1:
			createNikaJump(NIKA_VARIANT_ACTIVE_1_LEVEL1);
			break;
		case TYPE_NIKA_ACTIVE_1_LEVEL5:
			createNikaJump(NIKA_VARIANT_ACTIVE_1_LEVEL5);
			break;
		case TYPE_NIKA_ACTIVE_2:
			createNikaJump(NIKA_VARIANT_ACTIVE_2);
			break;
		case TYPE_NIKA_BUFF:
			createNikaBuff();
			break;
		case TYPE_LIGHT_ACTIVE_1_LEVEL5:
			createLightActive1Level5();
			break;
		case TYPE_LIGHT_ACTIVE_2_LEVEL5:
			createLightActive2Level5();
			break;
		case TYPE_LOVE_ACTIVE_1_LEVEL5:
			createSkillBuff((short)timeBegin);
			break;
		case TYPE_LOVE_ACTIVE_2_LEVEL5:
			createLoveActive2Level5();
			break;
		case TYPE_NIKYU_ACTIVE_1:
			createNikyuActive1();
			break;
		case TYPE_NIKYU_ACTIVE_2:
			createNikyuActive2();
			break;
		case TYPE_NIKYU_BUFF:
			createNikyuBuff();
			break;
		case 2000:
			createEffFireExplore();
			break;
		case 1998:
		case 1999:
			createEffThunderFalls();
			break;
		case 0:
			createNormal();
			break;
		case 1:
		case 37:
			createLuffy1();
			break;
		case 47:
		case 48:
			createSanji1();
			break;
		case 154:
			createZoro1();
			break;
		case 155:
			createZoro2();
			break;
		case 7:
			createUssopSea1();
			break;
		case 141:
			createUssopSea2();
			break;
		case 142:
			createUssopSea3();
			break;
		case 57:
		case 64:
		case 66:
		case 206:
		case 207:
			createUssop2();
			break;
		case 127:
		case 193:
		case 225:
		case 302:
			create_Ussop_S2_L3();
			break;
		case 502:
			create_Ussop_S2_L7();
			break;
		case 58:
			createUssopSkill1_Lv3();
			break;
		case 159:
			createUssopSkill1_Lv3_New();
			break;
		case 126:
		case 192:
			createUssopSkill1_Lv3_SHORT();
			break;
		case 224:
		case 301:
			create_Ussop_S1_L5();
			break;
		case 501:
			create_Ussop_S1_L7();
			break;
		case 9:
		case 53:
		case 163:
			createNami1();
			break;
		case 63:
		case 190:
		case 222:
		case 312:
			createNami1_SHORT();
			break;
		case 512:
			create_Nami_S2_L7();
			break;
		case 11:
			fRemove = 15;
			createNamiSea1_2();
			break;
		case 139:
			fRemove = 20;
			createNamiSea1_2();
			break;
		case 140:
			fRemove = 40;
			createNamiSea3();
			break;
		case 12:
		case 49:
		case 50:
		case 188:
		case 220:
		case 293:
			createSanji2();
			break;
		case 493:
			create_Sanji_S3_L7();
			break;
		case 266:
			createRankyaku();
			break;
		case 276:
		case 277:
			createSoi();
			break;
		case 278:
		case 279:
			createHuou();
			break;
		case 267:
			createShigan();
			break;
		case 268:
		case 269:
			createDoor();
			break;
		case 14:
		case 44:
			fRemove = 9;
			break;
		case 15:
		case 38:
			createZoro3();
			break;
		case 16:
		case 51:
			createNamiSkill1();
			break;
		case 52:
		case 189:
		case 221:
		case 311:
			createNamiSkill1_L3();
			break;
		case 511:
			create_Nami_S1_L7();
			break;
		case 19:
			createZoro4();
			break;
		case 42:
			fRemove = 15;
			createZoroSkill3_Lv1();
			break;
		case 43:
			fRemove = 20;
			createZoroSkill3_Lv1();
			break;
		case 21:
		case 33:
		case 176:
			fRemove = 8;
			break;
		case 34:
		case 35:
			createLuffy6();
			break;
		case 41:
			createZoro_S2_L1_New();
			break;
		case 29:
			createZoro8();
			break;
		case 31:
		case 55:
		case 56:
		case 191:
		case 223:
		case 313:
			createNamiSkill3();
			break;
		case 513:
			create_Nami_S3_L7();
			break;
		case 46:
			addSoundBuff();
			objFireMain.toX = objFireMain.x;
			objFireMain.toY = objFireMain.y;
			if (objFireMain.posTransRoad != null)
			{
				objFireMain.posTransRoad = null;
			}
			GameScreen.addEffectEnd(85, 0, x, y, 500, Dir, objMainEff);
			isEff = true;
			fRemove = 1;
			return true;
		case 67:
		case 68:
		case 69:
		case 194:
		case 226:
			create_Ussop_S3_L1();
			break;
		case 303:
			create_Ussop_S3_L6();
			break;
		case 503:
			create_Ussop_S3_L7();
			break;
		case 164:
		case 227:
			createCausu_1();
			break;
		case 30:
			createMon_1();
			break;
		case 71:
		case 145:
		case 146:
		case 147:
		case 148:
			createMon2();
			break;
		case 72:
		case 92:
			createMon3();
			break;
		case 73:
		case 74:
			createMon_4_5();
			break;
		case 75:
			createMon6();
			break;
		case 76:
			createAlvida1();
			break;
		case 77:
			createAlvida2();
			break;
		case 81:
		case 143:
		case 149:
			createMon_10();
			break;
		case 82:
		case 144:
			createMon_11();
			break;
		case 156:
			fRemove = 33;
			break;
		case 83:
			fRemove = 16;
			if (objFireMain.type_left_right == 0)
			{
				Dir = 0;
			}
			else
			{
				Dir = 2;
			}
			break;
		case 180:
		case 212:
			fRemove = 20;
			if (objFireMain.type_left_right == 0)
			{
				Dir = 0;
			}
			else
			{
				Dir = 2;
			}
			if (typeEffect == 212)
			{
				fraImgEff = new FrameImage(61, 24, 30);
			}
			break;
		case 271:
			fRemove = 15;
			if (objFireMain.type_left_right == 0)
			{
				Dir = 0;
			}
			else
			{
				Dir = 2;
			}
			fraImgEff = new FrameImage(61, 24, 30);
			break;
		case 471:
			fRemove = 21;
			if (objFireMain.type_left_right == 0)
			{
				Dir = 0;
			}
			else
			{
				Dir = 2;
			}
			fraImgEff = new FrameImage(61, 24, 30);
			break;
		case 274:
		case 275:
			fRemove = 15;
			fraImgEff = new FrameImage(431, 2);
			break;
		case 160:
			createLuffy_New2();
			break;
		case 84:
		case 181:
		case 213:
		case 272:
			createLuffy_New2_SHORT();
			break;
		case 472:
			create_Luffy_S2_L7();
			break;
		case 85:
		case 182:
		case 214:
		case 273:
			createLuffy_New3();
			break;
		case 473:
			create_Luffy_S3_L7();
			break;
		case 157:
			createZoro_New1();
			break;
		case 86:
		case 183:
		case 215:
			createZoro_S1_L3_SHORT();
			break;
		case 281:
			createZoro_S1_L6();
			break;
		case 481:
			create_Zoro_S1_L7();
			break;
		case 87:
		case 184:
		case 216:
			createZoro_New2();
			break;
		case 282:
			fRemove = 34;
			vMax = 12;
			fraImgEff = new FrameImage(413, 91, 73);
			if (isAddSound)
			{
				mSound.playSound(8, mSound.volumeSound);
			}
			GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
			fraImgSubEff = new FrameImage(415, 3);
			break;
		case 482:
		{
			fRemove = 42;
			vMax = 12;
			fraImgEff = new FrameImage(413, 91, 73);
			mframe = new int[42]
			{
				-2, -2, -2, -2, -2, -2, 0, 1, 2, -1,
				-1, -2, -2, -2, -2, 0, 1, 2, -1, -1,
				-2, -2, -2, -2, 0, 1, 2, -1, -1, -2,
				-2, -2, -2, 0, 1, 2, -1, -1, -2, -2,
				-2, -2
			};
			fraImgSubEff = new FrameImage(440, 12);
			mframeSub = new int[42]
			{
				0, 0, 1, 1, 2, 2, -1, -1, -1, 3,
				3, 4, 4, 5, 5, -1, -1, -1, 6, -1,
				7, -1, 8, 8, -1, -1, -1, 9, -1, 10,
				-1, 11, 11, -1, -1, -1, 9, -1, 10, -1,
				11, 11
			};
			x1000 = x + 30 * am_duong;
			int num6 = x1000 - x;
			int ydich2 = 0;
			VecSubEff.addElement(create_Speed(num6, ydich2, new Point_Focus(), x, y - objFireMain.hOne / 3, toX, toY));
			VecSubEff.addElement(create_Speed(-num6, ydich2, new Point_Focus(), x, y - objFireMain.hOne / 3, toX, toY));
			if (isAddSound)
			{
				mSound.playSound(8, mSound.volumeSound);
			}
			GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
			break;
		}
		case 161:
			createZoro_New2_SHORT();
			break;
		case 88:
			GameScreen.addEffectEnd(30, 0, x, y - 30, 300, Dir, objMainEff);
			fRemove = 8;
			addSound(3);
			break;
		case 89:
			createMorgan_2();
			break;
		case 90:
		case 91:
			fRemove = 1;
			break;
		case 93:
			toY = objBeFireMain.y;
			fRemove = 32;
			fraImgEff = new FrameImage(8, 40, 47, 40, 47);
			break;
		case 94:
			createMohji_2();
			break;
		case 95:
			createBuggy_1();
			break;
		case 96:
			createBuggy_2();
			break;
		case 97:
			createCabaji_1();
			break;
		case 22:
		case 98:
			createCabaji_2();
			break;
		case 248:
		case 255:
			createKilo_1();
			break;
		case 99:
			createNyaban_1();
			break;
		case 100:
			createNyaban_2();
			break;
		case 101:
			createNyaban_3();
			break;
		case 102:
			createJango_1();
			break;
		case 103:
			createKuro_1();
			break;
		case 104:
			createKuro_2();
			break;
		case 105:
			createPearl_1();
			break;
		case 106:
			createPearl_2();
			break;
		case 65:
		case 70:
		case 107:
			createGhin_1();
			break;
		case 108:
			createGhin_2();
			break;
		case 109:
			createDonKrieg_1();
			break;
		case 110:
			createDonKrieg_2();
			break;
		case 111:
			createDonKrieg_3();
			break;
		case 112:
			numNextFrame = 2;
			fraImgEff = new FrameImage(140, 70, 70);
			if (Dir == 0)
			{
				x -= 20;
			}
			else
			{
				x += 20;
			}
			fRemove = 15;
			break;
		case 270:
			numNextFrame = 2;
			fraImgEff = new FrameImage(427, 4);
			if (Dir == 0)
			{
				x -= 20;
			}
			else
			{
				x += 20;
			}
			fRemove = 15;
			break;
		case 113:
		case 150:
		case 151:
		case 152:
		case 153:
			createHachi_2();
			break;
		case 114:
			createChu_1();
			break;
		case 115:
			createChu_2();
			break;
		case 116:
			createKurobi_1();
			break;
		case 117:
			createKurobi_2();
			break;
		case 118:
			createArlong_1();
			break;
		case 119:
			createArlong_2();
			break;
		case 120:
			createArlong_3();
			break;
		case 121:
			create_Zoro_S3_L1();
			break;
		case 122:
			create_Zoro_S3_L2();
			break;
		case 123:
		case 185:
		case 217:
		case 283:
			create_Zoro_S3_L3();
			break;
		case 483:
			create_Zoro_S3_L7();
			break;
		case 158:
		case 177:
			createSanji_s1_l3_New();
			break;
		case 124:
		case 186:
		case 218:
			createSanji_s1_l3_SHORT();
			break;
		case 162:
			createSanji_s2_l3_New();
			break;
		case 125:
		case 187:
			createSanji_s2_l3_New_SHORT();
			break;
		case 131:
			fRemove = 6;
			break;
		case 132:
			fRemove = 10;
			break;
		case 133:
			fraImgEff = new FrameImage(193, 25, 15);
			fraImgSubEff = new FrameImage(68, 28, 44);
			fRemove = 15;
			vMax = 18;
			break;
		case 134:
		case 135:
			fraImgEff = new FrameImage(193, 25, 15);
			fraImgSubEff = new FrameImage(68, 28, 44);
			fraImgSub2Eff = new FrameImage(194, 48, 34, 1);
			if (typeEffect == 135)
			{
				fraImgSub3Eff = new FrameImage(30, 38, 38);
			}
			fRemove = 20;
			vMax = 18;
			break;
		case 136:
			fraImgEff = new FrameImage(183, 20, 54);
			fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
			fRemove = 15;
			y = objFireMain.y;
			break;
		case 137:
		case 138:
			fraImgEff = new FrameImage(183, 20, 54);
			fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
			fRemove = 20;
			y = objFireMain.y;
			break;
		case 2:
		case 228:
		case 259:
		case 260:
		case 261:
			create_Devil_FIRE1();
			break;
		case 10030:
			create_ho_den_vu_tru();
			break;
		case 3:
		case 229:
		case 262:
		case 263:
		case 264:
			create_Devil_FIRE2();
			break;
		case 4:
		case 230:
			create_Devil_ICE1();
			break;
		case 5:
		case 231:
			create_Devil_ICE2();
			break;
		case 6:
		case 232:
			create_Devil_Smoker1();
			break;
		case 10:
		case 234:
			create_Devil_Smoker2();
			break;
		case 13:
		case 258:
			createSmoker1();
			break;
		case 280:
		{
			fraImgEff = new FrameImage(mImage.createImage("/eff/khungthanh.png"), 1);
			fraImgSubEff = new FrameImage(mImage.createImage("/eff/ball.png"), 4);
			x = objFireMain.x;
			y = objFireMain.y;
			toX = x + 200;
			toY = y - 30;
			vx = 5;
			vy = -1;
			vMax = 10;
			int xdich = toX - x;
			int ydich = toY - y - CRes.random(20);
			create_Speed(xdich, ydich, null);
			fRemove = 40;
			objFireMain.Dir = 2;
			break;
		}
		case 18:
			createSmoker2();
			break;
		case 78:
			fRemove = 6;
			break;
		case 79:
			fRemove = 8;
			break;
		case 24:
		case 80:
			fRemove = 14;
			break;
		case 128:
			fRemove = 10;
			break;
		case 129:
		case 130:
			fRemove = 16;
			break;
		case 10001:
			fraImgEff = new FrameImage(173, 70, 42, 50, 30);
			fraImgSubEff = new FrameImage(172, 60, 43);
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			levelPaint = -1;
			break;
		case 10002:
			fraImgEff = new FrameImage(76, 32, 70);
			fraImgSubEff = new FrameImage(129, 40, 80);
			x = objFireMain.x;
			y = objFireMain.y;
			fRemove = 22;
			break;
		case 10003:
			fraImgEff = new FrameImage(77, 64, 75, 43, 50);
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 4;
			break;
		case 10004:
			fraImgEff = new FrameImage(174, 40, 40);
			fraImgSubEff = new FrameImage(26, 40, 40);
			x = objFireMain.x;
			y = objFireMain.y;
			fRemove = 30;
			break;
		case 10005:
			objFireMain.Dir = Dir;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			break;
		case 10006:
		case 10011:
			fraImgSubEff = new FrameImage(172, 60, 43);
			levelPaint = -1;
			break;
		case 10007:
			fraImgEff = new FrameImage(118, 62, 64, 47, 48);
			fraImgSubEff = new FrameImage(173, 70, 42, 50, 30);
			break;
		case 10009:
			objFireMain.Dir = Dir;
			fRemove = 30;
			break;
		case 10008:
			levelPaint = -1;
			fraImgEff = new FrameImage(175, 13, 11);
			objFireMain.Dir = Dir;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			break;
		case 10010:
		case 10013:
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			fraImgEff = new FrameImage(178, 70, 65);
			numNextFrame = 2;
			break;
		case 10012:
			createXerath3();
			break;
		case 10015:
			createUrgot3();
			break;
		case 10017:
			objFireMain.Dir = Dir;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 4;
			fraImgEff = new FrameImage(180, 32, 63);
			numNextFrame = 3;
			break;
		case 10018:
			objFireMain.Dir = Dir;
			x = objFireMain.x;
			y = objFireMain.y;
			fraImgEff = new FrameImage(8, 40, 47, 40, 47);
			break;
		case 10019:
			objFireMain.Dir = Dir;
			x = objFireMain.x;
			y = objFireMain.y;
			fRemove = 8;
			break;
		case 10020:
			objFireMain.Dir = Dir;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			fraImgEff = new FrameImage(189, 37, 62);
			numNextFrame = 3;
			levelPaint = -1;
			break;
		case 10021:
		case 10022:
			objFireMain.Dir = Dir;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			fraImgEff = new FrameImage(181, 47, 63, 38, 51);
			numNextFrame = 3;
			levelPaint = -1;
			break;
		case 10023:
			fRemove = 4;
			break;
		case 10024:
			setAngle();
			objFireMain.Dir = Dir;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			fraImgEff = new FrameImage(181, 47, 63, 38, 51);
			fraImgSubEff = new FrameImage(172, 60, 43);
			numNextFrame = 3;
			levelPaint = -1;
			break;
		case 10025:
			setAngle();
			objFireMain.Dir = Dir;
			createMonster_NEM_BOOM_2();
			break;
		case 10026:
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			fraImgEff = new FrameImage(182, 56, 80, 40, 57);
			numNextFrame = 2;
			levelPaint = -1;
			break;
		case 10027:
		{
			for (int num5 = 0; num5 < vecPos.size(); num5++)
			{
				Point_Focus point_Focus = (Point_Focus)vecPos.elementAt(num5);
				GameScreen.addEffectEnd_ObjTo(22, 0, point_Focus.x, point_Focus.y - 30, objFireMain.ID, objFireMain.typeObject, (sbyte)objFireMain.Dir, objMainEff);
			}
			fRemove = 10;
			break;
		}
		case 165:
			addSoundBuff();
			isEff = true;
			Dir = (sbyte)objFireMain.type_left_right;
			addSoundBuff();
			GameScreen.addEffectEnd(85, 0, x, y, 900, Dir, objMainEff);
			GameScreen.addEffectEnd(85, 0, x, y, 900, Dir, objMainEff);
			y = objFireMain.y;
			objFireMain.toX = objFireMain.x;
			objFireMain.toY = objFireMain.y;
			fraImgEff = new FrameImage(101, 40, 47);
			fRemove = 40;
			return true;
		case 166:
			isEff = true;
			Dir = (sbyte)objFireMain.type_left_right;
			addSoundBuff();
			y = objFireMain.y;
			objFireMain.toX = objFireMain.x;
			objFireMain.toY = objFireMain.y;
			fraImgEff = new FrameImage(101, 40, 47);
			fRemove = 20;
			return true;
		case 17:
			isEff = true;
			Dir = (sbyte)objFireMain.type_left_right;
			addSoundBuff();
			y = objFireMain.y;
			objFireMain.toX = objFireMain.x;
			objFireMain.toY = objFireMain.y;
			fraImgEff = new FrameImage(101, 40, 47);
			fRemove = 20;
			return true;
		case 20:
			fRemove = 24;
			levelPaint = -1;
			fraImgEff = new FrameImage(171, 153, 84, 100, 54);
			GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2, 600, Dir, objMainEff);
			y = objFireMain.y + 20;
			break;
		case 23:
		{
			fRemove = 3;
			fraImgEff = new FrameImage(20, 10, 10);
			vMax = 18;
			y -= 5;
			if (Dir == 0)
			{
				x -= 10;
			}
			else
			{
				x += 10;
			}
			int xdich = toX - x;
			int ydich = toY - y;
			create_Speed(xdich, ydich, null);
			GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
			fPlayFrameSuper = fRemove;
			if (fRemove < 5)
			{
				fRemove = 5;
			}
			break;
		}
		case 25:
		case 235:
			create_Crocodile_1();
			break;
		case 26:
		case 236:
			addVir(10, 5, 10, isPlayer: true);
			fRemove = 20;
			fraImgEff = new FrameImage(99, 32, 32);
			y = objFireMain.y;
			if (isAddSound)
			{
				addSoundBuff();
			}
			break;
		case 27:
			createChess();
			break;
		case 28:
			createKuromarimo();
			break;
		case 32:
			createWapol();
			break;
		case 36:
			createWapol2();
			break;
		case 39:
			createWapol3();
			break;
		case 40:
			create_Wapol4();
			break;
		case 45:
			createMr3_1();
			break;
		case 54:
			createMr3_2();
			break;
		case 59:
		case 60:
			createMissGold_1();
			break;
		case 61:
			createLapin();
			break;
		case 62:
			createMon29();
			break;
		case 167:
			fraImgEff = new FrameImage(152, 25, 21);
			fraImgSubEff = new FrameImage(201, 64, 50, 45, 35);
			fraImgSub2Eff = new FrameImage(217, 39, 18);
			fraImgSub3Eff = new FrameImage(92, 64, 126, 45, 89, 1);
			fraImgSub4Eff = new FrameImage(218, 64, 64);
			x = objFireMain.x;
			y = objFireMain.y + 10;
			fRemove = 30;
			x1000 = x;
			y1000 = y;
			levelPaint = -1;
			break;
		case -1:
			fRemove = 60;
			fraImgEff = new FrameImage(mImage.createImage("/eff/n1.png"), 14, 15);
			fraImgSubEff = new FrameImage(mImage.createImage("/eff/n1.png"), 14, 15);
			vMax = 16000;
			createDanFocus();
			frame = setFrameAngle(gocT_Arc);
			break;
		case 400:
		{
			// Teach Skill 1 Lv 1-4 (Hút bóng tối - INSTANT, không animation xoáy)
			// Chỉ apply hiệu ứng lên targets rồi kết thúc ngay (fRemove=0)
			for (int n = 0; n < vecObjsBeFire.size(); n++)
			{
				Object_Effect_Skill oes = (Object_Effect_Skill)vecObjsBeFire.elementAt(n);
				if (oes != null)
				{
					MainObject mo = MainObject.get_Object(oes.ID, oes.tem);
					if (mo != null)
					{
						GameScreen.addHightDataeff(2, mo.x, mo.y);
						LoadMap.timeVibrateScreen = CRes.random(6, 20);
						GameScreen.addEffectEnd(112, 0, mo.x, mo.y, Dir, objFireMain);
					}
				}
			}
			break;
		}
		case 401:
			// Teach Skill 2 Lv 1-4 (Hố đen)
			levelPaint = 1;
			x = objBeFireMain.x;
			y = objBeFireMain.y;
			GameScreen.addHightDataeff(4, x, y);
			GameScreen.addEffectEnd(63, 0, x, y, Dir, objFireMain);
			GameScreen.addEffectEnd(110, 0, x, y, Dir, objFireMain);
			LoadMap.timeVibrateScreen = CRes.random(1, 5);
			removeEff();
			break;
		case 402:
		{
			// Teach Skill 1 Lv 5 (Hút bóng tối nâng cấp / Blackhole Lv 5)
			if (VecEff == null) VecEff = new mVector();
			VecEff.removeAllElements();
			VecSubEff = new mVector();
			for (int m = 0; m < vecObjsBeFire.size(); m++)
			{
				Object_Effect_Skill object_Effect_Skill4 = (Object_Effect_Skill)vecObjsBeFire.elementAt(m);
				if (object_Effect_Skill4 != null)
				{
					MainObject mainObject3 = MainObject.get_Object(object_Effect_Skill4.ID, object_Effect_Skill4.tem);
					if (mainObject3 != null)
					{
						GameScreen.addHightDataeff(2, mainObject3.x, mainObject3.y);
						LoadMap.timeVibrateScreen = CRes.random(6, 20);
						GameScreen.addEffectEnd(112, 0, mainObject3.x, mainObject3.y, Dir, objFireMain);
						VecSubEff.addElement(new Point(mainObject3.x, mainObject3.y));
					}
				}
			}
			frameSuper = 4;
			fraImgEff = new FrameImage(32, 45, 45, (sbyte)5, frameSuper);
			fRemove = 30;
			vMax = 12;
			y = objFireMain.y;
			break;
		}
		case 403:
			// Teach Skill 2 Lv 5 (Hố đen nâng cấp)
			levelPaint = 1;
			x = objBeFireMain.x;
			y = objBeFireMain.y;
			GameScreen.addHightDataeff(8, x, y);
			GameScreen.addEffectEnd(63, 0, x, y, Dir, objFireMain);
			GameScreen.addEffectEnd(110, 0, x, y, Dir, objFireMain);
			LoadMap.timeVibrateScreen = CRes.random(1, 5);
			removeEff();
			break;
		case 10028:
		{
			fraImgSub3Eff = new FrameImage(242, 49, 28, 2);
			fRemove = 33;
			x = objBeFireMain.x;
			y = objBeFireMain.y;
			y1000 = 240;
			for (int l = 0; l < vecObjsBeFire.size(); l++)
			{
				Object_Effect_Skill object_Effect_Skill3 = (Object_Effect_Skill)vecObjsBeFire.elementAt(l);
				if (object_Effect_Skill3 != null)
				{
					GameScreen.addEffectSkill2(-1, objFireMain, object_Effect_Skill3, x + posSmock[CRes.random(posSmock.Length - 1)], y - 200 + CRes.random_Am(-10, 10));
					GameScreen.addEffectSkill2(-1, objFireMain, object_Effect_Skill3, x + posSmock[CRes.random(posSmock.Length - 1)], y - 200 + CRes.random_Am(-10, 10));
					GameScreen.addEffectSkill2(-1, objFireMain, object_Effect_Skill3, x + posSmock[CRes.random(posSmock.Length - 1)], y - 200 + CRes.random_Am(-10, 10));
				}
			}
			GameScreen.addEffectEnd(112, 0, x, y + 10, Dir, objMainEff);
			break;
		}
		case 169:
		case 237:
			fraImgEff = new FrameImage(240, 30, 73, 1);
			fraImgSubEff = new FrameImage(241, 40, 27, 2);
			fraImgSub2Eff = new FrameImage(104, 30, 30);
			fraImgSub3Eff = new FrameImage(242, 49, 28, 2);
			fraImgSub4Eff = new FrameImage(243, 36, 39);
			fRemove = 33;
			x = objBeFireMain.x;
			y = objBeFireMain.y;
			y1000 = 240;
			break;
		case 170:
		case 238:
			fraImgEff = new FrameImage(244, 20, 37, 3);
			fraImgSubEff = new FrameImage(152, 25, 21);
			fraImgSub4Eff = new FrameImage(243, 36, 39);
			fraImgSub2Eff = new FrameImage(240, 30, 73, 1);
			fraImgSub3Eff = new FrameImage(241, 40, 27, 2);
			fRemove = 43;
			vMax = 30;
			if (isAddSound)
			{
				addSoundBuffShort();
			}
			break;
		case 171:
		case 239:
			y = objBeFireMain.y;
			x = objBeFireMain.x;
			fraImgEff = new FrameImage(118, 62, 64, 47, 48);
			fraImgSubEff = new FrameImage(174, 40, 40);
			fraImgSub2Eff = new FrameImage(247, 49, 28);
			fraImgSub3Eff = new FrameImage(254, 30, 40);
			vMax = 16;
			fRemove = 24;
			break;
		case 172:
		case 240:
			fRemove = 24;
			fraImgEff = new FrameImage(254, 30, 40);
			if (isAddSound)
			{
				addSoundBuffShort();
			}
			break;
		case 168:
			fRemove = 10;
			fraImgEff = new FrameImage(255, 42, 50, 3);
			y = objFireMain.y;
			break;
		case 173:
			fRemove = 8;
			fraImgEff = new FrameImage(257, 15, 51);
			fraImgSubEff = new FrameImage(3, 30, 50);
			vMax = 12;
			x += am_duong * 30;
			break;
		case 174:
			fRemove = 12;
			fraImgEff = new FrameImage(219, 47, 7);
			vMax = 7;
			break;
		case 175:
			levelPaint = -1;
			fRemove = 8;
			fraImgEff = new FrameImage(258, 35, 28);
			break;
		case 178:
			fraImgEff = new FrameImage(266, 80, 100, 64, 80, 2);
			fraImgSubEff = new FrameImage(201, 64, 50, 45, 35);
			fRemove = 35;
			GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne, 200, Dir, objMainEff);
			x -= am_duong * 15;
			y = objFireMain.y;
			vMax = 10;
			frame = -1;
			break;
		case 179:
		case 241:
			fRemove = 26;
			if (objFireMain.vecBuffCur != null)
			{
				for (int k = 0; k < objFireMain.vecBuffCur.size(); k++)
				{
					if (((MainBuff)objFireMain.vecBuffCur.elementAt(k)).IdBuff == 2037)
					{
						fraImgEff = new FrameImage(267, 46, 53);
						fraImgSubEff = new FrameImage(270, 80, 47);
						fraImgSub2Eff = new FrameImage(271, 130, 80, 3);
						fraImgSub3Eff = new FrameImage(272, 50, 24);
						if (typeEffect == 241)
						{
							fraImgSub4Eff = new FrameImage(224, 22, 28);
						}
						frame = 1;
						break;
					}
				}
			}
			if (fraImgEff == null)
			{
				fraImgEff = new FrameImage(10, 40, 47);
				fraImgSubEff = new FrameImage(260, 54, 54, 1);
				frame = 0;
			}
			if (typeEffect == 241)
			{
				step = 1;
			}
			break;
		case 195:
			fraImgEff = new FrameImage(238, 30, 73);
			fraImgSubEff = new FrameImage(195, 40, 27);
			fRemove = 20;
			GameScreen.addEffectEnd(30, 0, x - am_duong * 20, objFireMain.y - objFireMain.hOne / 2 - 5, 300, Dir, objMainEff);
			break;
		case 196:
			fraImgEff = new FrameImage(225, 24, 32);
			fraImgSubEff = new FrameImage(286, 50, 100);
			fraImgSub2Eff = new FrameImage(98, 78, 70);
			GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2, 800, Dir, objMainEff);
			fRemove = 25;
			break;
		case 197:
			fraImgEff = new FrameImage(287, 76, 27);
			fRemove = 8;
			x += am_duong * 20;
			y -= 10;
			break;
		case 198:
			fraImgEff = new FrameImage(288, 30, 30);
			fRemove = 20;
			vMax = 12;
			x += am_duong * 25;
			break;
		case 199:
			fraImgEff = new FrameImage(291, 47, 48);
			fRemove = 16;
			y = objMainEff.y;
			break;
		case 200:
			Dir = (sbyte)objFireMain.type_left_right;
			am_duong = -1;
			if (Dir == 2)
			{
				am_duong = 1;
			}
			fraImgEff = new FrameImage(292, 78, 24);
			fraImgSubEff = new FrameImage(293, 50, 14);
			fRemove = 16;
			vMax = 10;
			objFireMain.isPaintWeapon = false;
			x += am_duong * 30;
			y -= 5;
			GameScreen.addEffectEnd(30, 0, x, y, 800, Dir, objMainEff);
			break;
		case 201:
			fRemove = 16;
			vMax = 10;
			GameScreen.addEffectEnd(30, 0, x, y, 400, Dir, objMainEff);
			y = objFireMain.y;
			break;
		case 202:
			fraImgEff = new FrameImage(295, 34, 24);
			vMax = 12;
			GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne - 10, 400, Dir, objMainEff);
			fRemove = 16;
			break;
		case 203:
			fraImgEff = new FrameImage(296, 36, 63);
			fRemove = 16;
			y = objMainEff.y;
			break;
		case 204:
			fRemove = 26;
			fraImgSubEff = new FrameImage(297, 83, 47);
			fraImgSub2Eff = new FrameImage(272, 50, 24);
			break;
		case 205:
			fraImgEff = new FrameImage(224, 22, 28);
			fraImgSubEff = new FrameImage(32, 45, 45);
			fRemove = 60;
			break;
		case 208:
			create_Eff_Tru();
			break;
		case 250:
			create_Eff_Tru_2();
			break;
		case 209:
		case 242:
			create_Eff_Lucci_1();
			break;
		case 210:
		case 243:
			create_Eff_Dong_Dat_1();
			break;
		case 211:
		case 244:
			create_Eff_Dong_Dat_2();
			break;
		case 219:
			fraImgEff = new FrameImage(323, 92, 64);
			fraImgSubEff = new FrameImage(183, 20, 54);
			fRemove = 26;
			GameScreen.addEffectEnd(30, 0, x + am_duong * 15, y, 200, Dir, objMainEff);
			mframe = new int[22]
			{
				-1, -1, -1, -1, -1, 0, 0, 1, 1, -1,
				2, 2, 2, 4, 4, 5, 5, -1, 6, 6,
				7, -1
			};
			x1000 = objFireMain.x;
			y1000 = objFireMain.y;
			break;
		case 292:
			fraImgEff = new FrameImage(323, 92, 64);
			fraImgSubEff = new FrameImage(183, 20, 54);
			fRemove = 26;
			GameScreen.addEffectEnd(30, 0, x + am_duong * 15, y, 200, Dir, objMainEff);
			mframe = new int[26]
			{
				4, 4, 5, 5, 4, 4, 5, 5, 4, 4,
				5, 5, 6, 6, 0, 0, 0, 0, 0, 0,
				1, 1, 1, 1, 1, 1
			};
			x1000 = objFireMain.x;
			y1000 = objFireMain.y;
			break;
		case 492:
			fraImgEff = new FrameImage(323, 92, 64);
			fraImgSubEff = new FrameImage(183, 20, 54);
			fraImgSub2Eff = null; // Old skill effs only 0-466. 468 is Thần Trang Venom
			fRemove = 26;
			GameScreen.addEffectEnd(30, 0, x + am_duong * 15, y, 200, Dir, objMainEff);
			mframe = new int[26]
			{
				4, 4, 5, 5, 4, 4, 5, 5, 4, 4,
				5, 5, 6, 6, 0, 0, 0, 0, 0, 0,
				1, 1, 1, 1, 1, 1
			};
			x1000 = objFireMain.x;
			y1000 = objFireMain.y;
			break;
		case 291:
			fraImgEff = new FrameImage(323, 92, 64);
			fraImgSubEff = new FrameImage(183, 20, 54);
			fRemove = 18;
			GameScreen.addEffectEnd(30, 0, x + am_duong * 15, y, 200, Dir, objMainEff);
			mframe = new int[20]
			{
				4, 4, 5, 5, 4, 4, 5, 5, 4, 4,
				5, 5, 4, 4, 5, 5, 6, 6, 6, -1
			};
			x1000 = objFireMain.x;
			y1000 = objFireMain.y;
			break;
		case 491:
			fraImgEff = new FrameImage(323, 92, 64);
			fraImgSub2Eff = new FrameImage(460, 13);
			fraImgSubEff = new FrameImage(183, 20, 54);
			fRemove = 29;
			GameScreen.addEffectEnd(30, 0, x + am_duong * 15, y, 200, Dir, objMainEff);
			mframe = new int[20]
			{
				4, 4, 5, 5, 4, 4, 5, 5, 4, 4,
				5, 5, 4, 4, 5, 5, 6, 6, 6, -1
			};
			x1000 = objFireMain.x;
			y1000 = objFireMain.y;
			break;
		case 233:
			fraImgEff = new FrameImage(107, 50, 54);
			fRemove = 20;
			break;
		case 245:
		case 251:
			fraImgEff = new FrameImage(357, 100, 100, 2);
			fraImgSubEff = new FrameImage(358, 51, 22);
			fRemove = 22;
			x += am_duong * 30;
			mframeSuper = new int[12][]
			{
				new int[3],
				new int[3] { 0, 10, 0 },
				new int[3] { 0, 25, 0 },
				new int[3] { 1, 0, -15 },
				new int[3] { 1, 10, -5 },
				new int[3] { 1, 20, 5 },
				new int[3] { 2, 10, 0 },
				new int[3] { 2, 15, 5 },
				new int[3] { 2, 30, 15 },
				new int[3] { 3, 0, 0 },
				new int[3] { 3, 10, 0 },
				new int[3] { 3, 30, 0 }
			};
			break;
		case 249:
		case 252:
			fraImgEff = new FrameImage(357, 100, 100, 2);
			fraImgSubEff = new FrameImage(359, 64, 64);
			fraImgSub2Eff = new FrameImage(183, 20, 54);
			fRemove = 22;
			x += am_duong * 30;
			addSoundBuffShort();
			mframeSuper = new int[12][]
			{
				new int[3],
				new int[3] { 0, 10, 0 },
				new int[3] { 0, 35, 0 },
				new int[3] { 1, 0, -15 },
				new int[3] { 1, 10, -5 },
				new int[3] { 1, 30, 5 },
				new int[3] { 2, 10, 0 },
				new int[3] { 2, 15, 5 },
				new int[3] { 2, 40, 15 },
				new int[3] { 3, 0, 0 },
				new int[3] { 3, 10, 0 },
				new int[3] { 3, 40, 0 }
			};
			break;
		case 246:
		case 253:
			fraImgEff = new FrameImage(351, 35, 62);
			fraImgSubEff = new FrameImage(354, 40, 47);
			fRemove = 26;
			if (isAddSound)
			{
				addSoundBuffShort();
			}
			GameScreen.addEffectEnd(30, 0, x, y, 400, Dir, objMainEff);
			break;
		case 247:
		case 254:
			y = objFireMain.y - objFireMain.hOne + 8;
			fraImgEff = new FrameImage(352, 52, 15);
			fraImgSubEff = new FrameImage(353, 9, 7);
			fraImgSub2Eff = new FrameImage(355, 9, 10);
			fraImgSub3Eff = new FrameImage(224, 22, 28);
			fRemove = 24;
			GameScreen.addEffectEnd(30, 0, x, y, 400, Dir, objMainEff);
			vMax = 140;
			if (CRes.abs(objFireMain.x - objBeFireMain.x) < 32)
			{
				objFireMain.x -= am_duong * 32;
			}
			break;
		case 404:
		{
			// Boa Hancock Skill 1 Level 1..4 (Mero Mero Mellow)
			DataSkillEff eff16 = new DataSkillEff(16, 0);
			fRemove = (eff16.sequence != null && eff16.sequence.Length > 0) ? eff16.sequence.Length : 44;
			DataSkillEff eff17 = new DataSkillEff(17, 0);
			int seq17Len = (eff17.sequence != null && eff17.sequence.Length > 0) ? eff17.sequence.Length : 26;
			mframe = new int[1];
			mframe[0] = fRemove - seq17Len;
			if (mframe[0] < 0) mframe[0] = 18;
			if (objFireMain != null)
			{
				objFireMain.addDataEff(14, 2000);
				objFireMain.addDataEff(15, 2000);
				objFireMain.addDataEff(16, 0);
				LoadMap.timeVibrateScreen = CRes.random(6, 20);
				GameScreen.addEffectEnd(112, 0, objFireMain.x, objFireMain.y, Dir, objFireMain);
			}
			break;
		}
		case 405:
		{
			// Boa Hancock Skill 2 Level 1..4 (Slave Arrow)
			frame = 5;
			mframe = new int[frame];
			mframe[0] = 0;
			int[] defaultSeqLens405 = new int[] { 8, 4, 6, 10 };
			for (short n = 1; n < 5; n++)
			{
				DataSkillEff eff = new DataSkillEff((short)(n + 18), 0);
				int seqLen = (eff.sequence != null && eff.sequence.Length > 0) ? eff.sequence.Length : defaultSeqLens405[n - 1];
				mframe[n] = seqLen + mframe[n - 1] + 1;
			}
			if (VecEff == null) VecEff = new mVector();
			VecEff.removeAllElements();
			if (VecSubEff == null) VecSubEff = new mVector();
			VecSubEff.removeAllElements();
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill objEff = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (objEff != null)
				{
					MainObject target = MainObject.get_Object(objEff.ID, objEff.tem);
					if (target != null)
					{
						Point pt = new Point();
						pt.obj = target;
						pt.frame = j;
						pt.f = -(j * 5);
						pt.fRe = mframe[frame - 1];
						VecEff.addElement(pt);
					}
				}
			}
			break;
		}
		case 406:
		{
			// Boa Hancock Skill 1 Level 5 (Upgraded Mero Mero Mellow)
			DataSkillEff eff37 = new DataSkillEff(37, 0);
			fRemove = (eff37.sequence != null && eff37.sequence.Length > 0) ? eff37.sequence.Length : 56;
			DataSkillEff eff17 = new DataSkillEff(17, 0);
			int seq17Len = (eff17.sequence != null && eff17.sequence.Length > 0) ? eff17.sequence.Length : 26;
			DataSkillEff eff38 = new DataSkillEff(38, 0);
			int seq38Len = (eff38.sequence != null && eff38.sequence.Length > 0) ? eff38.sequence.Length : 6;
			DataSkillEff eff40 = new DataSkillEff(40, 0);
			int seq40Len = (eff40.sequence != null && eff40.sequence.Length > 0) ? eff40.sequence.Length : 6;
			mframe = new int[3];
			mframe[0] = fRemove - seq17Len - seq38Len - seq40Len; // 56 - 26 - 6 - 6 = 18
			mframe[1] = fRemove - seq38Len - seq40Len;            // 56 - 6 - 6 = 44
			mframe[2] = fRemove - seq40Len;                       // 56 - 6 = 50
			if (mframe[0] < 0) mframe[0] = 18;
			if (mframe[1] <= mframe[0]) mframe[1] = 44;
			if (mframe[2] <= mframe[1]) mframe[2] = 50;
			if (objFireMain != null)
			{
				objFireMain.addDataEff(14, 2000);
				objFireMain.addDataEff(15, 2000);
				objFireMain.addDataEff(37, 0);
				LoadMap.timeVibrateScreen = CRes.random(6, 20);
				GameScreen.addEffectEnd(112, 0, objFireMain.x, objFireMain.y, Dir, objFireMain);
			}
			break;
		}
		case 407:
		{
			// Boa Hancock Skill 2 Level 5 (Upgraded Slave Arrow)
			frame = 5;
			mframe = new int[frame];
			mframe[0] = 0;
			for (short n = 1; n < 5; n++)
			{
				DataSkillEff eff = new DataSkillEff((short)(n + 45), 0);
				int seqLen = (eff.sequence != null) ? eff.sequence.Length : 8;
				mframe[n] = seqLen + mframe[n - 1] + 1;
			}
			if (VecEff == null) VecEff = new mVector();
			VecEff.removeAllElements();
			if (VecSubEff == null) VecSubEff = new mVector();
			VecSubEff.removeAllElements();
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill objEff = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (objEff != null)
				{
					MainObject target = MainObject.get_Object(objEff.ID, objEff.tem);
					if (target != null)
					{
						Point pt = new Point();
						pt.obj = target;
						pt.frame = j;
						pt.f = -(j * 5);
						pt.fRe = mframe[frame - 1];
						VecEff.addElement(pt);
					}
				}
			}
			break;
		}
		case 408:
		{
			// Kizaru Skill 1 Level 1..4 (Yasakani no Magatama)
			DataSkillEff eff26 = new DataSkillEff(26, 0);
			frame = ((eff26.sequence != null) ? eff26.sequence.Length : 20) / 2;
			if (VecEff == null) VecEff = new mVector();
			VecEff.removeAllElements();
			int dist = 0;
			for (int idx = 0; idx < vecObjsBeFire.size(); idx++)
			{
				Object_Effect_Skill objEff = (Object_Effect_Skill)vecObjsBeFire.elementAt(idx);
				if (objEff != null)
				{
					MainObject target = MainObject.get_Object(objEff.ID, objEff.tem);
					if (target != null)
					{
						dist = (objFireMain.x < target.x)
							? (target.x - objFireMain.x - objFireMain.wOne) / 18 + 3
							: (objFireMain.x - target.x) / 18 + 3;
						Point pt = new Point(objFireMain.x, objFireMain.y);
						pt.fRe = dist;
						pt.dir = (objFireMain.x < target.x) ? 2 : 0;
						pt.frame = idx;
						pt.obj = target;
						VecEff.addElement(pt);
					}
				}
			}
			DataSkillEff eff28 = new DataSkillEff(28, 0);
			fRemove = frame + dist + 3 + ((eff28.sequence != null) ? eff28.sequence.Length : 10);
			objFireMain.addDataEff(25, 0);
			objFireMain.addDataEff(26, 0);
			objFireMain.addDataEff(27, 0);
			LoadMap.timeVibrateScreen = CRes.random(6, 20);
			GameScreen.addEffectEnd(112, 0, objFireMain.x, objFireMain.y, Dir, objFireMain);
			break;
		}
		case 409:
		{
			// Kizaru Skill 2 Level 1..4 (Yata no Kagami)
			frame = 2;
			fRemove = 1;
			mframe = new int[frame];
			for (short i = 0; i < frame; i++)
			{
				DataSkillEff eff = new DataSkillEff((short)(i + 29), 0);
				int seqLen = (eff.sequence != null) ? eff.sequence.Length : 4;
				mframe[i] = seqLen + fRemove + 1;
				fRemove = mframe[i];
			}
			DataSkillEff eff33 = new DataSkillEff(33, 0);
			fRemove += (eff33.sequence != null) ? eff33.sequence.Length : 10;
			objFireMain.addDataEff(29, 0);
			break;
		}
		case 410:
		{
			// Kizaru Skill 1 Level 5 (Upgraded Yasakani no Magatama)
			DataSkillEff eff26 = new DataSkillEff(26, 0);
			frame = ((eff26.sequence != null) ? eff26.sequence.Length : 20) / 2;
			if (VecEff == null)
			{
				VecEff = new mVector();
			}
			VecEff.removeAllElements();
			int dist = 0;
			for (int idx = 0; idx < vecObjsBeFire.size(); idx++)
			{
				Object_Effect_Skill objEff = (Object_Effect_Skill)vecObjsBeFire.elementAt(idx);
				if (objEff != null)
				{
					MainObject target = MainObject.get_Object(objEff.ID, objEff.tem);
					if (target != null)
					{
						dist = (objFireMain.x < target.x)
							? (target.x - objFireMain.x - objFireMain.wOne) / 18 + 3
							: (objFireMain.x - target.x) / 18 + 3;
						Point pt = new Point(objFireMain.x, objFireMain.y);
						pt.fRe = dist;
						pt.dir = ((objFireMain.x < target.x) ? 2 : 0);
						pt.frame = idx;
						pt.obj = target;
						VecEff.addElement(pt);
					}
				}
			}
			DataSkillEff eff28 = new DataSkillEff(28, 0);
			fRemove = frame + dist + 3 + ((eff28.sequence != null) ? eff28.sequence.Length : 10);
			objFireMain.addDataEff(25, 0);
			objFireMain.addDataEff(26, 0);
			objFireMain.addDataEff(27, 0);
			LoadMap.timeVibrateScreen = CRes.random(6, 20);
			GameScreen.addEffectEnd(112, 0, objFireMain.x, objFireMain.y, Dir, objFireMain);
			break;
		}
		case 411:
		{
			// Kizaru Skill 2 Level 5 (Upgraded Yata no Kagami)
			frame = 2;
			fRemove = 1;
			mframe = new int[frame];
			for (short i = 0; i < frame; i++)
			{
				DataSkillEff eff = new DataSkillEff((short)(i + 41), 0);
				int seqLen = (eff.sequence != null) ? eff.sequence.Length : 4;
				mframe[i] = seqLen + fRemove + 1;
				fRemove = mframe[i];
			}
			DataSkillEff eff45 = new DataSkillEff(45, 0);
			fRemove += (eff45.sequence != null) ? eff45.sequence.Length : 10;
			objFireMain.addDataEff(41, 0);
			break;
		}
		}
		if (objFireMain == GameScreen.player)
		{
			for (int num7 = 0; num7 < vecObjsBeFire.size(); num7++)
			{
				Object_Effect_Skill object_Effect_Skill6 = (Object_Effect_Skill)vecObjsBeFire.elementAt(num7);
				if (object_Effect_Skill6 == null)
				{
					continue;
				}
				if (GameScreen.typePaintGameScreen == 1)
				{
					MainObject mainObject5 = MainObject.get_Object(object_Effect_Skill6.ID, object_Effect_Skill6.tem);
					if (mainObject5 != null)
					{
						mainObject5.isPaintSpec = true;
					}
					continue;
				}
				MainObject mainObject6 = MainObject.get_Object(object_Effect_Skill6.ID, object_Effect_Skill6.tem);
				if (mainObject6 == null || mainObject6.typeObject != 1)
				{
					continue;
				}
				int num8 = CRes.abs(objFireMain.x - mainObject6.x);
				if (num8 < 32)
				{
					mainObject6.x += am_duong * (num8 - 32 + 10);
					mainObject6.vXEffAva = (54 - num8) / 2 * am_duong;
					if (mainObject6.Action != 4 && mainObject6.Action != 2 && mainObject6.Hp > 0)
					{
						mainObject6.Action = 3;
						mainObject6.f = 0;
						mainObject6.resetAction();
					}
					else
					{
						mainObject6.vXEffAva = 0;
						mainObject6.dy = 0;
					}
				}
			}
		}
		if (!isEff)
		{
			setHP_New(vecObjsBeFire, objFireMain, isAdd: false);
			if (vecObjsBeFire.size() == 0)
			{
				isStop = true;
				return false;
			}
		}
		return true;
	}

	public void createDanFocus()
	{
		switch (CRes.random(4))
		{
		case 0:
			gocT_Arc = 90;
			break;
		case 1:
			gocT_Arc = 270;
			break;
		case 2:
			gocT_Arc = 180;
			break;
		case 3:
			gocT_Arc = 0;
			break;
		}
		va = 4096;
		vx = 0;
		vy = 0;
		life = 0;
		vX1000 = va * CRes.getcos(gocT_Arc) >> 10;
		vY1000 = va * CRes.getsin(gocT_Arc) >> 10;
	}

	private void create_Eff_Tru_2()
	{
		fraImgEff = new FrameImage(100, 15, 20);
		y = objFireMain.y - 55;
		if (objFireMain.IdIcon == 58)
		{
			fraImgEff = new FrameImage(366, 15, 20);
			y = objFireMain.y - 80;
		}
		vMax = 20;
		for (int i = 0; i < vecObjsBeFire.size(); i++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
			if (object_Effect_Skill == null)
			{
				continue;
			}
			MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
			if (mainObject != null)
			{
				Point_Focus point_Focus = new Point_Focus(x, y);
				int xdich = mainObject.x - x;
				int ydich = mainObject.y - mainObject.hOne / 2 - y;
				create_Speed(xdich, ydich, point_Focus, x, y, mainObject.x, mainObject.y - mainObject.hOne / 2);
				point_Focus.dis = 0;
				if (mainObject.x > x)
				{
					point_Focus.dis = 2;
				}
				VecEff.addElement(point_Focus);
			}
		}
	}

	private void createKilo_1()
	{
		fraImgEff = new FrameImage(356, 40, 80);
		fraImgSubEff = new FrameImage(183, 20, 54);
		toY = objBeFireMain.y;
		fRemove = 22;
	}

	private void create_Crocodile_1()
	{
		fRemove = 20;
		y = objFireMain.y;
		fraImgEff = new FrameImage(200, 54, 70, 40, 52);
		objFireMain.isTanHinh = true;
		if (isAddSound)
		{
			mSound.playSound(42, mSound.volumeSound);
		}
		if (typeEffect == 235)
		{
			vMax = 120;
			fraImgSubEff = new FrameImage(118, 62, 64);
		}
	}

	private void create_Ussop_S1_L5()
	{
		fraImgEff = new FrameImage(183, 20, 54);
		fraImgSubEff = new FrameImage(330, 46, 49);
		mframeSuper = new int[6][]
		{
			new int[2] { -5, -15 },
			new int[2] { 5, 15 },
			new int[2] { 15, -5 },
			new int[2] { -15, 5 },
			new int[2] { -10, -10 },
			new int[2] { 10, 10 }
		};
		y = objFireMain.y;
		fRemove = 18;
		vMax = 24;
		if (typeEffect == 301)
		{
			x1000 = x + 30 * am_duong;
			xLight1 = x1000;
			xLight2 = x1000;
			fraImgSub2Eff = new FrameImage(416, 4);
			int xdich = x1000 - x;
			int ydich = 0;
			VecSubEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y - objFireMain.hOne / 4 * 3, toX, toY));
			VecSubEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y - objFireMain.hOne / 2, toX, toY));
		}
	}

	private void create_Ussop_S1_L7()
	{
		fraImgEff = new FrameImage(183, 20, 54);
		fraImgSubEff = new FrameImage(330, 46, 49);
		mframeSuper = new int[6][]
		{
			new int[2] { -5, -15 },
			new int[2] { 5, 15 },
			new int[2] { 15, -5 },
			new int[2] { -15, 5 },
			new int[2] { -10, -10 },
			new int[2] { 10, 10 }
		};
		y = objFireMain.y;
		fRemove = 18;
		vMax = 24;
		x1000 = x + 30 * am_duong;
		xLight1 = x1000;
		xLight2 = x1000;
		fraImgSub2Eff = new FrameImage(453, 4);
		int xdich = x1000 - x;
		int ydich = 0;
		VecSubEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y - objFireMain.hOne / 4 * 3, toX, toY));
		VecSubEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y - objFireMain.hOne / 2, toX, toY));
	}

	private void create_Eff_Dong_Dat_2()
	{
		fraImgEff = new FrameImage(118, 62, 64);
		if (typeEffect == 244)
		{
			fraImgSubEff = new FrameImage(138, 62, 64);
		}
		fRemove = 30;
		GameScreen.addEffectEnd(30, 0, x + 10, objFireMain.y - objFireMain.hOne / 2, 600, Dir, objMainEff);
		GameScreen.addEffectEnd(30, 0, x - 10, objFireMain.y - objFireMain.hOne / 2, 600, Dir, objMainEff);
		y = objFireMain.y;
		if (isAddSound)
		{
			addSoundBuffShort();
		}
	}

	private void create_Eff_Dong_Dat_1()
	{
		fraImgEff = new FrameImage(310, 73, 59);
		fraImgSubEff = new FrameImage(311, 149, 179);
		GameScreen.addEffectEnd(30, 0, x + 10, objFireMain.y - objFireMain.hOne / 2, 400, Dir, objMainEff);
		GameScreen.addEffectEnd(30, 0, x - 10, objFireMain.y - objFireMain.hOne / 2, 400, Dir, objMainEff);
		fRemove = 50;
		if (objFireMain == GameScreen.player)
		{
			fRemove = 70;
		}
		if (isAddSound)
		{
			addSoundBuffShort();
		}
	}

	private void create_Eff_Lucci_1()
	{
		fraImgEff = new FrameImage(274, 23, 74, 3);
		frame = 0;
		vx = am_duong * 12;
		x1000 = x;
		GameScreen.addEffectEnd(30, 0, x + am_duong * 20, objFireMain.y - objFireMain.hOne / 2, 400, Dir, objMainEff);
		x = x1000 - am_duong * 24;
		fRemove = 20;
		if (isAddSound)
		{
			mSound.playSound(42, mSound.volumeSound);
			addSoundBuffShort();
		}
		if (objFireMain.vecBuffCur != null)
		{
			for (int i = 0; i < objFireMain.vecBuffCur.size(); i++)
			{
				MainBuff mainBuff = (MainBuff)objFireMain.vecBuffCur.elementAt(i);
				if (mainBuff.IdBuff == 2040 || mainBuff.IdBuff == 2064)
				{
					fraImgSubEff = new FrameImage(273, 24, 24, 4);
					frame = 1;
					break;
				}
				if (mainBuff.IdBuff == 2061)
				{
					fraImgSubEff = new FrameImage(273, 24, 24, 4);
					frame = 3;
					break;
				}
			}
		}
		if (frame == 0)
		{
			mframe = new int[23]
			{
				1, 0, 1, 0, 1, 0, 0, 1, 1, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2
			};
		}
		else
		{
			mframe = new int[20]
			{
				1, 0, 1, 0, 1, 0, 0, 1, 2, 2,
				3, 3, 4, 4, 4, 4, 4, 4, 4, 4
			};
		}
		if (typeEffect == 242)
		{
			GameScreen.addEffectEnd(147, (sbyte)frame, x + am_duong * 120, objFireMain.y - objFireMain.hOne / 2, 400, Dir, objMainEff);
		}
	}

	private void create_Eff_Tru()
	{
		fraImgEff = new FrameImage(100, 15, 20);
		vMax = 20;
		y = objFireMain.y - 55;
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
	}

	private void createMon29()
	{
		fraImgEff = new FrameImage(118, 62, 64, 47, 48);
		toY = objBeFireMain.y + 5;
		numNextFrame = 2;
		fRemove = 8;
	}

	private void createLapin()
	{
		vMax = 16;
		fraImgEff = new FrameImage(213, 15, 15);
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
	}

	public void paintEffThunderFall(mGraphics g)
	{
		if (!isPauseImgEff)
		{
			if (typeEffect == 1999)
			{
				fraImgEff.drawFrame(fImg, x + xLech[0], y + yLech[0], 0, mGraphics.BOTTOM | mGraphics.HCENTER, g);
			}
			fraImgSub2Eff.drawFrame(fImg, x, y, 0, mGraphics.BOTTOM | mGraphics.HCENTER, g);
			fraImgSubEff.drawFrame(fImg, x + xLech[1], y + yLech[1], 0, mGraphics.BOTTOM | mGraphics.HCENTER, g);
		}
		if (!isPauseSubEff && typeEffect == 1999)
		{
			fraImgEff.drawFrame(fSub, x + xLech[2], y + yLech[2], 0, mGraphics.BOTTOM | mGraphics.HCENTER, g);
		}
		if (!isPauseSub2Eff)
		{
			fraImgSubEff.drawFrame(fSub2, x + xLech[3], y + yLech[3], 0, mGraphics.BOTTOM | mGraphics.HCENTER, g);
		}
	}

	public override void paint(mGraphics g)
	{
		if (g == null) return;
		if (typeEffect == 4017 || typeEffect == 4010)
		{
			paintThanTrangSkill(g);
			return;
		}
		if (typeEffect == 4018)
		{
			paintVenomRain4018(g);
			return;
		}
		if (typeEffect == 4019)
		{
			paintVenomBuff4019(g);
			return;
		}
		if (typeEffect == 4021)
		{
			paintThanhLongActive4021(g);
			return;
		}
		if (typeEffect == 4022)
		{
			paintThanhLongActive4022(g);
			return;
		}
		if (typeEffect == 4023)
		{
			paintThanhLongBuff4023(g);
			return;
		}
		if ((typeEffect >= 4001 && typeEffect <= 4080) || (typeEffect >= 4201 && typeEffect <= 4216) || (typeEffect >= 4501 && typeEffect <= 4516))
		{
			paintThanTrangSkill(g);
			return;
		}
		try
		{
			switch (typeEffect)
			{
			case TYPE_NIKA_ACTIVE_1_LEVEL1:
			case TYPE_NIKA_ACTIVE_1_LEVEL5:
			case TYPE_NIKA_ACTIVE_2:
				paintNikaJump(g);
				break;
			case TYPE_NIKA_BUFF:
				break;
			case TYPE_LIGHT_ACTIVE_1_LEVEL5:
			case TYPE_LIGHT_ACTIVE_2_LEVEL5:
			case TYPE_LOVE_ACTIVE_1_LEVEL5:
			case TYPE_LOVE_ACTIVE_2_LEVEL5:
			case TYPE_NIKYU_ACTIVE_1:
			case TYPE_NIKYU_ACTIVE_2:
			case TYPE_NIKYU_BUFF:
				break;
			case 2000:
				fraImgEff.drawFrame(fImg, x, y - 22, 0, 3, g);
				break;
			case 1998:
			case 1999:
				paintEffThunderFall(g);
				break;
			case 280:
				fraImgSubEff.drawFrame(f % fraImgSubEff.nFrame, x, y, 0, 3, g);
				fraImgEff.drawFrame(0, toX, toY, 0, 3, g);
				break;
			case 0:
			case 36:
			case 61:
			case 71:
			case 76:
			case 81:
			case 143:
			case 145:
			case 146:
			case 148:
				fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, x, y, Dir, 3, g);
				break;
			case 82:
			case 144:
				if (f <= fRemove)
				{
					fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, x, y, Dir, 3, g);
				}
				break;
			case 1:
			case 37:
			{
				fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, x, y, Dir, 3, g);
				for (int num39 = 0; num39 < VecEff.size(); num39++)
				{
					Point point22 = (Point)VecEff.elementAt(num39);
					fraImgSubEff.drawFrame(point22.f % fraImgSubEff.nFrame, point22.x, point22.y, Dir, 3, g);
				}
				break;
			}
			case 154:
				fraImgEff.drawFrame(f % fraImgEff.nFrame, x, y, Dir, 3, g);
				break;
			case 155:
				if (f < 2 || f > 5)
				{
					fraImgEff.drawFrame(f % fraImgEff.nFrame, x + xplus, y, Dir, 3, g);
				}
				if (f < 6 && f > 1)
				{
					fraImgSubEff.drawFrame((f + ((f > 3) ? 1 : 0)) % 2, x + xplus, y, 0, 3, g);
				}
				break;
			case 7:
			case 141:
			{
				for (int num67 = 0; num67 < VecEff.size(); num67++)
				{
					Point_Focus point_Focus22 = (Point_Focus)VecEff.elementAt(num67);
					paint_Bullet(g, fraImgEff, point_Focus22.frame, point_Focus22.x, point_Focus22.y, isMore: false, 0);
				}
				break;
			}
			case 142:
			{
				for (int num24 = 0; num24 < VecEff.size(); num24++)
				{
					Point_Focus point_Focus9 = (Point_Focus)VecEff.elementAt(num24);
					fraImgEff.drawFrame((point_Focus9.frame + f) % fraImgEff.nFrame, point_Focus9.x / 10, point_Focus9.y / 10, point_Focus9.dis, 3, g);
				}
				break;
			}
			case 57:
			case 64:
			case 66:
				if (f < fPlayFrameSuper)
				{
					fraImgEff.drawFrame(0, x, y, 0, 3, g);
				}
				break;
			case 58:
				fraImgEff.drawFrame(2, x, y, 0, 3, g);
				break;
			case 126:
			case 159:
			case 192:
			{
				for (int num110 = 0; num110 < VecEff.size(); num110++)
				{
					fraImgEff.drawFrame(2, x, y, 0, 3, g);
				}
				if (objFireMain.isTanHinh)
				{
					fraImgSubEff.drawFrame(0, objFireMain.x, objFireMain.y - objFireMain.dy, Dir, 33, g);
				}
				break;
			}
			case 224:
				if (f == 1 || f == 15)
				{
					fraImgEff.drawFrame(0, x, y, 0, 33, g);
				}
				if (objFireMain.isTanHinh)
				{
					fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x + mframeSuper[(f - 2) / 2][0], y + mframeSuper[(f - 2) / 2][1], Dir, 33, g);
				}
				break;
			case 301:
			{
				if (f == 1 || f == 15)
				{
					fraImgEff.drawFrame(0, x, y, 0, 33, g);
				}
				if (objFireMain.isTanHinh)
				{
					fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x + mframeSuper[(f - 2) / 2][0], y + mframeSuper[(f - 2) / 2][1], Dir, 33, g);
				}
				for (int num85 = 0; num85 < VecSubEff.size(); num85++)
				{
					if (f > num85 * 4)
					{
						Point_Focus point_Focus31 = (Point_Focus)VecSubEff.elementAt(num85);
						int trans9 = 0;
						if (Dir == 0)
						{
							trans9 = 2;
						}
						fraImgSub2Eff.drawFrame(f % fraImgSub2Eff.nFrame, point_Focus31.x, point_Focus31.y, trans9, 3, g);
					}
				}
				break;
			}
			case 501:
			{
				if (f == 1 || f == 15)
				{
					fraImgEff.drawFrame(0, x, y, 0, 33, g);
				}
				if (objFireMain.isTanHinh)
				{
					fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x + mframeSuper[(f - 2) / 2][0], y + mframeSuper[(f - 2) / 2][1], Dir, 33, g);
				}
				for (int l = 0; l < VecSubEff.size(); l++)
				{
					if (f > l * 4)
					{
						Point_Focus point_Focus3 = (Point_Focus)VecSubEff.elementAt(l);
						fraImgSub2Eff.drawFrame(f % fraImgSub2Eff.nFrame, point_Focus3.x, point_Focus3.y, Dir, 3, g);
					}
				}
				break;
			}
			case 9:
			case 53:
			case 163:
			{
				if (f < 3)
				{
					fraImgEff.drawFrame(f, x, y, Dir, 3, g);
					break;
				}
				for (int num38 = 0; num38 < VecEff.size(); num38++)
				{
					Point_Focus point_Focus14 = (Point_Focus)VecEff.elementAt(num38);
					fraImgSubEff.drawFrameNew(indexEff_1 * fraImgSubEff.maxNumFrame + point_Focus14.f % fraImgSubEff.maxNumFrame, point_Focus14.x, point_Focus14.y, 0, 3, g);
					if (typeEffect != 9)
					{
						fraImgSub2Eff.drawFrame(CRes.random(fraImgSub2Eff.nFrame), point_Focus14.x, point_Focus14.y, 0, 3, g);
					}
				}
				break;
			}
			case 63:
			case 190:
			case 222:
			case 312:
			{
				if ((f >= 20 && f < 23) || (f >= 10 && f < 13))
				{
					fraImgEff.drawFrame(f % 10, x, y, Dir, 3, g);
				}
				if (typeEffect == 222 || typeEffect == 312)
				{
					for (int num17 = 0; num17 < VecSubEff.size(); num17++)
					{
						Point point12 = (Point)VecSubEff.elementAt(num17);
						fraImgSub3Eff.drawFrame(point12.f / 2, point12.x, point12.y, 0, 3, g);
					}
				}
				for (int num18 = 0; num18 < VecEff.size(); num18++)
				{
					Point_Focus point_Focus8 = (Point_Focus)VecEff.elementAt(num18);
					fraImgSubEff.drawFrameNew(indexEff_1 * fraImgSubEff.maxNumFrame + point_Focus8.f % fraImgSubEff.maxNumFrame, point_Focus8.x, point_Focus8.y, 0, 3, g);
					fraImgSub2Eff.drawFrame(CRes.random(fraImgSub2Eff.nFrame), point_Focus8.x, point_Focus8.y, 0, 3, g);
				}
				break;
			}
			case 512:
			{
				if ((f >= 20 && f < 23) || (f >= 10 && f < 13))
				{
					fraImgEff.drawFrame(f % 10, x, y, Dir, 3, g);
				}
				for (int num148 = 0; num148 < VecSubEff.size(); num148++)
				{
					Point point67 = (Point)VecSubEff.elementAt(num148);
					fraImgSub3Eff.drawFrame(point67.f / 2, point67.x, point67.y, 0, 3, g);
				}
				for (int num149 = 0; num149 < VecEff.size(); num149++)
				{
					Point_Focus point_Focus51 = (Point_Focus)VecEff.elementAt(num149);
					fraImgSubEff.drawFrameNew(indexEff_1 * fraImgSubEff.maxNumFrame + point_Focus51.f % fraImgSubEff.maxNumFrame, point_Focus51.x, point_Focus51.y, 0, 3, g);
					fraImgSub2Eff.drawFrame(CRes.random(fraImgSub2Eff.nFrame), point_Focus51.x, point_Focus51.y, 0, 3, g);
				}
				break;
			}
			case 11:
			{
				if (f > 3 && f < 12)
				{
					fraImgSub2Eff.drawFrameNew(indexEff_1 * fraImgSub2Eff.maxNumFrame + f % fraImgSub2Eff.maxNumFrame, xplus, yplus, Dir, 3, g);
					fraImgSub3Eff.drawFrame(CRes.random(fraImgSub3Eff.nFrame), xplus, yplus, Dir, 3, g);
					fraImgSubEff.drawFrame(f % fraImgSubEff.nFrame, x, y, 0, 33, g);
				}
				for (int num106 = 0; num106 < VecEff.size(); num106++)
				{
					Point_Focus point_Focus36 = (Point_Focus)VecEff.elementAt(num106);
					fraImgEff.drawFrame(point_Focus36.f % fraImgEff.nFrame, point_Focus36.x, point_Focus36.y, 0, 33, g);
				}
				break;
			}
			case 139:
			{
				if (f > 2 && f < 16)
				{
					fraImgSub2Eff.drawFrameNew(indexEff_1 * fraImgSub2Eff.maxNumFrame + f % fraImgSub2Eff.maxNumFrame, xplus, yplus, Dir, 3, g);
					fraImgSub3Eff.drawFrame(CRes.random(fraImgSub3Eff.nFrame), xplus, yplus, Dir, 3, g);
					fraImgSubEff.drawFrame(f % fraImgSubEff.nFrame, x, y, 0, 33, g);
				}
				for (int num78 = 0; num78 < VecEff.size(); num78++)
				{
					Point_Focus point_Focus27 = (Point_Focus)VecEff.elementAt(num78);
					fraImgEff.drawFrame(point_Focus27.f % fraImgEff.nFrame, point_Focus27.x, point_Focus27.y, 0, 33, g);
					fraImgSub3Eff.drawFrame(CRes.random(fraImgSub3Eff.nFrame), point_Focus27.x + CRes.random_Am_0(10), point_Focus27.y - CRes.random(10), 0, 33, g);
				}
				break;
			}
			case 140:
			{
				if (f > 2 && f < 18)
				{
					fraImgSub2Eff.drawFrameNew(indexEff_1 * fraImgSub2Eff.maxNumFrame + f % fraImgSub2Eff.maxNumFrame, xplus, yplus, Dir, 3, g);
					fraImgSub3Eff.drawFrame(CRes.random(fraImgSub3Eff.nFrame), xplus, yplus, Dir, 3, g);
				}
				for (int num93 = 0; num93 < VecEff.size(); num93++)
				{
					Point_Focus point_Focus33 = (Point_Focus)VecEff.elementAt(num93);
					mImgframe[2].drawFrame(point_Focus33.f % mImgframe[2].nFrame, point_Focus33.x, point_Focus33.y, 0, 3, g);
				}
				if (f >= 32 && f <= 36 && !checkNullObject(2) && CRes.random(4) != 0)
				{
					int num94 = CRes.random(1, 5);
					for (int num95 = 0; num95 < num94; num95++)
					{
						int num96 = CRes.random_Am(0, 25) + objBeFireMain.x;
						mImgframe[1].drawFrame(CRes.random(mImgframe[1].nFrame), num96, objBeFireMain.y - 70, 0, 0, g);
					}
				}
				if (f >= 20 && f <= 38)
				{
					if (f < 24 || f >= 36)
					{
						mImgframe[0].drawFrame(0, objBeFireMain.x, objBeFireMain.y - 60, 0, 33, g);
					}
					else if (f < 28)
					{
						mImgframe[0].drawFrame(1, objBeFireMain.x, objBeFireMain.y - 60, 0, 33, g);
					}
					else if (f < 36)
					{
						mImgframe[0].drawFrame(2, objBeFireMain.x, objBeFireMain.y - 60, 0, 33, g);
					}
				}
				break;
			}
			case 12:
			case 49:
			case 50:
			case 188:
			case 220:
			case 293:
				paintSanji_3(g);
				break;
			case 493:
				paint_Sanji_S3_L7(g);
				break;
			case 16:
			case 51:
				if (f < fRemove)
				{
					fraImgSubEff.drawFrameNew(indexEff_1 * fraImgSubEff.maxNumFrame + f % fraImgSubEff.maxNumFrame, x, y, Dir, 3, g);
					if (fraImgEff != null)
					{
						fraImgEff.drawFrameNew(indexEff_1 * fraImgEff.maxNumFrame + CRes.random(fraImgEff.maxNumFrame), x, y, Dir, 3, g);
					}
				}
				break;
			case 52:
			case 189:
			case 221:
			case 311:
			{
				if (f < fRemove)
				{
					fraImgSubEff.drawFrameNew(indexEff_1 * fraImgSubEff.maxNumFrame + f % fraImgSubEff.maxNumFrame, x, y, Dir, 3, g);
					int num26 = 12 + CRes.random(fraImgEff.maxNumFrame);
					if ((typeEffect == 221 || typeEffect == 311) && CRes.random(2) == 0)
					{
						num26 -= 4;
					}
					fraImgEff.drawFrameNew(num26, x, y, Dir, 3, g);
				}
				for (int num27 = 0; num27 < VecEff.size(); num27++)
				{
					Point point16 = (Point)VecEff.elementAt(num27);
					fraImgEff.drawFrameNew(12 - point16.frame * 4 + point16.f, point16.x, point16.y, Dir, 3, g);
				}
				break;
			}
			case 511:
			{
				if (f < fRemove)
				{
					fraImgSubEff.drawFrameNew(indexEff_1 * fraImgSubEff.maxNumFrame + f % fraImgSubEff.maxNumFrame, x, y, Dir, 3, g);
					int num103 = 12 + CRes.random(fraImgEff.maxNumFrame);
					if (CRes.random(2) == 0)
					{
						num103 -= 4;
					}
					fraImgEff.drawFrameNew(num103, x, y, Dir, 3, g);
				}
				if (f >= 5 && f < fRemove)
				{
					fraImgSub2Eff.drawFrame((f - 5) / 2 % fraImgSub2Eff.nFrame, objBeFireMain.x - am_duong * 30, objBeFireMain.y, Dir, 33, g);
				}
				if (f > 5 && f % 4 == 0)
				{
					fraImgSub3Eff.drawFrameNew(f % fraImgSub3Eff.nFrame, objBeFireMain.x - am_duong * 30, objBeFireMain.y, Dir, 33, g);
				}
				for (int num104 = 0; num104 < VecEff.size(); num104++)
				{
					Point point53 = (Point)VecEff.elementAt(num104);
					fraImgEff.drawFrameNew(12 - point53.frame * 4 + point53.f, point53.x, point53.y, Dir, 3, g);
				}
				break;
			}
			case 19:
				if (!checkNullObject(1) && f >= 1 && f <= 12)
				{
					fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, objFireMain.x, objFireMain.y, 0, 33, g);
				}
				break;
			case 34:
				if (f <= 1 && objFireMain != null)
				{
					if (f == 0)
					{
						fraImgSubEff.drawFrame(f, x, y + objFireMain.hOne / 2, Dir, 33, g);
					}
					else
					{
						fraImgSubEff.drawFrame(f, objFireMain.x, objFireMain.y, Dir, 33, g);
					}
				}
				if (f >= 7 && objFireMain != null)
				{
					int num32 = 16;
					if (Dir == 0)
					{
						num32 = -16;
					}
					fraImgEff.drawFrame(2, objFireMain.x + num32, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, 3, g);
				}
				break;
			case 35:
			{
				if (f == 0 && objFireMain != null)
				{
					fraImgSubEff.drawFrame(0, x, y + objFireMain.hOne / 2, Dir, 33, g);
				}
				if (f >= 5 && objFireMain != null)
				{
					int num136 = 16;
					if (Dir == 0)
					{
						num136 = -16;
					}
					fraImgEff.drawFrame(2, objFireMain.x + num136, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, 3, g);
				}
				for (int num137 = 0; num137 < VecEff.size(); num137++)
				{
					Point point63 = (Point)VecEff.elementAt(num137);
					fraImgSubEff.drawFrame(point63.f / 2, point63.x, point63.y, Dir, 33, g);
				}
				break;
			}
			case 29:
				fraImgEff.drawFrame(f % fraImgEff.nFrame, objFireMain.x, objFireMain.y + 5, Dir, 33, g);
				break;
			case 31:
			case 55:
			case 56:
			case 191:
			case 223:
			case 313:
			{
				if (f < fRemove)
				{
					fraImgSubEff.drawFrameNew(indexEff_1 * fraImgSubEff.maxNumFrame + f % fraImgSubEff.maxNumFrame, x, y, Dir, 3, g);
					if (fraImgSub2Eff != null)
					{
						fraImgSub2Eff.drawFrame(f % fraImgSubEff.nFrame, x, y, Dir, 3, g);
					}
				}
				for (int num124 = 0; num124 < VecEff.size(); num124++)
				{
					Point_Focus point_Focus43 = (Point_Focus)VecEff.elementAt(num124);
					fraImgEff.drawFrame(point_Focus43.f % fraImgEff.nFrame, point_Focus43.x, point_Focus43.y, Dir, 3, g);
					if (fraImgSub2Eff != null)
					{
						fraImgSub2Eff.drawFrame(point_Focus43.f % fraImgSub2Eff.nFrame, point_Focus43.x, point_Focus43.y, Dir, 3, g);
					}
				}
				break;
			}
			case 513:
			{
				if (f < fRemove)
				{
					fraImgSubEff.drawFrameNew(indexEff_1 * fraImgSubEff.maxNumFrame + f % fraImgSubEff.maxNumFrame, x, y, Dir, 3, g);
					if (fraImgSub2Eff != null)
					{
						fraImgSub2Eff.drawFrame(f % fraImgSubEff.nFrame, x, y, Dir, 3, g);
					}
				}
				for (int num82 = 0; num82 < VecEff.size(); num82++)
				{
					Point_Focus point_Focus30 = (Point_Focus)VecEff.elementAt(num82);
					fraImgEff.drawFrame(point_Focus30.f % fraImgEff.nFrame, point_Focus30.x, point_Focus30.y, Dir, 3, g);
					if (fraImgSub2Eff != null)
					{
						fraImgSub2Eff.drawFrame(point_Focus30.f % fraImgSub2Eff.nFrame, point_Focus30.x, point_Focus30.y, Dir, 3, g);
					}
				}
				break;
			}
			case 67:
			case 68:
			case 69:
			case 194:
			case 226:
				if (f >= 10 && f <= fRemove)
				{
					paint_Bullet(g, fraImgEff, frame, x, y, isMore: false, f % 2 * 3);
				}
				break;
			case 303:
			case 503:
				if (f >= 10 && f <= fRemove)
				{
					paint_Bullet(g, fraImgEff, frame, x, y, isMore: false, 0);
					paint_Bullet(g, fraImgEff, frame1, rocket1.x, rocket1.y, isMore: false, 0);
					paint_Bullet(g, fraImgEff, frame2, rocket2.x, rocket2.y, isMore: false, 0);
				}
				break;
			case 164:
			case 227:
			{
				for (int num88 = 0; num88 < VecEff.size(); num88++)
				{
					Point point45 = (Point)VecEff.elementAt(num88);
					if (point45.dis == 0)
					{
						fraImgEff.drawFrame(point45.frame, point45.x, point45.y, Dir, 3, g);
					}
					else
					{
						fraImgSubEff.drawFrame(point45.frame, point45.x, point45.y, Dir, 3, g);
					}
				}
				break;
			}
			case 72:
			case 92:
				fraImgEff.drawFrame(3, x, y, Dir, 3, g);
				break;
			case 75:
				if (f < 2)
				{
					fraImgSubEff.drawFrame(0, x1000, y1000, Dir, 3, g);
				}
				fraImgEff.drawFrame(frame, x, y, Dir, 3, g);
				break;
			case 77:
			{
				int num56 = x;
				int num57 = y;
				if (f > 7)
				{
					fraImgEff.drawFrame(0, num56, num57, Dir, 3, g);
					num57 += 15;
					fraImgSubEff.drawFrame(0, num56, num57, Dir, 3, g);
				}
				break;
			}
			case 160:
				paintLuffy_New2(g);
				break;
			case 84:
			case 181:
			case 213:
			case 272:
				paintLuffy_New2_SHORT(g);
				break;
			case 472:
				paint_Luffy_S2_L7(g);
				break;
			case 85:
			case 182:
			case 214:
			case 273:
				paintLuffy_New3(g);
				break;
			case 473:
				paint_Luffy_S3_L7(g);
				break;
			case 86:
			case 157:
			case 183:
			case 215:
			{
				for (int num58 = 0; num58 < VecEff.size(); num58++)
				{
					Point_Focus point_Focus20 = (Point_Focus)VecEff.elementAt(num58);
					int num59 = point_Focus20.f * fraImgEff.frameHeight / 3 + fraImgEff.frameHeight / 3;
					if (num59 > fraImgEff.frameHeight)
					{
						num59 = fraImgEff.frameHeight;
					}
					if (fraImgEff.getImageFrame() != null)
					{
						g.drawRegion(fraImgEff.getImageFrame(), 0, fraImgEff.frameHeight - num59 + point_Focus20.f % fraImgEff.nFrame * fraImgEff.frameHeight, fraImgEff.frameWidth, num59, 0, point_Focus20.x, point_Focus20.y, 33);
					}
				}
				break;
			}
			case 281:
			{
				for (int num28 = 0; num28 < VecEff.size(); num28++)
				{
					Point_Focus point_Focus11 = (Point_Focus)VecEff.elementAt(num28);
					int num29 = point_Focus11.f * fraImgEff.frameHeight / 3 + fraImgEff.frameHeight / 3;
					if (num29 > fraImgEff.frameHeight)
					{
						num29 = fraImgEff.frameHeight;
					}
					if (fraImgEff.getImageFrame() != null)
					{
						g.drawRegion(fraImgEff.getImageFrame(), 0, fraImgEff.frameHeight - num29 + point_Focus11.f % fraImgEff.nFrame * fraImgEff.frameHeight, fraImgEff.frameWidth, num29, 0, point_Focus11.x, point_Focus11.y, 33);
					}
				}
				for (int num30 = 0; num30 < VecSubEff.size(); num30++)
				{
					if (f > 8 + num30 * 4)
					{
						Point_Focus point_Focus12 = (Point_Focus)VecSubEff.elementAt(num30);
						int trans4 = 0;
						if (Dir == 2)
						{
							trans4 = 2;
						}
						fraImgSub2Eff.drawFrame(f % fraImgSub2Eff.nFrame, point_Focus12.x, point_Focus12.y, trans4, 3, g);
					}
				}
				break;
			}
			case 481:
			{
				for (int num11 = 0; num11 < VecEff.size(); num11++)
				{
					Point_Focus point_Focus6 = (Point_Focus)VecEff.elementAt(num11);
					int num12 = point_Focus6.f * fraImgEff.frameHeight / fraImgEff.nFrame + fraImgEff.frameHeight / fraImgEff.nFrame;
					if (num12 > fraImgEff.frameHeight)
					{
						num12 = fraImgEff.frameHeight;
					}
					if (fraImgEff.getImageFrame() != null)
					{
						g.drawRegion(fraImgEff.getImageFrame(), 0, fraImgEff.frameHeight - num12 + point_Focus6.f % fraImgEff.nFrame * fraImgEff.frameHeight, fraImgEff.frameWidth, num12, 0, x, y, 33);
					}
				}
				for (int num13 = 0; num13 < VecSubEff.size(); num13++)
				{
					Point_Focus point_Focus7 = (Point_Focus)VecSubEff.elementAt(num13);
					int trans2 = 2;
					if (Dir == 2)
					{
						trans2 = 0;
					}
					if (f > 8)
					{
						fraImgSub2Eff.drawFrame((f - 8) / numNextFrame % fraImgSub2Eff.nFrame, point_Focus7.x, point_Focus7.y + 5, trans2, 3, g);
					}
				}
				break;
			}
			case 266:
			{
				for (int k = 0; k < VecEff.size(); k++)
				{
					if (f > 3 + k * 4)
					{
						Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(k);
						int trans = 0;
						if (Dir == 2)
						{
							trans = 2;
						}
						fraImgEff.drawFrame(0, point_Focus.x, point_Focus.y, trans, 3, g);
					}
				}
				break;
			}
			case 276:
			case 277:
			{
				for (int num138 = 0; num138 < VecEff.size(); num138++)
				{
					Point_Focus point_Focus48 = (Point_Focus)VecEff.elementAt(num138);
					fraImgEff.drawFrame((point_Focus48.frame + point_Focus48.f) % fraImgEff.nFrame, point_Focus48.x, point_Focus48.y, Dir, 3, g);
				}
				break;
			}
			case 278:
			case 279:
			{
				for (int num127 = 0; num127 < VecEff.size(); num127++)
				{
					Point point59 = (Point)VecEff.elementAt(num127);
					fraImgEff.drawFrameNew(point59.frame + point59.dis * 5, objBeFireMain.x + point59.x, y + point59.y, 0, 3, g);
				}
				for (int num128 = 0; num128 < VecSubEff.size(); num128++)
				{
					Point point60 = (Point)VecSubEff.elementAt(num128);
					fraImgSubEff.drawFrameNew(point60.frame + point60.dis * 4, objBeFireMain.x + point60.x, y + point60.y, 0, 3, g);
				}
				break;
			}
			case 267:
			{
				if (f <= 2)
				{
					break;
				}
				for (int num118 = 0; num118 < VecEff.size(); num118++)
				{
					Point_Focus point_Focus41 = (Point_Focus)VecEff.elementAt(num118);
					int trans11 = 0;
					if (Dir == 2)
					{
						trans11 = 2;
					}
					fraImgEff.drawFrame(0, point_Focus41.x, point_Focus41.y - 5, trans11, 3, g);
				}
				break;
			}
			case 268:
				if ((f >= 2 && f <= 11) || (f >= 16 && f <= 25))
				{
					fraImgEff.drawFrame(GameCanvas.gameTickChia4 % 2, x1000, y + 3, 0, 3, g);
				}
				break;
			case 269:
			{
				for (int num107 = 0; num107 < VecEff.size(); num107++)
				{
					Point point54 = (Point)VecEff.elementAt(num107);
					fraImgEff.drawFrame(GameCanvas.gameTickChia4 % 2, point54.x, point54.y, Dir, 3, g);
				}
				break;
			}
			case 87:
			case 184:
			case 216:
				if (f > 12 && f < 15)
				{
					fraImgEff.drawFrame(f - 13, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				else if (f > 22 && f < 25)
				{
					fraImgEff.drawFrame(f - 23, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				else if (f > 28 && f < 31)
				{
					fraImgEff.drawFrame(f - 29, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				else if (f > 34 && f < 37)
				{
					fraImgEff.drawFrame(f - 35, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				break;
			case 282:
			{
				if (f > 12 && f < 15)
				{
					fraImgEff.drawFrame(f - 13, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				else if (f > 22 && f < 25)
				{
					fraImgEff.drawFrame(f - 23, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				else if (f > 28 && f < 31)
				{
					fraImgEff.drawFrame(f - 29, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				else if (f > 34 && f < 37)
				{
					fraImgEff.drawFrame(f - 35, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				for (int num40 = 0; num40 < VecEff.size(); num40++)
				{
					Point point23 = (Point)VecEff.elementAt(num40);
					int trans6 = point23.dis;
					int idx2 = point23.frame;
					if (point23.frame == 2)
					{
						trans6 = 5;
					}
					else if (point23.frame == 3)
					{
						idx2 = 2;
					}
					fraImgSubEff.drawFrame(idx2, point23.x, point23.y, trans6, 3, g);
				}
				break;
			}
			case 482:
			{
				if (mframe[f] > -1)
				{
					fraImgEff.drawFrame(mframe[f], objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				for (int num36 = 0; num36 < VecEff.size(); num36++)
				{
					Point point21 = (Point)VecEff.elementAt(num36);
					if (mframeSub[point21.f] > -1)
					{
						fraImgSubEff.drawFrame(mframeSub[point21.f], point21.x, point21.y, (Dir != 2) ? 2 : 0, 3, g);
					}
				}
				if (f <= 30)
				{
					break;
				}
				for (int num37 = 0; num37 < VecSubEff.size(); num37++)
				{
					Point_Focus point_Focus13 = (Point_Focus)VecSubEff.elementAt(num37);
					if (Dir == 2)
					{
						fraImgSubEff.drawFrame(7, point_Focus13.x, point_Focus13.y, (num37 == 0) ? 2 : 0, 3, g);
					}
					else
					{
						fraImgSubEff.drawFrame(7, point_Focus13.x, point_Focus13.y, (num37 != 0) ? 2 : 0, 3, g);
					}
				}
				break;
			}
			case 161:
				paintZoroS2_L3_SHORT(g);
				break;
			case 93:
				if (f > 2 && f < 6)
				{
					fraImgEff.drawFrame(f - 3, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				if (f > 8 && f < 12)
				{
					fraImgEff.drawFrame(11 - f, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				if (f > 26 && f < 29)
				{
					fraImgEff.drawFrame(f - 27, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
				}
				break;
			case 94:
				if (f <= 3)
				{
					fraImgEff.drawFrame(f / 2, x, y, Dir, 33, g);
				}
				if (f > 3 && f <= 7)
				{
					fraImgEff.drawFrame((f - 4) / 2, x, y, (Dir == 0) ? 2 : 0, 33, g);
				}
				break;
			case 95:
			{
				if (f < 2)
				{
					fraImgEff.drawFrame(f, x, y + 3, Dir, 3, g);
				}
				for (int num119 = 0; num119 < VecEff.size(); num119++)
				{
					Point_Focus point_Focus42 = (Point_Focus)VecEff.elementAt(num119);
					fraImgEff.drawFrame(point_Focus42.frame, point_Focus42.x, point_Focus42.y, Dir, 3, g);
				}
				break;
			}
			case 96:
				paintBuggy_2(g);
				break;
			case 97:
			{
				if (f < 4)
				{
					fraImgSub2Eff.drawFrame(f, x, y, Dir, 3, g);
				}
				for (int num108 = 0; num108 < VecEff.size(); num108++)
				{
					Point_Focus point_Focus37 = (Point_Focus)VecEff.elementAt(num108);
					if (point_Focus37.frame == 0)
					{
						fraImgEff.drawFrame(f % fraImgEff.nFrame, point_Focus37.x, point_Focus37.y, Dir, 3, g);
					}
					else
					{
						fraImgSubEff.drawFrame(f % fraImgSubEff.nFrame, point_Focus37.x, point_Focus37.y, Dir, 3, g);
					}
				}
				break;
			}
			case 22:
			case 98:
				if (!checkNullObject(1))
				{
					if (f < 5)
					{
						fraImgSubEff.drawFrame(f % fraImgSubEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy, 0, 33, g);
					}
					if (f >= 10 && f <= 14)
					{
						fraImgEff.drawFrame(f % fraImgEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy, 0, 33, g);
					}
				}
				break;
			case 248:
			case 255:
				if (!checkNullObject(1))
				{
					if (f == 5 || f == 6 || f == 10 || f == 11 || f == 14)
					{
						fraImgSubEff.drawFrame(0, objFireMain.x, objFireMain.y - objFireMain.dy, 0, 33, g);
					}
					if (f >= 15 && f <= 19)
					{
						fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy, 0, 33, g);
					}
				}
				break;
			case 99:
			{
				int num72 = f % 4;
				if (num72 < 4 && f < 8)
				{
					fraImgEff.drawFrame(num72, x, y - num72 * 2 + 5, Dir, 3, g);
					int trans8 = 1;
					if (Dir == 2)
					{
						trans8 = 3;
					}
					fraImgEff.drawFrame(num72, x, y - num72 * 2 - 15, trans8, 3, g);
				}
				break;
			}
			case 100:
				if (f >= 5 && f <= 11)
				{
					fraImgEff.drawFrame(0, objFireMain.x, objFireMain.y, Dir, 33, g);
				}
				break;
			case 101:
				if (f >= 6 && f <= 15)
				{
					int num62 = (f - 2) % 4;
					fraImgEff.drawFrame(num62, objFireMain.x + x1000, objFireMain.y - objFireMain.hOne / 2 - num62 * 2 + 5, Dir, 3, g);
					int trans7 = 1;
					if (Dir == 2)
					{
						trans7 = 3;
					}
					fraImgEff.drawFrame(num62, objFireMain.x + x1000, objFireMain.y - objFireMain.hOne / 2 - num62 * 2 - 15, trans7, 3, g);
				}
				break;
			case 102:
			{
				if (f < 4)
				{
					fraImgSub2Eff.drawFrame(f, x, y, Dir, 3, g);
				}
				for (int num45 = 0; num45 < VecEff.size(); num45++)
				{
					Point_Focus point_Focus17 = (Point_Focus)VecEff.elementAt(num45);
					fraImgEff.drawFrame(point_Focus17.f % fraImgEff.nFrame, point_Focus17.x, point_Focus17.y, Dir, 3, g);
				}
				for (int num46 = 0; num46 < VecSubEff.size(); num46++)
				{
					Point point26 = (Point)VecSubEff.elementAt(num46);
					fraImgSubEff.drawFrame((point26.f + point26.frame) % fraImgSubEff.nFrame, point26.x, point26.y, Dir, 3, g);
				}
				break;
			}
			case 103:
			{
				if (f < 4)
				{
					fraImgSubEff.drawFrame(0, x, y, Dir, 33, g);
				}
				for (int num35 = 0; num35 < VecEff.size(); num35++)
				{
					Point point20 = (Point)VecEff.elementAt(num35);
					int trans5 = Dir;
					if (point20.frame == 2)
					{
						trans5 = 5;
					}
					fraImgEff.drawFrame(point20.frame, point20.x, point20.y, trans5, 3, g);
				}
				break;
			}
			case 104:
			{
				if (f < 8 && f % 2 == 1)
				{
					fraImgSubEff.drawFrame(0, x, y, Dir, 33, g);
				}
				for (int num23 = 0; num23 < VecEff.size(); num23++)
				{
					Point point15 = (Point)VecEff.elementAt(num23);
					if (point15.frame == 4)
					{
						fraImgSubEff.drawFrame(0, point15.x, point15.y, Dir, 33, g);
						continue;
					}
					int trans3 = point15.dis;
					int idx = point15.frame;
					if (point15.frame == 2)
					{
						trans3 = 5;
					}
					else if (point15.frame == 3)
					{
						idx = 2;
					}
					fraImgEff.drawFrame(idx, point15.x, point15.y, trans3, 3, g);
				}
				break;
			}
			case 106:
			{
				if (f < 10 || f % 4 > 1)
				{
					for (int num5 = 0; num5 < VecEff.size(); num5++)
					{
						Point point6 = (Point)VecEff.elementAt(num5);
						fraImgEff.drawFrame((f / 2 + point6.frame) % 3, point6.x, point6.y, Dir, 3, g);
					}
				}
				for (int num6 = 0; num6 < VecSubEff.size(); num6++)
				{
					Point_Focus point_Focus4 = (Point_Focus)VecSubEff.elementAt(num6);
					fraImgEff.drawFrame((f + point_Focus4.frame) % 3, point_Focus4.x, point_Focus4.y - 4, Dir, 3, g);
					if (point_Focus4.f % 2 == 0)
					{
						fraImgSubEff.drawFrame(0, point_Focus4.x, point_Focus4.y + 4, Dir, 3, g);
					}
				}
				break;
			}
			case 65:
			case 107:
				if (f < 4)
				{
					fraImgEff.drawFrame(f / 2, x, y, Dir, 33, g);
				}
				break;
			case 70:
				if (f < 4)
				{
					fraImgEff.drawFrame(f / 2, x, y, Dir, 33, g);
				}
				else
				{
					fraImgSubEff.drawFrame(f % fraImgSubEff.nFrame, x, y, Dir, 33, g);
				}
				break;
			case 108:
			{
				for (int j = 0; j < VecEff.size(); j++)
				{
					Point point2 = (Point)VecEff.elementAt(j);
					fraImgEff.drawFrame((point2.frame + f / point2.dis) % fraImgEff.nFrame, point2.x, point2.y, Dir, 3, g);
				}
				break;
			}
			case 109:
				paintDonKrieg_1(g);
				break;
			case 110:
				paintDonKrieg_2(g);
				break;
			case 111:
				paintDonKrieg_3(g);
				break;
			case 112:
			case 270:
				fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, x, y, Dir, 3, g);
				break;
			case 113:
			case 150:
			{
				for (int num139 = 0; num139 < VecEff.size(); num139++)
				{
					Point_Focus point_Focus49 = (Point_Focus)VecEff.elementAt(num139);
					fraImgEff.drawFrame(f % fraImgEff.nFrame, point_Focus49.x, point_Focus49.y, Dir, 3, g);
				}
				break;
			}
			case 151:
			case 152:
			case 153:
			{
				for (int num140 = 0; num140 < VecEff.size(); num140++)
				{
					Point_Focus point_Focus50 = (Point_Focus)VecEff.elementAt(num140);
					fraImgEff.drawFrame(frame * 3 + f / 2 % 2, point_Focus50.x, point_Focus50.y, Dir, 3, g);
				}
				break;
			}
			case 114:
			case 115:
			{
				for (int num129 = 0; num129 < VecEff.size(); num129++)
				{
					Point_Focus point_Focus45 = (Point_Focus)VecEff.elementAt(num129);
					fraImgEff.drawFrame(4, point_Focus45.x, point_Focus45.y, Dir, 3, g);
				}
				break;
			}
			case 116:
				if (f >= 11 && f <= 16)
				{
					fraImgEff.drawFrame((f - 11) / 3, x + x1000, y, Dir, 3, g);
				}
				else if (f >= 26 && f <= 31)
				{
					fraImgEff.drawFrame((f - 26) / 3, x + x1000, y, Dir, 3, g);
				}
				break;
			case 117:
				paintKurobi_2(g);
				break;
			case 118:
				if (vecObjsBeFire.size() > 1)
				{
					for (int num113 = 0; num113 < VecEff.size(); num113++)
					{
						Point point55 = (Point)VecEff.elementAt(num113);
						fraImgEff.drawFrame(point55.f / 2, point55.x, point55.y, point55.dis, 3, g);
					}
				}
				else
				{
					fraImgEff.drawFrame(f / 2, x, y, Dir, 3, g);
				}
				break;
			case 119:
			{
				for (int num114 = 0; num114 < VecEff.size(); num114++)
				{
					Point_Focus point_Focus39 = (Point_Focus)VecEff.elementAt(num114);
					fraImgEff.drawFrame(0, point_Focus39.x, point_Focus39.y, point_Focus39.dis, 3, g);
					fraImgSubEff.drawFrame(0, point_Focus39.x, point_Focus39.y + 30, point_Focus39.dis, 3, g);
					if (f % 2 == 0)
					{
						if (point_Focus39.dis == 0)
						{
							fraImgSub2Eff.drawFrame(CRes.random(2), point_Focus39.x - 25, point_Focus39.y, 0, 3, g);
						}
						else if (point_Focus39.dis == 2)
						{
							fraImgSub2Eff.drawFrame(CRes.random(2), point_Focus39.x + 25, point_Focus39.y, 2, 3, g);
						}
					}
				}
				break;
			}
			case 120:
			{
				if (f <= 9)
				{
					fraImgSubEff.drawFrame(0, x + plusxy[2][0], y + plusxy[2][1], Dir, 3, g);
				}
				else if (f >= 10 && f <= 11)
				{
					fraImgEff.drawFrame(0, x + plusxy[0][0], y + plusxy[0][1], Dir, 3, g);
					fraImgSubEff.drawFrame(1, x + plusxy[3][0], y + plusxy[3][1], Dir, 3, g);
				}
				else if (f >= 12 && f <= 13)
				{
					fraImgEff.drawFrame(1, x + plusxy[1][0], y + plusxy[1][1], Dir, 3, g);
					fraImgSubEff.drawFrame(2, x + plusxy[4][0], y + plusxy[4][1], Dir, 3, g);
				}
				for (int num102 = 0; num102 < VecEff.size(); num102++)
				{
					Point point52 = (Point)VecEff.elementAt(num102);
					fraImgSub2Eff.drawFrame(point52.frame, point52.x, point52.y, Dir, 33, g);
				}
				break;
			}
			case 121:
				if (f >= 13)
				{
					fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, x, y, Dir, 3, g);
				}
				break;
			case 122:
			{
				if (f >= 16)
				{
					fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, x, y, Dir, 3, g);
					fraImgSubEff.drawFrame(f / 2 % 2, x1000, y1000, Dir, 0, g);
				}
				for (int num105 = 0; num105 < VecEff.size(); num105++)
				{
					Point_Focus point_Focus35 = (Point_Focus)VecEff.elementAt(num105);
					fraImgSub2Eff.drawFrame(point_Focus35.f % fraImgSub2Eff.nFrame, point_Focus35.x, point_Focus35.y, Dir, 33, g);
				}
				break;
			}
			case 123:
			case 185:
			case 217:
			case 283:
			{
				if ((f >= 9 && f <= 11) || (f >= 24 && f <= 26))
				{
					fraImgSub4Eff.drawFrame(0, objFireMain.x, objFireMain.y, Dir, 33, g);
				}
				if (f <= 11 || f >= 26)
				{
					if (typeEffect == 185 || typeEffect == 217 || typeEffect == 283)
					{
						int num53 = f / 2 % fraImgEff.nFrame;
						if (typeEffect == 217 || typeEffect == 283)
						{
							num53 += 2;
						}
						fraImgEff.drawFrameNew(num53, x + am_duong * 5, y, Dir, 3, g);
					}
					else
					{
						fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, x, y, Dir, 3, g);
					}
					fraImgSubEff.drawFrame(f / 2 % 2, x1000, y1000, Dir, 0, g);
				}
				if (typeEffect == 283)
				{
					for (int num54 = 0; num54 < VecEff.size(); num54++)
					{
						Point_Focus point_Focus19 = (Point_Focus)VecEff.elementAt(num54);
						fraImgSub2Eff.drawFrame(point_Focus19.f % fraImgSub2Eff.nFrame, point_Focus19.x, point_Focus19.y, Dir, 33, g);
					}
					break;
				}
				for (int num55 = 0; num55 < VecEff.size(); num55++)
				{
					Point point32 = (Point)VecEff.elementAt(num55);
					if (point32.f >= 3 && (point32.f - 3) / 2 < 3)
					{
						fraImgSub2Eff.drawFrame((point32.f - 3) / 2, point32.x, point32.y, Dir, 3, g);
					}
					if (point32.f / 2 < 3)
					{
						fraImgSub3Eff.drawFrame(point32.f / 2, point32.x, point32.y, Dir, 3, g);
					}
				}
				break;
			}
			case 483:
			{
				if ((f >= 9 && f <= 11) || (f >= 24 && f <= 26))
				{
					fraImgSub4Eff.drawFrame(0, objFireMain.x, objFireMain.y, Dir, 33, g);
				}
				if (f <= 11 || f >= 26)
				{
					fraImgEff.drawFrameNew(f / numNextFrame % fraImgEff.nFrame, x + am_duong * 5, y, (Dir == 0) ? 2 : 0, 3, g);
				}
				fraImgSubEff.drawFrame(f % fraImgSubEff.nFrame, objFireMain.x, objFireMain.y + 20, (Dir == 0) ? 2 : 0, 33, g);
				for (int num25 = 0; num25 < VecEff.size(); num25++)
				{
					Point_Focus point_Focus10 = (Point_Focus)VecEff.elementAt(num25);
					fraImgSub2Eff.drawFrame(point_Focus10.f / numNextFrame % fraImgSub2Eff.nFrame, point_Focus10.x, point_Focus10.y, Dir, 33, g);
				}
				break;
			}
			case 158:
			case 177:
				if (f >= 20 && f <= 25)
				{
					fraImgEff.drawFrame(0, objFireMain.x, objFireMain.y - objFireMain.dy, Dir, 33, g);
				}
				break;
			case 124:
			case 186:
			case 218:
				if (f >= 0 && f <= 5)
				{
					fraImgEff.drawFrame(0, objFireMain.x, objFireMain.y - objFireMain.dy, Dir, 33, g);
				}
				break;
			case 125:
			case 162:
			case 187:
				if (objFireMain.isTanHinh)
				{
					fraImgEff.drawFrame(0, objFireMain.x, objFireMain.y - objFireMain.dy, Dir, 33, g);
				}
				break;
			case 127:
			case 193:
			case 225:
			case 302:
				if (typeEffect == 302 && f > 2 && f < 15)
				{
					Point_Focus point_Focus2 = (Point_Focus)VecEff.elementAt(0);
					fraImgSub3Eff.drawFrame((f / 3 < fraImgSub3Eff.nFrame) ? (f / 3) : (fraImgSub3Eff.nFrame - 1), point_Focus2.x, point_Focus2.y, Dir ^ 2, 3, g);
				}
				if (f >= 7 && f <= 15)
				{
					fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, objFireMain.x + am_duong * 40, objFireMain.y - objFireMain.hOne / 2 - 10, Dir, 3, g);
				}
				if (f >= 15)
				{
					fraImgSub2Eff.drawFrame(0, x, y + 50, Dir, 3, g);
					fraImgSubEff.drawFrame(mframe[f / 2 % mframe.Length], x, y, Dir, 3, g);
				}
				break;
			case 502:
				if (f > 2 && f < 15)
				{
					Point_Focus point_Focus44 = (Point_Focus)VecEff.elementAt(0);
					fraImgSub3Eff.drawFrame((f / 3 < fraImgSub3Eff.nFrame) ? (f / 3) : (fraImgSub3Eff.nFrame - 1), point_Focus44.x, point_Focus44.y, Dir ^ 2, 3, g);
				}
				if (f >= 7 && f <= 15)
				{
					fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, objFireMain.x + am_duong * 40, objFireMain.y - objFireMain.hOne / 2 - 10, Dir, 3, g);
				}
				if (f >= 15)
				{
					int num125 = 40;
					int num126 = 20;
					if (Dir == 2)
					{
						num125 = -40;
						num126 = 20;
					}
					fraImgSub2Eff.drawFrame(0, x, y + 50, Dir, 3, g);
					fraImgSubEff.drawFrame(mframe[f / 2 % mframe.Length], x, y, Dir, 3, g);
					fraImgSub2Eff.drawFrame(0, x + num125, y - num126 + 50, Dir, 3, g);
					fraImgSubEff.drawFrame(mframe[f / 2 % mframe.Length], x + num125, y - num126, Dir, 3, g);
				}
				break;
			case 133:
			{
				for (int num109 = 0; num109 < VecEff.size(); num109++)
				{
					Point_Focus point_Focus38 = (Point_Focus)VecEff.elementAt(num109);
					fraImgEff.drawFrame((point_Focus38.frame + point_Focus38.f / 2) % fraImgEff.nFrame, point_Focus38.x + CRes.random_Am_0(3), point_Focus38.y + CRes.random_Am_0(3), point_Focus38.dis, 3, g);
				}
				if (f >= 2 && f <= 4)
				{
					fraImgSubEff.drawFrame(f - 7, x + am_duong * 17, y, (Dir != 2) ? 2 : 0, 3, g);
				}
				if (f >= 10 && f <= 12)
				{
					fraImgSubEff.drawFrame(f - 15, x + am_duong * 17, y, (Dir != 2) ? 2 : 0, 3, g);
				}
				break;
			}
			case 134:
			case 135:
			{
				if (fraImgSub3Eff != null)
				{
					for (int num75 = 0; num75 < VecSubEff.size(); num75++)
					{
						Point point41 = (Point)VecSubEff.elementAt(num75);
						fraImgSub3Eff.drawFrame(1 + point41.f / 2, point41.x, point41.y, 0, 3, g);
					}
				}
				for (int num76 = 0; num76 < VecEff.size(); num76++)
				{
					Point_Focus point_Focus25 = (Point_Focus)VecEff.elementAt(num76);
					if (point_Focus25.maxdis == 1)
					{
						fraImgSub2Eff.drawFrameNew(point_Focus25.frame % fraImgSub2Eff.nFrame, point_Focus25.x + CRes.random_Am_0(5), point_Focus25.y + CRes.random_Am_0(5), point_Focus25.dis, 3, g);
					}
					else
					{
						fraImgEff.drawFrame((point_Focus25.frame + point_Focus25.f / 2) % fraImgEff.nFrame, point_Focus25.x + CRes.random_Am_0(5), point_Focus25.y + CRes.random_Am_0(5), point_Focus25.dis, 3, g);
					}
				}
				if (f >= 2 && f <= 4)
				{
					fraImgSubEff.drawFrame(f - 7, x + am_duong * 17, y, (Dir != 2) ? 2 : 0, 3, g);
				}
				if (f >= 5 && f <= 7)
				{
					fraImgSubEff.drawFrame(f - 15, x + am_duong * 17, y, (Dir != 2) ? 2 : 0, 3, g);
				}
				if (!checkNullObject(1) && f >= 10 && f <= 13)
				{
					fraImgSubEff.drawFrame(f - 15, x + am_duong * 17, y - objFireMain.dy, (Dir != 2) ? 2 : 0, 3, g);
				}
				break;
			}
			case 136:
				if (f == 4 || f == 10 || f == 14)
				{
					fraImgSubEff.drawFrame(0, objFireMain.x, objFireMain.y, Dir, 33, g);
				}
				if (f == 1 || f == 3 || f == 11 || f == 13)
				{
					fraImgEff.drawFrame(0, objFireMain.x, objFireMain.y, 0, 33, g);
				}
				break;
			case 137:
			case 138:
				if (f == 2 || f == 13 || f == 18)
				{
					fraImgSubEff.drawFrame(0, objFireMain.x, objFireMain.y, Dir, 33, g);
				}
				if (f == 3 || f == 12 || f == 19)
				{
					fraImgEff.drawFrame(0, objFireMain.x, objFireMain.y, 0, 33, g);
				}
				break;
			case 2:
			{
				for (int num44 = 0; num44 < VecEff.size(); num44++)
				{
					Point point25 = (Point)VecEff.elementAt(num44);
					fraImgEff.drawFrameNew(point25.frame, point25.x, point25.y, 0, 33, g);
				}
				break;
			}
			case 228:
			case 259:
			case 260:
			case 261:
			{
				for (int num33 = 0; num33 < VecSubEff.size(); num33++)
				{
					Point point18 = (Point)VecSubEff.elementAt(num33);
					fraImgSub2Eff.drawFrameNew_BeginSuper(point18.f, point18.x, point18.y, 0, 3, g);
				}
				for (int num34 = 0; num34 < VecEff.size(); num34++)
				{
					Point point19 = (Point)VecEff.elementAt(num34);
					fraImgEff.drawFrameNew_BeginSuper(point19.frame, point19.x, point19.y, 0, 33, g);
				}
				if (!checkNullObject(1) && f >= 4 && f <= 12)
				{
					fraImgSubEff.drawFrameNew_BeginSuper(f % fraImgSubEff.maxNumFrame, objFireMain.x - am_duong * 20, objFireMain.y - objFireMain.dy - 15, objFireMain.type_left_right, 3, g);
				}
				break;
			}
			case 3:
			case 229:
			case 262:
			case 263:
			case 264:
			{
				for (int num19 = 0; num19 < VecEff.size(); num19++)
				{
					Point point13 = (Point)VecEff.elementAt(num19);
					if (point13.frame == 0)
					{
						int num20 = 0;
						num20 = ((point13.f >= point13.fRe - 3) ? (fraImgSubEff.maxNumFrame - (point13.fRe - point13.f)) : (point13.f % 2));
						fraImgSubEff.drawFrameNew_BeginSuper(num20, point13.x / 1000, point13.y / 1000, 0, 3, g);
					}
					else
					{
						int num21 = 0;
						num21 = ((point13.f >= point13.fRe - 3) ? (fraImgEff.maxNumFrame - (point13.fRe - point13.f)) : ((point13.f + point13.fSmall) % 3));
						fraImgEff.drawFrameNew_BeginSuper(num21, point13.x / 1000, point13.y / 1000, 0, 3, g);
					}
				}
				for (int num22 = 0; num22 < VecSubEff.size(); num22++)
				{
					Point point14 = (Point)VecSubEff.elementAt(num22);
					if (point14.frame == 0)
					{
						fraImgSubEff.drawFrameNew_BeginSuper(point14.f % fraImgSubEff.maxNumFrame, point14.x, point14.y, 0, 3, g);
					}
					else if (point14.frame == 1)
					{
						fraImgSub2Eff.drawFrameNew_BeginSuper(point14.f / 2 % 3, point14.x, point14.y, 0, 33, g);
					}
				}
				if (f >= 13 && f <= 23)
				{
					fraImgSub3Eff.drawFrameNew_BeginSuper(f / 2 % 3, x, y + 8, 0, 33, g);
				}
				else if (f >= 8 && f <= 28)
				{
					fraImgEff.drawFrameNew_BeginSuper(f % 5, x, y + 3, 0, 33, g);
				}
				break;
			}
			case 400:
			case 401:
			case 403:
				break;
			case 402:
			{
				for (int num14 = 0; num14 < VecEff.size(); num14++)
				{
					Point point10 = (Point)VecEff.elementAt(num14);
					if (point10.frame != 0)
					{
						int num15 = 0;
						num15 = ((point10.f >= point10.fRe - 3) ? (fraImgEff.maxNumFrame - (point10.fRe - point10.f)) : ((point10.f + point10.fSmall) % 3));
						fraImgEff.drawFrameNew_BeginSuper(num15, point10.x / 1000, point10.y / 1000, 0, 3, g);
					}
				}
				break;
			}

			case 404:
			case 405:
			case 406:
			case 407:
			case 408:
			case 409:
			case 410:
			case 411:
				break;
			case 4:
			case 230:
			{
				if (f >= 0 && f < mframe.Length)
				{
					fraImgSub2Eff.drawFrame(mframe[f], x, y + 4, 0, 33, g);
				}
				for (int n = 0; n < VecSubEff.size(); n++)
				{
					Point point4 = (Point)VecSubEff.elementAt(n);
					fraImgSub3Eff.drawFrame(point4.f / 2 % fraImgSub3Eff.nFrame, point4.x, point4.y, 0, 3, g);
				}
				for (int num2 = 0; num2 < VecEff.size(); num2++)
				{
					Point point5 = (Point)VecEff.elementAt(num2);
					if (point5.f >= point5.fSmall)
					{
						if (point5.frame == 0)
						{
							fraImgEff.drawFrame(0, point5.x, point5.y, 0, 3, g);
						}
						else if (point5.frame == 1 && fraImgEff.getImageFrame() != null)
						{
							g.drawRegion(fraImgEff.getImageFrame(), 0, 0, fraImgEff.frameWidth, fraImgEff.frameHeight - point5.dis, 0, point5.x, point5.y, 33);
						}
					}
				}
				break;
			}
			case 5:
			case 231:
			{
				for (int num150 = 0; num150 < VecSubEff.size(); num150++)
				{
					Point point68 = (Point)VecSubEff.elementAt(num150);
					fraImgSub3Eff.drawFrame(point68.f / 2 % fraImgSub3Eff.nFrame, point68.x, point68.y, 0, 3, g);
				}
				if (f >= 10 && f <= 15)
				{
					fraImgEff.drawFrame(0, x + plusxy[0][0] * am_duong, y + plusxy[0][1], Dir, 3, g);
				}
				if (f > 15 && f <= 17)
				{
					fraImgEff.drawFrame(1, x + plusxy[1][0] * am_duong, y + plusxy[1][1], Dir, 3, g);
				}
				if (f > 17 && f <= 26)
				{
					fraImgSubEff.drawFrame((f - 18) / 3, x + plusxy[2][0] * am_duong, y + plusxy[2][1], Dir, 3, g);
				}
				break;
			}
			case 6:
			case 232:
			{
				if (f >= 20 && f <= 24)
				{
					fraImgEff.drawFrame((f - 30) / 2, x, y, Dir, 3, g);
				}
				for (int num130 = 0; num130 < VecSubEff.size(); num130++)
				{
					Point point61 = (Point)VecSubEff.elementAt(num130);
					if (point61.frame == 1)
					{
						fraImgSub3Eff.drawFrame(3 + point61.f % 3, point61.x, point61.y, 0, 3, g);
					}
					else
					{
						fraImgSub2Eff.drawFrame(point61.f % fraImgSub2Eff.nFrame, point61.x, point61.y, 0, 3, g);
					}
				}
				for (int num131 = 0; num131 < VecEff.size(); num131++)
				{
					Point_Focus point_Focus46 = (Point_Focus)VecEff.elementAt(num131);
					fraImgSubEff.drawFrame(point_Focus46.frame / 2, point_Focus46.x, point_Focus46.y, Dir, 3, g);
				}
				break;
			}
			case 10:
			case 234:
			{
				if (!checkNullObject(1))
				{
					if (f >= 7)
					{
						fraImgEff.drawFrame((f - 7) / 2, objFireMain.x, objFireMain.y, Dir, 33, g);
					}
					if (f >= 7 && f <= 16)
					{
						fraImgSubEff.drawFrame((f - 11) / 2 % fraImgSubEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy + 5, Dir, 33, g);
					}
					if (f >= 24 && f <= 29)
					{
						fraImgSubEff.drawFrame((2 - (f - 34)) / 2 % fraImgSubEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy + 5, Dir, 33, g);
					}
				}
				for (int num116 = 0; num116 < VecSubEff.size(); num116++)
				{
					Point point56 = (Point)VecSubEff.elementAt(num116);
					if (point56.frame == 1)
					{
						fraImgSub4Eff.drawFrame(3 + point56.f % 3, point56.x, point56.y, 0, 3, g);
					}
					else
					{
						fraImgSub3Eff.drawFrame(point56.f % fraImgSub3Eff.nFrame, point56.x, point56.y, 0, 3, g);
					}
				}
				for (int num117 = 0; num117 < VecEff.size(); num117++)
				{
					Point_Focus point_Focus40 = (Point_Focus)VecEff.elementAt(num117);
					fraImgSub2Eff.drawFrame(point_Focus40.f / 2 % fraImgSub2Eff.nFrame, point_Focus40.x, point_Focus40.y, Dir, 3, g);
				}
				break;
			}
			case 147:
				fraImgEff.drawFrame(5, x, y, Dir, 3, g);
				break;
			case 149:
			{
				int num115 = f;
				if (num115 > 2)
				{
					num115 = 2;
				}
				fraImgEff.drawFrame(num115, x, y, Dir, 3, g);
				break;
			}
			case 13:
			case 258:
			{
				if (!checkNullObject(1))
				{
					if (f >= 7 && f <= 12)
					{
						fraImgEff.drawFrame((f - 7) / 2, objFireMain.x, objFireMain.y, Dir, 33, g);
					}
					if (f >= 9 && f <= 11)
					{
						fraImgSubEff.drawFrame((f - 9) % fraImgSubEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy + 5, Dir, 33, g);
					}
					if (f >= 18 && f <= 20)
					{
						fraImgSubEff.drawFrame((2 - (f - 18)) % fraImgSubEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy + 5, Dir, 33, g);
					}
				}
				for (int num100 = 0; num100 < VecEff.size(); num100++)
				{
					Point_Focus point_Focus34 = (Point_Focus)VecEff.elementAt(num100);
					if (typeEffect == 13)
					{
						fraImgSub2Eff.drawFrame(0, point_Focus34.x, point_Focus34.y, Dir, 3, g);
					}
					else if (fraImgSub2Eff.getImageFrame() != null)
					{
						g.drawRegion(fraImgSub2Eff.getImageFrame(), 0, 0, fraImgSub2Eff.frameWidth, 62, 0, point_Focus34.x, point_Focus34.y, 33);
					}
				}
				for (int num101 = 0; num101 < VecSubEff.size(); num101++)
				{
					Point point51 = (Point)VecSubEff.elementAt(num101);
					fraImgSub3Eff.drawFrame(point51.f % fraImgSub3Eff.nFrame, point51.x, point51.y, 0, 3, g);
				}
				break;
			}
			case 18:
			{
				for (int num89 = 0; num89 < VecSubEff.size(); num89++)
				{
					Point point46 = (Point)VecSubEff.elementAt(num89);
					fraImgSubEff.drawFrame(point46.f / 2 % fraImgSubEff.nFrame, point46.x, point46.y, Dir, 3, g);
				}
				for (int num90 = 0; num90 < VecEff.size(); num90++)
				{
					Point_Focus point_Focus32 = (Point_Focus)VecEff.elementAt(num90);
					fraImgEff.drawFrame(0, point_Focus32.x, point_Focus32.y, frame, 3, g);
				}
				break;
			}
			case 30:
				if (f < 3)
				{
					fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, x, y, Dir, 3, g);
				}
				else
				{
					fraImgSubEff.drawFrame(0, x1000, y1000, Dir, 3, g);
				}
				break;
			case 73:
			case 74:
				if (f < 2)
				{
					fraImgEff.drawFrame(0, x, y, Dir, 3, g);
				}
				break;
			case 10025:
				fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, x, y, Dir, 3, g);
				break;
			case 10001:
				paintPan_1(g);
				break;
			case 10002:
				if (f < 6)
				{
					fraImgEff.drawFrame(f % fraImgEff.nFrame, x, y - objFireMain.dy, Dir, 33, g);
				}
				if (f >= 13 && f <= 18)
				{
					fraImgSubEff.drawFrame(f % fraImgSubEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy, Dir, 33, g);
				}
				break;
			case 10003:
				paintGalio_1(g);
				break;
			case 10004:
			{
				if (f < 4)
				{
					fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x, y, Dir, 3, g);
				}
				for (int num79 = 0; num79 < VecEff.size(); num79++)
				{
					Point point42 = (Point)VecEff.elementAt(num79);
					fraImgEff.drawFrame(point42.f / 2 % fraImgEff.nFrame, point42.x, point42.y, Dir, 33, g);
				}
				break;
			}
			case 10006:
			case 10011:
			{
				for (int num77 = 0; num77 < vecPos.size(); num77++)
				{
					Point_Focus point_Focus26 = (Point_Focus)vecPos.elementAt(num77);
					fraImgSubEff.drawFrame(point_Focus26.frame + point_Focus26.f / 3 % 2, point_Focus26.x, point_Focus26.y, 0, mGraphics.BOTTOM | mGraphics.RIGHT, g);
					fraImgSubEff.drawFrame(point_Focus26.frame + point_Focus26.f / 3 % 2, point_Focus26.x, point_Focus26.y, 2, mGraphics.BOTTOM | mGraphics.LEFT, g);
					fraImgSubEff.drawFrame(point_Focus26.frame + point_Focus26.f / 3 % 2, point_Focus26.x, point_Focus26.y, 1, mGraphics.TOP | mGraphics.RIGHT, g);
					fraImgSubEff.drawFrame(point_Focus26.frame + point_Focus26.f / 3 % 2, point_Focus26.x, point_Focus26.y, 3, 0, g);
					g.setColor(0);
					g.fillRect(point_Focus26.x - 1, point_Focus26.y - 1, 3, 3);
				}
				break;
			}
			case 10007:
			{
				for (int num74 = 0; num74 < vecPos.size(); num74++)
				{
					Point_Focus point_Focus24 = (Point_Focus)vecPos.elementAt(num74);
					fraImgEff.drawFrame(point_Focus24.f / 2 % fraImgEff.nFrame, point_Focus24.x, point_Focus24.y, Dir, 33, g);
					fraImgSubEff.drawFrame(point_Focus24.f / 2 % fraImgSubEff.nFrame, point_Focus24.x, point_Focus24.y, Dir, 3, g);
				}
				break;
			}
			case 10008:
			{
				for (int num71 = 0; num71 < VecSubEff.size(); num71++)
				{
					Point point40 = (Point)VecSubEff.elementAt(num71);
					fraImgEff.drawFrame(point40.frame, point40.x, point40.y, Dir, 3, g);
				}
				break;
			}
			case 10012:
			{
				if (f <= fRemove)
				{
					fraImgEff.drawFrame(1, x1000 / 1000, y1000, Dir, 3, g);
				}
				for (int num69 = 0; num69 < VecEff.size(); num69++)
				{
					Point point38 = (Point)VecEff.elementAt(num69);
					point38.fraImgEff.drawFrame(point38.f / 2, point38.x, point38.y, Dir, 3, g);
				}
				break;
			}
			case 10010:
			case 10013:
				fraImgEff.drawFrame(GameCanvas.gameTick / numNextFrame % fraImgEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, Dir, 3, g);
				break;
			case 10015:
			{
				for (int num66 = 0; num66 < VecEff.size(); num66++)
				{
					Point point36 = (Point)VecEff.elementAt(num66);
					fraImgEff.drawFrame(point36.frame, objFireMain.x, objFireMain.y + point36.y, Dir, 33, g);
				}
				break;
			}
			case 10017:
				fraImgEff.drawFrame(GameCanvas.gameTick / numNextFrame % fraImgEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.hOne / 4, Dir, 3, g);
				break;
			case 10020:
			case 10021:
			case 10022:
			case 10026:
				fraImgEff.drawFrame(GameCanvas.gameTick / numNextFrame % fraImgEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, Dir, 3, g);
				break;
			case 10024:
			{
				fraImgEff.drawFrame(GameCanvas.gameTick / numNextFrame % fraImgEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, Dir, 3, g);
				for (int num64 = 0; num64 < vecPos.size(); num64++)
				{
					Point_Focus point_Focus21 = (Point_Focus)vecPos.elementAt(num64);
					fraImgSubEff.drawFrame(point_Focus21.frame + point_Focus21.f / 3 % 2, point_Focus21.x, point_Focus21.y, 0, mGraphics.BOTTOM | mGraphics.RIGHT, g);
					fraImgSubEff.drawFrame(point_Focus21.frame + point_Focus21.f / 3 % 2, point_Focus21.x, point_Focus21.y, 2, mGraphics.BOTTOM | mGraphics.LEFT, g);
					fraImgSubEff.drawFrame(point_Focus21.frame + point_Focus21.f / 3 % 2, point_Focus21.x, point_Focus21.y, 1, mGraphics.TOP | mGraphics.RIGHT, g);
					fraImgSubEff.drawFrame(point_Focus21.frame + point_Focus21.f / 3 % 2, point_Focus21.x, point_Focus21.y, 3, 0, g);
				}
				break;
			}
			case 10018:
			{
				for (int num61 = 0; num61 < VecEff.size(); num61++)
				{
					Point point33 = (Point)VecEff.elementAt(num61);
					fraImgEff.drawFrame(point33.frame, point33.x, point33.y, Dir, 33, g);
				}
				break;
			}
			case 165:
			case 166:
			{
				int num60 = f / 2 % 6;
				if (num60 < 2)
				{
					fraImgEff.drawFrame(num60, x, y, Dir, 33, g);
				}
				break;
			}
			case 20:
				if (f >= 17 && f <= 24)
				{
					fraImgEff.drawFrame((f - 17) / 2, x, y, Dir, 33, g);
				}
				break;
			case 23:
				if (f < fPlayFrameSuper)
				{
					fraImgEff.drawFrame(3, x, y, 0, 3, g);
				}
				break;
			case 25:
			case 235:
				paintCrocodile1(g);
				break;
			case 26:
			case 236:
				paintCrocodile2(g);
				break;
			case 27:
				if (f % 4 < 2)
				{
					paint_Bullet(g, fraImgEff, frame, x, y, isMore: false, 0);
				}
				else
				{
					paint_Bullet(g, fraImgSubEff, frame, x, y, isMore: false, 0);
				}
				break;
			case 28:
				fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, x, y, 0, 3, g);
				break;
			case 32:
			{
				for (int num48 = 0; num48 < VecEff.size(); num48++)
				{
					Point point27 = (Point)VecEff.elementAt(num48);
					if (point27.frame == 0)
					{
						fraImgEff.drawFrame(0, point27.x, point27.y, Dir, 33, g);
					}
					else
					{
						fraImgSubEff.drawFrame(point27.f / 2 % fraImgSubEff.nFrame, point27.x, point27.y, Dir, 33, g);
					}
				}
				break;
			}
			case 39:
			{
				for (int num47 = 0; num47 < VecEff.size(); num47++)
				{
					Point_Focus point_Focus18 = (Point_Focus)VecEff.elementAt(num47);
					fraImgEff.drawFrame(2, point_Focus18.x, point_Focus18.y, Dir, 33, g);
				}
				break;
			}
			case 40:
				if (Dir == 0)
				{
					g.setColor(15956504);
					g.fillRect(x, y - 3, x1000 - x, 6);
					g.setColor(15985419);
					g.fillRect(x, y - 2, x1000 - x, 4);
					g.setColor(16777215);
					g.fillRect(x, y - 1, x1000 - x, 2);
				}
				else
				{
					g.setColor(15956504);
					g.fillRect(x1000, y - 3, x, 6);
					g.setColor(15985419);
					g.fillRect(x1000, y - 2, x, 4);
					g.setColor(16777215);
					g.fillRect(x1000, y - 1, x, 2);
				}
				break;
			case 45:
			{
				for (int num43 = 0; num43 < VecEff.size(); num43++)
				{
					Point_Focus point_Focus16 = (Point_Focus)VecEff.elementAt(num43);
					fraImgEff.drawFrame(1, point_Focus16.x, point_Focus16.y, point_Focus16.dis, 3, g);
				}
				break;
			}
			case 54:
			{
				for (int num41 = 0; num41 < VecEff.size(); num41++)
				{
					Point_Focus point_Focus15 = (Point_Focus)VecEff.elementAt(num41);
					if (point_Focus15.frame == 0)
					{
						fraImgEff.drawFrame(0, point_Focus15.x, point_Focus15.y, point_Focus15.dis, 3, g);
					}
					else
					{
						fraImgSub2Eff.drawFrame(point_Focus15.f / 2 % 3, point_Focus15.x, point_Focus15.y, point_Focus15.dis, 3, g);
					}
				}
				for (int num42 = 0; num42 < VecSubEff.size(); num42++)
				{
					Point point24 = (Point)VecSubEff.elementAt(num42);
					if (point24.obj != null && !point24.obj.returnAction())
					{
						if (point24.frame == 0)
						{
							fraImgEff.drawFrame(2, point24.obj.x, point24.obj.y - point24.obj.hOne / 2, point24.dis, 3, g);
						}
						else if (point24.frame == 1)
						{
							fraImgSubEff.drawFrame(point24.f / 2 % fraImgSubEff.nFrame, point24.obj.x, point24.obj.y - point24.obj.hOne / 2 + 5, point24.dis, 33, g);
						}
					}
				}
				break;
			}
			case 59:
				if (!checkNullObject(2))
				{
					if (f < 6)
					{
						fraImgEff.drawFrame(f / 2, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, 0, 3, g);
					}
					else if (f % 4 < 2)
					{
						fraImgEff.drawFrame(3, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, 0, 3, g);
					}
				}
				break;
			case 60:
				if (!checkNullObject(2))
				{
					if (f < 9)
					{
						fraImgEff.drawFrame(4 + f / 3, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, 0, 3, g);
					}
					else if (f % 4 < 3)
					{
						fraImgEff.drawFrame(7, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, 0, 3, g);
					}
				}
				break;
			case 62:
				fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, toX, toY, 0, 33, g);
				break;
			case 167:
			{
				for (int num9 = 0; num9 < VecSubEff.size(); num9++)
				{
					Point point8 = (Point)VecSubEff.elementAt(num9);
					fraImgSub2Eff.drawFrame(point8.frame, point8.x, point8.y, 0, 33, g);
				}
				for (int num10 = 0; num10 < VecEff.size(); num10++)
				{
					Point point9 = (Point)VecEff.elementAt(num10);
					fraImgSub3Eff.drawFrameNew(point9.f / 3, point9.x, point9.y, 0, 33, g);
				}
				if (f >= 4 && f <= 5)
				{
					g.drawRegion(fraImgSub4Eff.getImageFrame(), 0, 0, fraImgSub4Eff.frameWidth, fraImgSub4Eff.frameHeight / (f - 3), 0, x1000, y1000, 33);
				}
				if (f > 4)
				{
					if (f > fRemove - 4)
					{
						fraImgSub2Eff.drawFrame(fRemove - f, x1000, y1000, 0, 33, g);
					}
					else
					{
						fraImgSub2Eff.drawFrame(3, x1000, y1000, 0, 33, g);
					}
				}
				if (f < 8)
				{
					fraImgSubEff.drawFrame(f / 2, x1000, y1000, 0, 33, g);
				}
				break;
			}
			case -1:
			{
				for (int num16 = 0; num16 < VecEff.size(); num16++)
				{
					Point point11 = (Point)VecEff.elementAt(num16);
					fraImgSubEff.drawFrame(point11.f / 2 % fraImgSubEff.nFrame, point11.x, point11.y, 0, 3, g);
				}
				break;
			}
			case 10028:
				if (f > 16)
				{
					int num3 = -4;
					int num4 = GameCanvas.gameTick % 2 * 2;
					if (y1000 == 0)
					{
						fraImgSub3Eff.drawFrameNew(GameCanvas.gameTick / 3 % fraImgSub3Eff.nFrame, x, y + 7, 0, 3, g);
					}
					g.setColor(1513500);
					g.fillRect(x - 20 - num3, y - 350 - y1000, 40 + num3 * 2, 360);
					g.setColor(1908256);
					g.fillRect(x - 18 - num3, y - 350 - y1000, 36 + num3 * 2, 360);
					g.setColor(7040880);
					g.fillRect(x - 16 - num3, y - 350 - y1000, 32 + num3 * 2, 360);
					g.setColor(2500651);
					g.fillRect(x - 14 - num3 + num4, y - 350 - y1000, 28 + num3 * 2 - num4 * 2, 360);
					g.setColor(9540757);
					g.fillRect(x - 12 - num3 + num4, y - 350 - y1000, 24 + num3 * 2 - num4 * 2, 360);
					g.setColor(16514814);
					g.fillRect(x - 10 - num3 + num4, y - 350 - y1000, 20 + num3 * 2 - num4 * 2, 360);
				}
				break;
			case 10030: // EFF_TRAI_AC_QUY_HO_DEN_VU_TRU_3 - không paint trực tiếp (J2ME: case 10030 return;)
				break;
			case 169:
			case 237:
			{
				if (f <= 20 && !checkNullObject(1))
				{
					fraImgSub4Eff.drawFrame(CRes.random(fraImgSub4Eff.nFrame), objFireMain.x, objFireMain.y - objFireMain.hOne / 2 + 3, 0, 3, g);
				}
				for (int num141 = 0; num141 < VecSubEff.size(); num141++)
				{
					if (num141 % 2 == 1)
					{
						Point point64 = (Point)VecSubEff.elementAt(num141);
						fraImgSubEff.drawFrameNew(point64.frame * fraImgSubEff.maxNumFrame + GameCanvas.gameTick / 2 % fraImgSubEff.maxNumFrame, point64.x / 10, point64.y / 10, 0, 3, g);
						for (int num142 = 0; num142 < 4; num142++)
						{
							fraImgEff.drawFrameNew(point64.frame * fraImgEff.maxNumFrame, point64.x / 10, point64.y / 10 - num142 * 73, (CRes.random(2) != 0) ? 2 : 0, 33, g);
						}
					}
				}
				if (f > 16)
				{
					int num143 = -4;
					int num144 = GameCanvas.gameTick % 2 * 2;
					if (y1000 == 0)
					{
						fraImgSub3Eff.drawFrameNew(GameCanvas.gameTick / 3 % fraImgSub3Eff.nFrame, x, y + 7, 0, 3, g);
					}
					g.setColor(407521);
					g.fillRect(x - 20 - num143, y - 350 - y1000, 40 + num143 * 2, 360);
					g.setColor(31983);
					g.fillRect(x - 18 - num143, y - 350 - y1000, 36 + num143 * 2, 360);
					g.setColor(11661052);
					g.fillRect(x - 16 - num143, y - 350 - y1000, 32 + num143 * 2, 360);
					g.setColor(31983);
					g.fillRect(x - 14 - num143 + num144, y - 350 - y1000, 28 + num143 * 2 - num144 * 2, 360);
					g.setColor(11661052);
					g.fillRect(x - 12 - num143 + num144, y - 350 - y1000, 24 + num143 * 2 - num144 * 2, 360);
					g.setColor(16514814);
					g.fillRect(x - 10 - num143 + num144, y - 350 - y1000, 20 + num143 * 2 - num144 * 2, 360);
				}
				for (int num145 = 0; num145 < VecEff.size(); num145++)
				{
					Point point65 = (Point)VecEff.elementAt(num145);
					fraImgSub2Eff.drawFrame(point65.f / 2, point65.x, point65.y, 0, 3, g);
				}
				for (int num146 = 0; num146 < VecSubEff.size(); num146++)
				{
					if (num146 % 2 == 0)
					{
						Point point66 = (Point)VecSubEff.elementAt(num146);
						fraImgSubEff.drawFrameNew(point66.frame * fraImgSubEff.maxNumFrame + GameCanvas.gameTick / 2 % fraImgSubEff.maxNumFrame, point66.x / 10, point66.y / 10, 0, 3, g);
						for (int num147 = 0; num147 < 4; num147++)
						{
							fraImgEff.drawFrameNew(point66.frame * fraImgEff.maxNumFrame, point66.x / 10, point66.y / 10 - num147 * 73, (CRes.random(2) != 0) ? 2 : 0, 33, g);
						}
					}
				}
				break;
			}
			case 170:
			case 238:
			{
				if (f <= 20 && !checkNullObject(1))
				{
					fraImgSub4Eff.drawFrame(CRes.random(fraImgSub4Eff.nFrame), objFireMain.x, objFireMain.y - objFireMain.hOne / 2 + 3, 0, 3, g);
				}
				for (int num132 = 0; num132 < VecEff.size(); num132++)
				{
					Point_Focus point_Focus47 = (Point_Focus)VecEff.elementAt(num132);
					int num133 = 0;
					if (Dir == 2)
					{
						num133 = 2;
					}
					if (point_Focus47.f >= point_Focus47.fRe)
					{
						if (fraImgEff.getImageFrame() != null && point_Focus47.f % 5 != 2 && point_Focus47.f < point_Focus47.fRe + 8)
						{
							g.drawRegion(fraImgEff.getImageFrame(), point_Focus47.typeSpec * fraImgEff.frameWidth, point_Focus47.frame * fraImgEff.frameHeight, fraImgEff.frameWidth, point_Focus47.maxdis, num133, point_Focus47.x, point_Focus47.y, 33);
						}
					}
					else if (point_Focus47.f % 5 != 2)
					{
						fraImgEff.drawFrameNew(point_Focus47.typeSpec * fraImgEff.maxNumFrame + point_Focus47.frame, point_Focus47.x, point_Focus47.y, num133, 3, g);
					}
				}
				for (int num134 = 0; num134 < VecSubEff.size(); num134++)
				{
					Point point62 = (Point)VecSubEff.elementAt(num134);
					fraImgSub3Eff.drawFrameNew(point62.frame * fraImgSub3Eff.maxNumFrame + GameCanvas.gameTick / 2 % fraImgSub3Eff.maxNumFrame, point62.x / 10, point62.y / 10, 0, 3, g);
					for (int num135 = 0; num135 < 4; num135++)
					{
						fraImgSub2Eff.drawFrameNew(point62.frame * fraImgSub2Eff.maxNumFrame, point62.x / 10, point62.y / 10 - num135 * 73, (CRes.random(2) != 0) ? 2 : 0, 33, g);
					}
				}
				break;
			}
			case 171:
			case 239:
			{
				if (f < 20 && !checkNullObject(1) && (f <= 8 || f >= 13))
				{
					fraImgSub3Eff.drawFrame(f / 2 % fraImgSub3Eff.nFrame, objFireMain.x, objFireMain.y + objFireMain.dy, Dir, 33, g);
				}
				for (int num120 = VecEff.size() - 1; num120 >= 0; num120--)
				{
					Point point57 = (Point)VecEff.elementAt(num120);
					if (point57.frame == 0 && point57.fSmall >= 2)
					{
						fraImgEff.drawFrame(point57.f / 2 % fraImgEff.nFrame, point57.x / 1000, point57.y / 1000, 0, 33, g);
					}
					else if (point57.frame == 1 && point57.fSmall == 3)
					{
						fraImgSubEff.drawFrame(point57.f / 2 % fraImgSubEff.nFrame, point57.x / 1000, point57.y / 1000, 0, 33, g);
					}
				}
				if (f > 6 && f < fRemove)
				{
					int num121 = -4;
					int num122 = GameCanvas.gameTick % 2 * 2;
					fraImgSub2Eff.drawFrame(GameCanvas.gameTick / 3 % fraImgSub2Eff.nFrame, x, y - 3, 0, 3, g);
					g.setColor(16722432);
					g.fillRect(x - 20 - num121, y - y1000, 40 + num121 * 2, y1000);
					g.setColor(16745472);
					g.fillRect(x - 18 - num121, y - y1000, 36 + num121 * 2, y1000);
					g.setColor(16765184);
					g.fillRect(x - 16 - num121, y - y1000, 32 + num121 * 2, y1000);
					g.setColor(16745472);
					g.fillRect(x - 14 - num121 + num122, y - y1000, 28 + num121 * 2 - num122 * 2, y1000);
					g.setColor(16765184);
					g.fillRect(x - 12 - num121 + num122, y - y1000, 24 + num121 * 2 - num122 * 2, y1000);
					g.setColor(16777085);
					g.fillRect(x - 10 - num121 + num122, y - y1000, 20 + num121 * 2 - num122 * 2, y1000);
				}
				for (int num123 = VecEff.size() - 1; num123 >= 0; num123--)
				{
					Point point58 = (Point)VecEff.elementAt(num123);
					if (point58.frame == 0 && point58.fSmall < 2)
					{
						fraImgEff.drawFrame(point58.f / 2 % fraImgEff.nFrame, point58.x / 1000, point58.y / 1000, 0, 33, g);
					}
					else if (point58.frame == 1 && point58.fSmall != 3)
					{
						fraImgSubEff.drawFrame(point58.f / 2 % fraImgSubEff.nFrame, point58.x / 1000, point58.y / 1000, 0, 33, g);
					}
				}
				break;
			}
			case 172:
			case 240:
				if (f < 20 && !checkNullObject(1) && (f <= 8 || f >= 13))
				{
					fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, objFireMain.x, objFireMain.y + objFireMain.dy, Dir, 33, g);
				}
				break;
			case 168:
				if (f < 12)
				{
					int num111 = Mr1[f / 2][1];
					int num112 = Mr1[f / 2][2];
					int trans10 = 0;
					if (!checkNullObject(1) && objFireMain.Dir == 2)
					{
						trans10 = 2;
						num111 = -Mr1[f / 2][1];
					}
					fraImgEff.drawFrameNew(f / 2 % fraImgEff.nFrame, x + num111, y + num112, trans10, 3, g);
				}
				break;
			case 173:
				paintEff_Mr1_2(g);
				break;
			case 174:
				if (f >= 4 && f <= fRemove && !checkNullObject(1))
				{
					int idx3 = 1;
					if (f < 6)
					{
						idx3 = 0;
					}
					fraImgEff.drawFrame(idx3, objFireMain.x + am_duong * 36, objFireMain.y - 25, objFireMain.type_left_right, 3, g);
				}
				break;
			case 175:
				paintEff_Df_2(g);
				break;
			case 178:
				paintEff_Mr0_1(g);
				break;
			case 179:
			case 241:
			{
				if ((f >= 1 && f <= 2) || (f >= 24 && f <= 25))
				{
					fraImgEff.drawFrame(0, x + am_duong * 5, y, Dir, 3, g);
				}
				for (int num97 = 0; num97 < VecSubEff.size(); num97++)
				{
					Point point48 = (Point)VecSubEff.elementAt(num97);
					if (point48.frame == 0)
					{
						if (frame == 1)
						{
							fraImgSub4Eff.drawFrameNew(point48.f / 2 % fraImgSub4Eff.nFrame, point48.x, point48.y, point48.dis, 3, g);
						}
						else
						{
							fraImgSubEff.drawFrameNew((point48.f + point48.frame) % fraImgSubEff.nFrame, point48.x, point48.y, point48.dis, 3, g);
						}
					}
				}
				for (int num98 = 0; num98 < VecEff.size(); num98++)
				{
					Point point49 = (Point)VecEff.elementAt(num98);
					if (frame == 1)
					{
						fraImgSubEff.drawFrame(point49.f % fraImgSubEff.nFrame, point49.x, point49.y, point49.dis, 3, g);
						fraImgSub3Eff.drawFrame(0, point49.x, point49.y + 60, point49.dis, 3, g);
						if (point49.f % 2 == 0)
						{
							fraImgSub2Eff.drawFrameNew(step * fraImgSub2Eff.maxNumFrame + point49.f / 3 % fraImgSub2Eff.maxNumFrame, point49.x + am_duong * 10, point49.y + 5, point49.dis, 3, g);
						}
					}
					else
					{
						fraImgSubEff.drawFrameNew(point49.f % fraImgSubEff.nFrame, point49.x, point49.y, point49.dis, 3, g);
					}
				}
				for (int num99 = 0; num99 < VecSubEff.size(); num99++)
				{
					Point point50 = (Point)VecSubEff.elementAt(num99);
					if (point50.frame == 1 && frame == 1)
					{
						fraImgSub4Eff.drawFrameNew(point50.f / 2 % fraImgSub4Eff.nFrame, point50.x, point50.y, point50.dis, 3, g);
					}
				}
				break;
			}
			case 195:
			{
				for (int num91 = 0; num91 < VecEff.size(); num91++)
				{
					Point point47 = (Point)VecEff.elementAt(num91);
					for (int num92 = 0; num92 < 4; num92++)
					{
						fraImgEff.drawFrame(0, point47.x / 10, point47.y / 10 - 73 - num92 * 73, CRes.random(2) * 2, 0, g);
					}
					fraImgSubEff.drawFrame(point47.f / 2 % fraImgSubEff.nFrame, point47.x / 10 + 15, point47.y / 10 + 4, CRes.random(2) * 2, 33, g);
				}
				break;
			}
			case 196:
			{
				for (int num86 = 0; num86 < VecEff.size(); num86++)
				{
					Point point43 = (Point)VecEff.elementAt(num86);
					fraImgEff.drawFrame(point43.f % fraImgEff.nFrame, point43.x, point43.y, Dir, 3, g);
				}
				for (int num87 = 0; num87 < VecSubEff.size(); num87++)
				{
					Point point44 = (Point)VecSubEff.elementAt(num87);
					if (point44.frame == 0)
					{
						fraImgSub2Eff.drawFrame(point44.f % fraImgSub2Eff.nFrame, point44.x, point44.y, Dir, 3, g);
						continue;
					}
					fraImgSubEff.drawFrame(point44.f / 2 % fraImgSubEff.nFrame, point44.x - 50, point44.y - 50, 0, 0, g);
					fraImgSubEff.drawFrame(point44.f / 2 % fraImgSubEff.nFrame, point44.x, point44.y - 50, 2, 0, g);
				}
				break;
			}
			case 197:
			{
				int num83 = 30 + f / 2 * 15;
				int num84 = 0;
				if (num83 > 76)
				{
					num83 = 76;
				}
				if (Dir == 0)
				{
					num84 = num83;
				}
				if (fraImgEff.getImageFrame() != null)
				{
					g.drawRegion(fraImgEff.getImageFrame(), 0, 0, num83, 27, Dir, x - num84, y - 13, 0);
				}
				break;
			}
			case 198:
			{
				if (f < 8)
				{
					fraImgEff.drawFrame(f / 4, x, y, Dir, 3, g);
				}
				for (int num81 = 0; num81 < VecEff.size(); num81++)
				{
					Point_Focus point_Focus29 = (Point_Focus)VecEff.elementAt(num81);
					fraImgEff.drawFrame(point_Focus29.frame, point_Focus29.x, point_Focus29.y, 0, 3, g);
				}
				break;
			}
			case 199:
				if (!checkNullObject(1) && objFireMain.isTanHinh)
				{
					fraImgEff.drawFrame(0, x, y, Dir, 33, g);
				}
				break;
			case 200:
			{
				if (f < 20)
				{
					fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x + 4 * am_duong, y - f % 4 / 2 * 3 + 2, Dir, 3, g);
				}
				for (int num80 = 0; num80 < VecEff.size(); num80++)
				{
					Point_Focus point_Focus28 = (Point_Focus)VecEff.elementAt(num80);
					fraImgEff.drawFrame((point_Focus28.frame + point_Focus28.f) % fraImgEff.nFrame, point_Focus28.x, point_Focus28.y, Dir, 3, g);
				}
				break;
			}
			case 202:
			{
				for (int num73 = 0; num73 < VecEff.size(); num73++)
				{
					Point_Focus point_Focus23 = (Point_Focus)VecEff.elementAt(num73);
					fraImgEff.drawFrame(point_Focus23.f / 2 % 2, point_Focus23.x, point_Focus23.y, 0, 3, g);
				}
				if (!checkNullObject(1))
				{
					if (f < 10)
					{
						fraImgEff.drawFrame(f / 2, x, objFireMain.y - objFireMain.hOne - 15, 0, 3, g);
					}
					else if (f < 12)
					{
						fraImgEff.drawFrame(2, x + am_duong * 20, objFireMain.y - objFireMain.hOne / 2 - 20, 0, 3, g);
					}
				}
				break;
			}
			case 203:
				if (!checkNullObject(1) && objFireMain.isTanHinh)
				{
					fraImgEff.drawFrame(0, x, y, Dir, 33, g);
				}
				break;
			case 204:
			{
				for (int num70 = 0; num70 < VecEff.size(); num70++)
				{
					Point point39 = (Point)VecEff.elementAt(num70);
					fraImgSubEff.drawFrame(point39.f % fraImgSubEff.nFrame, point39.x, point39.y, point39.dis, 3, g);
					fraImgSub2Eff.drawFrame(0, point39.x, point39.y + 60, point39.dis, 3, g);
				}
				break;
			}
			case 205:
			{
				if (f > 10 && f <= fRemove)
				{
					fraImgEff.drawFrame(0, x + x1000 / 1000, y + y1000 / 1000, 0, 3, g);
				}
				for (int num68 = 0; num68 < VecEff.size(); num68++)
				{
					Point point37 = (Point)VecEff.elementAt(num68);
					fraImgEff.drawFrame(1 + point37.f / 3, point37.x, point37.y, 0, 3, g);
				}
				break;
			}
			case 207:
			{
				if (f < fPlayFrameSuper)
				{
					fraImgEff.drawFrame(3, x, y, 0, 3, g);
				}
				for (int num65 = 0; num65 < VecEff.size(); num65++)
				{
					Point point35 = (Point)VecEff.elementAt(num65);
					fraImgSubEff.drawFrame(point35.f / 2 % fraImgSubEff.nFrame, point35.x, point35.y, 0, 3, g);
				}
				break;
			}
			case 206:
			{
				if (f < fPlayFrameSuper)
				{
					paint_Bullet(g, fraImgEff, frame, x, y, isMore: false, 0);
				}
				for (int num63 = 0; num63 < VecEff.size(); num63++)
				{
					Point point34 = (Point)VecEff.elementAt(num63);
					fraImgSubEff.drawFrame(point34.f / 2 % fraImgSubEff.nFrame, point34.x, point34.y, 0, 3, g);
				}
				break;
			}
			case 208:
				paintEffTru(g);
				break;
			case 250:
				paintEffTru2(g);
				break;
			case 209:
			case 242:
				if (typeEffect == 242 && objFireMain != null && f < 11)
				{
					objFireMain.paintBody(g, objFireMain.x + am_duong * 120, objFireMain.y, objFireMain.frame, (objFireMain.type_left_right == 0) ? 2 : 0, isEye: true);
				}
				if (frame == 1)
				{
					for (int num51 = 0; num51 < VecEff.size(); num51++)
					{
						Point point30 = (Point)VecEff.elementAt(num51);
						if (fraImgSubEff != null && fraImgSubEff.imgFrame != null)
						{
							fraImgSubEff.drawFrameNew(CRes.random(fraImgSubEff.maxNumFrame), point30.x, point30.y, 0, 3, g);
						}
						fraImgEff.drawFrameNew(6 + point30.frame, point30.x, point30.y, 0, 3, g);
					}
				}
				else
				{
					for (int num52 = 0; num52 < VecEff.size(); num52++)
					{
						Point point31 = (Point)VecEff.elementAt(num52);
						fraImgEff.drawFrameNew(6 + point31.frame, point31.x, point31.y, 0, 3, g);
					}
				}
				if (f < fRemove)
				{
					fraImgEff.drawFrameNew(6 + mframe[f], x, y, Dir, 3, g);
				}
				break;
			case 210:
			case 243:
				paint_Dong_Dat_1(g);
				break;
			case 211:
			case 244:
				paint_Dong_Dat_2(g);
				break;
			case 212:
			case 271:
			case 274:
			case 275:
			{
				for (int num50 = 0; num50 < VecSubEff.size(); num50++)
				{
					Point point29 = (Point)VecSubEff.elementAt(num50);
					fraImgEff.drawFrame(point29.f % fraImgEff.nFrame, point29.x, point29.y, 0, 3, g);
				}
				break;
			}
			case 471:
			{
				for (int num49 = 0; num49 < VecSubEff.size(); num49++)
				{
					Point point28 = (Point)VecSubEff.elementAt(num49);
					fraImgEff.drawFrame(point28.f % fraImgEff.nFrame, point28.x, point28.y, 0, 3, g);
				}
				break;
			}
			case 219:
			case 292:
				if (f == 4)
				{
					fraImgSubEff.drawFrame(0, x, y, Dir, 3, g);
				}
				if (f == 24)
				{
					fraImgSubEff.drawFrame(0, x1000, y1000, Dir, 33, g);
				}
				if (mframe[f] >= 0)
				{
					fraImgEff.drawFrame(mframe[f], x, y + 5, Dir, 33, g);
				}
				break;
			case 492:
				if (f == 4)
				{
					fraImgSubEff.drawFrame(0, x, y, Dir, 3, g);
				}
				if (f == 24)
				{
					fraImgSubEff.drawFrame(0, x1000, y1000, Dir, 33, g);
				}
				if (mframe[f] >= 0)
				{
					fraImgEff.drawFrame(mframe[f], x, y + 5, Dir, 33, g);
				}
				break;
			case 291:
				if (f == 4)
				{
					fraImgSubEff.drawFrame(0, x1000, y1000, Dir, 33, g);
				}
				if (mframe[f] >= 0)
				{
					fraImgEff.drawFrame(mframe[f], objBeFireMain.x - am_duong * 30, objBeFireMain.y + 5, Dir, 33, g);
				}
				break;
			case 491:
				if (f == 4)
				{
					fraImgSubEff.drawFrame(0, x1000, y1000, Dir, 33, g);
				}
				if (f > 4 && f < fRemove)
				{
					fraImgSub2Eff.drawFrame((f - 5) / 2 % fraImgSub2Eff.nFrame, objBeFireMain.x - am_duong * 30, objBeFireMain.y, Dir, 33, g);
				}
				if (mframe[f] >= 0)
				{
					fraImgEff.drawFrame(mframe[f], objBeFireMain.x - am_duong * 30, objBeFireMain.y + 5, Dir, 33, g);
				}
				break;
			case 246:
			case 253:
			{
				if (f >= 10 && f <= fRemove - 4 && f % 3 != 2)
				{
					fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x, y + 3, Dir, 3, g);
				}
				for (int num31 = 0; num31 < VecEff.size(); num31++)
				{
					Point point17 = (Point)VecEff.elementAt(num31);
					if (point17.frame == 0)
					{
						fraImgEff.drawFrame(0, point17.x, point17.y, 0, 3, g);
					}
					else if (point17.frame == 1)
					{
						if (fraImgEff.getImageFrame() != null)
						{
							g.drawRegion(fraImgEff.getImageFrame(), 0, 0, fraImgEff.frameWidth, fraImgEff.frameHeight - point17.dis, 0, point17.x, point17.y, 33);
						}
					}
					else if ((point17.frame == 2 || point17.frame == 3) && fraImgEff.getImageFrame() != null)
					{
						g.drawRegion(fraImgEff.getImageFrame(), 0, (point17.frame - 1) * fraImgEff.frameHeight, fraImgEff.frameWidth, fraImgEff.frameHeight - point17.dis, 0, point17.x, point17.y, 33);
					}
				}
				break;
			}
			case 247:
			case 254:
			{
				if (f >= 5 && f <= 7)
				{
					if (fraImgEff.getImageFrame() != null)
					{
						g.drawRegion(fraImgEff.getImageFrame(), 0, 0, fraImgEff.frameWidth / 4 * (f - 4), fraImgEff.frameHeight, Dir, x, y, 3);
					}
					if (!checkNullObject(1))
					{
						fraImgSub2Eff.drawFrame(0, x - am_duong * 10, objFireMain.y - objFireMain.hOne + 10, Dir, 3, g);
					}
				}
				if (f == 7 || f == 8 || f == 14 || f == 15)
				{
					fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, x - am_duong * 14, y, Dir, 3, g);
					if (!checkNullObject(1))
					{
						fraImgSub2Eff.drawFrame(0, x - am_duong * 10, objFireMain.y - objFireMain.hOne + 10, Dir, 3, g);
					}
				}
				for (int num7 = 0; num7 < VecEff.size(); num7++)
				{
					Point_Focus point_Focus5 = (Point_Focus)VecEff.elementAt(num7);
					if (typeEffect == 254 && CRes.random(2) == 0)
					{
						fraImgSub3Eff.drawFrame(CRes.random(5), point_Focus5.x / 10 + CRes.random_Am_0(5) + am_duong * 5, point_Focus5.y / 10 - 8, point_Focus5.Dir, 3, g);
					}
					fraImgEff.drawFrame(point_Focus5.f / 2 % fraImgEff.nFrame, point_Focus5.x / 10, point_Focus5.y / 10, point_Focus5.Dir, 3, g);
					if (typeEffect == 254 && CRes.random(2) == 0)
					{
						fraImgSub3Eff.drawFrame(CRes.random(5), point_Focus5.x / 10 + CRes.random_Am_0(5) + am_duong * 20, point_Focus5.y / 10 - 8, point_Focus5.Dir, 3, g);
					}
				}
				for (int num8 = 0; num8 < VecSubEff.size(); num8++)
				{
					Point point7 = (Point)VecSubEff.elementAt(num8);
					if (point7.fRe == 5)
					{
						fraImgSub3Eff.drawFrame(point7.f % fraImgSub3Eff.nFrame, point7.x, point7.y, 0, 3, g);
					}
					else
					{
						fraImgSubEff.drawFrame(point7.f % fraImgSubEff.nFrame, point7.x, point7.y, 0, 3, g);
					}
				}
				break;
			}
			case 245:
			case 251:
			{
				if (!checkNullObject(1) && f >= 8 && f <= 19 && f - 8 < mframeSuper.Length)
				{
					fraImgEff.drawFrameNew(mframeSuper[f - 8][0], objFireMain.x + am_duong * (mframeSuper[f - 8][1] + 20), objFireMain.y - objFireMain.hOne / 2 - mframeSuper[f - 8][2] - objFireMain.dy, objFireMain.type_left_right, 3, g);
				}
				for (int m = 0; m < VecEff.size(); m++)
				{
					Point point3 = (Point)VecEff.elementAt(m);
					fraImgSubEff.drawFrame(point3.f / 2 % fraImgSubEff.nFrame, point3.x, point3.y, point3.dis, 3, g);
				}
				break;
			}
			case 249:
			case 252:
			{
				if (!checkNullObject(1) && (f == 6 || f == 8 || f == 19 || f == 21))
				{
					fraImgSub2Eff.drawFrame(0, objFireMain.x, objFireMain.y, 0, 33, g);
				}
				for (int i = 0; i < VecEff.size(); i++)
				{
					Point point = (Point)VecEff.elementAt(i);
					int num = (point.f + point.frame) % mframeSuper.Length;
					fraImgSubEff.drawFrameNew(point.f % fraImgSubEff.nFrame, point.x, point.y - point.limitY, point.dis, 3, g);
					fraImgEff.drawFrameNew(mframeSuper[num][0], point.x + point.fSmall * (mframeSuper[num][1] + 20), point.y - mframeSuper[num][2] - point.limitY, point.dis, 3, g);
				}
				break;
			}
			}
		}
		catch (Exception)
		{
		}
	}

	private void paintEffTru2(mGraphics g)
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			fraImgEff.drawFrame(0, point_Focus.x, point_Focus.y, 0, 3, g);
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point point = (Point)VecSubEff.elementAt(j);
			fraImgEff.drawFrame(point.f, point.x, point.y, 0, 3, g);
		}
	}

	private void paint_Dong_Dat_2(mGraphics g)
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			if (point.frame == 0 && typeEffect == 244)
			{
				fraImgSubEff.drawFrameNew(point.f / 3 % fraImgSubEff.nFrame, point.x, point.y, Dir, 33, g);
			}
			else
			{
				fraImgEff.drawFrameNew(point.f / 3 % fraImgEff.nFrame, point.x, point.y, Dir, 33, g);
			}
		}
	}

	private void paint_Dong_Dat_1(mGraphics g)
	{
		if (f > 20 && fraImgEff.getImageFrame() != null)
		{
			int num = 1;
			int num2 = 0;
			int num3 = 0;
			if (f < 24)
			{
				num = 3;
				num3 = fraImgEff.frameWidth / num;
				num2 = fraImgEff.frameWidth / 2 - num3 / 2;
			}
			else if (f < 27)
			{
				num = 2;
				num3 = fraImgEff.frameWidth / num;
				num2 = fraImgEff.frameWidth / 2 - num3 / 2;
			}
			else
			{
				num3 = fraImgEff.frameWidth;
				num2 = 0;
			}
			g.drawRegion(fraImgEff.getImageFrame(), num2, 0, num3, fraImgEff.frameHeight, 0, MainScreen.cameraMain.xCam + x, MainScreen.cameraMain.yCam + y, 3);
			if (f < 24)
			{
				num3 = fraImgSubEff.frameWidth / num;
				num2 = fraImgSubEff.frameWidth / 2 - num3 / 2;
			}
			else if (f < 27)
			{
				num3 = fraImgSubEff.frameWidth / num;
				num2 = fraImgSubEff.frameWidth / 2 - num3 / 2;
			}
			else
			{
				num3 = fraImgSubEff.frameWidth;
				num2 = 0;
			}
			g.drawRegion(fraImgSubEff.getImageFrame(), num2, 0, num3, fraImgSubEff.frameHeight, 0, MainScreen.cameraMain.xCam + x1000, MainScreen.cameraMain.yCam + y1000, 3);
		}
	}

	private void paintEffTru(mGraphics g)
	{
		if (f <= fRemove)
		{
			fraImgEff.drawFrame(0, x, y, 0, 3, g);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			fraImgEff.drawFrame(point.f, point.x, point.y, 0, 3, g);
		}
	}

	private void paintEff_Mr0_1(mGraphics g)
	{
		int num = 14;
		if (frame == 0)
		{
			fraImgEff.drawFrameNew(4, x - am_duong * 4, y + num - 4 - 8, Dir, 33, g);
			fraImgEff.drawFrameNew(4, x + am_duong * 4, y + num - 2 - 8, Dir, 33, g);
			fraImgEff.drawFrameNew(5, x + am_duong * 8, y + num - 8, Dir, 33, g);
			fraImgEff.drawFrameNew(5, x + am_duong * 12, y + num + 2 - 8, Dir, 33, g);
		}
		else if (frame == 1)
		{
			fraImgEff.drawFrameNew(2, x - am_duong * 5, y - 6 - 24, Dir, 33, g);
			fraImgEff.drawFrameNew(2, x + am_duong * 5, y - 3 - 16, Dir, 33, g);
			fraImgEff.drawFrameNew(3, x + am_duong * 15, y + 2 - 10, Dir, 33, g);
			fraImgEff.drawFrameNew(3, x + am_duong * 25, y + 13 - 10, Dir, 33, g);
		}
		else if (frame == 2)
		{
			fraImgEff.drawFrameNew(0, x - am_duong * 5, y - 6 - 24, Dir, 33, g);
			fraImgEff.drawFrameNew(0, x + am_duong * 5, y - 3 - 6 - 10, Dir, 33, g);
			fraImgEff.drawFrameNew(0, x + am_duong * 15, y + 2 - 10, Dir, 33, g);
			fraImgEff.drawFrameNew(0, x + am_duong * 25, y + 13 - 10, Dir, 33, g);
		}
		if (f >= 10 && f < 14 && !checkNullObject(1))
		{
			fraImgSubEff.drawFrameNew(2 + (f - 10) / 2, objFireMain.x + am_duong * 20, objFireMain.y - 50, Dir, 3, g);
		}
	}

	private void paintEff_Df_2(mGraphics g)
	{
		for (int num = VecEff.size() - 1; num >= 0; num--)
		{
			Point point = (Point)VecEff.elementAt(num);
			fraImgEff.drawFrame(point.frame, point.x, point.y, Dir, 33, g);
		}
	}

	private void paintEff_Mr1_2(mGraphics g)
	{
		int trans = 0;
		if (!checkNullObject(1) && objFireMain.Dir == 2)
		{
			trans = 2;
		}
		if (f < 6)
		{
			fraImgSubEff.drawFrameNew(f / 2 % fraImgSubEff.nFrame, x - 10 * am_duong, y, trans, 3, g);
		}
		for (int num = VecEff.size() - 1; num >= 0; num--)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(num);
			fraImgEff.drawFrame(point_Focus.f / 2 % fraImgEff.nFrame, point_Focus.x, point_Focus.y, Dir, 3, g);
		}
	}

	private void paintCrocodile2(mGraphics g)
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			fraImgEff.drawFrame(2 + point.f % 3, point.x, point.y, Dir, 33, g);
		}
	}

	private void paintCrocodile1(mGraphics g)
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			fraImgSubEff.drawFrame(point.f % fraImgSubEff.nFrame, point.x / 10, point.y / 10, 0, 33, g);
		}
		if (f <= 2)
		{
			fraImgEff.drawFrame(f, x, y, 0, 33, g);
		}
		if (f >= 10 && f <= 12)
		{
			fraImgEff.drawFrame(12 - f, x, y, 0, 33, g);
		}
	}

	private void paintZoroS2_L3_SHORT(mGraphics g)
	{
		if (f > 2 && f < 5)
		{
			fraImgEff.drawFrame(f - 13, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
		}
		else if (f > 12 && f < 15)
		{
			fraImgEff.drawFrame(f - 23, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
		}
		else if (f > 18 && f < 21)
		{
			fraImgEff.drawFrame(f - 29, objFireMain.x, objFireMain.y - 10, Dir, 33, g);
		}
	}

	public override void update()
	{
		if (objFireMain != null && (objFireMain.returnAction() || objFireMain.Action == 4))
		{
			removeEff();
			return;
		}
		base.update();
		if (typeEffect == 4017 || typeEffect == 4010)
		{
			updateThanTrangSkill(4010);
			return;
		}
		if (typeEffect == 4018)
		{
			updateVenomRain4018();
			return;
		}
		if (typeEffect == 4019)
		{
			updateVenomBuff4019();
			return;
		}
		if (typeEffect == 4021)
		{
			updateThanhLongActive4021();
			return;
		}
		if (typeEffect == 4022)
		{
			updateThanhLongActive4022();
			return;
		}
		if (typeEffect == 4023)
		{
			updateThanhLongBuff4023();
			return;
		}
		if ((typeEffect >= 4001 && typeEffect <= 4080) || (typeEffect >= 4201 && typeEffect <= 4216) || (typeEffect >= 4501 && typeEffect <= 4516))
		{
			updateThanTrangSkill(typeEffect);
			return;
		}
		switch (typeEffect)
		{
		case TYPE_NIKA_ACTIVE_1_LEVEL1:
			updateNikaJump(NIKA_VARIANT_ACTIVE_1_LEVEL1);
			break;
		case TYPE_NIKA_ACTIVE_1_LEVEL5:
			updateNikaJump(NIKA_VARIANT_ACTIVE_1_LEVEL5);
			break;
		case TYPE_NIKA_ACTIVE_2:
			updateNikaJump(NIKA_VARIANT_ACTIVE_2);
			break;
		case TYPE_NIKA_BUFF:
			updateNikaBuff();
			break;
		case TYPE_LIGHT_ACTIVE_1_LEVEL5:
			updateLightActive1Level5();
			break;
		case TYPE_LIGHT_ACTIVE_2_LEVEL5:
			updateLightActive2Level5();
			break;
		case TYPE_LOVE_ACTIVE_1_LEVEL5:
			updateSkillBuff();
			break;
		case TYPE_LOVE_ACTIVE_2_LEVEL5:
			updateLoveActive2Level5();
			break;
		case TYPE_NIKYU_ACTIVE_1:
			updateNikyuActive1();
			break;
		case TYPE_NIKYU_ACTIVE_2:
			updateNikyuActive2();
			break;
		case TYPE_NIKYU_BUFF:
			updateNikyuBuff();
			break;
		case 2000:
			updateEffFireExplore();
			break;
		case 1998:
		case 1999:
			updateEffThunderFall();
			break;
		case 280:
			if (x > toX)
			{
				x = toX;
				if (y < toY + 20)
				{
					y += 5;
				}
			}
			if (f == 15)
			{
				GameScreen.addEffectEnd(178, 0, toX, toY - 55, Dir, objMainEff);
			}
			if (f == 10)
			{
				GameScreen.addEffectEnd(119, 4, objFireMain.x + 20, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
			}
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 0:
			updateAngleNormal(objBeFireMain, 0);
			break;
		case 1:
		case 37:
			updateLuffy1();
			break;
		case 47:
		case 48:
			updateSanji1();
			break;
		case 154:
			updateZoro1();
			break;
		case 155:
			updateZoro2();
			break;
		case 7:
			updateUssopSea1();
			break;
		case 141:
			updateUssopSea2();
			break;
		case 142:
			updateUssopSea3();
			break;
		case 57:
			updateUssop2();
			break;
		case 64:
		case 66:
			updateUssop_Skill2();
			break;
		case 58:
			updateUssopSkill1_Lv3();
			break;
		case 159:
			updateUssopSkill1_Lv3_New();
			break;
		case 126:
		case 192:
			updateUssopSkill1_Lv3_SHORT();
			break;
		case 224:
		case 301:
			update_Ussop_S1_L5();
			break;
		case 501:
			update_Ussop_S1_L7();
			break;
		case 9:
		case 53:
		case 163:
			updateNami1();
			break;
		case 63:
		case 190:
		case 222:
		case 312:
			updateNami1_SHORT();
			break;
		case 512:
			update_Nami_S2_L7();
			break;
		case 11:
			updateNamiSea1();
			break;
		case 139:
			updateNamiSea2();
			break;
		case 140:
			updateNamiSea3();
			break;
		case 12:
		case 188:
		case 220:
		case 293:
			updateSanji2();
			break;
		case 493:
			update_Sanji_S3_L7();
			break;
		case 49:
		case 50:
			updateSanjiSkill3_Lv1();
			break;
		case 266:
			updateRankyaku();
			break;
		case 276:
			updateSoi();
			break;
		case 277:
			updateSoi2();
			break;
		case 278:
		case 279:
			updateHuou();
			break;
		case 267:
			updateShigan();
			break;
		case 268:
			updateDoor();
			break;
		case 269:
			updateDoor2();
			break;
		case 14:
		case 44:
			updateSanji4();
			break;
		case 15:
		case 38:
			updateZoro3();
			break;
		case 16:
		case 51:
			updateNami4();
			break;
		case 52:
		case 189:
		case 221:
		case 311:
			update_Nami_S1_L3();
			break;
		case 511:
			update_Nami_S1_L7();
			break;
		case 19:
			updateZoroSea3();
			break;
		case 42:
			updateZoroSea1();
			break;
		case 43:
			updateZoroSea2();
			break;
		case 21:
		case 33:
		case 176:
			updateLuffyS1();
			break;
		case 34:
			updateLuffy6();
			break;
		case 35:
			updateLuffy_S2_L2();
			break;
		case 41:
			updateZoroS2_L1_NEW();
			break;
		case 29:
			updateZoro8();
			break;
		case 31:
		case 55:
		case 56:
		case 191:
		case 223:
			updateNami5();
			break;
		case 313:
			updateNami6();
			break;
		case 513:
			update_Nami_S3_L7();
			break;
		case 46:
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 17:
		case 165:
		case 166:
			if (f >= fRemove)
			{
				if (!checkNullObject(1))
				{
					objFireMain.isTanHinh = false;
				}
				removeEff();
			}
			break;
		case 67:
		case 68:
		case 69:
		case 194:
			update_Ussop_S3_L1();
			break;
		case 226:
			update_Ussop_S3_L5();
			break;
		case 303:
			update_Ussop_S3_L6();
			break;
		case 503:
			update_Ussop_S3_L7();
			break;
		case 164:
		case 227:
		{
			for (int num16 = 0; num16 < VecEff.size(); num16++)
			{
				Point point15 = (Point)VecEff.elementAt(num16);
				point15.update();
				point15.frame++;
				if (point15.dis == 0)
				{
					if (point15.frame >= fraImgEff.nFrame)
					{
						point15.frame = 0;
					}
				}
				else if (point15.frame >= fraImgSubEff.nFrame)
				{
					point15.frame = 0;
				}
				if (point15.f >= point15.fRe)
				{
					if (CRes.random(2) == 1)
					{
						setAva(0, objBeFireMain);
					}
					VecEff.removeElement(point15);
					num16--;
				}
			}
			if (f <= fRemove - 5 && f % 3 == 0)
			{
				Point point16 = new Point();
				point16.x = x + am_duong * 15;
				point16.y = y;
				point16.vx = am_duong * (5 + CRes.random(2));
				if (typeEffect == 227)
				{
					point16.vx = am_duong * (5 + CRes.random(2));
				}
				point16.vy = CRes.random_Am_0(2);
				point16.fRe = 6 + CRes.random(3);
				point16.dis = ((CRes.random(3) != 0) ? 1 : 0);
				VecEff.addElement(point16);
				if (CRes.random(2) == 0)
				{
					addSound(4);
					if (!checkNullObject(2))
					{
						GameScreen.addEffectEnd(108, 1, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
					}
				}
			}
			if (f >= fRemove && VecEff.size() == 0)
			{
				removeEff();
			}
			break;
		}
		case 30:
			if (f >= fRemove)
			{
				GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
				removeEff();
			}
			break;
		case 71:
		case 72:
		case 75:
		case 92:
		case 145:
		case 146:
		case 147:
		case 148:
			if (f >= fRemove)
			{
				if (!checkNullObject(1))
				{
					objFireMain.isPaintWeapon = true;
				}
				GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
				removeEff();
			}
			break;
		case 73:
		case 78:
			if (f == 5 || f == 0)
			{
				if (!checkNullObject(1))
				{
					objFireMain.isPaintWeapon = true;
				}
				GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
			}
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 79:
			if (f == 6 || f == 0)
			{
				if (!checkNullObject(1))
				{
					objFireMain.isPaintWeapon = true;
				}
				GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
			}
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 24:
		case 80:
			if (f == 7 || f == 2 || f == 12)
			{
				if (!checkNullObject(1))
				{
					objFireMain.isPaintWeapon = true;
				}
				GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
				if (typeEffect == 24 && !checkNullObject(2))
				{
					GameScreen.addEffectEnd(4, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3) - 10, Dir, objMainEff);
				}
			}
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 74:
			update_Mon_5();
			break;
		case 76:
			if (f >= fRemove)
			{
				GameScreen.addEffectEnd(8, 0, toX, toY, Dir, objMainEff);
				removeEff();
			}
			break;
		case 77:
			updateAlvida2();
			break;
		case 81:
		case 143:
		case 149:
			updateMon10();
			break;
		case 82:
		case 144:
			updateMon11();
			break;
		case 156:
			updateLuffyS1_NEW();
			break;
		case 83:
		case 180:
		case 212:
			updateLuffyS1_L3_SHORT();
			break;
		case 271:
			update_Luffy_S1_L6();
			break;
		case 471:
			update_Luffy_S1_L7();
			break;
		case 274:
		case 275:
			updateXaPhong();
			break;
		case 160:
			updateLuffyS2_NEW();
			break;
		case 84:
		case 181:
		case 213:
		case 272:
			updateLuffyS2_NEW_SHORT();
			break;
		case 472:
			update_Luffy_S2_L7();
			break;
		case 85:
		case 182:
			updateLuffyS3_New();
			break;
		case 214:
		case 273:
			updateLuffyS3_L5();
			break;
		case 473:
			update_Luffy_S3_L7();
			break;
		case 157:
			updateZoroS1_New();
			break;
		case 86:
		case 183:
		case 215:
			updateZoro_S1_L3_SHORT();
			break;
		case 281:
			update_Zoro_S1_L6();
			break;
		case 481:
			update_Zoro_S1_L7();
			break;
		case 87:
		case 184:
		case 216:
			updateZoroS2_New();
			break;
		case 282:
			update_Zoro_S2_L6();
			break;
		case 482:
			update_Zoro_S2_L7();
			break;
		case 161:
			updateZoroS2_New_SHORT();
			break;
		case 88:
			updateMorgan_1();
			break;
		case 89:
			updateMorgan_2();
			break;
		case 90:
		case 91:
			if (f > fRemove)
			{
				addSound(2);
				GameScreen.addEffectEnd(3, 0, toX, toY, Dir, objMainEff);
				removeEff();
			}
			break;
		case 93:
			updateMohji_1();
			break;
		case 94:
			updateMohji_2();
			break;
		case 95:
			updateBuggy_1();
			break;
		case 96:
			updateBuggy_2();
			break;
		case 97:
			updateCabaji_1();
			break;
		case 22:
		case 98:
			updateCabaji_2();
			break;
		case 248:
		case 255:
			if (!checkNullObject(1))
			{
				if (f == 6 || f == 11)
				{
					objFireMain.isTanHinh = true;
				}
				if (f == 7)
				{
					objFireMain.isTanHinh = false;
				}
				if (f == 12)
				{
					if (isAddSound)
					{
						addSound(51);
					}
					if (MainObject.getDistance(objFireMain.x, objFireMain.y, objBeFireMain.x, objBeFireMain.y) < 260)
					{
						objFireMain.x = objBeFireMain.x;
						objFireMain.y = objBeFireMain.y + 5;
						objFireMain.dy = 400;
					}
				}
				if (f == 14)
				{
					objFireMain.isTanHinh = false;
					objFireMain.dy = 400;
				}
				if (f >= 15 && f <= 19)
				{
					if (typeEffect == 255)
					{
						GameScreen.addEffectEnd(108, 5, objFireMain.x, objFireMain.y - objFireMain.dy, Dir, objMainEff);
						GameScreen.addEffectEnd(108, 5, objFireMain.x, objFireMain.y - objFireMain.dy + 40, Dir, objMainEff);
					}
					if (objFireMain.dy >= 0)
					{
						objFireMain.dy -= 80;
					}
				}
				if (f == 19)
				{
					if (isAddSound)
					{
						addSound(5);
					}
					objFireMain.dy = 0;
					setAva(1, objBeFireMain);
					GameScreen.addEffectEnd(148, 0, objFireMain.x, objFireMain.y, Dir, objMainEff);
					if (typeEffect == 255)
					{
						GameScreen.addEffectEnd(54, 12, objFireMain.x, objFireMain.y, Dir, objMainEff);
					}
					GameScreen.addEffectEnd(45, 0, objFireMain.x, objFireMain.y + 25, Dir, objMainEff);
				}
			}
			if (f > fRemove)
			{
				removeEff();
			}
			break;
		case 99:
			if (f == 2)
			{
				addSound(10);
			}
			if (f == 2 || f == 8)
			{
				GameScreen.addEffectEnd(3, 0, toX, toY, Dir, objMainEff);
			}
			if (f > fRemove)
			{
				setAva(0, objBeFireMain);
				removeEff();
			}
			break;
		case 100:
			updateNyaban_2();
			break;
		case 101:
			updateNyaban_3();
			break;
		case 102:
			updateJango_1();
			break;
		case 103:
			updateKuro_1();
			break;
		case 104:
			updateKuro_2();
			break;
		case 105:
		case 107:
			if (f >= fRemove)
			{
				addSound(14);
				addVir(3, 5, 10, isPlayer: false);
				GameScreen.addEffectEnd(35, 0, x, y, Dir, objMainEff);
				setAva(1, objBeFireMain);
				removeEff();
			}
			break;
		case 65:
			if (f >= fRemove)
			{
				addSound(5);
				addVir(3, 5, 10, isPlayer: false);
				GameScreen.addEffectEnd(35, 0, toX, toY, Dir, objMainEff);
				GameScreen.addEffectEnd(21, 0, toX, toY, Dir, objMainEff);
				GameScreen.addEffectEnd(107, 0, toX, toY, Dir, objMainEff);
				setAva(2, objBeFireMain);
				removeEff();
			}
			break;
		case 70:
			if (f == 4)
			{
				x += am_duong * 20;
				y -= 10;
				int xdich = toX - x;
				int ydich = toY - y;
				create_Speed(xdich, ydich, null);
				fRemove += 4;
			}
			if (f >= fRemove)
			{
				GameScreen.addEffectEnd(4, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
				removeEff();
			}
			break;
		case 106:
			updatePearl_2();
			break;
		case 108:
			updateGhin_2();
			break;
		case 109:
			updateDonKrieg_1();
			break;
		case 110:
			updateDonKrieg_2();
			break;
		case 111:
			updateDonKrieg_3();
			break;
		case 112:
		case 270:
			updateHachi_1();
			break;
		case 113:
		case 150:
		case 151:
		case 152:
		case 153:
			updateHachi_2();
			break;
		case 114:
			updateChu_1();
			break;
		case 115:
			updateChu_2();
			break;
		case 116:
			updateKurobi_1();
			break;
		case 117:
			updateKurobi_2();
			break;
		case 118:
			updateArlong_1();
			break;
		case 119:
			updateArlong_2();
			break;
		case 120:
			updateArlong_3();
			break;
		case 121:
			update_Zoro_S3_L1();
			break;
		case 122:
			update_Zoro_S3_L2();
			break;
		case 123:
		case 185:
		case 217:
			update_Zoro_S3_L3();
			break;
		case 283:
			update_Zoro_S3_L6();
			break;
		case 483:
			update_Zoro_S3_L7();
			break;
		case 158:
		case 177:
			updateSanji_S1_L3_New();
			break;
		case 124:
		case 186:
		case 218:
			updateSanji_S1_L3_SHORT();
			break;
		case 162:
			updateSanji_S2_L3_New();
			break;
		case 125:
		case 187:
			updateSanji_S2_L3_New_SHORT();
			break;
		case 127:
		case 193:
		case 225:
			updateUssop_S2_L3_New();
			break;
		case 302:
			updateUssop_S2_L6();
			break;
		case 502:
			update_Ussop_S2_L7();
			break;
		case 128:
			if (f == 0 || f == 8)
			{
				GameScreen.addEffectEnd(8, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
			}
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 129:
		case 130:
			if (f == 0 || f == 8 || f == 14)
			{
				GameScreen.addEffectEnd(8, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
			}
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 131:
		case 132:
			updateLuffyMon16_17();
			break;
		case 133:
			updateLuffySea1();
			break;
		case 134:
		case 135:
			updateLuffySea2();
			break;
		case 136:
			updateSanjiSea1();
			break;
		case 137:
		case 138:
			updateSanjiSea2();
			break;
		case 2:
			update_Ace_1();
			break;
		case 228:
		case 259:
		case 260:
		case 261:
			update_Ace_1_L2();
			break;
		case 10030:
			update_ho_den_vu_tru();
			break;
		case 3:
		case 229:
		case 262:
		case 263:
		case 264:
			update_Ace_2();
			break;
		case 400:
		case 401:
		case 403:
			break;
		case 402:
			update_Blackhole();
			break;
		case 4:
		case 230:
			update_Aokiji_1();
			break;
		case 5:
		case 231:
			update_Aokiji_2();
			break;
		case 6:
		case 232:
			update_Smoker_1();
			break;
		case 10:
		case 234:
			update_Smoker_2();
			break;
		case 13:
		case 258:
			update_Mon_Smoker_1();
			break;
		case 18:
			update_Mon_Smoker_2();
			break;
		case 10001:
			update_Pan1();
			break;
		case 10002:
			updatePan2();
			break;
		case 10003:
		case 10017:
		case 10020:
		case 10021:
		case 10022:
		case 10026:
			if (GameCanvas.timeNow - timeBegin >= timeEnd)
			{
				removeEff();
			}
			break;
		case 10004:
			updateGalio2();
			break;
		case 10005:
			updateNoNangLuong1();
			break;
		case 10006:
			updateNoNangLuong2();
			break;
		case 10007:
			updateNoNangLuong3();
			break;
		case 10008:
			updateNoTheoHuong_1();
			break;
		case 10009:
			updateNoTheoHuong_2();
			break;
		case 10010:
		case 10013:
			updateXerath1();
			break;
		case 10011:
		case 10024:
			updateXerath2();
			break;
		case 10012:
			updatexerath3();
			break;
		case 10015:
			updateUrgot3();
			break;
		case 10018:
			updateMonster_Chay_Thang();
			break;
		case 10019:
			if (!checkNullObject(1))
			{
				objFireMain.vx = am_duong * 15;
			}
			if (f >= fRemove)
			{
				if (!checkNullObject(1))
				{
					objFireMain.vx = 0;
				}
				removeEff();
			}
			break;
		case 10023:
			updateMonster_DanhTron();
			break;
		case 10025:
			if (f >= fRemove)
			{
				GameScreen.addEffectEnd(57, 0, toX, toY, Dir, objMainEff);
				removeEff();
			}
			break;
		case 10027:
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 20:
			update_Mon_Valentine();
			break;
		case 23:
			update_Mon_Mr5();
			break;
		case 25:
		case 235:
			update_Crocodile_1();
			break;
		case 26:
		case 236:
			update_Crocodile_2();
			break;
		case 27:
			if (f >= fRemove)
			{
				if (!checkNullObject(2))
				{
					setAva(0, objBeFireMain);
					GameScreen.addEffectEnd(36, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				}
				removeEff();
			}
			break;
		case 28:
			if (f >= fRemove)
			{
				if (!checkNullObject(2))
				{
					setAva(0, objBeFireMain);
					GameScreen.addEffectEnd_ObjTo(102, subType, toX, toY, objBeFireMain.ID, objBeFireMain.typeObject, Dir, null);
				}
				removeEff();
			}
			break;
		case 32:
			updateWapol_1();
			break;
		case 36:
			if (f >= fRemove)
			{
				setAva(1, objBeFireMain);
				removeEff();
			}
			break;
		case 39:
			updateWapol_3();
			break;
		case 40:
			update_Wapol_4();
			break;
		case 45:
			updateMr3_1();
			break;
		case 54:
			updateMr3_2();
			break;
		case 59:
		case 60:
		case 62:
			if (f >= fRemove)
			{
				removeEff();
			}
			break;
		case 61:
			if (f >= fRemove)
			{
				GameScreen.addEffectEnd(60, 2, toX, toY, Dir, objMainEff);
				removeEff();
			}
			break;
		case 167:
			updateMissMS_1();
			break;
		case 169:
		case 237:
			updateSet_1();
			break;
		case 170:
		case 238:
			updateSet_2();
			break;
		case 171:
		case 239:
			updateNamThach_1();
			break;
		case 172:
		case 240:
			update_Nham_thach_2();
			break;
		case 168:
			update_Mr1_1();
			break;
		case 173:
			update_Mr1_2();
			break;
		case 174:
			update_DF_1();
			break;
		case 175:
			update_DF_2();
			break;
		case 178:
			update_Mr0_1();
			break;
		case 179:
		case 241:
			update_Pell_1();
			break;
		case 195:
			update_Enel_1();
			break;
		case 196:
			update_Enel_2();
			break;
		case 197:
			update_Enel_3();
			break;
		case 198:
			update_Satori_1();
			break;
		case 199:
			update_Satori_2();
			break;
		case 200:
			update_Ohm_1();
			break;
		case 201:
			update_Ohm_2();
			break;
		case 202:
			update_Gedatsu_1();
			break;
		case 203:
			update_Gedatsu_2();
			break;
		case 204:
			update_Shura_1();
			break;
		case 205:
			update_Shura_2();
			break;
		case 206:
		case 207:
			update_Linh_Troi();
			break;
		case 208:
			update_Tru_1();
			break;
		case 250:
			update_Tru_2();
			break;
		case 209:
		case 242:
			update_Lucci_1();
			break;
		case 210:
		case 243:
			update_Dong_Dat_1();
			break;
		case 211:
		case 244:
			update_Dong_Dat_2();
			break;
		case 219:
			if (f >= fRemove || checkNullObject(3))
			{
				objFireMain.isTanHinh = false;
				removeEff();
			}
			if (f == 5 || f == 14)
			{
				if (MainObject.getDistance(objFireMain.x, objFireMain.y, objBeFireMain.x, objBeFireMain.y) < 160)
				{
					objFireMain.x = objBeFireMain.x;
					objFireMain.y = objBeFireMain.y;
				}
				objFireMain.isTanHinh = true;
				changeDir();
				am_duong = -1;
				if (Dir == 2)
				{
					am_duong = 1;
				}
				objFireMain.Dir = Dir;
				x = objBeFireMain.x + am_duong * 30;
				y = objBeFireMain.y;
			}
			if (f == 9 || f == 19)
			{
				objFireMain.isTanHinh = true;
				changeDir();
				am_duong = -1;
				if (Dir == 2)
				{
					am_duong = 1;
				}
				objFireMain.Dir = Dir;
				x = objBeFireMain.x + am_duong * 30;
				y = objBeFireMain.y;
			}
			if (f == 6 || f == 11 || f == 15 || f == 20)
			{
				sbyte subtype5 = 0;
				if (f == 6 || f == 15)
				{
					if (isAddSound)
					{
						mSound.playSound(14, mSound.volumeSound);
					}
					subtype5 = 1;
				}
				GameScreen.addEffectEnd(108, 7, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
				addVir(5, 5, 10, isPlayer: true);
				GameScreen.addEffectEnd(36, subtype5, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
				GameScreen.addEffectEnd(25, 4, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
				setAva(1, objBeFireMain);
			}
			if (f == 24)
			{
				objFireMain.isTanHinh = false;
				objFireMain.x = x1000;
				objFireMain.y = y1000;
			}
			break;
		case 292:
		{
			if (f >= fRemove || checkNullObject(3))
			{
				objFireMain.isTanHinh = false;
				removeEff();
			}
			if (f == 0 || f == 8 || f == 15 || f == 25)
			{
				if (MainObject.getDistance(objFireMain.x, objFireMain.y, objBeFireMain.x, objBeFireMain.y) < 160)
				{
					objFireMain.x = objBeFireMain.x;
					objFireMain.y = objBeFireMain.y;
				}
				objFireMain.isTanHinh = true;
				changeDir();
				am_duong = -1;
				if (Dir == 2)
				{
					am_duong = 1;
				}
				objFireMain.Dir = Dir;
				x = objBeFireMain.x + am_duong * 30;
				y = objBeFireMain.y;
			}
			if (f >= 15 && f <= 19)
			{
				y -= 12 * (f - 14);
				objFireMain.isTanHinh = true;
			}
			if (f >= 20 && f <= 24)
			{
				y += 12 * (f - 19);
				objFireMain.isTanHinh = true;
			}
			if (f == 25)
			{
				GameScreen.addEffectEnd(172, 0, objBeFireMain.x, y + 5, Dir, objMainEff);
			}
			if (f != 6 && f != 11 && f != 15 && f != 20)
			{
				break;
			}
			sbyte subtype4 = 0;
			if (f == 6 || f == 15)
			{
				if (isAddSound)
				{
					mSound.playSound(14, mSound.volumeSound);
				}
				subtype4 = 1;
			}
			GameScreen.addEffectEnd(108, 7, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(36, subtype4, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			GameScreen.addEffectEnd(25, 4, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			setAva(1, objBeFireMain);
			break;
		}
		case 492:
		{
			if (f >= fRemove || checkNullObject(3))
			{
				objFireMain.isTanHinh = false;
				removeEff();
			}
			if (f == 0 || f == 8 || f == 15 || f == 25)
			{
				if (MainObject.getDistance(objFireMain.x, objFireMain.y, objBeFireMain.x, objBeFireMain.y) < 160)
				{
					objFireMain.x = objBeFireMain.x;
					objFireMain.y = objBeFireMain.y;
				}
				objFireMain.isTanHinh = true;
				changeDir();
				am_duong = -1;
				if (Dir == 2)
				{
					am_duong = 1;
				}
				objFireMain.Dir = Dir;
				x = objBeFireMain.x + am_duong * 30;
				y = objBeFireMain.y;
			}
			if (f >= 15 && f <= 19)
			{
				y -= 12 * (f - 14);
				objFireMain.isTanHinh = true;
			}
			if (f >= 20 && f <= 24)
			{
				y += 12 * (f - 19);
				objFireMain.isTanHinh = true;
			}
			if (f == 25)
			{
				GameScreen.addEffectEnd(172, 1, objBeFireMain.x, y + 5, Dir, objMainEff);
				GameScreen.addEffectEnd(112, 2, objFireMain.x - objFireMain.wOne, objFireMain.y + 10, Dir, objFireMain);
			}
			if (f != 6 && f != 11 && f != 15 && f != 20)
			{
				break;
			}
			sbyte subtype3 = 0;
			if (f == 6 || f == 15)
			{
				if (isAddSound)
				{
					mSound.playSound(14, mSound.volumeSound);
				}
				subtype3 = 1;
			}
			GameScreen.addEffectEnd(108, 7, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(36, subtype3, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			GameScreen.addEffectEnd(25, 4, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			setAva(1, objBeFireMain);
			break;
		}
		case 291:
		{
			if (f >= fRemove || checkNullObject(3))
			{
				objFireMain.isTanHinh = false;
				removeEff();
			}
			if (f == 0 || f == 8 || f == 15 || f == 25)
			{
				objFireMain.isTanHinh = true;
				am_duong = -1;
				if (Dir == 2)
				{
					am_duong = 1;
				}
				objFireMain.Dir = Dir;
				x = objBeFireMain.x + am_duong * 30;
				y = objBeFireMain.y;
				if (f == 8 || f == 15)
				{
					GameScreen.addEffectEnd(119, 4, objBeFireMain.x + am_duong * 10, objBeFireMain.y - objBeFireMain.hOne / 2 - 5, Dir, objMainEff);
				}
			}
			if (f != 6 && f != 11 && f != 15 && f != 20)
			{
				break;
			}
			sbyte subtype2 = 0;
			if (f == 6 || f == 15)
			{
				if (isAddSound)
				{
					mSound.playSound(14, mSound.volumeSound);
				}
				subtype2 = 1;
			}
			GameScreen.addEffectEnd(108, 7, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(36, subtype2, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			GameScreen.addEffectEnd(25, 4, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			setAva(1, objBeFireMain);
			break;
		}
		case 491:
		{
			if (f >= fRemove || checkNullObject(3))
			{
				objFireMain.isTanHinh = false;
				removeEff();
			}
			if (f == 0 || f == 8 || f == 15 || f == 25)
			{
				objFireMain.isTanHinh = true;
				am_duong = -1;
				if (Dir == 2)
				{
					am_duong = 1;
				}
				objFireMain.Dir = Dir;
				x = objBeFireMain.x + am_duong * 30;
				y = objBeFireMain.y;
				setAva(2, objBeFireMain);
				if (f == 8 || f == 15)
				{
					GameScreen.addEffectEnd(119, 4, objBeFireMain.x + am_duong * 15, objBeFireMain.y - objBeFireMain.hOne / 2 - 5, Dir, objMainEff);
				}
			}
			if (f != 6 && f != 11 && f != 15 && f != 20)
			{
				break;
			}
			sbyte subtype = 0;
			if (f == 6 || f == 15)
			{
				if (isAddSound)
				{
					mSound.playSound(14, mSound.volumeSound);
				}
				subtype = 1;
			}
			GameScreen.addEffectEnd(108, 7, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			addVir(3, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(36, subtype, objBeFireMain.x - am_duong * 35, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			GameScreen.addEffectEnd(25, 4, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			objBeFireMain.x += am_duong * 18;
			setAva(2, objBeFireMain);
			break;
		}
		case 246:
		case 253:
		{
			for (int num11 = 0; num11 < VecEff.size(); num11++)
			{
				Point point11 = (Point)VecEff.elementAt(num11);
				point11.update();
				if (point11.frame == 0 && point11.f == point11.fRe)
				{
					point11.frame = 1;
					point11.vx = 0;
					point11.vy = 0;
					point11.fSmall = CRes.random(16, 24);
					GameScreen.addEffectEnd(133, 1, point11.x, point11.y, Dir, objMainEff);
					GameScreen.addEffectEnd(59, 0, point11.x, point11.y + 5, Dir, objMainEff);
					if (point11.obj != null)
					{
						setAva(1, point11.obj);
					}
				}
				if (point11.frame == 1 && point11.f == point11.fRe + point11.fSmall)
				{
					point11.frame = 2;
					point11.f = 0;
				}
				if (point11.frame == 2 && point11.f == 4)
				{
					point11.frame = 3;
					point11.f = 0;
				}
				if (point11.frame == 3 && point11.f == 2)
				{
					VecEff.removeElement(point11);
					num11--;
				}
			}
			if (f == 12)
			{
				if (isAddSound)
				{
					addSound(14);
				}
				for (int num12 = 0; num12 < vecObjsBeFire.size(); num12++)
				{
					Object_Effect_Skill object_Effect_Skill2 = (Object_Effect_Skill)vecObjsBeFire.elementAt(num12);
					if (object_Effect_Skill2 == null)
					{
						continue;
					}
					MainObject mainObject2 = MainObject.get_Object(object_Effect_Skill2.ID, object_Effect_Skill2.tem);
					if (mainObject2 == null)
					{
						continue;
					}
					Point point12 = new Point();
					point12.vy = CRes.random(30, 40);
					point12.dis = CRes.random(14, 26);
					point12.fRe = 4 + num12;
					point12.x = mainObject2.x;
					point12.y = mainObject2.y + CRes.random(5);
					if (typeEffect == 253)
					{
						for (int num13 = 0; num13 < 2; num13++)
						{
							Point point13 = new Point();
							point13.vy = CRes.random(30, 40);
							point13.dis = point12.dis + 10;
							point13.fRe = 5 + num12 + num13;
							point13.x = mainObject2.x - 25 + num13 * 50;
							point13.y = point12.y - point13.vy * point13.fRe;
							point13.frame = 0;
							VecEff.addElement(point13);
						}
					}
					point12.y += -point12.vy * point12.fRe;
					point12.frame = 0;
					point12.obj = mainObject2;
					VecEff.addElement(point12);
				}
				if (!checkNullObject(1))
				{
					int num14 = CRes.random(1, 3);
					for (int num15 = 0; num15 < num14; num15++)
					{
						Point point14 = new Point();
						point14.vy = CRes.random(30, 40);
						point14.dis = CRes.random(14, 26);
						point14.fRe = 4 + vecObjsBeFire.size() + num15;
						point14.x = objBeFireMain.x + CRes.random_Am_0(100);
						point14.y = objBeFireMain.y + CRes.random_Am_0(80);
						point14.frame = 0;
						if (GameCanvas.loadmap.getTile(point14.x, point14.y) == 0)
						{
							point14.y += -(point14.vy * point14.fRe) + CRes.random(5);
							VecEff.addElement(point14);
						}
					}
				}
			}
			if (f >= fRemove && VecEff.size() == 0)
			{
				removeEff();
			}
			break;
		}
		case 247:
		case 254:
		{
			for (int num5 = 0; num5 < VecSubEff.size(); num5++)
			{
				Point point9 = (Point)VecSubEff.elementAt(num5);
				point9.f++;
				if (point9.f >= point9.fRe)
				{
					VecSubEff.removeElement(point9);
				}
			}
			for (int num6 = 0; num6 < VecEff.size(); num6++)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(num6);
				point_Focus.update_Vx_Vy();
				if (point_Focus.f >= point_Focus.fRe + 10)
				{
					VecEff.removeElement(point_Focus);
				}
				else
				{
					int num7 = CRes.random(1, 4);
					for (int num8 = 0; num8 < num7; num8++)
					{
						Point point10 = new Point(point_Focus.x / 10 + CRes.random_Am_0(4) - point_Focus.vx / 10, point_Focus.y / 10 + CRes.random_Am_0(4) - point_Focus.vy / 10);
						point10.fRe = 4;
						if (typeEffect == 254 && num8 == 0 && CRes.random(3) == 0)
						{
							point10.fRe = 5;
						}
						VecSubEff.addElement(point10);
					}
				}
				if (point_Focus.f == point_Focus.fRe && !checkNullObject(2))
				{
					setAva(1, objBeFireMain);
					GameScreen.addEffectEnd(8, 0, point_Focus.x / 10, point_Focus.y / 10, Dir, objMainEff);
					if (typeEffect == 254)
					{
						GameScreen.addEffectEnd(108, 1, point_Focus.x / 10, point_Focus.y / 10 + CRes.random_Am_0(10), Dir, objMainEff);
					}
				}
			}
			if (f == 10 || f == 15)
			{
				if (isAddSound)
				{
					addSound(18);
				}
				Point_Focus p = new Point_Focus(x * 10, y * 10);
				int num9 = 0;
				int num10 = 0;
				if (!checkNullObject(2))
				{
					num9 = objBeFireMain.x - x;
					num10 = objBeFireMain.y - objBeFireMain.hOne / 2 - y;
					p = create_Speed(num9 * 10, num10 * 10, p, x * 10, y * 10, objBeFireMain.x * 10, (objBeFireMain.y - objBeFireMain.hOne / 2) * 10);
					p.Dir = 0;
					if (num9 > 0)
					{
						p.Dir = 2;
					}
					VecEff.addElement(p);
				}
			}
			if (f >= fRemove && VecEff.size() == 0)
			{
				removeEff();
			}
			break;
		}
		case 233: // EFF_MR5_1 - không update gì (khớp J2ME: case 233 return;)
			break;
		case 245:
		case 251:
		{
			for (int n = 0; n < VecEff.size(); n++)
			{
				Point point6 = (Point)VecEff.elementAt(n);
				point6.f++;
				if (point6.f >= point6.fRe)
				{
					VecEff.removeElementAt(n);
					n--;
				}
			}
			if (f == 6 && !checkNullObject(1))
			{
				objFireMain.isTanHinh = true;
				Point point7 = new Point(objFireMain.x + am_duong * 30, objFireMain.y - objFireMain.hOne / 2);
				point7.fRe = 6;
				point7.dis = objFireMain.type_left_right;
				VecEff.addElement(point7);
			}
			if (f == 7 && MainObject.getDistance(objMainEff.x, objMainEff.y, objBeFireMain.x, objBeFireMain.y) < 260 && !checkNullObject(3))
			{
				objFireMain.x = objBeFireMain.x - am_duong * 30;
				objFireMain.y = objBeFireMain.y;
				Point point8 = new Point(objFireMain.x - am_duong * 30, objFireMain.y - objFireMain.hOne / 2);
				point8.fRe = 6;
				point8.dis = objFireMain.type_left_right;
				VecEff.addElement(point8);
			}
			if (f == 8 && !checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			if (typeEffect == 251 && f >= 8 && f <= 20 && !checkNullObject(3))
			{
				objFireMain.dy = (f - 8) / 3 * 12;
				objBeFireMain.dy = objFireMain.dy;
			}
			if (!checkNullObject(2) && (f == 9 || f == 12 || f == 15 || f == 18))
			{
				if (isAddSound)
				{
					mSound.playSound(7, mSound.volumeSound);
				}
				GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
				if (f == 9 || f == 15)
				{
					setAva(1, objBeFireMain);
				}
			}
			if (f >= fRemove && VecEff.size() == 0)
			{
				removeEff();
			}
			break;
		}
		case 249:
		case 252:
		{
			for (int l = 0; l < VecEff.size(); l++)
			{
				Point point4 = (Point)VecEff.elementAt(l);
				point4.f++;
				if (typeEffect == 252)
				{
					point4.limitY = (point4.f - 1) / 3 * 12;
					if (point4.obj != null)
					{
						point4.obj.dy = point4.limitY;
					}
				}
				if (point4.f == 1 || point4.f == 4 || point4.f == 7 || point4.f == 10)
				{
					if (point4.obj != null)
					{
						GameScreen.addEffectEnd(10, 0, point4.obj.x, point4.obj.y - point4.obj.dy - point4.obj.hOne / 2, Dir, objMainEff);
						if (point4.f == 1 || point4.f == 7)
						{
							setAva(1, point4.obj);
						}
					}
				}
				if (point4.f >= point4.fRe)
				{
					VecEff.removeElementAt(l);
					l--;
				}
			}
			if (f == 7 && !checkNullObject(1))
			{
				objFireMain.isTanHinh = true;
			}
			if (f == 8)
			{
				for (int m = 0; m < vecObjsBeFire.size(); m++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(m);
					if (object_Effect_Skill == null)
					{
						continue;
					}
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point point5 = new Point();
						point5.dis = CRes.random(2) * 2;
						point5.fSmall = -1;
						if (point5.dis == 2)
						{
							point5.fSmall = 1;
						}
						int num4 = 30 * point5.fSmall;
						point5.x = mainObject.x - num4;
						point5.fRe = mframeSuper.Length;
						point5.y = mainObject.y - mainObject.hOne / 2;
						point5.frame = CRes.random(4) * 3;
						point5.obj = mainObject;
						VecEff.addElement(point5);
					}
				}
			}
			if (!checkNullObject(2) && (f == 9 || f == 12 || f == 15 || f == 18))
			{
				if (isAddSound)
				{
					mSound.playSound(7, mSound.volumeSound);
				}
				if (CRes.random(2) == 0)
				{
					mSound.playSound(9, mSound.volumeSound);
				}
			}
			if (f == 20 && !checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			if (f >= fRemove && VecEff.size() == 0)
			{
				removeEff();
			}
			break;
		}
		case -1:
		{
			if (f < fRemove)
			{
				updateAngleXP();
				frame = setFrameAngle(gocT_Arc);
			}
			if (VecEff.size() == 0 && f > fRemove)
			{
				removeEff();
			}
			for (int k = 0; k < VecEff.size(); k++)
			{
				Point point3 = (Point)VecEff.elementAt(k);
				point3.f++;
				if (point3.f / 2 > 3)
				{
					VecEff.removeElement(point3);
					k--;
				}
			}
			if (f == fRemove)
			{
				GameScreen.addEffectEnd(108, 0, x, y + 10, Dir, objMainEff);
			}
			break;
		}
		case 10028:
			updateHoDen();
			break;
		case 404:
		{
			// Boa Hancock Skill 1 Level 1..4 (Mero Mero Mellow)
			if (f > fRemove)
			{
				removeEff();
			}
			MainObject target404 = (objBeFireMain != null) ? objBeFireMain :
				(vecObjsBeFire != null && vecObjsBeFire.size() > 0 ?
					MainObject.get_Object(((Object_Effect_Skill)vecObjsBeFire.elementAt(0)).ID, ((Object_Effect_Skill)vecObjsBeFire.elementAt(0)).tem) : null);
			if (target404 != null)
			{
				if (mframe != null && mframe.Length > 0 && f == mframe[0])
				{
					for (short n = 17; n < 19; n++)
					{
						GameScreen.addHightDataeff(n, target404.x, target404.y);
					}
				}
				if (mframe != null && mframe.Length > 0 && f == mframe[0] + 7)
				{
					GameScreen.addEffectEnd(108, 5, target404.x + CRes.random_Am_0(60), target404.y - CRes.random(30), Dir, target404);
					LoadMap.timeVibrateScreen = CRes.random(6, 20);
					GameScreen.addEffectEnd(112, 0, target404.x, target404.y, Dir, target404);
				}
			}
			break;
		}
		case 405:
		{
			// Boa Hancock Skill 2 Level 1..4 (Slave Arrow)
			for (int j = 0; j < VecEff.size(); j++)
			{
				Point pt = (Point)VecEff.elementAt(j);
				if (pt.obj != null)
				{
					for (short n = 0; n < frame - 1; n++)
					{
						if (pt.f == mframe[n])
						{
							GameScreen.addHightDataeff((short)(n + 19), pt.obj.x, pt.obj.y);
							if (n == 1)
							{
								GameScreen.addEffectEnd(110, 0, pt.obj.x + CRes.random_Am_0(15), pt.obj.y + CRes.random_Am_0(5), Dir, pt.obj);
								GameScreen.addEffectEnd(112, 0, pt.obj.x, pt.obj.y, Dir, pt.obj);
								LoadMap.timeVibrateScreen = CRes.random(1, 5);
							}
							if (n == 3)
							{
								LoadMap.timeVibrateScreen = CRes.random(6, 20);
								GameScreen.addEffectEnd(112, 0, pt.obj.x, pt.obj.y, Dir, pt.obj);
							}
						}
					}
					if (mframe != null && mframe.Length > 1 && pt.f > mframe[1])
					{
						if (pt.f % 3 == 0)
						{
							GameScreen.addEffectEnd(108, 5, pt.obj.x + CRes.random_Am_0(10), pt.obj.y - CRes.random(240), Dir, pt.obj);
						}
						if (frame >= 2 && pt.f == mframe[frame - 2] + 5)
						{
							GameScreen.addEffectEnd(108, 5, pt.obj.x + CRes.random_Am_0(60), pt.obj.y - CRes.random(30), Dir, pt.obj);
						}
					}
				}
				pt.f++;
				if (pt.f >= pt.fRe)
				{
					VecEff.removeElementAt(j);
					j--;
				}
			}
			if (VecEff.size() == 0)
			{
				removeEff();
			}
			break;
		}
		case 406:
		{
			// Boa Hancock Skill 1 Level 5 (Upgraded Mero Mero Mellow)
			if (f > fRemove)
			{
				removeEff();
			}
			MainObject target406 = (objBeFireMain != null) ? objBeFireMain :
				(vecObjsBeFire != null && vecObjsBeFire.size() > 0 ?
					MainObject.get_Object(((Object_Effect_Skill)vecObjsBeFire.elementAt(0)).ID, ((Object_Effect_Skill)vecObjsBeFire.elementAt(0)).tem) : null);
			if (target406 != null)
			{
				if (mframe != null && mframe.Length > 0 && f == mframe[0])
				{
					for (short n = 17; n < 19; n++)
					{
						GameScreen.addHightDataeff(n, target406.x, target406.y);
					}
				}
				if (mframe != null && mframe.Length > 1 && f == mframe[1])
				{
					GameScreen.addHightDataeff(38, target406.x, target406.y);
				}
				if (mframe != null && mframe.Length > 2 && f == mframe[2])
				{
					GameScreen.addHightDataeff(39, target406.x, target406.y);
					GameScreen.addHightDataeff(40, target406.x, target406.y);
				}
				if (mframe != null && mframe.Length > 0 && f == mframe[0] + 7)
				{
					GameScreen.addEffectEnd(108, 5, target406.x + CRes.random_Am_0(60), target406.y - CRes.random(30), Dir, target406);
					LoadMap.timeVibrateScreen = CRes.random(6, 20);
					GameScreen.addEffectEnd(112, 0, target406.x, target406.y, Dir, target406);
				}
			}
			break;
		}
		case 407:
		{
			// Boa Hancock Skill 2 Level 5 (Upgraded Slave Arrow)
			for (int j = 0; j < VecEff.size(); j++)
			{
				Point pt = (Point)VecEff.elementAt(j);
				if (pt.obj != null)
				{
					for (short n = 0; n < frame - 1; n++)
					{
						if (pt.f == mframe[n])
						{
							GameScreen.addHightDataeff((short)(n + 46), pt.obj.x, pt.obj.y);
							if (n == 1)
							{
								GameScreen.addEffectEnd(110, 0, pt.obj.x + CRes.random_Am_0(15), pt.obj.y + CRes.random_Am_0(5), Dir, pt.obj);
								GameScreen.addEffectEnd(112, 0, pt.obj.x, pt.obj.y, Dir, pt.obj);
								LoadMap.timeVibrateScreen = CRes.random(1, 5);
							}
							if (n == 3)
							{
								LoadMap.timeVibrateScreen = CRes.random(6, 20);
								GameScreen.addEffectEnd(112, 0, pt.obj.x, pt.obj.y, Dir, pt.obj);
							}
						}
					}
					if (mframe != null && mframe.Length > 1 && pt.f > mframe[1])
					{
						if (pt.f % 3 == 0)
						{
							GameScreen.addEffectEnd(108, 5, pt.obj.x + CRes.random_Am_0(10), pt.obj.y - CRes.random(240), Dir, pt.obj);
						}
						if (frame >= 2 && pt.f == mframe[frame - 2] + 5)
						{
							GameScreen.addEffectEnd(108, 5, pt.obj.x + CRes.random_Am_0(60), pt.obj.y - CRes.random(30), Dir, pt.obj);
						}
					}
				}
				pt.f++;
				if (pt.f >= pt.fRe)
				{
					VecEff.removeElementAt(j);
					j--;
				}
			}
			if (VecEff.size() == 0)
			{
				removeEff();
			}
			break;
		}
		case 408:
		{
			// Kizaru Skill 1 Level 1..4 (Yasakani no Magatama)
			if (f > fRemove)
			{
				removeEff();
			}
			if (f > frame && VecEff != null)
			{
				for (int idx = 0; idx < VecEff.size(); idx++)
				{
					Point pt = (Point)VecEff.elementAt(idx);
					pt.x += (pt.dir - 1) * 20;
					short num3 = (short)((pt.frame == 0) ? 36 : 34);
					if (pt.dir > 0)
					{
						GameScreen.addHightDataeff(num3, pt.x, pt.y);
					}
					else
					{
						GameScreen.addHightDataeff(num3, pt.x, pt.y, changeFlip: true);
					}
					if (pt.f == pt.fRe - 2)
					{
						if (pt.obj != null)
						{
							pt.obj.x = pt.x + (pt.dir - 1) * 10;
							GameScreen.addHightDataeff(33, pt.obj.x, pt.obj.y);
							GameScreen.addEffectEnd(112, 0, pt.obj.x, pt.obj.y, Dir, pt.obj);
							setAva(2, pt.obj);
						}
					}
					if (pt.f == pt.fRe)
					{
						GameScreen.addHightDataeff(28, pt.x, pt.y - 40);
						GameScreen.addHightDataeff(28, pt.x, pt.y);
						VecEff.removeElementAt(idx);
						idx--;
					}
					else
					{
						pt.f++;
					}
				}
			}
			break;
		}
		case 409:
		{
			// Kizaru Skill 2 Level 1..4 (Yata no Kagami)
			if (f > fRemove)
			{
				removeEff();
			}
			for (int i = 0; i < frame; i++)
			{
				if (f == mframe[i])
				{
					objFireMain.addDataEff((short)(i + 30), 0);
					if (i == 0)
					{
						LoadMap.timeVibrateScreen = CRes.random(1, 5);
						GameScreen.addEffectEnd(112, 0, objFireMain.x, objFireMain.y, Dir, objFireMain);
						int dirFactor = ((objFireMain.type_left_right == 2) ? 1 : (-1));
						for (int targetIdx = 0; targetIdx < vecObjsBeFire.size(); targetIdx++)
						{
							Object_Effect_Skill objEff = (Object_Effect_Skill)vecObjsBeFire.elementAt(targetIdx);
							if (objEff != null)
							{
								MainObject target = MainObject.get_Object(objEff.ID, objEff.tem);
								if (target != null)
								{
									GameScreen.addHightDataeff(33, target.x, target.y);
									GameScreen.addEffectEnd(112, 0, target.x, target.y, Dir, target);
									target.x += dirFactor * 20;
									setAva(2, target);
								}
							}
						}
					}
					if (i == frame - 1)
					{
						objFireMain.addDataEff(32, 0);
						LoadMap.timeVibrateScreen = CRes.random(1, 5);
					}
				}
			}
			break;
		}
		case 410:
		{
			// Kizaru Skill 1 Level 5 (Upgraded Yasakani no Magatama)
			if (f > fRemove)
			{
				removeEff();
			}
			if (f > frame && VecEff != null)
			{
				for (int idx = 0; idx < VecEff.size(); idx++)
				{
					Point pt = (Point)VecEff.elementAt(idx);
					pt.x += (pt.dir - 1) * 20;
					short num54 = (short)((pt.frame == 0) ? 35 : 36);
					if (pt.dir > 0)
					{
						GameScreen.addHightDataeff(num54, pt.x, pt.y);
					}
					else
					{
						GameScreen.addHightDataeff(num54, pt.x, pt.y, changeFlip: true);
					}
					if (pt.f == pt.fRe - 2)
					{
						if (pt.obj != null)
						{
							pt.obj.x = pt.x + (pt.dir - 1) * 10;
							GameScreen.addHightDataeff(33, pt.obj.x, pt.obj.y);
							GameScreen.addEffectEnd(112, 0, pt.obj.x, pt.obj.y, Dir, pt.obj);
							setAva(2, pt.obj);
						}
					}
					if (pt.f == pt.fRe)
					{
						int offset = (pt.dir - 1) * 15;
						GameScreen.addHightDataeff(28, pt.x, pt.y - 40);
						GameScreen.addHightDataeff(28, pt.x, pt.y);
						GameScreen.addHightDataeff(28, pt.x + offset, pt.y - 60);
						GameScreen.addHightDataeff(28, pt.x + offset, pt.y + 20);
						VecEff.removeElementAt(idx);
						idx--;
					}
					else
					{
						pt.f++;
					}
				}
			}
			break;
		}
		case 411:
		{
			// Kizaru Skill 2 Level 5 (Upgraded Yata no Kagami)
			if (f > fRemove)
			{
				removeEff();
			}
			for (int i = 0; i < frame; i++)
			{
				if (f == mframe[i])
				{
					objFireMain.addDataEff((short)(i + 42), 0);
					if (i == 0)
					{
						LoadMap.timeVibrateScreen = CRes.random(1, 5);
						GameScreen.addEffectEnd(112, 0, objFireMain.x, objFireMain.y, Dir, objFireMain);
						int dirFactor = ((objFireMain.type_left_right == 2) ? 1 : (-1));
						for (int targetIdx = 0; targetIdx < vecObjsBeFire.size(); targetIdx++)
						{
							Object_Effect_Skill objEff = (Object_Effect_Skill)vecObjsBeFire.elementAt(targetIdx);
							if (objEff != null)
							{
								MainObject target = MainObject.get_Object(objEff.ID, objEff.tem);
								if (target != null)
								{
									GameScreen.addHightDataeff(45, target.x, target.y);
									GameScreen.addEffectEnd(112, 0, target.x, target.y, Dir, target);
									target.x += dirFactor * 20;
									setAva(2, target);
								}
							}
						}
					}
					if (i == frame - 1)
					{
						objFireMain.addDataEff(44, 0);
						LoadMap.timeVibrateScreen = CRes.random(1, 5);
					}
				}
			}
			break;
		}
		}
	}

	public void updateAngleXP()
	{
		if (typeEffect == -1)
		{
			Point point = new Point();
			point.x = x;
			point.y = y;
			VecEff.addElement(point);
		}
		if (objBeFireMain == null || objBeFireMain.isRemove || f >= fRemove || objBeFireMain.isStop)
		{
			f = fRemove;
			return;
		}
		int num = objBeFireMain.x - x;
		int num2 = objBeFireMain.y - (objBeFireMain.hOne >> 1) - y;
		life++;
		if ((CRes.abs(num) < 16 && CRes.abs(num2) < 16) || life > fRemove)
		{
			f = fRemove;
			return;
		}
		int num3 = CRes.angle(num, num2);
		if (CRes.abs(num3 - gocT_Arc) < 90 || num * num + num2 * num2 > 4096)
		{
			if (CRes.abs(num3 - gocT_Arc) < 15)
			{
				gocT_Arc = num3;
			}
			else if ((num3 - gocT_Arc >= 0 && num3 - gocT_Arc < 180) || num3 - gocT_Arc < -180)
			{
				gocT_Arc = CRes.fixangle(gocT_Arc + 15);
			}
			else
			{
				gocT_Arc = CRes.fixangle(gocT_Arc - 15);
			}
		}
		if (va < 8192)
		{
			va += 3096;
		}
		vX1000 = va * CRes.getcos(gocT_Arc) >> 10;
		vY1000 = va * CRes.getsin(gocT_Arc) >> 10;
		num += vX1000;
		int num4 = num >> 10;
		x += num4;
		num &= 0x3FF;
		num2 += vY1000;
		int num5 = num2 >> 10;
		y += num5;
		num2 &= 0x3FF;
		if (typeEffect != -1)
		{
			Point point2 = new Point();
			point2.x = x;
			point2.y = y;
			VecEff.addElement(point2);
		}
	}

	private void update_Tru_2()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.f++;
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElementAt(i);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(25, 4, point_Focus.x, point_Focus.y, (sbyte)point_Focus.dis, objMainEff);
				VecEff.removeElementAt(j);
				j--;
			}
			else
			{
				Point point2 = new Point(point_Focus.x, point_Focus.y);
				point2.fRe = 5;
				VecSubEff.addElement(point2);
			}
		}
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Dong_Dat_2()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.f++;
			if (point.frame == 1 && point.f >= 6)
			{
				point.frame = 0;
				point.f = 0;
			}
			if (point.f / 3 >= 4)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		if (f >= 12 && f % 2 == 0 && f <= 30)
		{
			int num = (f - 12) / 4;
			int num2 = 4 + num;
			int num3 = 30 + 35 * num;
			int num4 = 100 / num2;
			if (f % 4 == 0)
			{
				int num5 = 180 + num2 * num4 / 2;
				for (int j = 0; j < num2; j++)
				{
					Point point2 = new Point();
					point2.x = x + CRes.getcos(CRes.fixangle(num5)) * num3 / 1000;
					point2.y = y + CRes.getsin(CRes.fixangle(num5)) * num3 / 1000;
					if (typeEffect == 244)
					{
						point2.frame = 1;
					}
					int tile = GameCanvas.loadmap.getTile(point2.x, point2.y);
					if (tile == 0 || tile == 2)
					{
						VecSubEff.addElement(point2);
						if (j % 2 == 0)
						{
							GameScreen.addEffectEnd(110, 0, point2.x, point2.y, Dir, objMainEff);
						}
						GameScreen.addEffectEnd(63, 0, point2.x, point2.y + 5, Dir, objMainEff);
					}
					num5 -= num4;
				}
			}
			if (f % 4 == 2)
			{
				num3 += 15;
				int num6 = 360 - num2 * num4 / 2;
				for (int k = 0; k < num2; k++)
				{
					Point point3 = new Point();
					point3.x = x + CRes.getcos(CRes.fixangle(num6)) * num3 / 1000;
					point3.y = y + CRes.getsin(CRes.fixangle(num6)) * num3 / 1000;
					if (typeEffect == 244)
					{
						point3.frame = 1;
					}
					int tile2 = GameCanvas.loadmap.getTile(point3.x, point3.y);
					if (tile2 == 0 || tile2 == 2)
					{
						VecSubEff.addElement(point3);
						if (k % 2 == 0)
						{
							GameScreen.addEffectEnd(110, 0, point3.x, point3.y, Dir, objMainEff);
						}
						GameScreen.addEffectEnd(63, 0, point3.x, point3.y + 5, Dir, objMainEff);
					}
					num6 += num4;
				}
			}
			if (isAddSound && f % 4 == 0)
			{
				mSound.playSound(41, mSound.volumeSound);
			}
		}
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Ace_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				if (CRes.random(2) == 0)
				{
					addSound(5);
				}
				VecEff.removeElement(point);
				GameScreen.addEffectEnd(2, 0, point.x, point.y, Dir, objMainEff);
				if (CRes.random(4) == 0)
				{
					GameScreen.addEffectEnd(110, 1, point.x, point.y, Dir, objMainEff);
					GameScreen.addEffectEnd(63, 0, point.x, point.y + 5, Dir, objMainEff);
				}
				i--;
			}
		}
		if (f >= 7 && f <= 12 && !checkNullObject(1))
		{
			objFireMain.dy = (f - 6) * 30;
		}
		if (f >= 14 && f <= 26)
		{
			if (f == 14 && !checkNullObject(3))
			{
				toY = objBeFireMain.y;
				toX = objBeFireMain.x;
				x1000 = objFireMain.x;
				y1000 = objFireMain.y;
			}
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = true;
			}
			if (f > 15 && f < 26 && f % 5 == 0)
			{
				addSound(15);
			}
			if (f > 15 && f % 2 == 1 && f <= 25)
			{
				int num = CRes.random(2, 4);
				for (int j = 0; j < num; j++)
				{
					Point point2 = new Point();
					point2.vy = CRes.random(25, 35);
					point2.x = toX + CRes.random_Am(0, 50);
					point2.y = toY - point2.vy * 3 + CRes.random_Am(0, 20) + 5;
					point2.frame = CRes.random(fraImgEff.maxNumFrame);
					point2.fRe = 3;
					VecEff.addElement(point2);
				}
			}
			if (f % 6 == 1)
			{
				setAva(0, objBeFireMain);
				addVir(1, 6, 12, isPlayer: false);
			}
		}
		if (f == 27 && !checkNullObject(1))
		{
			objFireMain.dy = 80;
			objFireMain.isTanHinh = false;
		}
		if (f > 27 && f <= 30 && !checkNullObject(1))
		{
			objFireMain.dy = 80 - (f - 27) * 20;
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
	}

	private void update_Ace_1_L2()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.f++;
			point.y += point.vy;
			if (point.f >= fraImgSub2Eff.maxNumFrame)
			{
				VecSubEff.removeElementAt(i);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point point2 = (Point)VecEff.elementAt(j);
			Point point3 = new Point(point2.x, point2.y - 30);
			point3.vy = -4;
			VecSubEff.addElement(point3);
			point2.update();
			if (point2.f < point2.fRe)
			{
				continue;
			}
			if (CRes.random(2) == 0)
			{
				addSound(5);
			}
			VecEff.removeElement(point2);
			if (CRes.random(2) == 0)
			{
				sbyte subtype = (sbyte)(frameSuper + 1);
				if (typeEffect == 228)
				{
					subtype = 0;
				}
				GameScreen.addEffectEnd(141, subtype, point2.x, point2.y, Dir, objMainEff);
			}
			else
			{
				GameScreen.addEffectEnd(2, 0, point2.x, point2.y, Dir, objMainEff);
			}
			if (CRes.random(2) == 0)
			{
				GameScreen.addEffectEnd(110, 1, point2.x, point2.y, Dir, objMainEff);
				GameScreen.addEffectEnd(63, 0, point2.x, point2.y + 5, Dir, objMainEff);
			}
			j--;
		}
		if (f >= 4 && f <= 26)
		{
			objFireMain.isPaintLeg = false;
		}
		if (f >= 7 && f <= 12 && !checkNullObject(1))
		{
			objFireMain.dy = (f - 6) * 40;
		}
		if (f >= 14 && f <= 26)
		{
			if (f == 14 && !checkNullObject(3))
			{
				toY = objBeFireMain.y;
				toX = objBeFireMain.x;
				x1000 = objFireMain.x;
				y1000 = objFireMain.y;
			}
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = true;
			}
			if (f > 15 && f < 26 && f % 5 == 0)
			{
				addSound(15);
			}
			if (f > 15 && f % 2 == 1 && f <= 25)
			{
				int num = CRes.random(2, 4);
				for (int k = 0; k < num; k++)
				{
					Point point4 = new Point();
					point4.vy = CRes.random(25, 35);
					point4.x = toX + CRes.random_Am(0, 50);
					point4.y = toY - point4.vy * 3 + CRes.random_Am(0, 20) + 5;
					point4.frame = CRes.random(fraImgEff.maxNumFrame);
					point4.fRe = 3;
					VecEff.addElement(point4);
				}
			}
			if (f % 6 == 1)
			{
				setAva(0, objBeFireMain);
				addVir(1, 6, 12, isPlayer: false);
			}
		}
		if (f == 27 && !checkNullObject(1))
		{
			objFireMain.dy = 80;
			objFireMain.isTanHinh = false;
			objFireMain.isPaintLeg = true;
		}
		if (f > 27 && f <= 30 && !checkNullObject(1))
		{
			objFireMain.dy = 80 - (f - 27) * 20;
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
	}

	private void update_Ace_2()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.frame == 0)
			{
				if (point.f / 2 >= 5)
				{
					VecSubEff.removeElement(point);
					i--;
				}
			}
			else if (point.f >= point.fRe)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point point2 = (Point)VecEff.elementAt(j);
			point2.update();
			if (point2.frame == 1 && f % 4 == 0)
			{
				Point point3 = new Point();
				point3.x = point2.x / 1000;
				point3.y = point2.y / 1000;
				point3.frame = 1;
				point3.fRe = CRes.random(8, 10);
				point3.f = CRes.random(3);
				point3.fRe += point3.f;
				VecSubEff.addElement(point3);
			}
			if (point2.f >= point2.fRe)
			{
				addSound(15);
				VecEff.removeElement(point2);
				j--;
			}
		}
		if (f <= 10 && CRes.random(2) == 0)
		{
			Point point4 = new Point();
			point4.x = x + CRes.random_Am_0(10);
			point4.y = y - 10 - CRes.random(20);
			point4.vx = CRes.random_Am_0(3);
			point4.vy = -CRes.random(3, 7);
			VecSubEff.addElement(point4);
		}
		if (f == 15)
		{
			addSound(15);
			int num = 12;
			if (typeEffect != 3)
			{
				num = 16;
			}
			int num2 = 0;
			for (int k = 0; k < num; k++)
			{
				num2 %= 360;
				Point point5 = new Point(x * 1000, y * 1000);
				point5.vx = CRes.getcos(num2) * vMax;
				point5.vy = CRes.getsin(num2) * (vMax / 2);
				point5.fRe = 7;
				point5.frame = 0;
				VecEff.addElement(point5);
				num2 += 360 / num;
			}
		}
		if (f == 20)
		{
			addSound(15);
			addVir(1, 6, 12, isPlayer: true);
			int num3 = 15;
			int num4 = 16;
			if (typeEffect != 3)
			{
				num4 = 20;
			}
			for (int l = 0; l < num4; l++)
			{
				num3 %= 360;
				Point point6 = new Point(x * 1000, y * 1000);
				point6.vx = CRes.getcos(num3) * vMax;
				point6.vy = CRes.getsin(num3) * (vMax / 2);
				point6.fRe = 12;
				point6.fSmall = CRes.random(fraImgEff.maxNumFrame);
				point6.frame = 1;
				VecEff.addElement(point6);
				num3 += 360 / num4;
			}
		}
		if (typeEffect != 3 && f == 23)
		{
			addSound(15);
			addVir(1, 6, 12, isPlayer: true);
			int num5 = 30;
			int num6 = 24;
			for (int m = 0; m < num6; m++)
			{
				num5 %= 360;
				Point point7 = new Point(x * 1000, y * 1000);
				point7.vx = CRes.getcos(num5) * vMax;
				point7.vy = CRes.getsin(num5) * (vMax / 2);
				point7.fRe = 16;
				point7.fSmall = CRes.random(fraImgEff.maxNumFrame);
				point7.frame = 1;
				VecEff.addElement(point7);
				num5 += 360 / num6;
			}
		}
		if (f == 26)
		{
			for (int n = 0; n < vecObjsBeFire.size(); n++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(n);
				if (object_Effect_Skill != null)
				{
					MainObject obj = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					setAva(1, obj);
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0 && VecSubEff.size() == 0)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
	}

	private void update_Blackhole()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				addSound(15);
				VecEff.removeElement(point);
				i--;
			}
		}
		if (f == 7)
		{
			addSound(15);
			int num = (typeEffect == 402) ? 16 : 12;
			int num2 = 0;
			for (int j = 0; j < VecSubEff.size(); j++)
			{
				Point point2 = (Point)VecSubEff.elementAt(j);
				for (int k = 0; k < num; k++)
				{
					num2 %= 360;
					Point point3 = new Point(point2.x * 1000, point2.y * 1000);
					point3.vx = CRes.getcos(num2) * vMax;
					point3.vy = CRes.getsin(num2) * (vMax / 2);
					point3.fRe = 7;
					point3.frame = 0;
					VecEff.addElement(point3);
					num2 += 360 / num;
				}
			}
		}
		if (f == 12)
		{
			addSound(15);
			addVir(1, 6, 12, isPlayer: true);
			int num3 = 15;
			int num4 = (typeEffect == 402) ? 20 : 16;
			for (int l = 0; l < VecSubEff.size(); l++)
			{
				Point point4 = (Point)VecSubEff.elementAt(l);
				for (int m = 0; m < num4; m++)
				{
					num3 %= 360;
					Point point5 = new Point(point4.x * 1000, point4.y * 1000);
					point5.vx = CRes.getcos(num3) * vMax;
					point5.vy = CRes.getsin(num3) * (vMax / 2);
					point5.fRe = 12;
					point5.fSmall = CRes.random(fraImgEff.maxNumFrame);
					point5.frame = 1;
					VecEff.addElement(point5);
					num3 += 360 / num4;
				}
			}
		}
		if (f == 26)
		{
			for (int n = 0; n < vecObjsBeFire.size(); n++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(n);
				if (object_Effect_Skill != null)
				{
					MainObject obj = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					setAva(1, obj);
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
	}

	private void create_Ice_Arc()
	{
		fRemove = 25;
		vMax = 5;
		for (int i = 0; i < 16; i++)
		{
			Point point = new Point();
			point.x = x * 1000;
			point.y = y * 1000;
			point.vx = 2 * CRes.getcos(225 * i / 10) * vMax;
			point.vy = CRes.getsin(225 * i / 10) * vMax;
			point.f = 0;
			VecEff.addElement(point);
		}
	}

	private void update_ho_den_vu_tru()
	{
		CR += 5;
		t2++;
		if (t2 % 2 == 0)
		{
			for (int i = 0; i < radian.Length; i++)
			{
				GameScreen.addEffectEnd(166, 0, 2 * (CRes.getcos(radian[i]) * CR) / 1024 + x, CRes.getsin(radian[i]) * CR / 1024 + y, Dir, objMainEff);
				radian[i] += 15;
				if (radian[i] > 360)
				{
					radian[i] = (radian[i] = 360);
				}
			}
		}
		Ctick++;
		if (Ctick % 5 == 0)
		{
			removeEff();
		}
	}

	private void update_Aokiji_1()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point point2 = (Point)VecEff.elementAt(j);
			if (point2.f >= point2.fSmall)
			{
				if (typeEffect == 230)
				{
					Point point3 = new Point(point2.x, point2.y - 25);
					point3.vy = -4;
					point3.f = CRes.random(2);
					point3.fRe = 6;
					VecSubEff.addElement(point3);
				}
				point2.update();
			}
			else
			{
				point2.f++;
			}
			if (point2.frame == 0)
			{
				if (point2.f >= point2.fRe)
				{
					point2.vy = 0;
					point2.frame = 1;
					point2.fRe = CRes.random(10, 12);
					point2.f = 0;
					GameScreen.addEffectEnd(17, CRes.random(20, 30), point2.x, point2.y, Dir, objMainEff);
					if (CRes.random(2) == 0)
					{
						GameScreen.addEffectEnd(110, 2, point2.x, point2.y, Dir, objMainEff);
					}
				}
			}
			else if (point2.frame == 1 && point2.f == point2.fRe)
			{
				GameScreen.addEffectEnd(14, 0, point2.x, point2.y, Dir, objMainEff);
				VecEff.removeElement(point2);
				j--;
			}
		}
		if (f <= 25 && CRes.random(4) == 0)
		{
			Point point4 = new Point();
			point4.x = x + CRes.random_Am_0(15);
			point4.y = y - CRes.random(20);
			point4.vx = CRes.random_Am_0(3);
			point4.vy = -CRes.random(3, 7);
			point4.fRe = 10;
			VecSubEff.addElement(point4);
		}
		if (typeEffect == 230 && f >= 16 && f < 20)
		{
			Point point5 = new Point();
			point5.vy = CRes.random(30, 40);
			point5.dis = CRes.random(25, 35);
			point5.fSmall = 10;
			point5.x = objFireMain.x + CRes.random_Am_0(MotherCanvas.w / 2);
			point5.y = objFireMain.y - point5.vy * 4 - 60;
			point5.frame = 0;
			point5.fRe = 5 + point5.fSmall + CRes.random(3);
			VecEff.addElement(point5);
		}
		if (f == 20)
		{
			addSound(15);
			for (int k = 0; k < vecObjsBeFire.size(); k++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point point6 = new Point();
						point6.vy = CRes.random(30, 40);
						point6.dis = CRes.random(25, 35);
						point6.fRe = 4;
						point6.x = mainObject.x;
						point6.y = mainObject.y - point6.vy * point6.fRe + CRes.random(5);
						point6.frame = 0;
						VecEff.addElement(point6);
					}
				}
			}
			if (!checkNullObject(1))
			{
				for (int l = 0; l < 5; l++)
				{
					Point point7 = new Point();
					point7.vy = CRes.random(30, 40);
					point7.dis = CRes.random(25, 35);
					point7.fSmall = l * 3;
					point7.x = objFireMain.x + CRes.random_Am_0(MotherCanvas.w / 2);
					point7.y = objFireMain.y - point7.vy * 4 - 60;
					point7.frame = 0;
					point7.fRe = 5 + point7.fSmall + CRes.random(3);
					VecEff.addElement(point7);
				}
			}
		}
		if (f == 24)
		{
			addVir(1, 6, 12, isPlayer: true);
			for (int m = 0; m < vecObjsBeFire.size(); m++)
			{
				Object_Effect_Skill object_Effect_Skill2 = (Object_Effect_Skill)vecObjsBeFire.elementAt(m);
				if (object_Effect_Skill2 != null)
				{
					MainObject obj = MainObject.get_Object(object_Effect_Skill2.ID, object_Effect_Skill2.tem);
					setAva(1, obj);
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Aokiji_2()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f / 2 >= 5)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			Point point2 = new Point(point_Focus.x, point_Focus.y);
			point2.vx = CRes.random_Am_0(3);
			point2.vy = -CRes.random(3, 7);
			VecSubEff.addElement(point2);
			if (point_Focus.f != point_Focus.fRe)
			{
				continue;
			}
			if (!checkNullObject(2))
			{
				setAva(1, objBeFireMain);
			}
			if (typeEffect == 231)
			{
				if (!checkNullObject(3) && MainObject.getDistance(objFireMain.x, objFireMain.y, objBeFireMain.x, objBeFireMain.y) < 60)
				{
					GameScreen.addEffectEnd(142, 1, point_Focus.x - 30, point_Focus.y + 8, Dir, objMainEff);
					GameScreen.addEffectEnd(142, 1, point_Focus.x + 30, point_Focus.y + 8, Dir, objMainEff);
				}
				else
				{
					int num = 1;
					if (point_Focus.vy < 0)
					{
						num = -1;
					}
					if (point_Focus.vy == 0)
					{
						num = 0;
					}
					GameScreen.addEffectEnd(142, 0, point_Focus.x - 40 * am_duong, point_Focus.y + 8 - num * 15, Dir, objMainEff);
					GameScreen.addEffectEnd(142, 1, point_Focus.x - 20 * am_duong, point_Focus.y + 8 - num * 7, Dir, objMainEff);
				}
			}
			GameScreen.addEffectEnd(88, 0, point_Focus.x, point_Focus.y + 8, Dir, objMainEff);
			addVir(2, 6, 12, isPlayer: true);
			VecEff.removeElement(point_Focus);
			j--;
		}
		if (f <= 15 && CRes.random(4) == 0)
		{
			Point point3 = new Point();
			point3.x = x + CRes.random_Am_0(15);
			point3.y = y - CRes.random(20);
			point3.vx = CRes.random_Am_0(3);
			point3.vy = -CRes.random(3, 7);
			VecSubEff.addElement(point3);
		}
		if (f == 18)
		{
			if (!checkNullObject(2))
			{
				toX = objBeFireMain.x;
				toY = objBeFireMain.y;
			}
			Point_Focus p = new Point_Focus();
			int xdich = toX - (x + am_duong * 75);
			int ydich = toY - y;
			p = create_Speed(xdich, ydich, p, x + am_duong * 75, y, toX, toY);
			VecEff.addElement(p);
			addVir(2, 6, 12, isPlayer: true);
			GameScreen.addEffectEnd(110, 2, p.x, p.y, Dir, objMainEff);
			GameScreen.addEffectEnd(110, 2, p.x, p.y, Dir, objMainEff);
		}
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Smoker_1()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			if (typeEffect == 232 && f % 2 == 0 && !GameCanvas.lowGraphic)
			{
				Point point2 = new Point();
				point2.x = point_Focus.x;
				point2.y = point_Focus.y;
				point2.fRe = 7;
				point2.frame = 1;
				VecSubEff.addElement(point2);
			}
			Point point3 = new Point();
			point3.x = point_Focus.x + CRes.random_Am_0(5);
			point3.y = point_Focus.y;
			point3.vx = 0;
			point3.vy = -CRes.random(1, 4);
			point3.fRe = CRes.random(4, 7);
			VecSubEff.addElement(point3);
			point_Focus.frame++;
			if (point_Focus.f >= point_Focus.fRe)
			{
				point_Focus.vx = 0;
				point_Focus.vy = 0;
				point_Focus.x = point_Focus.toX;
				point_Focus.y = point_Focus.toY;
			}
			else if (point_Focus.frame / 2 > fraImgSubEff.nFrame - 1)
			{
				point_Focus.frame = (fraImgSubEff.nFrame - 1) * 2;
			}
			if (point_Focus.f >= point_Focus.fRe && point_Focus.frame >= fraImgSubEff.nFrame)
			{
				if (!checkNullObject(2))
				{
					setAva(2, objBeFireMain);
				}
				addSound(15);
				GameScreen.addEffectEnd(18, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 4, point_Focus.x, point_Focus.y, Dir, objMainEff);
				GameScreen.addEffectEnd(54, 3, toX, toY, Dir, objMainEff);
				if (typeEffect == 232)
				{
					GameScreen.addEffectEnd(146, 0, toX, toY, Dir, objMainEff);
				}
				VecEff.removeElement(point_Focus);
				j--;
			}
		}
		if (f <= 10 || (f > 20 && f < 26))
		{
			if (CRes.random(4) == 0)
			{
				Point point4 = new Point();
				point4.x = x + CRes.random_Am_0(15);
				point4.y = y + CRes.random(20);
				point4.vx = CRes.random_Am_0(3);
				point4.vy = -CRes.random(3, 7);
				point4.fRe = CRes.random(6, 10);
				VecSubEff.addElement(point4);
			}
		}
		else if (f <= 20 && CRes.random(2) == 0)
		{
			Point point5 = new Point();
			point5.x = x + CRes.random_Am_0(15) - am_duong * 10;
			point5.y = y + CRes.random(20);
			point5.vx = CRes.random_Am_0(4);
			point5.vy = -CRes.random(4, 8);
			point5.fRe = CRes.random(8, 14);
			VecSubEff.addElement(point5);
		}
		if (f == 24)
		{
			addSound(32);
			if (!checkNullObject(2))
			{
				toX = objBeFireMain.x;
				toY = objBeFireMain.y - objBeFireMain.hOne / 2;
			}
			Point_Focus p = new Point_Focus();
			int xdich = toX - x;
			int ydich = toY - y;
			p = create_Speed(xdich, ydich, p, x, y, toX, toY);
			VecEff.addElement(p);
			addVir(5, 6, 12, isPlayer: true);
		}
		if (f >= fRemove && VecEff.size() == 0 && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Smoker_2()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			if (typeEffect == 234 && !GameCanvas.lowGraphic)
			{
				Point point2 = new Point();
				point2.x = point_Focus.x;
				point2.y = point_Focus.y;
				point2.fRe = 3;
				point2.frame = 1;
				VecSubEff.addElement(point2);
			}
			Point point3 = new Point();
			point3.x = point_Focus.x;
			point3.y = point_Focus.y;
			point3.vx = 0;
			point3.vy = -CRes.random(1, 3);
			point3.fRe = CRes.random(3, 6);
			VecSubEff.addElement(point3);
			if (point_Focus.f >= point_Focus.fRe)
			{
				addVir(5, 6, 12, isPlayer: true);
				LoadMap.timeVibrateScreen = CRes.random(6, 12);
				GameScreen.addEffectEnd(18, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				GameScreen.addEffectEnd(63, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				GameScreen.addEffectEnd(110, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				j--;
			}
		}
		if (f <= 5 && CRes.random(3) == 0)
		{
			Point point4 = new Point();
			point4.x = x + CRes.random_Am_0(15);
			point4.y = y + CRes.random(20);
			point4.vx = CRes.random_Am_0(3);
			point4.vy = -CRes.random(3, 7);
			point4.fRe = CRes.random(6, 10);
			VecSubEff.addElement(point4);
		}
		if (f == 8 && !checkNullObject(1))
		{
			objFireMain.isPaintLeg = false;
		}
		if (f == 10 && !checkNullObject(1))
		{
			objFireMain.isTanHinh = true;
		}
		if (f == 15 || f == 20 || ((f == 12 || f == 17) && typeEffect == 234 && !GameCanvas.lowGraphic))
		{
			addSound(15);
			int num = x - am_duong * 40 + CRes.random_Am_0(30);
			int num2 = y - 160 + CRes.random_Am_0(20);
			for (int k = 0; k < vecObjsBeFire.size(); k++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus p = new Point_Focus();
						int num3 = CRes.random_Am_0(15);
						int num4 = CRes.random_Am_0(10);
						int xdich = mainObject.x + num3 - num;
						int ydich = mainObject.y + num4 - num2;
						p = create_Speed(xdich, ydich, p, num, num2, mainObject.x + num3, mainObject.y + num4);
						VecEff.addElement(p);
					}
				}
			}
		}
		if (f == 26 && !checkNullObject(1))
		{
			objFireMain.dy = 0;
			objFireMain.isTanHinh = false;
			objFireMain.isPaintLeg = true;
		}
		if (f >= fRemove && VecEff.size() == 0 && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Mon_Smoker_1()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			Point point2 = new Point();
			point2.x = point_Focus.x;
			point2.y = point_Focus.y;
			point2.vx = 0;
			point2.vy = -CRes.random(1, 3);
			point2.fRe = CRes.random(3, 6);
			VecSubEff.addElement(point2);
			if (point_Focus.f >= point_Focus.fRe)
			{
				addSound(5);
				addVir(5, 5, 10, isPlayer: false);
				GameScreen.addEffectEnd(18, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				j--;
			}
		}
		if (f <= 7 && CRes.random(4) == 0)
		{
			Point point3 = new Point();
			point3.x = x + CRes.random_Am_0(15);
			point3.y = y + CRes.random(20);
			point3.vx = CRes.random_Am_0(3);
			point3.vy = -CRes.random(3, 7);
			point3.fRe = CRes.random(6, 10);
			VecSubEff.addElement(point3);
		}
		if (f == 9 && !checkNullObject(1))
		{
			objFireMain.isPaintLeg = false;
		}
		if (f == 11)
		{
			addSound(3);
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = true;
			}
		}
		if (f == 15)
		{
			int num = y - 160 + CRes.random_Am_0(20);
			for (int k = 0; k < vecObjsBeFire.size(); k++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus p = new Point_Focus();
						int num2 = CRes.random_Am_0(15);
						int num3 = CRes.random_Am_0(10);
						int xdich = mainObject.x + num2 - mainObject.x;
						int ydich = mainObject.y + num3 - num;
						p = create_Speed(xdich, ydich, p, mainObject.x, num, mainObject.x + num2, mainObject.y + num3);
						VecEff.addElement(p);
					}
				}
			}
		}
		if (f == 18 && !checkNullObject(1))
		{
			objFireMain.dy = 0;
			objFireMain.isTanHinh = false;
			objFireMain.isPaintLeg = true;
		}
		if (f >= fRemove && VecEff.size() == 0 && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Mon_Smoker_2()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.f++;
			if (point.f / 2 >= fraImgSubEff.nFrame)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			if (f % 2 == 0)
			{
				Point point2 = new Point();
				point2.x = point_Focus.x;
				point2.y = point_Focus.y;
				VecSubEff.addElement(point2);
			}
			if (point_Focus.f == point_Focus.fRe)
			{
				addSound(14);
				GameScreen.addEffectEnd(18, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				j--;
			}
		}
		if (f == 8)
		{
			addSound(19);
			for (int k = 0; k < vecObjsBeFire.size(); k++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus p = new Point_Focus();
						int xdich = mainObject.x - x;
						int ydich = mainObject.y - mainObject.hOne / 2 - y;
						p = create_Speed(xdich, ydich, p, x, y, mainObject.x, mainObject.y - mainObject.hOne / 2);
						VecEff.addElement(p);
					}
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0 && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Mon_5()
	{
		if (f == 3 && !checkNullObject(1))
		{
			int num = 20;
			if (Dir == 0)
			{
				num = -20;
			}
			GameScreen.addEffectEnd(72, 2, x + num, objFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isPaintWeapon = true;
			}
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
			removeEff();
		}
	}

	private void update_Mon_Valentine()
	{
		if (f == 16)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(63, 0, mainObject.x, mainObject.y, Dir, objMainEff);
						GameScreen.addEffectEnd(59, 0, mainObject.x, mainObject.y, Dir, objMainEff);
						setAva(1, mainObject);
					}
				}
			}
			LoadMap.timeVibrateScreen = 10;
			if (!checkNullObject(1))
			{
				objFireMain.y += 4;
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void update_Mon_Mr5()
	{
		if (f < fRemove)
		{
			return;
		}
		removeEff();
		for (int i = 0; i < vecObjsBeFire.size(); i++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
			if (object_Effect_Skill != null)
			{
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					GameScreen.addEffectEnd(4, 0, mainObject.x + CRes.random_Am_0(15), mainObject.y - CRes.random(0, mainObject.hOne / 4 * 3) - 10, Dir, objMainEff);
					setAva(1, mainObject);
				}
			}
		}
	}

	private void update_Crocodile_1()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElementAt(i);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f % 2 == 0)
			{
				Point point2 = new Point(point_Focus.x, point_Focus.y);
				point2.fRe = 4;
				VecSubEff.addElement(point2);
			}
			if (point_Focus.f >= point_Focus.fRe)
			{
				VecEff.removeElementAt(j);
				j--;
			}
		}
		if (f == 9 && !checkNullObject(2))
		{
			if (typeEffect == 235 && !GameCanvas.lowGraphic)
			{
				int num = objBeFireMain.x - am_duong * 48 - objFireMain.x;
				int num2 = objBeFireMain.y - objFireMain.y;
				Point_Focus point_Focus2 = new Point_Focus(objFireMain.x * 10, objFireMain.y * 10);
				create_Speed(num * 10, num2 * 10, point_Focus2, objFireMain.x * 10, objFireMain.y * 10, (objBeFireMain.x - am_duong * 48) * 10, objBeFireMain.y);
				VecEff.addElement(point_Focus2);
			}
			objFireMain.x = objBeFireMain.x - am_duong * 48;
			objFireMain.y = objBeFireMain.y;
		}
		if (f == 12 && !checkNullObject(1))
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 15)
		{
			if (!checkNullObject(2))
			{
				if (isAddSound)
				{
					mSound.playSound(5, mSound.volumeSound);
				}
				addVir(10, 5, 10, isPlayer: true);
				if (vecObjsBeFire.size() > 1)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (object_Effect_Skill != null)
						{
							MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
							if (mainObject != null)
							{
								GameScreen.addEffectEnd(63, 0, mainObject.x + 10, mainObject.y, Dir, mainObject);
								GameScreen.addEffectEnd(98, 0, mainObject.x, mainObject.y + 5, Dir, mainObject);
								GameScreen.addEffectEnd(110, 0, mainObject.x, mainObject.y + 5, Dir, mainObject);
								GameScreen.addEffectEnd(108, 5, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
								setAva(1, mainObject);
							}
						}
					}
				}
				else
				{
					GameScreen.addEffectEnd(63, 0, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
					GameScreen.addEffectEnd(63, 0, objBeFireMain.x - 10, objBeFireMain.y, Dir, objMainEff);
					GameScreen.addEffectEnd(63, 0, objBeFireMain.x + 10, objBeFireMain.y, Dir, objMainEff);
					GameScreen.addEffectEnd(98, 0, objBeFireMain.x, objBeFireMain.y + 5, Dir, objMainEff);
					GameScreen.addEffectEnd(59, 0, objBeFireMain.x, objBeFireMain.y + 5, Dir, objMainEff);
					GameScreen.addEffectEnd(110, 0, objBeFireMain.x, objBeFireMain.y + 5, Dir, objMainEff);
					GameScreen.addEffectEnd(108, 5, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				}
				if (typeEffect == 235 && !GameCanvas.lowGraphic)
				{
					GameScreen.addEffectEnd(54, 10, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
				}
			}
			setAva(2, objBeFireMain);
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
	}

	private void update_Crocodile_2()
	{
		if (f % 2 == 0 && f <= fRemove - 3)
		{
			Point point = new Point(x + CRes.random_Am_0(10), y + 10 + CRes.random_Am_0(10));
			point.vx = CRes.random_Am_0(3);
			point.vy = -CRes.random(3, 5);
			point.fRe = CRes.random(10, 14);
			VecEff.addElement(point);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point2 = (Point)VecEff.elementAt(i);
			point2.update();
			if (point2.f >= point2.fRe)
			{
				VecEff.removeElement(point2);
				i--;
			}
		}
		if (f == 13 && !checkNullObject(1))
		{
			sbyte subtype = 1;
			if (typeEffect == 236 && !GameCanvas.lowGraphic)
			{
				subtype = 11;
			}
			GameScreen.addEffectEnd(54, subtype, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
		}
		if (f == 15)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(63, 0, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
						GameScreen.addEffectEnd(99, 0, mainObject.x, mainObject.y, Dir, objMainEff);
						GameScreen.addEffectEnd(59, 0, mainObject.x, mainObject.y, Dir, objMainEff);
						setAva(1, mainObject);
					}
				}
			}
		}
		if (isAddSound && (f == 14 || f == 17 || f == 20))
		{
			mSound.playSound(5, mSound.volumeSound);
		}
	}

	private void update_Wapol_4()
	{
		if (f == 5 && !checkNullObject(2))
		{
			GameScreen.addEffectEnd(57, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			setAva(1, objBeFireMain);
		}
		if (f == 8)
		{
			vx = 0;
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void update_Nham_thach_2()
	{
		if (f > 10 && f % 3 == 0)
		{
			sbyte subtype = 0;
			if (typeEffect == 240)
			{
				subtype = 1;
			}
			if (indexObjBefire < vecObjsBeFire.size())
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
				indexObjBefire++;
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(113, subtype, mainObject.x, mainObject.y, Dir, objMainEff);
						setAva(2, mainObject);
					}
				}
			}
			else if (CRes.random(2) == 0)
			{
				GameScreen.addEffectEnd(113, subtype, objBeFireMain.x + CRes.random_Am_0(160), objBeFireMain.y + CRes.random_Am_0(80), Dir, objMainEff);
			}
			if (f % 6 == 0)
			{
				addSound(15);
			}
			addVir(3, 5, 10, isPlayer: false);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void update_Mr1_1()
	{
		if (f >= fRemove)
		{
			removeEff();
		}
		else
		{
			if (f % 4 != 0)
			{
				return;
			}
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill == null)
				{
					continue;
				}
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					setAva(1, mainObject);
					if (!checkNullObject(2))
					{
						GameScreen.addEffectEnd(1, 0, mainObject.x + CRes.random_Am_0(10), mainObject.y - mainObject.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
					}
				}
			}
		}
	}

	private void update_Mr1_2()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f == point_Focus.fRe)
			{
				GameScreen.addEffectEnd(1, 0, point_Focus.x + CRes.random_Am_0(10), point_Focus.y + CRes.random_Am_0(10), Dir, objMainEff);
				setAva(1, point_Focus.objMain);
			}
			if (point_Focus.f >= point_Focus.fRe + 6)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 2)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill == null)
				{
					continue;
				}
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					Point_Focus point_Focus2 = new Point_Focus();
					point_Focus2.x = x;
					point_Focus2.y = y;
					int xdich = mainObject.x - point_Focus2.x;
					int ydich = mainObject.y - mainObject.hOne / 2 - point_Focus2.y;
					point_Focus2 = create_Speed(xdich, ydich, point_Focus2);
					point_Focus2.objMain = mainObject;
					point_Focus2.dis = 0;
					if (x < mainObject.x)
					{
						point_Focus2.dis = 2;
					}
					VecEff.addElement(point_Focus2);
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_DF_1()
	{
		if (!checkNullObject(1))
		{
			if (f >= 4 && f <= 10)
			{
				objFireMain.vx = vMax * am_duong;
			}
			else
			{
				objFireMain.vx = 0;
			}
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			removeEff();
		}
	}

	private void update_DF_2()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (point.f < 4)
			{
				point.frame = point.f / 2;
			}
			else if (point.f >= 4 && point.f <= point.fRe - 2)
			{
				point.frame = 2;
			}
			else
			{
				point.frame = point.fRe - point.f;
			}
			if (point.f >= point.fRe)
			{
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f == 2)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point point2 = new Point();
						point2.x = mainObject.x;
						point2.y = mainObject.y + 4;
						point2.fRe = 30 + CRes.random(12);
						GameScreen.addEffectEnd(10, 0, mainObject.x, mainObject.y - mainObject.dy - mainObject.hOne / 2, Dir, objMainEff);
						VecEff.addElement(point2);
					}
				}
			}
			if (objBeFireMain != null)
			{
				for (int k = 0; k < 4; k++)
				{
					Point point3 = new Point();
					point3.x = objBeFireMain.x + CRes.random_Am_0(160);
					point3.y = objBeFireMain.y + 4 + CRes.random_Am_0(80);
					point3.fRe = 30 + CRes.random(12);
					VecEff.addElement(point3);
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Mr0_1()
	{
		if (f == 10 && !checkNullObject(2))
		{
			vx = am_duong * vMax;
		}
		if (f == 15)
		{
			GameScreen.addEffectEnd_ToX_ToY(62, 0, x, y - 30, x + vx * 20, y - 30, Dir, objMainEff);
			GameScreen.addEffectEnd_ToX_ToY(62, 0, x + 10 * am_duong, y - 20, x + vx * 20, y - 20, Dir, objMainEff);
			GameScreen.addEffectEnd_ToX_ToY(62, 0, x + 20 * am_duong, y - 10, x + vx * 20, y - 10, Dir, objMainEff);
			GameScreen.addEffectEnd_ToX_ToY(62, 0, x + 30 * am_duong, y, x + vx * 20, y, Dir, objMainEff);
		}
		if (f < 10)
		{
			frame = -1;
		}
		else if (f < 14)
		{
			frame = 0;
		}
		else if (f < 30)
		{
			frame = 1;
		}
		else if (f < 35)
		{
			frame = 2;
		}
		if (f < fRemove)
		{
			return;
		}
		removeEff();
		for (int i = 0; i < vecObjsBeFire.size(); i++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
			if (object_Effect_Skill != null)
			{
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					setAva(2, mainObject);
				}
			}
		}
	}

	private void update_Pell_1()
	{
		if (f > 1 && f < 26)
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (frame == 1)
			{
				point.frame = CRes.random(2);
			}
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point point2 = (Point)VecEff.elementAt(j);
			point2.update();
			if (typeEffect == 241)
			{
				Point point3 = new Point(point2.x, point2.y);
				if (frame == 1)
				{
					point3.x = point2.x + point2.vx + CRes.random_Am_0(5);
					point3.y = point2.y + point2.vy + 5 + CRes.random_Am_0(15);
					point3.fRe = 10;
					point3.vx = CRes.random_Am_0(2);
					point3.vy = -CRes.random_Am(2, 5);
					Point point4 = new Point(point2.x + point2.vx + CRes.random_Am_0(5), point2.y + point2.vy + 5 + CRes.random_Am_0(15));
					point4.fRe = 10;
					point4.vx = CRes.random_Am_0(2);
					point4.vy = -CRes.random_Am(2, 5);
					VecSubEff.addElement(point4);
				}
				else
				{
					point3.fRe = 3;
					point3.frame = CRes.random(3);
				}
				VecSubEff.addElement(point3);
			}
			if (point2.f > 10)
			{
				point2.vy -= 2;
			}
			else
			{
				point2.vy--;
			}
			if (point2.f == 10 && !checkNullObject(2))
			{
				if (frame == 1)
				{
					GameScreen.addEffectEnd(118, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
					GameScreen.addEffectEnd(54, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				}
				else
				{
					GameScreen.addEffectEnd(1, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				}
				setAva(2, objBeFireMain);
			}
			if (point2.f >= point2.fRe)
			{
				VecEff.removeElement(point2);
				j--;
			}
		}
		if (f == 4)
		{
			if (isAddSound)
			{
				if (frame == 0)
				{
					mSound.playSound(13, mSound.volumeSound);
				}
				else
				{
					mSound.playSound(23, mSound.volumeSound);
				}
			}
			Point point5 = new Point();
			int num = toX;
			int num2 = toY;
			if (!checkNullObject(2))
			{
				num = objBeFireMain.x;
				num2 = objBeFireMain.y - objBeFireMain.hOne / 2;
			}
			point5.x = num - am_duong * 240;
			point5.vx = 24 * am_duong;
			point5.y = num2 - 55;
			point5.vy = 9;
			point5.dis = Dir;
			point5.fRe = 20;
			VecEff.addElement(point5);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Enel_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f > 10 && f < fRemove && f % 2 == 0)
		{
			if (indexObjBefire < vecObjsBeFire.size())
			{
				for (int j = 0; j < vecObjsBeFire.size(); j++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							Point point2 = new Point();
							point2.x = mainObject.x * 10;
							point2.y = (mainObject.y + 4) * 10;
							point2.vx = CRes.random_Am_0(30);
							point2.vy = CRes.random_Am_0(30);
							point2.fRe = 15 + CRes.random(6);
							GameScreen.addEffectEnd(10, 0, mainObject.x, mainObject.y - mainObject.dy - mainObject.hOne / 2, Dir, objMainEff);
							setAva(2, mainObject);
							VecEff.addElement(point2);
						}
					}
					indexObjBefire++;
				}
			}
			else
			{
				Point point3 = new Point();
				point3.x = (objBeFireMain.x + CRes.random_Am_0(100)) * 10;
				point3.y = (objBeFireMain.y + CRes.random_Am_0(80)) * 10;
				point3.vx = CRes.random_Am_0(30);
				point3.vy = CRes.random_Am_0(30);
				point3.fRe = 10 + CRes.random(6);
				VecEff.addElement(point3);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Enel_2()
	{
		if (f >= fRemove)
		{
			removeEff();
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.f++;
			if (point.f >= 3)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point point2 = (Point)VecSubEff.elementAt(j);
			point2.f++;
			if (point2.f >= point2.fRe)
			{
				VecSubEff.removeElement(point2);
				j--;
			}
		}
		if (f == 0 || f == 9)
		{
			int num = 0;
			for (int k = 0; k < 8; k++)
			{
				Point o = new Point(x + CRes.getcos(num) * 30 / 1000, y + CRes.getsin(num) * 25 / 1000);
				VecEff.addElement(o);
				num += 45;
			}
		}
		if (f == 3 || f == 12)
		{
			int num2 = 0;
			for (int l = 0; l < 12; l++)
			{
				Point o2 = new Point(x + CRes.getcos(num2) * 40 / 1000, y + CRes.getsin(num2) * 30 / 1000);
				VecEff.addElement(o2);
				num2 += 30;
			}
		}
		int num3 = 0;
		if (f == 15)
		{
			int num4 = 0;
			for (int m = 0; m < 16; m++)
			{
				num3++;
				Point o3 = new Point(x + CRes.getcos(num4) * 55 / 1000, y + CRes.getsin(num4) * 35 / 1000);
				VecEff.addElement(o3);
				num4 += 22;
			}
			Point point3 = new Point(x, y);
			point3.frame = 0;
			point3.fRe = 4;
			VecSubEff.addElement(point3);
		}
		if (f == 18)
		{
			Point point4 = new Point(x, y);
			point4.frame = 1;
			point4.fRe = 4;
			VecSubEff.addElement(point4);
		}
		if (f != 22)
		{
			return;
		}
		for (int n = 0; n < vecObjsBeFire.size(); n++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(n);
			if (object_Effect_Skill != null)
			{
				MainObject obj = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				setAva(2, obj);
			}
		}
		GameScreen.addEffectEnd(121, 0, x, y, Dir, objMainEff);
	}

	private void update_Enel_3()
	{
		if (f == 4)
		{
			vx = am_duong * 8;
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						setAva(2, mainObject);
						GameScreen.addEffectEnd(42, 0, mainObject.x, mainObject.y, Dir, mainObject);
					}
				}
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void update_Satori_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			point_Focus.frame = 2 + point_Focus.f % 2;
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(122, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f == 8)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus point_Focus2 = new Point_Focus();
						point_Focus2.x = x;
						point_Focus2.y = y;
						int xdich = mainObject.x - point_Focus2.x;
						int ydich = mainObject.y - mainObject.hOne / 2 - point_Focus2.y;
						point_Focus2 = create_Speed(xdich, ydich, point_Focus2);
						point_Focus2.objMain = mainObject;
						point_Focus2.frame = 1;
						if (point_Focus2.fRe < 3)
						{
							point_Focus2.fRe = 3;
						}
						VecEff.addElement(point_Focus2);
					}
				}
				indexObjBefire++;
			}
			for (int k = indexObjBefire; k < 5; k++)
			{
				Point_Focus point_Focus3 = new Point_Focus();
				point_Focus3.x = x;
				point_Focus3.y = y;
				int xdich2 = 110 * am_duong + CRes.random_Am_0(50);
				int ydich2 = CRes.random_Am_0(40);
				point_Focus3 = create_Speed(xdich2, ydich2, point_Focus3);
				point_Focus3.objMain = null;
				point_Focus3.frame = 1;
				if (point_Focus3.fRe < 3)
				{
					point_Focus3.fRe = 3;
				}
				VecEff.addElement(point_Focus3);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Satori_2()
	{
		if (f < 4 || (f >= 9 && f < 13))
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 2 && !checkNullObject(3))
		{
			x1000 = objFireMain.x;
			y1000 = objFireMain.y;
			objFireMain.x = objBeFireMain.x - am_duong * 30;
			objFireMain.y = objBeFireMain.y;
			x = objFireMain.x;
			y = objFireMain.y;
		}
		if (f == 11 && !checkNullObject(3))
		{
			objFireMain.x = x1000;
			objFireMain.y = y1000;
			x = objFireMain.x;
			y = objFireMain.y;
		}
		if (f == 7)
		{
			GameScreen.addEffectEnd(123, 1, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - objBeFireMain.hOne / 2 - 10 + CRes.random_Am_0(15), Dir, objMainEff);
			setAva(2, objBeFireMain);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void update_Ohm_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f == point_Focus.fRe && point_Focus.objMain != null)
			{
				GameScreen.addEffectEnd(123, 3, point_Focus.x, point_Focus.y, Dir, objMainEff);
				setAva(2, point_Focus.objMain);
			}
			if (point_Focus.f >= point_Focus.fRe + 3)
			{
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f >= 10 && f % 2 == 0 && f < 20)
		{
			if (indexObjBefire < vecObjsBeFire.size())
			{
				for (int j = 0; j < vecObjsBeFire.size(); j++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							Point_Focus point_Focus2 = new Point_Focus();
							point_Focus2.x = x;
							point_Focus2.y = y;
							int xdich = mainObject.x - point_Focus2.x;
							int ydich = mainObject.y - mainObject.hOne / 2 - point_Focus2.y;
							point_Focus2 = create_Speed(xdich, ydich, point_Focus2);
							point_Focus2.objMain = mainObject;
							point_Focus2.frame = CRes.random(4);
							VecEff.addElement(point_Focus2);
						}
						indexObjBefire++;
					}
				}
			}
			else
			{
				Point_Focus point_Focus3 = new Point_Focus();
				point_Focus3.x = x;
				point_Focus3.y = y;
				int xdich2 = 150 * am_duong + CRes.random_Am_0(50);
				int ydich2 = CRes.random_Am_0(40);
				point_Focus3 = create_Speed(xdich2, ydich2, point_Focus3);
				point_Focus3.objMain = null;
				point_Focus3.frame = CRes.random(4);
				point_Focus3.fRe += 5;
				VecEff.addElement(point_Focus3);
			}
		}
		if (f == 20)
		{
			objFireMain.isPaintWeapon = true;
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Ohm_2()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f % 2 == 1)
			{
				GameScreen.addEffectEnd(66, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
			}
			if (point_Focus.f == point_Focus.fRe)
			{
				int tile = GameCanvas.loadmap.getTile(point_Focus.x, point_Focus.y);
				if (tile == 0 || tile == 2)
				{
					GameScreen.addEffectEnd(124, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
					GameScreen.addEffectEnd(125, 0, point_Focus.x, point_Focus.y + 8, Dir, objMainEff);
				}
				if (point_Focus.objMain != null)
				{
					setAva(2, point_Focus.objMain);
				}
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f == 10)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus point_Focus2 = new Point_Focus();
						point_Focus2.x = x;
						point_Focus2.y = y;
						int xdich = mainObject.x - point_Focus2.x;
						int ydich = mainObject.y - mainObject.hOne / 2 - point_Focus2.y;
						point_Focus2 = create_Speed(xdich, ydich, point_Focus2);
						point_Focus2.objMain = mainObject;
						point_Focus2.frame = 1;
						if (point_Focus2.fRe < 3)
						{
							point_Focus2.fRe = 3;
						}
						VecEff.addElement(point_Focus2);
					}
				}
				indexObjBefire++;
			}
			for (int k = indexObjBefire; k < 5; k++)
			{
				Point_Focus point_Focus3 = new Point_Focus();
				point_Focus3.x = x;
				point_Focus3.y = y;
				int xdich2 = CRes.random_Am(60, 140);
				int ydich2 = CRes.random_Am_0(60);
				point_Focus3 = create_Speed(xdich2, ydich2, point_Focus3);
				point_Focus3.objMain = null;
				point_Focus3.frame = 1;
				if (point_Focus3.fRe < 3)
				{
					point_Focus3.fRe = 3;
				}
				VecEff.addElement(point_Focus3);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Gedatsu_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f > point_Focus.fRe && point_Focus.objMain != null)
			{
				point_Focus.x = point_Focus.objMain.x;
				point_Focus.y = point_Focus.objMain.y - point_Focus.objMain.hOne / 2;
			}
			if (point_Focus.f > point_Focus.fRe + 5)
			{
				GameScreen.addEffectEnd(123, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				setAva(2, point_Focus.objMain);
				VecEff.removeElementAt(i);
				i--;
			}
			if (point_Focus.f == point_Focus.fRe)
			{
				if (point_Focus.objMain == null)
				{
					GameScreen.addEffectEnd(123, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
					VecEff.removeElementAt(i);
					i--;
				}
				else
				{
					point_Focus.vx = 0;
					point_Focus.vy = 0;
					point_Focus.x = point_Focus.objMain.x;
					point_Focus.y = point_Focus.objMain.y - point_Focus.objMain.hOne / 2;
				}
			}
		}
		if (f == 11 && !checkNullObject(1))
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus point_Focus2 = new Point_Focus();
						point_Focus2.x = objFireMain.x + am_duong * 30;
						point_Focus2.y = objFireMain.y - objFireMain.hOne / 2 - 10;
						int xdich = mainObject.x - point_Focus2.x;
						int ydich = mainObject.y - mainObject.hOne / 2 - point_Focus2.y;
						point_Focus2 = create_Speed(xdich, ydich, point_Focus2);
						point_Focus2.objMain = mainObject;
						point_Focus2.frame = 1;
						VecEff.addElement(point_Focus2);
					}
				}
				indexObjBefire++;
			}
			for (int k = indexObjBefire; k < 5; k++)
			{
				Point_Focus point_Focus3 = new Point_Focus();
				point_Focus3.x = objFireMain.x + am_duong * 30;
				point_Focus3.y = objFireMain.y - objFireMain.hOne / 2 - 10;
				int xdich2 = CRes.random_Am(60, 140);
				int ydich2 = CRes.random_Am_0(60);
				point_Focus3 = create_Speed(xdich2, ydich2, point_Focus3);
				point_Focus3.objMain = null;
				point_Focus3.frame = 0;
				if (point_Focus3.fRe < 3)
				{
					point_Focus3.fRe = 3;
				}
				VecEff.addElement(point_Focus3);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Gedatsu_2()
	{
		if (f < 4)
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 2 && !checkNullObject(3))
		{
			x1000 = objFireMain.x;
			y1000 = objFireMain.y;
			if (!checkNullObject(2))
			{
				objFireMain.x = objBeFireMain.x - am_duong * 30;
				objFireMain.y = objBeFireMain.y;
				x = objFireMain.x;
				y = objFireMain.y;
			}
			GameScreen.addEffectEnd(30, 0, x, y - objFireMain.hOne / 2 - 10, 140, Dir, objMainEff);
		}
		if (f == 14)
		{
			GameScreen.addEffectEnd(123, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - objBeFireMain.hOne / 2 - 15 + CRes.random_Am_0(15), Dir, objMainEff);
			setAva(2, objBeFireMain);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void update_Shura_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (point.f > 10)
			{
				point.vy -= 2;
			}
			else
			{
				point.vy--;
			}
			if (point.f == 10 && !checkNullObject(2))
			{
				GameScreen.addEffectEnd(123, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				setAva(2, objBeFireMain);
			}
			if (point.f >= point.fRe)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
		if (f == 10)
		{
			Point point2 = new Point();
			int num = toX;
			int num2 = toY;
			if (!checkNullObject(2))
			{
				num = objBeFireMain.x;
				num2 = objBeFireMain.y - objBeFireMain.hOne / 2;
			}
			point2.x = num - am_duong * 240;
			point2.vx = 24 * am_duong;
			point2.y = num2 - 55;
			point2.vy = 9;
			point2.dis = Dir;
			point2.fRe = 20;
			VecEff.addElement(point2);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Shura_2()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.f++;
			if (point.f >= point.fRe)
			{
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f == 10)
		{
			x += am_duong * 20;
			if (Dir == 0)
			{
				x -= 30;
			}
			vMax = 5;
			vx = am_duong * vMax;
		}
		if (f > 10)
		{
			int num = 360 - f % 12 * 30;
			int num2 = 26 + f / 4 * 3;
			x1000 = CRes.getcos(CRes.fixangle(num)) * (num2 * 2 / 3);
			y1000 = CRes.getsin(CRes.fixangle(num)) * num2;
			if (f < fRemove)
			{
				Point point2 = new Point(x + x1000 / 1000, y + y1000 / 1000);
				point2.fRe = 12;
				VecEff.addElement(point2);
			}
		}
		if (f == 20)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						setAva(1, mainObject);
					}
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Linh_Troi()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f == fPlayFrameSuper)
		{
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(12), toY + CRes.random_Am_0(12), Dir, objMainEff);
		}
		else if (f < fPlayFrameSuper)
		{
			Point point2 = new Point();
			point2.x = x;
			point2.y = y;
			point2.fRe = 6;
			VecEff.addElement(point2);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Tru_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.f++;
			if (point.f >= point.fRe)
			{
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f < fRemove)
		{
			Point point2 = new Point(x, y);
			point2.fRe = 5;
			VecEff.addElement(point2);
		}
		if (f == fRemove)
		{
			GameScreen.addEffectEnd(25, 4, toX, toY, Dir, objMainEff);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Lucci_1()
	{
		if (f == 2 || f == 4 || f == 6)
		{
			x = x1000 - am_duong * 24;
		}
		if (f >= 7 && vx <= 20)
		{
			vx += am_duong * 2;
		}
		if (f == 6 && isAddSound)
		{
			mSound.playSound(39, mSound.volumeSound);
			if (frame == 1)
			{
				mSound.playSound(17, mSound.volumeSound);
			}
		}
		if (f == 8)
		{
			setAva(2, objBeFireMain);
			GameScreen.addEffectEnd(132, (sbyte)frame, x + am_duong * 10, objFireMain.y - objFireMain.hOne / 2 - 5, 0, Dir, objMainEff);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (frame == 0 && point.f == 2)
			{
				point.frame = 0;
			}
			if (point.f >= point.fRe)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
		if (f < fRemove)
		{
			if (frame == 1)
			{
				for (int j = 0; j <= mframe[f]; j++)
				{
					Point point2 = new Point();
					point2.x = x;
					point2.y = y - mframe[f] * 10 + j * 20;
					if (mframe[f] < 2)
					{
						point2.fRe = 2;
					}
					else if (mframe[f] == 2)
					{
						if (j == 1)
						{
							point2.fRe = 4;
						}
						else
						{
							point2.fRe = 2;
						}
					}
					else if (mframe[f] == 3)
					{
						if (j == 1 || j == 2)
						{
							point2.fRe = 4;
						}
						else
						{
							point2.fRe = 2;
						}
					}
					else
					{
						switch (j)
						{
						case 2:
							point2.fRe = 6;
							break;
						case 1:
						case 3:
							point2.fRe = 4;
							break;
						default:
							point2.fRe = 2;
							break;
						}
					}
					VecEff.addElement(point2);
				}
			}
			else if (mframe[f] == 2)
			{
				Point point3 = new Point();
				point3.x = x;
				point3.y = y;
				point3.frame = 1;
				point3.fRe = 4;
				VecEff.addElement(point3);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void update_Dong_Dat_1()
	{
		if (f >= 2 && f <= 22)
		{
			addVir(2, 5, 12, isPlayer: true);
		}
		if (f == 15)
		{
			if (isAddSound)
			{
				mSound.playSound(40, mSound.volumeSound);
				mSound.playSound(41, mSound.volumeSound);
			}
			int num = objFireMain.x - GameScreen.player.x;
			int num2 = (objFireMain.y - GameScreen.player.y) / 2;
			x = MotherCanvas.hw + 30 + CRes.random_Am_0(10) + num;
			y = MotherCanvas.hh + CRes.random_Am_0(10) + num2;
			x1000 = x - 90 + CRes.random_Am_0(10);
			y1000 = y + CRes.random_Am_0(10);
		}
		if (f == 22)
		{
			GameScreen.addEffectEnd(133, 0, objFireMain.x, objFireMain.y, Dir, objMainEff);
			if (typeEffect == 243)
			{
				GameScreen.addEffectEnd(113, 2, objFireMain.x, objFireMain.y, Dir, objMainEff);
			}
			objFireMain.y += 3;
		}
		int subtype = 0;
		if (f >= 22 && f % 2 == 0 && f <= 32)
		{
			if (indexObjBefire < vecObjsBeFire.size())
			{
				for (int i = 0; i < vecObjsBeFire.size(); i++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
					if (typeEffect == 243 && CRes.random(2) == 0)
					{
						subtype = 1;
					}
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							setAva(2, mainObject);
							GameScreen.addEffectEnd(134, subtype, mainObject.x, mainObject.y, Dir, objMainEff);
						}
					}
					indexObjBefire++;
				}
			}
			else
			{
				if (typeEffect == 243 && CRes.random(2) == 0)
				{
					subtype = 1;
				}
				int num3 = objFireMain.x + CRes.random_Am(110, 140);
				int num4 = objFireMain.y + CRes.random_Am(10, 40);
				GameScreen.addEffectEnd(134, subtype, num3, num4, Dir, objMainEff);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void endUpdate()
	{
	}

	private void updateNamThach_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.f++;
			if (point.f == 1)
			{
				if (point.dis == 0)
				{
					int tile = GameCanvas.loadmap.getTile(point.x / 1000, point.y / 1000);
					if (tile == 0 || tile == 2)
					{
						if (point.frame == 0)
						{
							GameScreen.addEffectEnd(63, 0, point.x / 1000, point.y / 1000, Dir, objMainEff);
						}
						if (point.frame == 1)
						{
							GameScreen.addEffectEnd(63, 3, point.x / 1000, point.y / 1000, Dir, objMainEff);
						}
					}
					else
					{
						point.isRemove = true;
					}
				}
				if (CRes.random(6) == 0)
				{
					GameScreen.addEffectEnd(110, point.frame, point.x / 1000, point.y / 1000, Dir, objMainEff);
				}
			}
			if (point.f / 2 >= fraImgEff.nFrame || point.isRemove)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
		if (f < 7)
		{
			int num = 0;
			if (typeEffect == 239)
			{
				num = f * 10;
			}
			Point point2 = new Point();
			point2.x = x * 1000 + CRes.getcos(35 + num) * (6 - f) * vMax;
			point2.y = y * 1000 + CRes.getsin(35 + num) * (6 - f) * (vMax - 4);
			point2.dis = f % 2;
			point2.fSmall = 0;
			VecEff.addElement(point2);
			Point point3 = new Point();
			point3.x = x * 1000 + CRes.getcos(145 + num) * (6 - f) * vMax;
			point3.y = y * 1000 + CRes.getsin(145 + num) * (6 - f) * (vMax - 4);
			point3.dis = f % 2;
			point3.fSmall = 1;
			VecEff.addElement(point3);
			Point point4 = new Point();
			point4.x = x * 1000 + CRes.getcos(215 + num) * (6 - f) * vMax;
			point4.y = y * 1000 + CRes.getsin(215 + num) * (6 - f) * (vMax - 4);
			point4.dis = f % 2;
			point4.fSmall = 2;
			VecEff.addElement(point4);
			Point point5 = new Point();
			point5.x = x * 1000 + CRes.getcos(CRes.fixangle(325 + num)) * (6 - f) * vMax;
			point5.y = y * 1000 + CRes.getsin(CRes.fixangle(325 + num)) * (6 - f) * (vMax - 4);
			point5.dis = f % 2;
			point5.fSmall = 3;
			VecEff.addElement(point5);
			if (point2.f % 2 == 1)
			{
				int tile2 = GameCanvas.loadmap.getTile(point2.x / 10, point2.y / 10);
				if (tile2 == 0 || tile2 == 2)
				{
					GameScreen.addEffectEnd(63, 0, point2.x / 10, point2.y / 10, Dir, objMainEff);
				}
			}
			if (f % 4 == 2 && isAddSound)
			{
				mSound.playSound(4, mSound.volumeSound);
			}
		}
		else if (f < 20)
		{
			if (f == 7 && !checkNullObject(2))
			{
				setAva(2, objBeFireMain);
				if (isAddSound)
				{
					mSound.playSound(43, mSound.volumeSound);
				}
			}
			GameScreen.addEffectEnd(108, 7, x, y - CRes.random(240), Dir, objMainEff);
			if (CRes.random(3) == 0)
			{
				GameScreen.addEffectEnd(110, 1, x, y, Dir, objMainEff);
			}
			y1000 += 60;
			if (y1000 > 480)
			{
				y1000 = 480;
			}
			if (f % 2 == 1)
			{
				int num2 = 0;
				if (typeEffect == 239)
				{
					num2 = (f - 7) / 2 * 5;
				}
				disHard++;
				Point point6 = new Point();
				point6.x = x * 1000 + CRes.getcos(CRes.fixangle(num2)) * ((f - 5) / 2) * vMax;
				point6.y = y * 1000 + CRes.getsin(CRes.fixangle(num2)) * ((f - 5) / 2) * (vMax - 4);
				point6.frame = 1;
				point6.dis = disHard % 2;
				point6.fSmall = 0;
				VecEff.addElement(point6);
				Point point7 = new Point();
				point7.x = x * 1000 + CRes.getcos(90 + num2) * ((f - 5) / 2) * vMax;
				point7.y = y * 1000 + CRes.getsin(90 + num2) * ((f - 5) / 2) * (vMax - 4);
				point7.frame = 1;
				point7.fSmall = 1;
				point7.dis = disHard % 2;
				VecEff.addElement(point7);
				Point point8 = new Point();
				point8.x = x * 1000 + CRes.getcos(180 + num2) * ((f - 5) / 2) * vMax;
				point8.y = y * 1000 + CRes.getsin(180 + num2) * ((f - 5) / 2) * (vMax - 4);
				point8.frame = 1;
				point8.dis = disHard % 2;
				point8.fSmall = 2;
				VecEff.addElement(point8);
				Point point9 = new Point();
				point9.x = x * 1000 + CRes.getcos(CRes.fixangle(270 + num2)) * ((f - 5) / 2) * vMax;
				point9.y = y * 1000 + CRes.getsin(CRes.fixangle(270 + num2)) * ((f - 5) / 2) * (vMax - 4);
				point9.frame = 1;
				point9.fSmall = 3;
				point9.dis = disHard % 2;
				VecEff.addElement(point9);
			}
		}
		if (f == fRemove - 5)
		{
			setAva(2, objBeFireMain);
			GameScreen.addEffectEnd(112, 1, x, y, Dir, objMainEff);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
		else if (f % 4 == 0)
		{
			LoadMap.timeVibrateScreen = 105;
			GameScreen.addEffectEnd(59, 0, x + CRes.random_Am_0(15), y + 5 + CRes.random_Am_0(5), Dir, objMainEff);
		}
	}

	private void updateWapol_1()
	{
		if (f < fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.x = x;
				objFireMain.y = y;
				objFireMain.dy = 4;
			}
			if (f % 2 == 1)
			{
				Point point = new Point(x, y);
				point.frame = 0;
				VecEff.addElement(point);
			}
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point2 = (Point)VecEff.elementAt(i);
			point2.f++;
			if (point2.f >= 4)
			{
				VecEff.removeElement(point2);
				i--;
			}
		}
		if (f == fRemove)
		{
			objFireMain.plashNow.setIsNextf(0);
			vx = 0;
			vy = 0;
			if (!checkNullObject(1))
			{
				objFireMain.dy = 0;
			}
			Point point3 = new Point(toX, toY - 24);
			point3.frame = 1;
			VecEff.addElement(point3);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void updateWapol_3()
	{
		if (f == 4)
		{
			GameScreen.addEffectEnd(30, 0, x, y, 200, Dir, objMainEff);
		}
		if (f >= 9 && f <= fRemove && f % 3 == 0)
		{
			if (indexObjBefire < vecObjsBeFire.size())
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
				indexObjBefire++;
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus p = new Point_Focus();
						int xdich = mainObject.x - x;
						int ydich = mainObject.y - mainObject.hOne / 2 - y;
						p = create_Speed(xdich, ydich, p);
						VecEff.addElement(p);
					}
				}
			}
			else
			{
				Point_Focus p2 = new Point_Focus();
				int num = 120 + CRes.random_Am_0(30);
				int ydich2 = CRes.random_Am_0(50);
				if (Dir == 0)
				{
					num = -num;
				}
				p2 = create_Speed(num, ydich2, p2);
				VecEff.addElement(p2);
			}
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(57, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void updateMr3_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(103, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				if (point_Focus.objMain != null)
				{
					setAva(2, point_Focus.objMain);
					GameScreen.addEffectEnd(8, 0, point_Focus.objMain.x, point_Focus.objMain.y - point_Focus.objMain.hOne / 2, Dir, point_Focus.objMain);
				}
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 9 && !checkNullObject(2))
		{
			if (vecObjsBeFire.size() > 1)
			{
				fRemove = 25;
				for (int j = 0; j < vecObjsBeFire.size(); j++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							Point_Focus p = new Point_Focus();
							int xdich = mainObject.x - x;
							int ydich = mainObject.y - y;
							p = create_Speed(xdich, ydich, p, x, y, mainObject.x, mainObject.y);
							p.dis = Dir;
							p.objMain = mainObject;
							VecEff.addElement(p);
						}
					}
				}
			}
			else
			{
				if (Dir == 0)
				{
					toX = objBeFireMain.x + 10;
				}
				else
				{
					toX = objBeFireMain.x - 10;
				}
				toY = objBeFireMain.y + 5;
				int xdich2 = toX - x;
				int ydich2 = toY - y;
				Point_Focus p2 = new Point_Focus();
				p2 = create_Speed(xdich2, ydich2, p2);
				p2.dis = Dir;
				p2.objMain = objBeFireMain;
				VecEff.addElement(p2);
				fRemove = 15 + p2.fRe;
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void updateMr3_2()
	{
		if (f == 1 || f == 10)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill == null)
				{
					continue;
				}
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					Point_Focus point_Focus = new Point_Focus();
					point_Focus.objMain = mainObject;
					int xdich = mainObject.x - x;
					int ydich = mainObject.y - mainObject.hOne / 2 - y;
					point_Focus = create_Speed(xdich, ydich, point_Focus);
					if (f == 1)
					{
						point_Focus.frame = 0;
					}
					if (f == 10)
					{
						point_Focus.frame = 1;
					}
					VecEff.addElement(point_Focus);
				}
			}
		}
		if (f == 6 && !checkNullObject(1))
		{
			objFireMain.addChat(T.haha, isStop: true);
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecEff.elementAt(j);
			point_Focus2.update_Vx_Vy();
			if (point_Focus2.f >= point_Focus2.fRe)
			{
				if (point_Focus2.frame == 0)
				{
					Point point = new Point();
					point.frame = point_Focus2.frame;
					point.fRe = point_Focus2.fRe * 2 + 10;
					point.obj = point_Focus2.objMain;
					VecSubEff.addElement(point);
				}
				else
				{
					Point point2 = new Point();
					point2.frame = point_Focus2.frame;
					point2.fRe = CRes.random(12, 20);
					point2.obj = point_Focus2.objMain;
					VecSubEff.addElement(point2);
				}
				VecEff.removeElement(point_Focus2);
				j--;
			}
		}
		for (int k = 0; k < VecSubEff.size(); k++)
		{
			Point point3 = (Point)VecSubEff.elementAt(k);
			point3.update();
			if (point3.f >= point3.fRe)
			{
				VecSubEff.removeElement(point3);
				k--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0 && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void updateMissMS_1()
	{
		if (f >= 30)
		{
			if (!checkNullObject(1) && f == 30)
			{
				objFireMain.isTanHinh = false;
				objFireMain.isPaintLeg = false;
				objFireMain.dy = -20;
			}
			if (!checkNullObject(1) && f == 31)
			{
				objFireMain.isTanHinh = false;
				objFireMain.isPaintLeg = false;
				objFireMain.dy = -10;
			}
			if (!checkNullObject(1) && f == 32)
			{
				objFireMain.isTanHinh = false;
				objFireMain.isPaintLeg = true;
				objFireMain.dy = 10;
			}
			if (!checkNullObject(1) && f == 33)
			{
				objFireMain.isTanHinh = false;
				objFireMain.isPaintLeg = true;
				objFireMain.dy = 20;
			}
		}
		else if (f >= 2 && f < 30 && !checkNullObject(1))
		{
			objFireMain.isTanHinh = true;
		}
		if (f >= 8 && f % 5 == 0 && indexObjBefire < vecObjsBeFire.size())
		{
			addSound(15);
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
			indexObjBefire++;
			if (object_Effect_Skill != null)
			{
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					Point point = new Point();
					point.x = mainObject.x;
					point.y = mainObject.y;
					point.fRe = 12;
					VecEff.addElement(point);
					Point point2 = new Point();
					point2.x = mainObject.x;
					point2.y = mainObject.y + 10;
					point2.fRe = 20;
					VecSubEff.addElement(point2);
					setAva(2, mainObject);
				}
			}
			addVir(3, 5, 10, isPlayer: false);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point3 = (Point)VecEff.elementAt(i);
			point3.update();
			if (point3.f >= point3.fRe)
			{
				VecEff.removeElement(point3);
				i--;
			}
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point point4 = (Point)VecSubEff.elementAt(j);
			point4.update();
			if (point4.f < 3)
			{
				point4.frame = point4.f;
			}
			if (point4.f > point4.fRe - 3)
			{
				point4.frame = point4.fRe - point4.f;
			}
			if (point4.f >= point4.fRe)
			{
				VecSubEff.removeElement(point4);
				j--;
			}
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
	}

	private void updateHoDen()
	{
		if (GameCanvas.gameTick % 20 == 0)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill != null)
				{
					GameScreen.addEffectSkill2(-1, objFireMain, object_Effect_Skill, x + posSmock[CRes.random(posSmock.Length - 1)], y - 200 + CRes.random_Am(-10, 10));
					GameScreen.addEffectSkill2(-1, objFireMain, object_Effect_Skill, x + posSmock[CRes.random(posSmock.Length - 1)], y - 200 + CRes.random_Am(-10, 10));
				}
			}
		}
		if (f == 16 && tickadd <= 1)
		{
			tickadd++;
			GameScreen.addEffectEnd(164, 0, x, y, Dir, objMainEff);
		}
		if (f == 10 || f == 16)
		{
			for (int j = 0; j < 4; j++)
			{
				Point point = new Point();
				switch (j)
				{
				case 0:
					point.x = (x - 80) * 10;
					point.y = y * 10;
					point.vx = CRes.random(30, 50);
					point.vy = CRes.random(30, 50);
					break;
				case 1:
					point.x = x * 10;
					point.y = (y - 40) * 10;
					point.vx = -CRes.random(40, 60);
					point.vy = CRes.random(20, 40);
					break;
				case 2:
					point.x = x * 10;
					point.y = (y + 40) * 10;
					point.vx = CRes.random(40, 60);
					point.vy = -CRes.random(25, 45);
					break;
				case 3:
					point.x = (x + 80) * 10;
					point.y = y * 10;
					point.vx = -CRes.random(30, 50);
					point.vy = -CRes.random(30, 50);
					break;
				}
				if ((j % 2 == 1 && f == 10) || (j % 2 == 0 && f == 16))
				{
					point.frame = 1;
				}
				point.fRe = 22;
				VecSubEff.addElement(point);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
		for (int k = 0; k < VecEff.size(); k++)
		{
			Point point2 = (Point)VecEff.elementAt(k);
			point2.f++;
			if (point2.f >= 6)
			{
				VecEff.removeElement(point2);
				k--;
			}
		}
		if (f <= 16)
		{
			return;
		}
		if (y1000 > 0)
		{
			y1000 -= 120;
			if (y1000 < 0)
			{
				y1000 = 0;
			}
		}
		if (y1000 == 0 && f < fRemove)
		{
			if (CRes.random(2) == 0)
			{
				Point o = new Point(x + CRes.random_Am_0(20), y + 5 + CRes.random_Am_0(10));
				VecEff.addElement(o);
			}
			if (f % 4 == 0)
			{
				LoadMap.timeVibrateScreen = 105;
			}
		}
	}

	private void updateSet_1()
	{
		if (f == 10 || (f == 16 && typeEffect == 237))
		{
			for (int i = 0; i < 4; i++)
			{
				Point point = new Point();
				switch (i)
				{
				case 0:
					point.x = (x - 80) * 10;
					point.y = y * 10;
					point.vx = CRes.random(30, 50);
					point.vy = CRes.random(30, 50);
					break;
				case 1:
					point.x = x * 10;
					point.y = (y - 40) * 10;
					point.vx = -CRes.random(40, 60);
					point.vy = CRes.random(20, 40);
					break;
				case 2:
					point.x = x * 10;
					point.y = (y + 40) * 10;
					point.vx = CRes.random(40, 60);
					point.vy = -CRes.random(25, 45);
					break;
				case 3:
					point.x = (x + 80) * 10;
					point.y = y * 10;
					point.vx = -CRes.random(30, 50);
					point.vy = -CRes.random(30, 50);
					break;
				}
				if (typeEffect == 237 && ((i % 2 == 1 && f == 10) || (i % 2 == 0 && f == 16)))
				{
					point.frame = 1;
				}
				point.fRe = 22;
				VecSubEff.addElement(point);
			}
			if (isAddSound)
			{
				mSound.playSound(38, mSound.volumeSound);
			}
		}
		if (f == fRemove - 5)
		{
			setAva(2, objBeFireMain);
			GameScreen.addEffectEnd(112, 0, x, y + 10, Dir, objMainEff);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point point2 = (Point)VecEff.elementAt(j);
			point2.f++;
			if (point2.f >= fraImgSub2Eff.nFrame * 2)
			{
				VecEff.removeElement(point2);
				j--;
			}
		}
		for (int k = 0; k < VecSubEff.size(); k++)
		{
			Point point3 = (Point)VecSubEff.elementAt(k);
			point3.update();
			if (point3.f % 5 == 0)
			{
				int tile = GameCanvas.loadmap.getTile(point3.x / 10, point3.y / 10);
				if (tile == 0 || tile == 2)
				{
					GameScreen.addEffectEnd(63, 0, point3.x / 10, point3.y / 10, Dir, objMainEff);
				}
			}
			if (point3.f >= point3.fRe)
			{
				VecSubEff.removeElement(point3);
				k--;
			}
		}
		if (f == 16 && isAddSound)
		{
			mSound.playSound(37, mSound.volumeSound);
		}
		if (f <= 16)
		{
			return;
		}
		if (y1000 > 0)
		{
			y1000 -= 120;
			if (y1000 < 0)
			{
				y1000 = 0;
			}
		}
		if (y1000 == 0 && f < fRemove)
		{
			GameScreen.addEffectEnd(108, 6, x, y - CRes.random(240), Dir, objMainEff);
			if (CRes.random(2) == 0)
			{
				Point o = new Point(x + CRes.random_Am_0(20), y + 5 + CRes.random_Am_0(10));
				VecEff.addElement(o);
			}
			if (f % 4 == 0)
			{
				LoadMap.timeVibrateScreen = 105;
				GameScreen.addEffectEnd(110, 0, x + CRes.random_Am_0(15), y + 5 + CRes.random_Am_0(5), Dir, objMainEff);
			}
		}
	}

	private void updateSet_2()
	{
		if (f >= 10 && f <= 20)
		{
			if (isAddSound && f % 3 == 0)
			{
				mSound.playSound(17, mSound.volumeSound);
			}
			if (indexObjBefire < vecObjsBeFire.size())
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
				indexObjBefire++;
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						int num = 1 + CRes.random(2);
						for (int i = 0; i < num; i++)
						{
							Point_Focus point_Focus = new Point_Focus();
							point_Focus.x = mainObject.x + 300;
							if (Dir == 2)
							{
								point_Focus.x = mainObject.x - 300;
							}
							point_Focus.y = mainObject.y - 400;
							point_Focus.toX = mainObject.x + CRes.random_Am_0(20);
							point_Focus.toY = mainObject.y + CRes.random_Am_0(10);
							point_Focus = create_Speed(point_Focus.toX - point_Focus.x, point_Focus.toY - point_Focus.y, point_Focus, point_Focus.x, point_Focus.y, point_Focus.toX, point_Focus.toY);
							point_Focus.dis = CRes.random(16, 30);
							point_Focus.maxdis = CRes.random(10, 25);
							point_Focus.frame = CRes.random(fraImgEff.nFrame);
							if (i == 0)
							{
								point_Focus.goc = 1;
							}
							if (typeEffect == 238 && CRes.random(2) == 0)
							{
								point_Focus.typeSpec = 1;
							}
							VecEff.addElement(point_Focus);
						}
					}
				}
			}
			else if (!checkNullObject(2))
			{
				int num2 = 1 + CRes.random(4) / 3;
				for (int j = 0; j < num2; j++)
				{
					Point_Focus point_Focus2 = new Point_Focus();
					point_Focus2.x = objBeFireMain.x + 300;
					if (Dir == 2)
					{
						point_Focus2.x = objBeFireMain.x - 300;
					}
					point_Focus2.y = objBeFireMain.y - 400;
					point_Focus2.toX = objBeFireMain.x + CRes.random_Am_0(160);
					point_Focus2.toY = objBeFireMain.y + CRes.random_Am_0(80);
					point_Focus2 = create_Speed(point_Focus2.toX - point_Focus2.x, point_Focus2.toY - point_Focus2.y, point_Focus2, point_Focus2.x, point_Focus2.y, point_Focus2.toX, point_Focus2.toY);
					point_Focus2.dis = CRes.random(16, 30);
					point_Focus2.maxdis = CRes.random(10, 25);
					point_Focus2.frame = CRes.random(fraImgEff.nFrame);
					if (typeEffect == 238 && CRes.random(2) == 0)
					{
						point_Focus2.typeSpec = 1;
					}
					VecEff.addElement(point_Focus2);
				}
			}
		}
		for (int k = 0; k < VecEff.size(); k++)
		{
			Point_Focus point_Focus3 = (Point_Focus)VecEff.elementAt(k);
			point_Focus3.update_Vx_Vy();
			if (point_Focus3.f == point_Focus3.fRe)
			{
				point_Focus3.vx = 0;
				point_Focus3.vy = 0;
				point_Focus3.x = point_Focus3.toX;
				point_Focus3.y = point_Focus3.toY;
				if (CRes.random(3) == 0 || point_Focus3.goc == 1)
				{
					Point point = new Point();
					point.x = point_Focus3.x * 10;
					point.y = point_Focus3.y * 10;
					point.vx = CRes.random_Am_0(30);
					point.vy = CRes.random_Am_0(30);
					point.fRe = 14 + CRes.random(6);
					point.frame = point_Focus3.typeSpec;
					VecSubEff.addElement(point);
					GameScreen.addEffectEnd(59, 0, point_Focus3.x, point_Focus3.y, Dir, objMainEff);
				}
				if (GameCanvas.loadmap.getTile(point_Focus3.x, point_Focus3.y) == -1)
				{
					point_Focus3.isSpeedUp = true;
				}
				else
				{
					GameScreen.addEffectEnd(63, 0, point_Focus3.x, point_Focus3.y, Dir, objMainEff);
				}
			}
			if (point_Focus3.f % 2 == 0)
			{
				point_Focus3.frame++;
				if (point_Focus3.frame >= fraImgEff.maxNumFrame)
				{
					point_Focus3.frame = 0;
				}
			}
			if (point_Focus3.f >= point_Focus3.fRe + point_Focus3.dis || point_Focus3.isSpeedUp)
			{
				VecEff.removeElement(point_Focus3);
				k--;
			}
		}
		for (int l = 0; l < VecSubEff.size(); l++)
		{
			Point point2 = (Point)VecSubEff.elementAt(l);
			point2.update();
			if (point2.f % 8 == 0)
			{
				int tile = GameCanvas.loadmap.getTile(point2.x / 10, point2.y / 10);
				if (tile == 0 || tile == 2)
				{
					GameScreen.addEffectEnd(63, 0, point2.x / 10, point2.y / 10, Dir, objMainEff);
				}
			}
			if (point2.f >= point2.fRe)
			{
				VecSubEff.removeElement(point2);
				l--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0 && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void updateZoroS2_L1_NEW()
	{
		if (f == 1)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			GameScreen.addEffectEnd(16, 0, objFireMain.x, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2, Dir, objMainEff);
		}
		if (f == 2)
		{
			GameScreen.addEffectEnd(26, 1, objBeFireMain.x, objBeFireMain.y, 0, objMainEff);
		}
		if (f == 4)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			int num = 10;
			if (Dir == 0)
			{
				num = -10;
			}
			GameScreen.addEffectEnd(16, 1, objFireMain.x + num, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2, Dir, objMainEff);
		}
		if (f == 5)
		{
			GameScreen.addEffectEnd(26, 1, objBeFireMain.x, objBeFireMain.y, 2, objMainEff);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public override void stopUpdateNormal()
	{
		removeEff();
	}

	public override void removeEff()
	{
		int effSetId = (typeEffect >= 4001 && typeEffect <= 4016) ? (typeEffect - 4000) : ((typeEffect >= 4201 && typeEffect <= 4216) ? (typeEffect - 4200) : ((typeEffect >= 4501 && typeEffect <= 4516) ? (typeEffect - 4500) : (((typeEffect - 4001) / 5) + 1)));
		if (effSetId == 5 || effSetId == 9)
		{
			Player.isBlock = false;
			GameCanvas.gameScr.isFullScreen = false;
		}
		if (objFireMain == GameScreen.player && GameScreen.typePaintGameScreen == 1)
		{
			GameScreen.isPaintNormal();
		}
		if (!isEff)
		{
			AddNumAndEffPlus(vecObjsBeFire);
		}
		isStop = true;
		f = -1;
	}

	private void AddNumAndEffPlus(mVector vec)
	{
		if (vec == null || vec.size() == 0)
		{
			return;
		}
		for (int i = 0; i < vec.size(); i++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vec.elementAt(i);
			MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
			if (mainObject == null || mainObject.returnAction())
			{
				continue;
			}
			bool flag = setAddEffPlus(object_Effect_Skill, mainObject, objFireMain, objMainEff);
			if (mainObject.Hp <= 0 && mainObject.Action != 4)
			{
				mainObject.beginDie(objFireMain);
			}
			sbyte typeColor = 15;
			if (!checkNullObject(1) && objFireMain == GameScreen.player)
			{
				typeColor = 13;
			}
			int num = object_Effect_Skill.hpShow;
			if (objFireMain.typeObject == 1)
			{
				typeColor = 14;
				num = -num;
			}
			if (objFireMain != GameScreen.player && mainObject != GameScreen.player && GameCanvas.lowGraphic)
			{
				continue;
			}
			if (object_Effect_Skill.hpShow == 0)
			{
				GameScreen.addEffectNumBig_NEW_AP(num, object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, 17);
			}
			else
			{
				if (flag)
				{
					typeColor = 16;
				}
				GameScreen.addEffectNumBig_NEW_AP(num, object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, typeColor);
			}
			int num2 = HasHapThuEffPlus(object_Effect_Skill, mainObject, objFireMain, objMainEff);
			if (num2 >= 0 && object_Effect_Skill.mEff_HP_Plus[num2] > 0)
			{
				GameScreen.addEffectNumBig_NEW_AP(object_Effect_Skill.mEff_HP_Plus[num2], object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, 25);
			}
		}
		if (objFireMain != GameScreen.player && objFireMain.Hp <= 0 && objFireMain.Action != 4)
		{
			objFireMain.beginDie(objFireMain);
		}
	}

	private void beginCreate()
	{
	}

	public void createNormal()
	{
		fRemove = 60;
		if (subType == 0)
		{
			fraImgEff = new FrameImage(0, 14, 14);
		}
		vMax = 8000;
		numNextFrame = 2;
		setInfoNormal(objFireMain);
	}

	public void create_Ussop_S3_L1()
	{
		y -= 6;
		if (Dir == 0)
		{
			x -= 30;
		}
		else
		{
			x += 30;
		}
		fRemove = 20;
		vMax = 10;
		numNextFrame = 2;
		fraImgEff = new FrameImage(111, 40, 30, 40, 30);
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2 - 3, 300, Dir, objMainEff);
		if (isAddSound)
		{
			mSound.playSound(24, mSound.volumeSound);
		}
	}

	public void create_Ussop_S3_L6()
	{
		y -= 6;
		x += 30 * am_duong;
		fRemove = 20;
		vMax = 10;
		numNextFrame = 2;
		fraImgEff = new FrameImage(418, 6);
		GameScreen.addEffectEnd(53, 0, x, objFireMain.y - objFireMain.hOne / 2 - 3, 300, Dir, objMainEff);
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2 - 3, 300, Dir, objMainEff);
		if (isAddSound)
		{
			mSound.playSound(24, mSound.volumeSound);
		}
	}

	public void create_Ussop_S3_L7()
	{
		y -= 6;
		x += 30 * am_duong;
		fRemove = 20;
		vMax = 10;
		numNextFrame = 2;
		fraImgEff = new FrameImage(418, 6);
		fraImgSubEff = new FrameImage(456, 10);
		GameScreen.addEffectEnd(53, 0, x, objFireMain.y - objFireMain.hOne / 2 - 3, 300, Dir, objMainEff);
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2 - 3, 300, Dir, objMainEff);
		if (isAddSound)
		{
			mSound.playSound(24, mSound.volumeSound);
		}
	}

	public void createLuffy1()
	{
		fRemove = vecObjsBeFire.size() * 3 + 6;
		if (fRemove < 12)
		{
			fRemove = 12;
		}
		fraImgEff = new FrameImage(1, 80, 40);
		if (typeEffect == 37)
		{
			fraImgSubEff = new FrameImage(27, 24, 32);
		}
		if (Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
		if (isAddSound)
		{
			mSound.playSound(4, mSound.volumeSound);
		}
	}

	public void createSanji1()
	{
		y = objFireMain.y;
		fRemove = 12;
	}

	public void createZoro1()
	{
		fraImgEff = new FrameImage(10, 40, 47);
		int a = objFireMain.x - objBeFireMain.x;
		if (CRes.abs(a) > 50)
		{
			fRemove = 5;
			vx = (CRes.abs(a) - 24) / 5;
		}
		else if (CRes.abs(a) > 24)
		{
			vx = 5;
			fRemove = (CRes.abs(a) - 24) / 5;
		}
		else
		{
			fRemove = 1;
			vx = 0;
		}
		if (Dir == 0)
		{
			xplus = 20;
			vx = -vx;
		}
		else
		{
			xplus = -20;
		}
	}

	public void createZoro2()
	{
		fraImgEff = new FrameImage(10, 40, 47);
		fraImgSubEff = new FrameImage(11, 40, 50);
		fRemove = 7;
		yplus = objBeFireMain.hOne / 2;
		if (objFireMain != null)
		{
			objFireMain.isTanHinh = true;
			if (objFireMain.plashNow != null)
			{
				objFireMain.plashNow.setIsNextf(1);
			}
		}
		if (Dir == 0)
		{
			toX += 30;
		}
		else
		{
			toX -= 30;
		}
	}

	public void createUssopSea1()
	{
		fraImgEff = new FrameImage(12, 15, 15);
		vMax = 24;
		fRemove = 15;
		y -= 6;
		if (Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2, 300, Dir, objMainEff);
	}

	public void createUssopSea2()
	{
		fraImgEff = new FrameImage(196, 15, 15);
		vMax = 24;
		fRemove = 20;
		y -= 6;
		if (Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
	}

	public void createUssopSea3()
	{
		fraImgEff = new FrameImage(197, 15, 10);
		Dir = (sbyte)objFireMain.type_left_right;
		vMax = 12;
		fRemove = 20;
		y -= 6;
		if (Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2, 600, Dir, objMainEff);
	}

	public void createUssop1()
	{
		if (typeEffect == 64)
		{
			fraImgEff = new FrameImage(20, 10, 10);
		}
		else if (typeEffect == 66)
		{
			fraImgEff = new FrameImage(20, 10, 10);
		}
		vMax = 24;
		fRemove = 5;
		GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
	}

	public void createUssop2()
	{
		fraImgEff = new FrameImage(20, 10, 10);
		vMax = 24;
		if (typeEffect == 206)
		{
			setAngle();
			fraImgEff = new FrameImage(305, 16, 12);
			fraImgSubEff = new FrameImage(304, 10, 7);
			vMax = 16;
		}
		else if (typeEffect == 207)
		{
			setAngle();
			fraImgEff = new FrameImage(20, 10, 10);
			fraImgSubEff = new FrameImage(304, 10, 7);
			vMax = 16;
		}
		fRemove = 5;
		y -= 6;
		if (Dir == 0)
		{
			x -= 30;
		}
		else
		{
			x += 30;
		}
		int num = toX - x;
		int num2 = toY - y;
		create_Speed(num, num2, null);
		if (typeEffect == 206)
		{
			int frameAngle = CRes.angle(num, num2);
			frame = setFrameAngle(frameAngle);
		}
		GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
		fPlayFrameSuper = fRemove;
		if (fRemove < 5)
		{
			fRemove = 5;
		}
	}

	public void createUssopSkill1_Lv3()
	{
		fraImgEff = new FrameImage(53, 9, 9);
		fraImgSubEff = new FrameImage(20, 10, 10);
		vMax = 24;
		fRemove = 5;
		y -= 6;
		if (Dir == 0)
		{
			x -= 30;
		}
		else
		{
			x += 30;
		}
		int xdich = toX - x;
		int ydich = toY - y;
		Point_Focus p = new Point_Focus();
		p = create_Speed(xdich, ydich, p);
		p.frame = 1;
		GameScreen.addEffectEnd(1, 0, x, y, Dir, objMainEff);
		VecEff.addElement(p);
	}

	public void createNami1()
	{
		fraImgEff = new FrameImage(22, 70, 50);
		fraImgSubEff = new FrameImage(298, 24, 24, 6);
		fRemove = 10;
		if (typeEffect == 53 || typeEffect == 163)
		{
			fraImgSub2Eff = new FrameImage(27, 24, 24);
		}
		indexEff_1 = objFireMain.indexEff_1;
		vMax = 12;
		y += 5;
		int num = 0;
		int xdich = toX - x;
		int ydich = toY - num - y;
		Point_Focus p = new Point_Focus();
		p = create_Speed(xdich, ydich, p);
		p.frame = 0;
		VecEff.addElement(p);
		if (isAddSound)
		{
			mSound.playSound(18, mSound.volumeSound);
		}
	}

	public void createNami1_SHORT()
	{
		fraImgEff = new FrameImage(22, 70, 50);
		fraImgSubEff = new FrameImage(298, 24, 24, 6);
		if (typeEffect == 190 || typeEffect == 222 || typeEffect == 312)
		{
			fraImgSubEff = new FrameImage(299, 26, 26, 2);
			if (typeEffect == 222 || typeEffect == 312)
			{
				fraImgEff = new FrameImage(324, 70, 50);
				fraImgSub3Eff = new FrameImage(326, 26, 26, 3);
			}
		}
		indexEff_1 = objFireMain.indexEff_1;
		fRemove = 24;
		fraImgSub2Eff = new FrameImage(27, 24, 24);
		vMax = 12;
		if (isAddSound)
		{
			mSound.playSound(22, mSound.volumeSound);
		}
		int num = 0;
		num = ((Dir != 0) ? (-15) : 15);
		GameScreen.addEffectEnd(30, 0, x + num, objFireMain.y - objFireMain.hOne / 2, 500, Dir, objMainEff);
	}

	public void create_Nami_S2_L7()
	{
		fraImgEff = new FrameImage(324, 70, 50);
		fraImgSubEff = new FrameImage(299, 26, 26, 2);
		fraImgSub3Eff = new FrameImage(326, 26, 26, 3);
		indexEff_1 = objFireMain.indexEff_1;
		fRemove = 24;
		fraImgSub2Eff = new FrameImage(27, 24, 24);
		vMax = 12;
		if (isAddSound)
		{
			mSound.playSound(22, mSound.volumeSound);
		}
		int num = 0;
		num = ((Dir != 0) ? (-15) : 15);
		GameScreen.addEffectEnd(30, 0, x + num, objFireMain.y - objFireMain.hOne / 2, 500, Dir, objMainEff);
	}

	public void createNamiSea1_2()
	{
		yplus = y;
		y += objFireMain.hOne / 2;
		vMax = 12;
		fraImgEff = new FrameImage(28, 46, 50, 46, 50);
		fraImgSubEff = new FrameImage(29, 28, 30, 28, 30);
		fraImgSub2Eff = new FrameImage(298, 24, 24, 6);
		indexEff_1 = objFireMain.indexEff_1;
		if (Dir == 0)
		{
			xplus = x - 20;
		}
		else
		{
			xplus = x + 20;
		}
		if (Dir == 0)
		{
			x -= 30;
		}
		else
		{
			x += 30;
		}
		if (typeEffect == 139)
		{
			fraImgSub3Eff = new FrameImage(27, 24, 24);
			GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2, 500, Dir, objMainEff);
		}
		else
		{
			fraImgSub3Eff = new FrameImage(13, 24, 24);
			GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2, 300, Dir, objMainEff);
		}
		if (isAddSound)
		{
			mSound.playSound(18, mSound.volumeSound);
		}
	}

	public void createNamiSea3()
	{
		yplus = y;
		y += objFireMain.hOne / 2;
		vMax = 12;
		fraImgSub2Eff = new FrameImage(298, 24, 24, 6);
		indexEff_1 = objFireMain.indexEff_1;
		if (Dir == 0)
		{
			xplus = x - 20;
		}
		else
		{
			xplus = x + 20;
		}
		if (Dir == 0)
		{
			x -= 30;
		}
		else
		{
			x += 30;
		}
		if (isAddSound)
		{
			mSound.playSound(18, mSound.volumeSound);
		}
		fraImgSub3Eff = new FrameImage(27, 24, 24);
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 2, 500, Dir, objMainEff);
		mImgframe = new FrameImage[3];
		mImgframe[0] = new FrameImage(25, 80, 40, 60, 30);
		mImgframe[1] = new FrameImage(24, 15, 60);
		mImgframe[2] = new FrameImage(81, 24, 24);
	}

	public void createSanji2()
	{
		numNextFrame = 2;
		vMax = 16;
		fRemove = 16;
		fraImgEff = new FrameImage(31, 70, 70);
		Dir = (sbyte)objFireMain.type_left_right;
		int time = 300;
		if (typeEffect == 12)
		{
			fraImgSubEff = new FrameImage(77, 64, 75, 43, 50);
			fraImgSub2Eff = new FrameImage(224, 22, 28);
			fraImgSub3Eff = new FrameImage(78, 22, 28);
			fRemove = 24;
			time = 600;
		}
		else if (typeEffect == 188 || typeEffect == 220 || typeEffect == 293)
		{
			fraImgSubEff = new FrameImage(282, 64, 75);
			if (typeEffect == 293)
			{
				fraImgSub2Eff = new FrameImage(406, 42, 34);
				fraImgSub4Eff = new FrameImage(283, 22, 28);
				fraImgSubEff = new FrameImage(412, 64, 75);
			}
			else if (typeEffect == 220)
			{
				fraImgSub2Eff = new FrameImage(325, 32, 31);
				fraImgSub4Eff = new FrameImage(224, 22, 28);
			}
			else
			{
				fraImgSub2Eff = new FrameImage(224, 22, 28);
			}
			fraImgSub3Eff = new FrameImage(283, 22, 28);
			fraImgEff = new FrameImage(284, 70, 70);
			fRemove = 24;
			time = 600;
		}
		else if (typeEffect == 49)
		{
			fraImgSubEff = new FrameImage(78, 22, 28);
			fraImgSub2Eff = new FrameImage(102, 35, 19);
		}
		else if (typeEffect == 50)
		{
			fraImgSub2Eff = new FrameImage(103, 35, 19, 35, 19);
			fraImgSubEff = new FrameImage(78, 22, 28);
		}
		x1000 = x;
		y1000 = objFireMain.y;
		if (Dir == 0)
		{
			x -= 16;
		}
		else
		{
			x += 16;
		}
		if (isAddSound)
		{
			mSound.playSound(16, mSound.volumeSound);
		}
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 3 * 2, time, Dir, objMainEff);
	}

	public void create_Sanji_S3_L7()
	{
		numNextFrame = 2;
		vMax = 16;
		fraImgEff = new FrameImage(31, 70, 70);
		Dir = (sbyte)objFireMain.type_left_right;
		int num = 300;
		fraImgEff = new FrameImage(284, 70, 70);
		fraImgSubEff = null; // Old skill effs only 0-466. 470 is Thần Trang Venom
		fraImgSub2Eff = new FrameImage(406, 42, 34);
		fraImgSub3Eff = new FrameImage(283, 22, 28);
		fraImgSub4Eff = new FrameImage(283, 22, 28);
		fRemove = 24;
		num = 600;
		x1000 = x;
		y1000 = objFireMain.y;
		if (Dir == 0)
		{
			x -= 16;
		}
		else
		{
			x += 16;
		}
		if (isAddSound)
		{
			mSound.playSound(16, mSound.volumeSound);
		}
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 3 * 2, num, Dir, objMainEff);
	}

	public void createRankyaku()
	{
		vMax = 16;
		fRemove = 22;
		Dir = (sbyte)objFireMain.type_left_right;
		fraImgEff = new FrameImage(428, 1);
		x1000 = x + 30 * am_duong;
		int xdich = x1000 - x;
		VecEff.addElement(create_Speed(xdich, 0, new Point_Focus(), x, y, toX, toY));
		VecEff.addElement(create_Speed(xdich, -7, new Point_Focus(), x, y, toX, toY));
		VecEff.addElement(create_Speed(xdich, 7, new Point_Focus(), x, y, toX, toY));
	}

	public void createSoi()
	{
		vMax = 12;
		fRemove = 20;
		Dir = (sbyte)objFireMain.type_left_right;
		fraImgEff = new FrameImage(429, 4);
		if (typeEffect == 277)
		{
			fraImgEff = new FrameImage(430, 4);
		}
	}

	public void createShigan()
	{
		vMax = 17;
		fRemove = 14;
		Dir = (sbyte)objFireMain.type_left_right;
		fraImgEff = new FrameImage(75, 1);
		x1000 = x + 30 * am_duong;
		int xdich = x1000 - x;
		VecEff.addElement(create_Speed(xdich, 0, new Point_Focus(), x, y, toX, toY));
		GameScreen.addEffectEnd(30, 0, x + 15 * am_duong, objFireMain.y - objFireMain.hOne / 3 * 2, 200, Dir, objMainEff);
	}

	public void createDoor()
	{
		fRemove = 26;
		fraImgEff = new FrameImage(426, 2);
		levelPaint = -1;
	}

	public void createHuou()
	{
		fRemove = 20;
		fraImgEff = new FrameImage(176, 3, 25, 1);
		fraImgSubEff = new FrameImage(220, 9, 9, 4);
		size1 = 30;
		if (typeEffect == 279)
		{
			size1 = 60;
		}
		if (GameCanvas.isLowGraOrWP_PvP())
		{
			size1 = 10;
		}
		for (int i = 0; i < size1; i++)
		{
			Point point = new Point();
			createPointHuou(point);
			point.vy = 20;
			VecEff.addElement(point);
		}
	}

	private Point createPointHuou(Point p)
	{
		p.frame = CRes.random(5);
		if (typeEffect == 279)
		{
			p.x = CRes.random_Am_0(60);
			p.y = -10 - CRes.random(60);
			p.dis = CRes.random(6);
		}
		else
		{
			p.x = CRes.random_Am_0(40);
			p.y = -10 - CRes.random(60);
			p.dis = 2;
		}
		return p;
	}

	public void createZoro3()
	{
		fRemove = 12;
		if (typeEffect == 15)
		{
			fRemove = 15;
		}
	}

	public void createZoro4()
	{
		fraImgSub2Eff = new FrameImage(71, 64, 25);
		fraImgEff = new FrameImage(88, 32, 70);
		fRemove = 20;
		vMax = 12;
	}

	public void createZoroSkill3_Lv1()
	{
		vMax = 12;
		y = objFireMain.y + 5;
	}

	public void createZoro8()
	{
		fraImgEff = new FrameImage(8, 40, 47, 40, 47);
		objFireMain.isTanHinh = true;
		if (objFireMain.plashNow != null)
		{
			objFireMain.plashNow.setIsNextf(1);
		}
		x = objFireMain.x;
		y = objFireMain.y;
		toX = objBeFireMain.x;
		toY = objBeFireMain.y;
		vMax = 20;
		int num = 0;
		int num2 = 0;
		num = toX - x;
		num2 = toY - y;
		int num3 = 90;
		int a = CRes.angle(num, num2);
		toX = x + num3 * CRes.getcos(a) / 1000;
		toY = y + num3 * CRes.getsin(a) / 1000;
		num = toX - x;
		num2 = toY - y;
		if (num2 == 0)
		{
			num2 = 1;
		}
		if (num == 0)
		{
			num = 1;
		}
		int num4 = 0;
		int num5 = 0;
		int num6 = MainObject.getDistance(num, num2) / vMax;
		if (num6 == 0)
		{
			num6 = 1;
		}
		num4 = num / num6;
		num5 = num2 / num6;
		if (CRes.abs(num4) > CRes.abs(num))
		{
			num4 = num;
		}
		if (CRes.abs(num5) > CRes.abs(num2))
		{
			num5 = num2;
		}
		vx = num4;
		vy = num5;
		fRemove = num6;
		if (fRemove > 0)
		{
			timeAddNum = (sbyte)(fRemove / 2);
		}
	}

	public void createLuffy6()
	{
		if (objFireMain == GameScreen.player)
		{
			GameScreen.setIsMoveEff(ismove: true);
		}
		fRemove = 11;
		if (!checkNullObject(3))
		{
			objFireMain.x = objBeFireMain.x + objFireMain.vMax * 3 * 7;
			if (Dir == 2)
			{
				objFireMain.x = objBeFireMain.x - objFireMain.vMax * 3 * 7;
			}
			objFireMain.y = objBeFireMain.y;
		}
		fraImgEff = new FrameImage(4, 20, 20);
		fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
	}

	public void createNamiSkill1_L3()
	{
		Dir = (sbyte)objFireMain.type_left_right;
		for (int i = 0; i < 2; i++)
		{
			int a = 25;
			if (objFireMain.hOne > 1)
			{
				a = objFireMain.hOne / 2;
			}
			Point o = new Point(x + CRes.random_Am_0(20), y + CRes.random_Am_0(a));
			VecEff.addElement(o);
		}
		fRemove = 16;
		if (Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
		fraImgSubEff = new FrameImage(299, 26, 26, 2);
		fraImgEff = new FrameImage(273, 24, 24, 4);
		indexEff_1 = objFireMain.indexEff_1;
		if (isAddSound)
		{
			mSound.playSound(10, mSound.volumeSound);
		}
	}

	public void create_Nami_S1_L7()
	{
		Dir = (sbyte)objFireMain.type_left_right;
		for (int i = 0; i < 2; i++)
		{
			int a = 25;
			if (objFireMain.hOne > 1)
			{
				a = objFireMain.hOne / 2;
			}
			Point o = new Point(x + CRes.random_Am_0(20), y + CRes.random_Am_0(a));
			VecEff.addElement(o);
		}
		fRemove = 25;
		if (Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
		fraImgEff = new FrameImage(273, 24, 24, 4);
		fraImgSubEff = new FrameImage(299, 26, 26, 2);
		fraImgSub2Eff = new FrameImage(446, 10);
		fraImgSub3Eff = new FrameImage(411, 3);
		if (isAddSound)
		{
			mSound.playSound(10, mSound.volumeSound);
		}
	}

	public void createNamiSkill3()
	{
		fRemove = 20;
		vMax = 10;
		x1000 = x;
		if (objFireMain.Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
		indexEff_1 = objFireMain.indexEff_1;
		if (typeEffect == 31)
		{
			fraImgEff = new FrameImage(83, 14, 14);
			fraImgSubEff = new FrameImage(298, 24, 24, 6);
		}
		else if (typeEffect == 55 || typeEffect == 56 || typeEffect == 191 || typeEffect == 223 || typeEffect == 313)
		{
			fraImgEff = new FrameImage(81, 24, 24);
			fraImgSubEff = new FrameImage(299, 26, 26, 2);
			fraImgSub2Eff = new FrameImage(27, 24, 24);
			if (typeEffect == 56 || typeEffect == 191 || typeEffect == 223 || typeEffect == 313)
			{
				GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 3 * 2, 1000, Dir, objMainEff);
				fRemove = 26;
			}
		}
		if (isAddSound)
		{
			mSound.playSound(10, mSound.volumeSound);
		}
	}

	public void create_Nami_S3_L7()
	{
		vMax = 10;
		x1000 = x;
		if (objFireMain.Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
		indexEff_1 = objFireMain.indexEff_1;
		fraImgEff = new FrameImage(81, 24, 24);
		fraImgSubEff = new FrameImage(299, 26, 26, 2);
		fraImgSub2Eff = new FrameImage(27, 24, 24);
		GameScreen.addEffectEnd(30, 0, x, objFireMain.y - objFireMain.hOne / 3 * 2, 1000, Dir, objMainEff);
		fRemove = 26;
		if (isAddSound)
		{
			mSound.playSound(10, mSound.volumeSound);
		}
	}

	public void createNamiSkill1()
	{
		fRemove = 16;
		if (Dir == 0)
		{
			x -= 20;
		}
		else
		{
			x += 20;
		}
		indexEff_1 = objFireMain.indexEff_1;
		fraImgSubEff = new FrameImage(298, 24, 24, 6);
		if (typeEffect == 51)
		{
			fraImgEff = new FrameImage(299, 26, 26, 2);
		}
		if (isAddSound)
		{
			mSound.playSound(10, mSound.volumeSound);
		}
	}

	public void createAlvida2()
	{
		if (Dir == 0)
		{
			x -= 30;
		}
		else
		{
			x += 30;
		}
		y -= 15;
		fraImgEff = new FrameImage(116, 38, 53);
		fraImgSubEff = new FrameImage(117, 38, 22);
		fRemove = 10;
		int num = x;
		num = ((Dir != 0) ? (num - 45) : (num + 45));
		GameScreen.addEffectEnd(30, 0, num, y - 30, 300, Dir, objMainEff);
	}

	public void createAlvida1()
	{
		if (Dir == 0)
		{
			x -= 26;
		}
		else
		{
			x += 26;
		}
		y -= 15;
		fraImgEff = new FrameImage(116, 38, 53);
		fRemove = 2;
		addSound(2);
	}

	public void createMon_4_5()
	{
		if (Dir == 0)
		{
			x -= 14;
		}
		else
		{
			x += 14;
		}
		y -= 10;
		fRemove = 6;
		if (typeEffect == 73)
		{
			fraImgEff = new FrameImage(115, 34, 27);
		}
		else
		{
			fraImgEff = new FrameImage(35, 34, 27);
		}
	}

	public void createMon6()
	{
		if (Dir == 0)
		{
			x -= 14;
		}
		else
		{
			x += 14;
		}
		x1000 = x;
		y1000 = y - 10;
		vMax = 14;
		fraImgEff = new FrameImage(47, 41, 14);
		fraImgSubEff = new FrameImage(35, 34, 27);
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
		frame = CRes.random(fraImgSubEff.nFrame);
	}

	public void createMon3()
	{
		if (Dir == 0)
		{
			x -= 25;
		}
		else
		{
			x += 25;
		}
		vMax = 14;
		fraImgEff = new FrameImage(20, 10, 10);
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
		GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
	}

	public void createMon2()
	{
		vMax = 12;
		if (typeEffect == 145)
		{
			fraImgEff = new FrameImage(60, 15, 15);
		}
		else if (typeEffect == 146)
		{
			fraImgEff = new FrameImage(59, 23, 23);
		}
		else if (typeEffect == 147)
		{
			fraImgEff = new FrameImage(20, 10, 10);
		}
		else if (typeEffect == 148)
		{
			fraImgEff = new FrameImage(73, 20, 20);
			numNextFrame = 2;
			vMax = 14;
		}
		else
		{
			fraImgEff = new FrameImage(114, 21, 14);
		}
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
		objFireMain.isPaintWeapon = false;
	}

	public void createZoro_New2()
	{
		fRemove = 44;
		vMax = 12;
		fraImgEff = new FrameImage(8, 40, 47, 40, 47);
		if (isAddSound)
		{
			mSound.playSound(8, mSound.volumeSound);
		}
		GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
	}

	public void createZoro_New1()
	{
		fRemove = 50;
		vMax = 12;
		fraImgEff = new FrameImage(88, 32, 70);
		y = objFireMain.y;
	}

	public void createZoro_S1_L3_SHORT()
	{
		fRemove = 16;
		if (typeEffect == 183 || typeEffect == 215)
		{
			fRemove = 18;
		}
		vMax = 12;
		fraImgEff = new FrameImage(88, 32, 70);
		if (typeEffect == 215)
		{
			fraImgEff = new FrameImage(319, 32, 70);
		}
		y = objFireMain.y;
	}

	public void createZoro_S1_L6()
	{
		fRemove = 20;
		vMax = 18;
		fraImgEff = new FrameImage(422, 32, 70);
		y = objFireMain.y;
		x1000 = x + 30 * am_duong;
		xLight1 = x1000;
		xLight2 = x1000;
		fraImgSub2Eff = new FrameImage(417, 3);
		int xdich = x1000 - x;
		int ydich = 0;
		VecSubEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y - objFireMain.hOne - 20, toX, toY));
		VecSubEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y - objFireMain.hOne - 15, toX, toY));
	}

	public void create_Zoro_S1_L7()
	{
		fRemove = 32;
		vMax = 18;
		fraImgEff = new FrameImage(443, 5);
		y = objFireMain.y;
		x1000 = x + 30 * am_duong;
		xLight1 = x1000;
		xLight2 = x1000;
		fraImgSub2Eff = new FrameImage(441, 7);
		numNextFrame = 3;
		int xdich = x1000 - x;
		int ydich = 0;
		VecSubEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y - objFireMain.hOne - 20, toX, toY));
		VecSubEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y - objFireMain.hOne - 15, toX, toY));
	}

	public void createLuffy_New3()
	{
		levelPaint = -1;
		fRemove = 30;
		if (typeEffect == 182)
		{
			fraImgEff = new FrameImage(276, 90, 50);
		}
		else if (typeEffect == 214 || typeEffect == 273)
		{
			fraImgEff = new FrameImage(317, 90, 50);
			levelPaint = 0;
		}
		else
		{
			fraImgEff = new FrameImage(1, 80, 40);
		}
		fraImgSubEff = new FrameImage(27, 24, 32);
		fraImgSub2Eff = new FrameImage(8, 40, 47, 40, 47);
		Dir = (sbyte)objFireMain.type_left_right;
	}

	public void create_Luffy_S3_L7()
	{
		fRemove = 30;
		levelPaint = 0;
		fraImgEff = new FrameImage(317, 90, 50);
		fraImgSubEff = new FrameImage(27, 24, 32);
		fraImgSub2Eff = new FrameImage(8, 40, 47, 40, 47);
		Dir = (sbyte)objFireMain.type_left_right;
	}

	public void createLuffy_New2()
	{
		if (objFireMain == GameScreen.player)
		{
			GameScreen.setIsMoveEff(ismove: true);
		}
		if (!checkNullObject(3))
		{
			objFireMain.x = objBeFireMain.x + 30;
			if (Dir == 2)
			{
				objFireMain.x = objBeFireMain.x - 30;
			}
			objFireMain.y = objBeFireMain.y;
		}
		int num = -15;
		if (Dir == 0)
		{
			num = 15;
		}
		GameScreen.addEffectEnd(30, 0, x + num, y, 300, Dir, objMainEff);
		fraImgEff = new FrameImage(4, 20, 20);
		fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
		fraImgSub2Eff = new FrameImage(11, 40, 50);
		fRemove = 34;
	}

	public void createLuffy_New2_SHORT()
	{
		if (objFireMain == GameScreen.player)
		{
			GameScreen.setIsMoveEff(ismove: true);
		}
		if (!checkNullObject(3))
		{
			objFireMain.x = objBeFireMain.x + 30;
			if (Dir == 2)
			{
				objFireMain.x = objBeFireMain.x - 30;
			}
			objFireMain.y = objBeFireMain.y;
		}
		fraImgEff = new FrameImage(4, 20, 20);
		fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
		fraImgSub2Eff = new FrameImage(11, 40, 50);
		if (typeEffect == 213 || typeEffect == 272)
		{
			fraImgSub3Eff = new FrameImage(316, 44, 47);
		}
		fRemove = 24;
	}

	public void create_Luffy_S2_L7()
	{
		if (objFireMain == GameScreen.player)
		{
			GameScreen.setIsMoveEff(ismove: true);
		}
		if (!checkNullObject(3))
		{
			objFireMain.x = objBeFireMain.x + 30;
			if (Dir == 2)
			{
				objFireMain.x = objBeFireMain.x - 30;
			}
			objFireMain.y = objBeFireMain.y;
		}
		fraImgEff = new FrameImage(4, 20, 20);
		fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
		fraImgSub2Eff = new FrameImage(11, 40, 50);
		fraImgSub3Eff = new FrameImage(316, 44, 47);
		fRemove = 24;
	}

	public void createMon_1()
	{
		if (Dir == 0)
		{
			x1000 = x - 10;
			x -= 20;
		}
		else
		{
			x1000 = x + 10;
			x += 20;
		}
		y1000 = y - 12;
		fraImgEff = new FrameImage(114, 16, 13);
		fraImgSubEff = new FrameImage(35, 34, 27);
		fRemove = 6;
		vx = 3 * am_duong;
	}

	public void createMon_10()
	{
		fRemove = 5;
		fraImgEff = new FrameImage(120, 50, 25);
		if (typeEffect == 143)
		{
			fraImgEff = new FrameImage(2, 53, 29);
		}
		if (typeEffect == 149)
		{
			fraImgEff = new FrameImage(68, 28, 44);
		}
		numNextFrame = 1;
		if (Dir == 0)
		{
			x -= 10;
		}
		else
		{
			x += 10;
		}
		if (Dir == 0)
		{
			vx = -8;
		}
		else
		{
			vx = 8;
		}
	}

	public void createMon_11()
	{
		numNextFrame = 1;
		if (Dir == 0)
		{
			x -= 10;
		}
		else
		{
			x += 10;
		}
		if (Dir == 0)
		{
			vx1000 = -12;
		}
		else
		{
			vx1000 = 12;
		}
		vMax = 12;
		fraImgEff = new FrameImage(120, 50, 25);
		if (typeEffect == 144)
		{
			fraImgEff = new FrameImage(2, 53, 29);
		}
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
	}

	public void createCausu_1()
	{
		fRemove = 26;
		if (typeEffect == 227)
		{
			fraImgEff = new FrameImage(317, 90, 50);
			fraImgSubEff = new FrameImage(334, 75, 42);
		}
		else
		{
			fraImgEff = new FrameImage(1, 80, 40);
			fraImgSubEff = new FrameImage(62, 48, 34);
		}
		if (!checkNullObject(3))
		{
			objFireMain.x = objBeFireMain.x - am_duong * 48;
			objFireMain.y = objBeFireMain.y;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
		}
		for (int i = 0; i < 2; i++)
		{
			Point point = new Point();
			point.x = x + am_duong * 15;
			point.y = y;
			point.vx = am_duong * (5 + CRes.random(2));
			point.vy = CRes.random_Am_0(2);
			point.fRe = 6 + CRes.random(3);
			point.dis = ((CRes.random(3) != 0) ? 1 : 0);
			VecEff.addElement(point);
		}
	}

	public void createMorgan_2()
	{
		int num = 20;
		if (Dir == 2)
		{
			num = -20;
		}
		GameScreen.addEffectEnd(30, 0, x + num, y, 300, Dir, objMainEff);
		fRemove = 8;
		addSound(7);
	}

	public void createCabaji_1()
	{
		fRemove = 5;
		fraImgEff = new FrameImage(186, 19, 22);
		fraImgSubEff = new FrameImage(187, 20, 20);
		fraImgSub2Eff = new FrameImage(120, 50, 25);
		vMax = 14;
		int num = -14;
		if (Dir == 2)
		{
			num = 14;
		}
		x += num;
	}

	public void createBuggy_2()
	{
		fraImgEff = new FrameImage(125, 60, 44, 60, 44);
		fraImgSubEff = new FrameImage(126, 45, 45);
		fraImgSub2Eff = new FrameImage(3, 30, 50);
		fraImgSub3Eff = new FrameImage(128, 16, 16);
		vMax = 24;
		int num = -14;
		if (Dir == 2)
		{
			num = 14;
		}
		x1000 = x + num;
		y1000 = y + 14;
		fRemove = 49;
	}

	public void createBuggy_1()
	{
		fRemove = 5;
		int num = -25;
		if (Dir == 2)
		{
			num = 25;
		}
		x += num;
		fraImgEff = new FrameImage(124, 27, 22);
		vMax = 10;
	}

	public void createMohji_2()
	{
		fRemove = 8;
		fraImgEff = new FrameImage(120, 50, 25);
		int num = -25;
		if (Dir == 2)
		{
			num = 25;
		}
		x += num;
		y += 10;
	}

	public void createKuro_1()
	{
		fraImgEff = new FrameImage(45, 80, 25);
		fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
		fRemove = 18;
		objFireMain.isTanHinh = true;
		toY = objBeFireMain.y;
		y = objFireMain.y;
	}

	public void createJango_1()
	{
		fRemove = 5;
		fraImgEff = new FrameImage(131, 20, 10);
		fraImgSubEff = new FrameImage(27, 12, 12);
		fraImgSub2Eff = new FrameImage(120, 50, 25);
		vMax = 14;
		int num = -14;
		if (Dir == 2)
		{
			num = 14;
		}
		x += num;
	}

	public void createNyaban_3()
	{
		fraImgEff = new FrameImage(120, 50, 25);
		fRemove = 27;
		x1000 = -15;
		if (Dir == 2)
		{
			x1000 = 15;
		}
		vx = (toX - (x + x1000)) / 5;
	}

	public void createNyaban_2()
	{
		fraImgEff = new FrameImage(130, 48, 39);
		fRemove = 12;
		vx = (toX - x) / 5;
	}

	public void createNyaban_1()
	{
		fRemove = 10;
		fraImgEff = new FrameImage(120, 50, 25);
		int num = -14;
		if (Dir == 2)
		{
			num = 14;
		}
		x += num;
	}

	public void createCabaji_2()
	{
		fraImgEff = new FrameImage(129, 40, 80);
		fraImgSubEff = new FrameImage(76, 32, 70);
		toY = objBeFireMain.y;
		fRemove = 15;
	}

	public void createKurobi_1()
	{
		fRemove = 32;
		fraImgEff = new FrameImage(144, 37, 55);
		x1000 = -30;
		if (Dir == 2)
		{
			x1000 = 30;
		}
		y -= 5;
		GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
	}

	public void createChu_2()
	{
		vMax = 14;
		fraImgEff = new FrameImage(20, 10, 10);
		fRemove = 40;
		y -= 5;
		int num = 10;
		if (Dir == 2)
		{
			num = -10;
		}
		x += num;
		GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
	}

	public void createChu_1()
	{
		vMax = 14;
		fraImgEff = new FrameImage(20, 10, 10);
		fRemove = 20;
		y -= 5;
		int num = 10;
		if (Dir == 2)
		{
			num = -10;
		}
		x += num;
	}

	public void createHachi_2()
	{
		vMax = 14;
		fraImgEff = new FrameImage(81, 24, 24);
		if (typeEffect == 150)
		{
			fraImgEff = new FrameImage(83, 14, 14);
		}
		else if (typeEffect == 151)
		{
			fraImgEff = new FrameImage(80, 30, 15);
			frame = 0;
		}
		else if (typeEffect == 152)
		{
			fraImgEff = new FrameImage(80, 30, 15);
			frame = 1;
		}
		else if (typeEffect == 153)
		{
			fraImgEff = new FrameImage(80, 30, 15);
			frame = 2;
		}
		fRemove = 24;
		y -= 10;
		int num = 10;
		if (Dir == 2)
		{
			num = -10;
		}
		if (typeEffect == 113)
		{
			GameScreen.addEffectEnd(30, 0, x, y, 600, Dir, objMainEff);
		}
		else
		{
			fRemove = 8;
			vMax = 16;
		}
		x += num;
		addSound(32);
	}

	public void createDonKrieg_3()
	{
		fRemove = 30;
		fraImgEff = new FrameImage(137, 75, 65);
		plusxy = new int[2][];
		plusxy[0] = new int[2];
		plusxy[1] = new int[2];
		plusxy[0][0] = 0;
		plusxy[0][1] = -37;
		plusxy[1][0] = -28;
		plusxy[1][1] = -28;
		int num = 25;
		if (Dir == 2)
		{
			plusxy[1][0] = 28;
			num = -25;
		}
		GameScreen.addEffectEnd(30, 0, x + num, y, 300, Dir, objMainEff);
	}

	public void createDonKrieg_1()
	{
		fraImgEff = new FrameImage(134, 30, 42);
		fraImgSubEff = new FrameImage(135, 20, 20);
		vMax = 12;
		int num = 10;
		x1000 = 15;
		xplus = -10;
		if (Dir == 0)
		{
			num = -10;
			x1000 = -15;
			xplus = 10;
		}
		x += num;
		y -= 5;
		fRemove = 22;
	}

	public void createDonKrieg_2()
	{
		fraImgEff = new FrameImage(134, 30, 42);
		fraImgSubEff = new FrameImage(136, 16, 12);
		fraImgSub2Eff = new FrameImage(131, 20, 10);
		vMax = 8;
		int num = 10;
		x1000 = 15;
		xplus = -10;
		if (Dir == 0)
		{
			num = -10;
			x1000 = -15;
			xplus = 10;
		}
		x += num;
		y -= 5;
		fRemove = 22;
		xArchor = x;
		yArchor = y;
	}

	public void createGhin_2()
	{
		objFireMain.isPaintWeapon = false;
		fRemove = 30;
		fraImgEff = new FrameImage(133, 36, 44);
		int num = 3;
		vx = -8;
		if (Dir == 2)
		{
			num = -3;
			vx = 8;
		}
		Point point = new Point(x - 15, y + num);
		point.frame = 0;
		point.dis = 4;
		VecEff.addElement(point);
		Point point2 = new Point(x + 15, y - num);
		point2.frame = 1;
		point2.dis = 4;
		VecEff.addElement(point2);
	}

	public void createGhin_1()
	{
		fraImgEff = new FrameImage(132, 60, 35);
		int num = 25;
		int num2 = 10;
		if (Dir == 0)
		{
			num = -25;
		}
		if (typeEffect == 65 || typeEffect == 70)
		{
			fraImgEff = new FrameImage(215, 60, 35);
			if (typeEffect == 70)
			{
				vMax = 16;
				fraImgSubEff = new FrameImage(216, 18, 18);
			}
			num = 28;
			if (Dir == 0)
			{
				num = -28;
			}
			num2 = 13;
			levelPaint = -1;
		}
		fRemove = 6;
		x += num;
		y += num2;
	}

	public void createPearl_2()
	{
		fRemove = 34;
		vMax = 12;
		fraImgEff = new FrameImage(78, 22, 28);
		fraImgSubEff = new FrameImage(20, 10, 10);
		int num = 10;
		Point point = new Point(x - 18, y - num);
		point.frame = CRes.random(3);
		VecEff.addElement(point);
		Point point2 = new Point(x + 18, y - num);
		point2.frame = CRes.random(3);
		VecEff.addElement(point2);
	}

	public void createPearl_1()
	{
		fRemove = 10;
		int num = 15;
		if (Dir == 0)
		{
			num = -15;
		}
		GameScreen.addEffectEnd(30, 0, x - num, y, 300, Dir, objMainEff);
		x += num;
	}

	public void createKuro_2()
	{
		fraImgEff = new FrameImage(45, 80, 25);
		fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
		fRemove = 38;
		y = objFireMain.y;
		x1000 = x;
		y1000 = y;
	}

	public void createArlong_3()
	{
		fraImgEff = new FrameImage(148, 104, 85);
		fraImgSubEff = new FrameImage(149, 73, 73);
		fraImgSub2Eff = new FrameImage(150, 66, 70, 42, 45);
		objFireMain.isTanHinh = false;
		plusxy = new int[5][];
		plusxy[0] = new int[2];
		plusxy[1] = new int[2];
		plusxy[2] = new int[2];
		plusxy[3] = new int[2];
		plusxy[4] = new int[2];
		plusxy[0][0] = -15;
		plusxy[0][1] = -30;
		plusxy[1][0] = -30;
		plusxy[1][1] = 10;
		plusxy[2][0] = 38;
		plusxy[2][1] = -30;
		plusxy[3][0] = -30;
		plusxy[3][1] = -20;
		plusxy[4][0] = -20;
		plusxy[4][1] = 20;
		if (Dir == 2)
		{
			for (int i = 0; i < plusxy.Length; i++)
			{
				plusxy[i][0] = -plusxy[i][0];
			}
		}
		GameScreen.addEffectEnd(30, 0, x + plusxy[2][0], y + plusxy[2][1], 350, Dir, objMainEff);
		fRemove = 20;
	}

	public void createArlong_2()
	{
		fraImgEff = new FrameImage(146, 96, 24);
		fraImgSubEff = new FrameImage(147, 48, 12);
		fraImgSub2Eff = new FrameImage(256, 80, 40);
		objFireMain.isTanHinh = false;
		fRemove = 40;
		vMax = 30;
	}

	public void createArlong_1()
	{
		fraImgEff = new FrameImage(145, 80, 80, 60, 60);
		fRemove = 12;
		objFireMain.isTanHinh = false;
		if (vecObjsBeFire.size() > 1)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill == null)
				{
					continue;
				}
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					Point point = new Point(mainObject.x, mainObject.y - mainObject.hOne / 2);
					if (x < point.x)
					{
						point.dis = 2;
					}
					else
					{
						point.dis = 0;
					}
					VecEff.addElement(point);
				}
			}
		}
		else
		{
			int num = -15;
			if (Dir == 2)
			{
				num = 15;
			}
			x += num;
			y -= 10;
		}
	}

	public void createKurobi_2()
	{
		fraImgEff = new FrameImage(144, 37, 55);
		fRemove = 30;
		x1000 = -25;
		y1000 = -25;
		if (Dir == 2)
		{
			x1000 = 25;
		}
		vx = (toX - (x + x1000)) / 5;
		GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
	}

	public void createUrgot3()
	{
		fRemove = 40;
		fraImgEff = new FrameImage(179, 54, 25);
		for (int i = 0; i < 5; i++)
		{
			Point point = new Point();
			point.y = -CRes.random(30);
			point.vy = CRes.random_Am(3, 8);
			point.frame = CRes.random(3);
			VecEff.addElement(point);
		}
	}

	public void createXerath3()
	{
		xplus = 4;
		yplus = 6;
		int num = 0;
		for (int i = 1; i <= yplus; i++)
		{
			num -= i * xplus;
		}
		fraImgEff = new FrameImage(83, 14, 14);
		fraImgSubEff = new FrameImage(51, 9, 9);
		fraImgSub2Eff = new FrameImage(52, 5, 5);
		x = objFireMain.x;
		y = objFireMain.y - objFireMain.hOne / 2;
		x1000 = x * 1000;
		y1000 = y;
		int num2 = num - (toY - y);
		int num3 = yplus - 1;
		if (num2 < 0)
		{
			for (int j = 1; j < 60; j++)
			{
				num2 += j * xplus;
				if (num2 >= 0)
				{
					num3 += j;
					break;
				}
			}
		}
		vy1000 = -(xplus * yplus);
		vx1000 = (toX - x) * 1000 / num3;
		fRemove = num3;
	}

	public void create_Zoro_S3_L2()
	{
		fraImgEff = new FrameImage(165, 27, 50);
		fraImgSubEff = new FrameImage(167, 78, 22);
		fraImgSub2Eff = new FrameImage(166, 50, 60);
		fRemove = 36;
		vMax = 12;
		if (isAddSound)
		{
			mSound.playSound(8, mSound.volumeSound);
		}
		GameScreen.addEffectEnd(30, 0, x, y, 500, Dir, objMainEff);
		int num = -15;
		x1000 = x + 15;
		y1000 = objFireMain.y - 22;
		if (Dir == 2)
		{
			num = 15;
			x1000 = x - 63;
		}
		x += num;
		y -= 5;
	}

	public void create_Zoro_S3_L1()
	{
		fraImgEff = new FrameImage(165, 27, 50);
		fRemove = 30;
		if (isAddSound)
		{
			mSound.playSound(8, mSound.volumeSound);
		}
		GameScreen.addEffectEnd(30, 0, x, y, 400, Dir, objMainEff);
		int num = -15;
		if (Dir == 2)
		{
			num = 15;
		}
		x += num;
		y -= 5;
	}

	public void createMonster_NEM_BOOM_2()
	{
		fraImgEff = new FrameImage(188, 9, 16);
		vMax = 12;
		y = objFireMain.y - objBeFireMain.hOne / 2;
		if (Dir == 0)
		{
			x -= 15;
		}
		else
		{
			x += 15;
		}
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
	}

	public void create_Ussop_S2_L3()
	{
		vMax = 12;
		fRemove = 34;
		GameScreen.addEffectEnd(30, 0, x + am_duong * 25, y - 5, 400, Dir, objMainEff);
		fraImgEff = new FrameImage(185, 55, 25);
		if (typeEffect == 193)
		{
			fraImgSubEff = new FrameImage(285, 111, 90);
			mframe = new int[4] { 0, 1, 2, 1 };
		}
		else if (typeEffect == 225)
		{
			fRemove = 40;
			fraImgEff = new FrameImage(333, 55, 25);
			fraImgSubEff = new FrameImage(332, 111, 90);
			mframe = new int[4] { 0, 1, 2, 1 };
		}
		else if (typeEffect == 302)
		{
			fraImgEff = new FrameImage(419, 2);
			fraImgSubEff = new FrameImage(404, 3);
			mframe = new int[4] { 0, 1, 2, 1 };
			fraImgSub3Eff = new FrameImage(405, 3);
			int num = x - 50 * am_duong;
			int xdich = num - x;
			int ydich = 0;
			VecEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y, num, y));
		}
		else
		{
			fraImgSubEff = new FrameImage(184, 111, 70, 79, 50);
			mframe = new int[2] { 0, 1 };
		}
		fraImgSub2Eff = new FrameImage(251, 52, 21);
	}

	public void create_Ussop_S2_L7()
	{
		vMax = 12;
		fRemove = 34;
		GameScreen.addEffectEnd(30, 0, x + am_duong * 25, y - 5, 400, Dir, objMainEff);
		fraImgEff = new FrameImage(419, 2);
		fraImgSubEff = new FrameImage(404, 3);
		fraImgSub2Eff = new FrameImage(251, 52, 21);
		fraImgSub3Eff = new FrameImage(405, 3);
		mframe = new int[4] { 0, 1, 2, 1 };
		int num = x - 50 * am_duong;
		int xdich = num - x;
		int ydich = 0;
		VecEff.addElement(create_Speed(xdich, ydich, new Point_Focus(), x, y, num, y));
	}

	public void createUssopSkill1_Lv3_New()
	{
		fraImgEff = new FrameImage(53, 9, 9);
		fraImgSubEff = new FrameImage(183, 20, 54);
		vMax = 24;
		fRemove = 25;
		y -= 6;
		if (Dir == 0)
		{
			x -= 30;
		}
		else
		{
			x += 30;
		}
		int xdich = toX - x;
		int ydich = toY - y;
		Point_Focus p = new Point_Focus();
		p = create_Speed(xdich, ydich, p);
		p.frame = 1;
		GameScreen.addEffectEnd(1, 0, x, y, Dir, objMainEff);
		VecEff.addElement(p);
		xArchor = objFireMain.x;
		yArchor = objFireMain.y;
	}

	public void createUssopSkill1_Lv3_SHORT()
	{
		fraImgEff = new FrameImage(53, 9, 9);
		fraImgSubEff = new FrameImage(183, 20, 54);
		vMax = 24;
		fRemove = 16;
		y -= 6;
		if (Dir == 0)
		{
			x -= 30;
		}
		else
		{
			x += 30;
		}
		int xdich = toX - x;
		int ydich = toY - y;
		Point_Focus p = new Point_Focus();
		p = create_Speed(xdich, ydich, p);
		p.frame = 1;
		GameScreen.addEffectEnd(1, 0, x, y, Dir, objMainEff);
		VecEff.addElement(p);
		xArchor = objFireMain.x;
		yArchor = objFireMain.y;
	}

	public void createSanji_s2_l3_New()
	{
		y = objFireMain.y;
		fraImgEff = new FrameImage(183, 20, 54);
		fRemove = 44;
		GameScreen.addEffectEnd(30, 0, x, y - objFireMain.hOne / 2, 300, Dir, objMainEff);
	}

	public void createSanji_s2_l3_New_SHORT()
	{
		y = objFireMain.y;
		fraImgEff = new FrameImage(183, 20, 54);
		fRemove = 24;
	}

	public void createSanji_s1_l3_New()
	{
		fraImgEff = new FrameImage(183, 20, 54);
		if (typeEffect == 177)
		{
			fraImgEff = new FrameImage(265, 20, 54);
		}
		fRemove = 50;
		GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
	}

	public void createSanji_s1_l3_SHORT()
	{
		fraImgEff = new FrameImage(183, 20, 54);
		fRemove = 16;
	}

	public bool checkNullObject(int type)
	{
		if (type == 1 && (objFireMain == null || objFireMain.returnAction()))
		{
			return true;
		}
		if (type == 2 && (objBeFireMain == null || objBeFireMain.returnAction()))
		{
			return true;
		}
		if (type == 3 && (objFireMain == null || objFireMain.returnAction() || objBeFireMain == null || objBeFireMain.returnAction()))
		{
			return true;
		}
		return false;
	}

	public Point createPointCausu1(Point p)
	{
		p.x = x + am_duong * (5 + CRes.random_Am_0(20));
		p.y = y - 10 + CRes.random_Am_0(25);
		p.vx = am_duong * CRes.random(7, 18);
		p.fRe = CRes.random(2, 5);
		p.frame = CRes.random(fraImgEff.nFrame);
		p.dis = Dir;
		return p;
	}

	public void create_Sanji_Sea_Lv3()
	{
		fraImgEff = new FrameImage(183, 20, 54);
		fraImgSubEff = new FrameImage(8, 40, 47, 40, 47);
		fRemove = 40;
		y = objFireMain.y;
	}

	public void create_Devil_FIRE1()
	{
		addSoundBuff();
		if (typeEffect == 259 || typeEffect == 260 || typeEffect == 261)
		{
			if (typeEffect == 259)
			{
				frameSuper = 1;
			}
			else if (typeEffect == 260)
			{
				frameSuper = 2;
			}
			else if (typeEffect == 261)
			{
				frameSuper = 3;
			}
			fraImgSubEff = new FrameImage(336, 74, 30, (sbyte)3, frameSuper);
			fraImgSub2Eff = new FrameImage(78, 22, 28, (sbyte)5, frameSuper);
			fraImgEff = new FrameImage(7, 34, 64, (sbyte)2, frameSuper);
		}
		else
		{
			fraImgEff = new FrameImage(7, 34, 64, 2);
			if (typeEffect == 228)
			{
				fraImgSubEff = new FrameImage(336, 74, 30, 3);
				fraImgSub2Eff = new FrameImage(78, 22, 28, 5);
			}
		}
		fRemove = 30;
		GameScreen.addEffectEnd(30, 0, x, y, 250, Dir, objMainEff);
		toY = objBeFireMain.y;
	}

	public void create_Devil_FIRE2()
	{
		addSoundBuff();
		frameSuper = 0;
		if (typeEffect == 262)
		{
			frameSuper = 1;
		}
		else if (typeEffect == 263)
		{
			frameSuper = 2;
		}
		else if (typeEffect == 264)
		{
			frameSuper = 3;
		}
		fraImgEff = new FrameImage(32, 45, 45, (sbyte)5, frameSuper);
		fraImgSubEff = new FrameImage(78, 22, 28, (sbyte)5, frameSuper);
		fraImgSub2Eff = new FrameImage(224, 22, 28, (sbyte)5, frameSuper);
		fraImgSub3Eff = new FrameImage(38, 50, 80, (sbyte)3, frameSuper);
		fRemove = 30;
		vMax = 12;
		y = objFireMain.y;
		GameScreen.addEffectEnd(30, 0, x, y - objFireMain.hOne / 2, 1200, Dir, objMainEff);
	}

	public void create_ho_den_vu_tru()
	{
		Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
		if (object_Effect_Skill != null)
		{
			MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
			if (mainObject != null)
			{
				x = mainObject.x;
				y = mainObject.y;
			}
		}
		for (int i = 0; i < radian.Length; i++)
		{
			GameScreen.addEffectEnd(166, 0, 2 * (CRes.getcos(radian[i]) * CR) / 1024 + x, CRes.getsin(radian[i]) * CR / 1024 + y, Dir, objMainEff);
		}
	}

	public void create_Devil_ICE1()
	{
		addSoundBuff();
		fraImgEff = new FrameImage(37, 31, 74);
		fraImgSub2Eff = new FrameImage(40, 63, 20);
		fraImgSub3Eff = new FrameImage(41, 40, 40);
		y = objFireMain.y;
		GameScreen.addEffectEnd(30, 0, x, y - objFireMain.hOne / 2, 1200, Dir, objMainEff);
		mframe = new int[41]
		{
			-1, -1, -1, -1, 0, 0, 0, 0, 1, 1,
			1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 2, 2, 2, 2, 1, 1, 1, 0, 0,
			0
		};
		fRemove = 30;
	}

	public void create_Devil_ICE2()
	{
		addSoundBuff();
		fraImgEff = new FrameImage(5, 80, 50);
		fraImgSub3Eff = new FrameImage(41, 40, 40);
		fraImgSubEff = new FrameImage(43, 84, 110);
		plusxy = new int[3][];
		plusxy[0] = new int[2];
		plusxy[1] = new int[2];
		plusxy[2] = new int[2];
		plusxy[0][0] = -40;
		plusxy[0][1] = -35 + objFireMain.lechYHead;
		plusxy[1][0] = 20;
		plusxy[1][1] = -67;
		plusxy[2][0] = 47;
		plusxy[2][1] = -50 + objFireMain.lechYHead;
		fRemove = 30;
		vMax = 10;
		y = objFireMain.y;
		GameScreen.addEffectEnd(30, 0, x, y - objFireMain.hOne / 2, 1200, Dir, objMainEff);
	}

	public void create_Devil_Smoker1()
	{
		addSoundBuff();
		fraImgEff = new FrameImage(58, 40, 27);
		fraImgSubEff = new FrameImage(57, 42, 50, 32, 38);
		fraImgSub2Eff = new FrameImage(61, 24, 30);
		if (typeEffect == 232)
		{
			fraImgSub3Eff = new FrameImage(85, 34, 34, 28, 28);
		}
		fRemove = 30;
		vMax = 12;
	}

	public void create_Devil_Smoker2()
	{
		addSoundBuff();
		fraImgEff = new FrameImage(64, 50, 45);
		fraImgSubEff = new FrameImage(63, 71, 60, 50, 40);
		fraImgSub2Eff = new FrameImage(65, 59, 65);
		fraImgSub3Eff = new FrameImage(61, 24, 30);
		if (typeEffect == 234)
		{
			fraImgSub4Eff = new FrameImage(85, 34, 34, 28, 28);
		}
		fRemove = 30;
		vMax = 26;
	}

	public void createSmoker1()
	{
		fraImgEff = new FrameImage(64, 50, 45);
		fraImgSubEff = new FrameImage(63, 71, 60, 51, 43);
		fraImgSub2Eff = new FrameImage(86, 32, 79);
		fraImgSub3Eff = new FrameImage(61, 24, 30);
		GameScreen.addEffectEnd(30, 0, x, y, 100, Dir, objMainEff);
		fRemove = 20;
		vMax = 26;
	}

	public void createSmoker2()
	{
		fraImgEff = new FrameImage(86, 32, 79);
		fraImgSubEff = new FrameImage(87, 35, 35, 28, 28);
		GameScreen.addEffectEnd(30, 0, x, y, 100, Dir, objMainEff);
		frame = 5;
		if (Dir == 2)
		{
			frame = 6;
		}
		fRemove = 20;
		vMax = 14;
	}

	public void createZoro_S2_L1_New()
	{
		fRemove = 6;
	}

	private void createMissGold_1()
	{
		fraImgEff = new FrameImage(212, 33, 24);
		fRemove = 24;
	}

	private void createMr3_2()
	{
		fraImgEff = new FrameImage(211, 35, 22);
		fraImgSubEff = new FrameImage(32, 45, 45, 34, 34);
		fraImgSub2Eff = new FrameImage(160, 9, 14);
		fRemove = 20;
		vMax = 16;
	}

	private void createMr3_1()
	{
		fraImgEff = new FrameImage(211, 35, 22);
		GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
		fRemove = 20;
		vMax = 16;
	}

	private void create_Wapol4()
	{
		fraImgEff = new FrameImage(20, 10, 10);
		if (Dir == 0)
		{
			x -= 25;
			vx = -48;
		}
		else
		{
			x += 25;
			vx = 48;
		}
		y += 7;
		x1000 = x;
		fRemove = 20;
	}

	private void createWapol3()
	{
		fraImgEff = new FrameImage(20, 10, 10);
		if (Dir == 0)
		{
			x -= 5;
		}
		else
		{
			x += 5;
		}
		y += 7;
		fRemove = 25;
		vMax = 14;
	}

	private void createWapol2()
	{
		fraImgEff = new FrameImage(209, 32, 46);
		if (Dir == 0)
		{
			x -= 10;
		}
		else
		{
			x += 10;
		}
		y -= 5;
		numNextFrame = 2;
		vy = -3;
		fRemove = 4;
	}

	private void createWapol()
	{
		levelPaint = -1;
		fraImgEff = new FrameImage(208, 50, 57);
		fraImgSubEff = new FrameImage(144, 37, 55);
		vMax = 14;
		y = objFireMain.y;
		toY = objBeFireMain.y;
		if (objFireMain.plashNow != null)
		{
			objFireMain.plashNow.setIsNextf(1);
		}
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
	}

	private void createKuromarimo()
	{
		fraImgEff = new FrameImage(207, 14, 14);
		vMax = 14;
		if (Dir == 0)
		{
			x -= 5;
		}
		else
		{
			x += 5;
		}
		y -= 20;
		if (CRes.random(2) == 0)
		{
			subType = 0;
			toX += 6;
		}
		else
		{
			subType = 1;
			toX -= 6;
		}
		if (!checkNullObject(2))
		{
			toY = objBeFireMain.y - objBeFireMain.hOne / 2;
		}
		toY += 14;
		int xdich = toX - x;
		int ydich = toY - y;
		create_Speed(xdich, ydich, null);
	}

	private void createChess()
	{
		fraImgEff = new FrameImage(205, 20, 20);
		fraImgSubEff = new FrameImage(206, 20, 20);
		vMax = 18;
		if (Dir == 0)
		{
			x -= 10;
		}
		else
		{
			x += 10;
		}
		y -= 10;
		int num = toX - x;
		int num2 = toY - y;
		int frameAngle = CRes.angle(num, num2);
		create_Speed(num, num2, null);
		frame = setFrameAngle(frameAngle);
	}

	private void create_Zoro_S3_L3()
	{
		Dir = (sbyte)objFireMain.type_left_right;
		if (typeEffect == 185 || typeEffect == 217)
		{
			fraImgEff = new FrameImage(280, 50, 74, 2);
		}
		else
		{
			fraImgEff = new FrameImage(165, 27, 50);
		}
		fraImgSubEff = new FrameImage(167, 78, 22);
		fraImgSub2Eff = new FrameImage(16, 55, 55);
		fraImgSub3Eff = new FrameImage(17, 55, 55);
		fraImgSub4Eff = new FrameImage(8, 40, 47, 40, 47);
		fRemove = 30;
		vMax = 12;
		if (typeEffect == 283)
		{
			fraImgEff = new FrameImage(421, 50, 74, 2);
			fraImgSub2Eff = new FrameImage(409, 4);
		}
		int num = -15;
		xArchor = objFireMain.x;
		yArchor = objFireMain.y;
		x1000 = x - 5;
		y1000 = objFireMain.y - 22;
		objFireMain.dy = 0;
		if (Dir == 2)
		{
			num = 15;
			x1000 = x - 73;
		}
		x += num;
		y -= 5;
	}

	private void create_Zoro_S3_L7()
	{
		Dir = (sbyte)objFireMain.type_left_right;
		fraImgEff = new FrameImage(440, 12);
		fraImgSubEff = new FrameImage(442, 14);
		fraImgSub2Eff = new FrameImage(445, 9);
		fraImgSub3Eff = new FrameImage(17, 55, 55);
		fraImgSub4Eff = new FrameImage(8, 40, 47, 40, 47);
		fRemove = 30;
		vMax = 12;
		numNextFrame = 2;
		int num = -15;
		xArchor = objFireMain.x;
		yArchor = objFireMain.y;
		x1000 = x - 5;
		y1000 = objFireMain.y - 22;
		objFireMain.dy = 0;
		if (Dir == 2)
		{
			num = 15;
			x1000 = x - 73;
		}
		x += num;
		y -= 5;
	}

	private void createZoro_New2_SHORT()
	{
		fRemove = 24;
		vMax = 12;
		fraImgEff = new FrameImage(8, 40, 47, 40, 47);
	}

	private void beginUpdate()
	{
	}

	public void updateLuffy1()
	{
		if (objBeFireMain != null && f % 3 == 0 && indexObjBefire < vecObjsBeFire.size())
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
			indexObjBefire++;
			if (object_Effect_Skill != null)
			{
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					sbyte dir = 0;
					if (objFireMain.x < mainObject.x)
					{
						dir = 2;
					}
					int num = 12;
					if (Dir == 0)
					{
						num = -12;
					}
					sbyte b = 0;
					if (typeEffect == 37)
					{
						b = 2;
					}
					GameScreen.addEffectEnd_ObjTo(13, b, objFireMain.x + num, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, mainObject.ID, mainObject.typeObject, dir, objMainEff);
				}
			}
		}
		if (f >= fRemove)
		{
			if (VecEff.size() == 0)
			{
				removeEff();
			}
		}
		else if (typeEffect == 37 && f % 2 == 0)
		{
			Point o = new Point(x + CRes.random_Am_0(15), y + CRes.random_Am_0(20));
			VecEff.addElement(o);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.f++;
			if (point.f >= 3)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
	}

	public void updateSanji1()
	{
		if (f == 1)
		{
			if (isAddSound)
			{
				mSound.playSound(16, mSound.volumeSound);
			}
			int num = 15;
			if (Dir == 0)
			{
				num = -15;
			}
			GameScreen.addEffectEnd(30, 0, x + num, y - objFireMain.hOne / 2, 300, Dir, objMainEff);
		}
		if (f == 8 && objFireMain != null)
		{
			int num2 = 27;
			if (Dir == 0)
			{
				num2 = -27;
			}
			if (typeEffect == 47 || typeEffect == 48)
			{
				sbyte b = 0;
				if (typeEffect == 48)
				{
					b = 1;
				}
				if (!checkNullObject(2))
				{
					GameScreen.addEffectEnd_ObjTo(37, b, x + num2, y - objFireMain.hOne / 2, objBeFireMain.ID, objBeFireMain.typeObject, Dir, objMainEff);
				}
			}
		}
		if (f >= fRemove)
		{
			objFireMain.dx = 0;
			removeEff();
		}
	}

	public void updateZoro1()
	{
		if (!checkNullObject(1))
		{
			objFireMain.isTanHinh = true;
			objFireMain.Action = 2;
			objFireMain.vx = vx;
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
				objFireMain.Action = 0;
			}
			GameScreen.addEffectEnd(86, 0, x + ((Dir == 0) ? 20 : (-20)), y, Dir, objMainEff);
			GameScreen.addEffectEnd(9, 0, toX, toY + 25, Dir, objMainEff);
			removeEff();
		}
	}

	public void updateZoro2()
	{
		if (f < 2)
		{
			xplus = -3 + f * 3;
		}
		if (f == 4)
		{
			if (!checkNullObject(1) && objFireMain == GameScreen.player)
			{
				sendMove(x, toX, y, toY + fraImgEff.frameHeight / 2);
			}
			xplus = 0;
			x = toX;
			y = toY;
		}
		if (f > 5)
		{
			xplus = 3 - (f - 5) * 3;
		}
		if (f < fRemove)
		{
			return;
		}
		if (!checkNullObject(1))
		{
			objFireMain.isTanHinh = false;
			if (objFireMain.plashNow != null)
			{
				objFireMain.plashNow.setIsNextf(0);
			}
		}
		if (!checkNullObject(2))
		{
			GameScreen.addEffectEnd(9, 0, objBeFireMain.x, objBeFireMain.y + 25, Dir, objMainEff);
		}
		GameScreen.addEffectEnd(86, 0, x + ((Dir == 0) ? (-10) : 10), y - 25, Dir, objMainEff);
		removeEff();
	}

	public void updateUssopSea1()
	{
		if ((f == 8 || f == 12) && isAddSound)
		{
			mSound.playSound(25, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				VecEff.removeElement(point_Focus);
				GameScreen.addEffectEnd(1, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				i--;
			}
		}
		if (f == 10 || f == 13 || f == 15)
		{
			if (!checkNullObject(2))
			{
				toX = objBeFireMain.x;
				toY = objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(8);
			}
			setAngle();
			if (!checkNullObject(1))
			{
				objFireMain.Dir = Dir;
			}
			int num = toX - x;
			int num2 = toY - y;
			int frameAngle = CRes.angle(num, num2);
			Point_Focus p = new Point_Focus();
			p = create_Speed(num, num2, p);
			p.frame = setFrameAngle(frameAngle);
			VecEff.addElement(p);
			GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
			GameScreen.addEffectEnd(93, 2, x, y, Dir, objMainEff);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateUssopSea2()
	{
		if ((f == 8 || f == 12) && isAddSound)
		{
			mSound.playSound(25, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(81, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				GameScreen.addEffectEnd(1, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 4 || f == 9 || f == 14 || f == 19)
		{
			if (!checkNullObject(3))
			{
				toX = objBeFireMain.x;
				toY = objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(8);
				y = objFireMain.y - objFireMain.hOne / 2 - 6;
				if (Dir == 0)
				{
					x = objFireMain.x - 22;
				}
				else
				{
					x = objFireMain.x + 22;
				}
			}
			GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
			setAngle();
			if (!checkNullObject(1))
			{
				objFireMain.Dir = Dir;
			}
			int num = 1;
			if (f == 9 || f == 19)
			{
				num = 2;
			}
			for (int j = 0; j < num; j++)
			{
				if (j == 1)
				{
					y -= 10;
				}
				int num2 = toX - x;
				int num3 = toY - y;
				int frameAngle = CRes.angle(num2, num3);
				Point_Focus p = new Point_Focus();
				p = create_Speed(num2, num3, p);
				p.frame = setFrameAngle(frameAngle);
				VecEff.addElement(p);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateUssopSea3()
	{
		if ((f == 4 || f == 8 || f == 12 || f == 16) && isAddSound)
		{
			mSound.playSound(25, mSound.volumeSound);
			if (f == 8 || f == 16)
			{
				mSound.playSound(15, mSound.volumeSound);
			}
		}
		if (f == 10 && !checkNullObject(2))
		{
			GameScreen.addEffectEnd(108, 1, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f > 4 && f % 3 == 0 && f <= 19)
		{
			GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
			int num = CRes.random(6, 9);
			for (int j = 0; j < num; j++)
			{
				Point_Focus point_Focus2 = new Point_Focus();
				point_Focus2.x = x * 10;
				point_Focus2.y = y * 10;
				point_Focus2.vx = vMax * 10 * am_duong + CRes.random_Am_0(7);
				point_Focus2.vy = -(num * 13) / 2 + 13 * j;
				point_Focus2.frame = 0;
				point_Focus2.fRe = 16;
				point_Focus2.dis = Dir;
				VecEff.addElement(point_Focus2);
			}
			addVir(5, 5, 10, isPlayer: true);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateUssop2()
	{
		if (f == 3)
		{
			GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
		}
		int a = 12;
		if ((f == 0 || f == 3) && isAddSound)
		{
			mSound.playSound(21, mSound.volumeSound);
		}
		if (f == fRemove - 2)
		{
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
			GameScreen.addEffectEnd(93, 1, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
		}
		if (f >= fRemove)
		{
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
			GameScreen.addEffectEnd(93, 1, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
			removeEff();
		}
	}

	public void updateUssop_Skill2()
	{
		if (f == 3 && isAddSound)
		{
			mSound.playSound(20, mSound.volumeSound);
		}
		if (f == 1)
		{
			GameScreen.addEffectEnd(5, 0, x, y, Dir, objMainEff);
		}
		if (f < fRemove)
		{
			return;
		}
		int a = 12;
		GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
		if (typeEffect == 64)
		{
			if (isAddSound)
			{
				mSound.playSound(19, mSound.volumeSound);
			}
			GameScreen.addEffectEnd(12, 1, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
		}
		else if (typeEffect == 66)
		{
			if (isAddSound)
			{
				mSound.playSound(14, mSound.volumeSound);
			}
			setAva(1, objBeFireMain);
			GameScreen.addEffectEnd(4, 2, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
		}
		GameScreen.addEffectEnd(93, 2, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
		removeEff();
	}

	public void updateNami1()
	{
		if (f <= 1)
		{
			return;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f < point_Focus.fRe)
			{
				continue;
			}
			if (isAddSound)
			{
				mSound.playSound(19, mSound.volumeSound);
			}
			setAva(1, objBeFireMain);
			sbyte subtype = 0;
			if (typeEffect == 9)
			{
				GameScreen.addEffectEnd(3, 0, toX, toY, Dir, objMainEff);
			}
			else if (typeEffect == 53)
			{
				GameScreen.addEffectEnd(38, 1, toX, toY, Dir, objMainEff);
				subtype = 1;
			}
			else if (typeEffect == 163)
			{
				if (isAddSound)
				{
					mSound.playSound(17, mSound.volumeSound);
				}
				addVir(5, 5, 10, isPlayer: true);
				GameScreen.addEffectEnd(42, 0, toX, toY, Dir, objMainEff);
				subtype = 1;
			}
			GameScreen.addEffectEnd(6, subtype, toX, toY, Dir, objMainEff);
			GameScreen.addEffectEnd(93, 1, toX, toY, Dir, objMainEff);
			VecEff.removeElement(point_Focus);
			i--;
		}
		if (VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateNami1_SHORT()
	{
		if (f == 12 || f == 22)
		{
			y += 5;
			int num = 0;
			int xdich = toX - x;
			int ydich = toY - num - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			if (f == 22)
			{
				p.frame = 1;
			}
			VecEff.addElement(p);
			if (isAddSound)
			{
				mSound.playSound(18, mSound.volumeSound);
			}
		}
		if (typeEffect == 222 || typeEffect == 312)
		{
			for (int i = 0; i < VecSubEff.size(); i++)
			{
				Point obj = (Point)VecSubEff.elementAt(i);
				obj.f++;
				if (obj.f / 2 >= fraImgSub3Eff.nFrame)
				{
					VecSubEff.removeElementAt(i);
					i--;
				}
			}
		}
		if (f > 1)
		{
			for (int j = 0; j < VecEff.size(); j++)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
				if ((typeEffect == 222 || typeEffect == 312) && !GameCanvas.lowGraphic)
				{
					Point o = new Point(point_Focus.x, point_Focus.y);
					VecSubEff.addElement(o);
				}
				point_Focus.update_Vx_Vy();
				if (point_Focus.f <= point_Focus.fRe)
				{
					continue;
				}
				if (isAddSound)
				{
					mSound.playSound(19, mSound.volumeSound);
				}
				setAva(1, objBeFireMain);
				sbyte subtype = 1;
				if (typeEffect == 190)
				{
					subtype = 2;
				}
				else if (typeEffect == 222 || typeEffect == 312)
				{
					subtype = 3;
				}
				if (isAddSound)
				{
					mSound.playSound(17, mSound.volumeSound);
				}
				addVir(5, 5, 10, isPlayer: true);
				if (point_Focus.frame == 1)
				{
					int subtype2 = 2;
					if (typeEffect == 190 || typeEffect == 222 || typeEffect == 312)
					{
						subtype2 = 8;
						GameScreen.addEffectEnd(108, 3, toX, toY, Dir, objMainEff);
					}
					GameScreen.addEffectEnd(54, subtype2, toX, toY, Dir, objMainEff);
				}
				else if (!GameCanvas.lowGraphic)
				{
					if (typeEffect == 222)
					{
						GameScreen.addEffectEnd(139, 0, toX, toY, Dir, objMainEff);
					}
					if (typeEffect == 312)
					{
						GameScreen.addEffectEnd(139, 1, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
					}
				}
				GameScreen.addEffectEnd(42, 0, toX, toY, Dir, objMainEff);
				GameScreen.addEffectEnd(6, subtype, toX, toY, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 8, toX, toY, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				j--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void update_Nami_S2_L7()
	{
		if (f == 12 || f == 22)
		{
			y += 5;
			int num = 0;
			int xdich = toX - x;
			int ydich = toY - num - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			if (f == 22)
			{
				p.frame = 1;
			}
			VecEff.addElement(p);
			if (isAddSound)
			{
				mSound.playSound(18, mSound.volumeSound);
			}
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point obj = (Point)VecSubEff.elementAt(i);
			obj.f++;
			if (obj.f / 2 >= fraImgSub3Eff.nFrame)
			{
				VecSubEff.removeElementAt(i);
				i--;
			}
		}
		if (f > 1)
		{
			for (int j = 0; j < VecEff.size(); j++)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
				if (!GameCanvas.lowGraphic)
				{
					Point o = new Point(point_Focus.x, point_Focus.y);
					VecSubEff.addElement(o);
				}
				point_Focus.update_Vx_Vy();
				if (point_Focus.f > point_Focus.fRe)
				{
					if (isAddSound)
					{
						mSound.playSound(19, mSound.volumeSound);
					}
					setAva(1, objBeFireMain);
					sbyte subtype = 3;
					if (isAddSound)
					{
						mSound.playSound(17, mSound.volumeSound);
					}
					addVir(5, 5, 10, isPlayer: true);
					if (point_Focus.frame == 1)
					{
						int subtype2 = 8;
						GameScreen.addEffectEnd(108, 3, toX, toY, Dir, objMainEff);
						GameScreen.addEffectEnd(54, subtype2, toX, toY, Dir, objMainEff);
					}
					else if (!GameCanvas.lowGraphic)
					{
						GameScreen.addEffectEnd(184, 0, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
					}
					GameScreen.addEffectEnd(42, 0, toX, toY, Dir, objMainEff);
					GameScreen.addEffectEnd(6, subtype, toX, toY, Dir, objMainEff);
					GameScreen.addEffectEnd(108, 8, toX, toY, Dir, objMainEff);
					VecEff.removeElement(point_Focus);
					j--;
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateNamiSea1()
	{
		if (f == 4 && isAddSound)
		{
			mSound.playSound(18, mSound.volumeSound);
		}
		if (f == 10 && !checkNullObject(2))
		{
			int xdich = objBeFireMain.x - x;
			int ydich = objBeFireMain.y - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			VecEff.addElement(p);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f < fRemove || VecEff.size() != 0)
		{
			return;
		}
		if (!checkNullObject(2))
		{
			if (isAddSound)
			{
				mSound.playSound(19, mSound.volumeSound);
			}
			GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(93, 1, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
		}
		removeEff();
	}

	public void updateNamiSea2()
	{
		if (f == 8 && isAddSound)
		{
			mSound.playSound(18, mSound.volumeSound);
		}
		if (f >= 2 && f <= 16)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isPaintWeapon = false;
			}
		}
		else
		{
			objFireMain.isPaintWeapon = true;
		}
		if (f == 14 && !checkNullObject(2))
		{
			int xdich = objBeFireMain.x - x;
			int ydich = objBeFireMain.y - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			VecEff.addElement(p);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f < fRemove || VecEff.size() != 0)
		{
			return;
		}
		if (!checkNullObject(2))
		{
			if (isAddSound)
			{
				mSound.playSound(19, mSound.volumeSound);
			}
			GameScreen.addEffectEnd(41, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
		}
		removeEff();
	}

	public void updateNamiSea3()
	{
		if (f == 2 && isAddSound)
		{
			mSound.playSound(18, mSound.volumeSound);
		}
		if (f >= 2 && f <= 16)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isPaintWeapon = false;
			}
		}
		else
		{
			objFireMain.isPaintWeapon = true;
		}
		if (f >= 24 && f <= 34 && !checkNullObject(2) && CRes.random(4) != 0)
		{
			int num = CRes.random(1, 3);
			for (int i = 0; i < num; i++)
			{
				int num2 = CRes.random_Am(0, 25) + objBeFireMain.x;
				GameScreen.addEffectEnd(90, 1, num2, objBeFireMain.y - 10, Dir, objMainEff);
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				VecEff.removeElement(point_Focus);
				j--;
			}
		}
		if ((f == 10 || f == 16) && !checkNullObject(3))
		{
			int xdich = objBeFireMain.x - x;
			int ydich = objBeFireMain.y - 60 - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p, x, objFireMain.y - objFireMain.hOne / 2, objBeFireMain.x, objBeFireMain.y - 70);
			p.frame = 1;
			VecEff.addElement(p);
		}
		if (f < fRemove || VecEff.size() != 0)
		{
			return;
		}
		if (!checkNullObject(2))
		{
			if (isAddSound)
			{
				mSound.playSound(17, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(41, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(108, 8, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
		}
		removeEff();
	}

	public void updateSanji2()
	{
		if (f == 4)
		{
			addVir(5, 5, 10, isPlayer: true);
		}
		if (f >= 6 && f <= fRemove)
		{
			if (!checkNullObject(1) && CRes.random(2) == 0)
			{
				objFireMain.dx = CRes.random_Am_0(2);
				xplus = objFireMain.dx;
			}
			if (f % 3 == 0)
			{
				if (isAddSound)
				{
					mSound.playSound(15, mSound.volumeSound);
				}
				if (indexObjBefire < vecObjsBeFire.size())
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
					indexObjBefire++;
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							int xdich = mainObject.x - x;
							int ydich = mainObject.y - objFireMain.hOne / 2 - y;
							Point_Focus p = new Point_Focus();
							int num = y;
							y += CRes.random_Am_0(15);
							p = create_Speed(xdich, ydich, p);
							y = num;
							p.dis = 1;
							p.maxdis = 0;
							if (typeEffect == 220 || typeEffect == 293)
							{
								p.maxdis = 5;
							}
							p.frame = indexObjBefire % 2;
							VecEff.addElement(p);
						}
					}
				}
				else if (!GameCanvas.lowGraphic)
				{
					indexObjBefire++;
					int xdich2 = am_duong * 140 + CRes.random_Am_0(20);
					int ydich2 = CRes.random_Am_0(80);
					Point_Focus p2 = new Point_Focus();
					int num2 = y;
					y += CRes.random_Am_0(15);
					p2 = create_Speed(xdich2, ydich2, p2);
					y = num2;
					p2.dis = 0;
					if (typeEffect == 220 || typeEffect == 293)
					{
						p2.maxdis = 5;
					}
					p2.frame = indexObjBefire % 2;
					VecEff.addElement(p2);
				}
			}
		}
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			if (!checkNullObject(1))
			{
				objFireMain.dx = 0;
			}
			removeEff();
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.f++;
			if (point.f >= fraImgSub3Eff.nFrame)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			Point point2 = new Point(point_Focus.x, point_Focus.y);
			point2.frame = point_Focus.frame;
			VecSubEff.addElement(point2);
			if (point_Focus.f == point_Focus.fRe && point_Focus.dis == 1)
			{
				GameScreen.addEffectEnd(35, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				int subtype = 7;
				if (typeEffect == 293)
				{
					subtype = 0;
				}
				GameScreen.addEffectEnd(108, subtype, point_Focus.x, point_Focus.y, Dir, objMainEff);
			}
			if (point_Focus.f >= point_Focus.fRe + point_Focus.maxdis)
			{
				VecEff.removeElement(point_Focus);
				j--;
			}
		}
	}

	public void update_Sanji_S3_L7()
	{
		if (f == 4)
		{
			addVir(5, 5, 10, isPlayer: true);
		}
		if (f >= 6 && f <= fRemove)
		{
			if (!checkNullObject(1) && CRes.random(2) == 0)
			{
				objFireMain.dx = CRes.random_Am_0(2);
				xplus = objFireMain.dx;
			}
			if (f % 3 == 0)
			{
				if (isAddSound)
				{
					mSound.playSound(15, mSound.volumeSound);
				}
				if (indexObjBefire < vecObjsBeFire.size())
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
					indexObjBefire++;
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							int num = mainObject.x - x;
							int ydich = mainObject.y - objFireMain.hOne / 2 - y;
							Point_Focus p = new Point_Focus();
							int num2 = y;
							y += CRes.random_Am_0(15);
							p = create_Speed(num, ydich, p);
							y = num2;
							p.dis = 1;
							p.maxdis = 0;
							p.maxdis = 5;
							p.frame = indexObjBefire % 4;
							if (Dir == 2)
							{
								p.Dir = 2;
							}
							else
							{
								p.Dir = 0;
							}
							VecEff.addElement(p);
							Point_Focus p2 = new Point_Focus();
							p2 = create_Speed(-num, ydich, p2);
							p2.dis = 1;
							p2.maxdis = 5;
							p2.frame = indexObjBefire % 4;
							if (Dir == 2)
							{
								p2.Dir = 0;
							}
							else
							{
								p2.Dir = 2;
							}
							VecEff.addElement(p2);
						}
					}
				}
				else if (!GameCanvas.lowGraphic)
				{
					indexObjBefire++;
					int num3 = am_duong * 140 + CRes.random_Am_0(20);
					int ydich2 = CRes.random_Am_0(80);
					Point_Focus p3 = new Point_Focus();
					int num4 = y;
					y += CRes.random_Am_0(15);
					p3 = create_Speed(num3, ydich2, p3);
					y = num4;
					p3.dis = 0;
					p3.maxdis = 5;
					p3.frame = indexObjBefire % 4;
					if (Dir == 2)
					{
						p3.Dir = 2;
					}
					else
					{
						p3.Dir = 0;
					}
					VecEff.addElement(p3);
					Point_Focus point_Focus = new Point_Focus();
					point_Focus.dis = 0;
					point_Focus.maxdis = 5;
					point_Focus.frame = indexObjBefire % 4;
					point_Focus = create_Speed(-num3, ydich2, point_Focus);
					if (Dir == 2)
					{
						point_Focus.Dir = 0;
					}
					else
					{
						point_Focus.Dir = 2;
					}
					VecEff.addElement(point_Focus);
				}
			}
		}
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			if (!checkNullObject(1))
			{
				objFireMain.dx = 0;
			}
			removeEff();
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.f++;
			if (point.f >= fraImgSub3Eff.nFrame)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecEff.elementAt(j);
			point_Focus2.update_Vx_Vy();
			Point point2 = new Point(point_Focus2.x, point_Focus2.y);
			point2.frame = point_Focus2.frame;
			point2.dis = point_Focus2.Dir;
			VecSubEff.addElement(point2);
			if (point_Focus2.f == point_Focus2.fRe && point_Focus2.dis == 1)
			{
				GameScreen.addEffectEnd(35, 0, point_Focus2.x, point_Focus2.y, Dir, objMainEff);
				int subtype = 0;
				GameScreen.addEffectEnd(108, subtype, point_Focus2.x, point_Focus2.y, Dir, objMainEff);
			}
			if (point_Focus2.f >= point_Focus2.fRe + point_Focus2.maxdis)
			{
				VecEff.removeElement(point_Focus2);
				j--;
			}
		}
	}

	public void updateRankyaku()
	{
		if (f >= 3 && isAddSound)
		{
			mSound.playSound(13, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			if (f > 3 + i * 4)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
				point_Focus.update_Vx_Vy();
				if ((i == 0) & (point_Focus.f == point_Focus.fRe))
				{
					GameScreen.addEffectEnd(19, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
					GameScreen.addEffectEnd(108, 8, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				}
				if (point_Focus.f >= point_Focus.fRe + 15)
				{
					VecEff.removeElementAt(i);
					i--;
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			GameScreen.addEffectEnd(1, 0, toX, toY, Dir, objMainEff);
			removeEff();
		}
	}

	public void updateSoi()
	{
		if (f >= 2 && isAddSound)
		{
			mSound.playSound(13, mSound.volumeSound);
		}
		x1000 = x + 30 * am_duong;
		int xdich = x1000 - x;
		if (f == 2)
		{
			VecEff.addElement(create_Speed(xdich, -8, new Point_Focus(), x1000, y, toX, toY));
			VecEff.addElement(create_Speed(xdich, 8, new Point_Focus(), x1000, y, toX, toY));
		}
		if (f == 4)
		{
			VecEff.addElement(create_Speed(xdich, 0, new Point_Focus(), x1000, y, toX, toY));
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if ((i == 0) & (point_Focus.f == point_Focus.fRe))
			{
				GameScreen.addEffectEnd(123, 3, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 3, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
			}
			if (point_Focus.f >= point_Focus.fRe + 25)
			{
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			GameScreen.addEffectEnd(1, 0, toX, toY, Dir, objMainEff);
			removeEff();
		}
	}

	public void updateSoi2()
	{
		if (f >= 2 && isAddSound)
		{
			mSound.playSound(13, mSound.volumeSound);
		}
		x1000 = x + 30 * am_duong;
		int xdich = x1000 - x;
		if (f == 2)
		{
			VecEff.addElement(create_Speed(xdich, -14, new Point_Focus(), x1000, y, toX, toY));
			VecEff.addElement(create_Speed(xdich, 14, new Point_Focus(), x1000, y, toX, toY));
		}
		if (f == 4)
		{
			VecEff.addElement(create_Speed(xdich, -8, new Point_Focus(), x1000, y, toX, toY));
			VecEff.addElement(create_Speed(xdich, 8, new Point_Focus(), x1000, y, toX, toY));
		}
		if (f == 6)
		{
			VecEff.addElement(create_Speed(xdich, 0, new Point_Focus(), x1000, y, toX, toY));
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if ((i == 0) & (point_Focus.f == point_Focus.fRe))
			{
				GameScreen.addEffectEnd(19, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
			}
			if (point_Focus.f >= point_Focus.fRe + 25)
			{
				VecEff.removeElementAt(i);
				i--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			GameScreen.addEffectEnd(1, 0, toX, toY, Dir, objMainEff);
			removeEff();
		}
	}

	public void updateHuou()
	{
		if (f >= 4 && isAddSound)
		{
			mSound.playSound(13, mSound.volumeSound);
		}
		if (f == 5)
		{
			GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.x += point.vx;
			point.y += point.vy;
			if (point.y < 40)
			{
				continue;
			}
			if (i >= size1)
			{
				VecEff.removeElementAt(i);
				i--;
				continue;
			}
			createPointHuou(point);
			Point point2 = new Point();
			point2.x = CRes.random_Am_0(40);
			point2.y = CRes.random_Am_0(30);
			point2.dis = 5;
			if (typeEffect == 279)
			{
				point2.dis = CRes.random(10);
			}
			point2.frame = 0;
			point2.maxframe = 3;
			VecSubEff.addElement(point2);
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point point3 = (Point)VecSubEff.elementAt(j);
			point3.frame++;
			if (point3.frame >= point3.maxframe)
			{
				VecSubEff.removeElementAt(j);
				j--;
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateShigan()
	{
		if (f > 2)
		{
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(0);
				point_Focus.update_Vx_Vy();
				if (f == 4)
				{
					GameScreen.addEffectEnd(35, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
					GameScreen.addEffectEnd(108, 5, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				}
			}
		}
		if (f >= fRemove)
		{
			GameScreen.addEffectEnd(1, 0, toX, toY, Dir, objMainEff);
			removeEff();
		}
	}

	public void updateDoor()
	{
		if (f == 1)
		{
			x1000 = x;
		}
		if (f == 15)
		{
			x1000 = objBeFireMain.x + 40 * am_duong;
		}
		if (f == 6)
		{
			objFireMain.isTanHinh = true;
			GameScreen.addEffectEnd(80, 0, objFireMain.x, y, Dir, objMainEff);
		}
		if (f == 20)
		{
			objFireMain.x = x1000;
			changeDir();
			objFireMain.Dir = Dir;
			GameScreen.addEffectEnd(80, 0, objFireMain.x, y, Dir, objMainEff);
			objFireMain.isTanHinh = false;
		}
		if (f == 23)
		{
			GameScreen.addEffectEnd(123, 2, objBeFireMain.x, y, Dir, objMainEff);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void addPoint(int x, int y)
	{
		Point point = new Point();
		point.x = x;
		point.y = y;
		VecEff.addElement(point);
	}

	public void updateDoor2()
	{
		if (f >= 2 && f <= 20)
		{
			if (f == 2)
			{
				addPoint(x, y);
			}
			if (f == 6)
			{
				addPoint(objBeFireMain.x + 90 * am_duong, y - 60);
			}
			if (f == 8)
			{
				addPoint(objBeFireMain.x - 90 * am_duong, y - 60);
			}
			if (f == 12)
			{
				addPoint(objBeFireMain.x + 90 * am_duong, y + 60);
			}
			if (f == 16)
			{
				addPoint(objBeFireMain.x - 90 * am_duong, y + 60);
			}
			if (f == 20)
			{
				addPoint(objBeFireMain.x + 40 * am_duong, y);
			}
		}
		if (f >= 4 && f <= 25 && (f - 4) % 4 == 0)
		{
			Point point = (Point)VecEff.elementAt((f - 4) / 4);
			objFireMain.isTanHinh = true;
			GameScreen.addEffectEnd(80, 0, point.x, point.y, Dir, objMainEff);
		}
		if (f == 25)
		{
			objFireMain.x = objBeFireMain.x + 40 * am_duong;
			changeDir();
			objFireMain.Dir = Dir;
			objFireMain.isTanHinh = false;
			GameScreen.addEffectEnd(80, 0, objFireMain.x, y, Dir, objMainEff);
		}
		if (f == 23)
		{
			GameScreen.addEffectEnd(123, 2, objBeFireMain.x, y, Dir, objMainEff);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateSanji4()
	{
		if (objBeFireMain != null && objBeFireMain.hOne > 0)
		{
			if (f == 1 && isAddSound)
			{
				mSound.playSound(13, mSound.volumeSound);
			}
			if (f % 4 == 0)
			{
				if (typeEffect == 14)
				{
					setAva(0, objBeFireMain);
				}
				if (!checkNullObject(2))
				{
					GameScreen.addEffectEnd(1, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
					GameScreen.addEffectEnd(93, 2, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
				}
				if (typeEffect == 44)
				{
					setAva(1, objBeFireMain);
					if (!checkNullObject(2))
					{
						GameScreen.addEffectEnd(1, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
					}
					int num = 25;
					if (Dir == 0)
					{
						num = -25;
					}
					if (!checkNullObject(1))
					{
						GameScreen.addEffectEnd(35, 0, objFireMain.x + num, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
					}
				}
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateZoroSea3()
	{
		if ((f == 4 || f == 10) && !checkNullObject(1))
		{
			GameScreen.addEffectEnd(30, 0, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, 200, Dir, objMainEff);
		}
		if (f >= 1 && f <= 4 && !checkNullObject(1))
		{
			objFireMain.dy = f * 14;
		}
		if (f >= 5 && f <= 13 && !checkNullObject(1))
		{
			objFireMain.dy = 56;
		}
		if (f >= 14 && f <= 17 && !checkNullObject(1))
		{
			objFireMain.dy = (17 - f) * 14;
		}
		if (f == 5 || f == 11)
		{
			if (isAddSound)
			{
				mSound.playSound(12, mSound.volumeSound);
			}
			int num = 20;
			if (Dir == 0)
			{
				num = -20;
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(16, 0, x, objFireMain.y - objFireMain.hOne / 2 - 10 - objFireMain.dy, Dir, objMainEff);
				GameScreen.addEffectEnd(16, 1, x + num, objFireMain.y - objFireMain.hOne / 2 - 10 - objFireMain.dy, Dir, objMainEff);
			}
		}
		if (!checkNullObject(3) && (f == 6 || f == 12))
		{
			addVir(5, 5, 10, isPlayer: true);
			sbyte dir = 0;
			if (objFireMain.x < objBeFireMain.x)
			{
				dir = 2;
			}
			int num2 = 18;
			if (Dir == 0)
			{
				num2 = -18;
			}
			sbyte b = 2;
			GameScreen.addEffectEnd_ObjTo(27, b, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2, objBeFireMain.ID, objBeFireMain.typeObject, dir, objMainEff);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			if (!checkNullObject(1))
			{
				objFireMain.dy = 0;
			}
			GameScreen.addEffectEnd(1, 0, toX, toY, Dir, objMainEff);
			removeEff();
		}
	}

	public void updateZoroSea1()
	{
		if (f == 1 && !checkNullObject(1))
		{
			GameScreen.addEffectEnd(30, 0, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, 300, Dir, objMainEff);
		}
		if (f == 11)
		{
			if (isAddSound)
			{
				mSound.playSound(12, mSound.volumeSound);
			}
			int num = 20;
			if (Dir == 0)
			{
				num = -20;
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(16, 0, x, objFireMain.y - objFireMain.hOne / 2 - 10, Dir, objMainEff);
				GameScreen.addEffectEnd(16, 1, x + num, objFireMain.y - objFireMain.hOne / 2 - 10, Dir, objMainEff);
			}
		}
		if (!checkNullObject(3) && f == 12)
		{
			sbyte dir = 0;
			if (objFireMain.x < objBeFireMain.x)
			{
				dir = 2;
			}
			int num2 = 18;
			if (Dir == 0)
			{
				num2 = -18;
			}
			sbyte b = 0;
			GameScreen.addEffectEnd_ObjTo(27, b, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2, objBeFireMain.ID, objBeFireMain.typeObject, dir, objMainEff);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			GameScreen.addEffectEnd(1, 0, toX, toY, Dir, objMainEff);
			removeEff();
		}
	}

	public void updateZoroSea2()
	{
		if (f == 7 && !checkNullObject(1))
		{
			GameScreen.addEffectEnd(30, 0, objFireMain.x, objFireMain.y - objFireMain.hOne, 250, Dir, objMainEff);
		}
		if (f == 4 || f == 16)
		{
			if (isAddSound)
			{
				mSound.playSound(12, mSound.volumeSound);
			}
			if (!checkNullObject(1))
			{
				int num = 20;
				if (Dir == 0)
				{
					num = -20;
				}
				if (!checkNullObject(1))
				{
					GameScreen.addEffectEnd(16, 0, x, objFireMain.y - objFireMain.hOne / 2 - 10, Dir, objMainEff);
					GameScreen.addEffectEnd(16, 1, x + num, objFireMain.y - objFireMain.hOne / 2 - 10, Dir, objMainEff);
				}
			}
		}
		if (!checkNullObject(3) && (f == 5 || f == 17))
		{
			sbyte dir = 0;
			if (objFireMain.x < objBeFireMain.x)
			{
				dir = 2;
			}
			int num2 = 18;
			if (Dir == 0)
			{
				num2 = -18;
			}
			sbyte b = 1;
			GameScreen.addEffectEnd_ObjTo(27, b, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2, objBeFireMain.ID, objBeFireMain.typeObject, dir, objMainEff);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			GameScreen.addEffectEnd(1, 0, toX, toY, Dir, objMainEff);
			removeEff();
		}
	}

	public void updateZoro3()
	{
		int num = 5;
		if (f == 5)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			int num2 = 20;
			if (Dir == 0)
			{
				num2 = -20;
			}
			setAva(0, objBeFireMain);
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				GameScreen.addEffectEnd(93, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(16, 1, x + num2, objFireMain.y - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
			}
		}
		if (f == 10)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			int num3 = 30;
			if (Dir == 0)
			{
				num3 = -30;
			}
			setAva(0, objBeFireMain);
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				GameScreen.addEffectEnd(93, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(16, 2, x + num3, objFireMain.y - objFireMain.hOne / 2 + num, Dir, objMainEff);
			}
		}
		if (f == 15)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			int num4 = 20;
			if (Dir == 0)
			{
				num4 = -20;
			}
			setAva(1, objBeFireMain);
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				GameScreen.addEffectEnd(93, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(16, 1, x + num4, objFireMain.y - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateLuffy6()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			removeEff();
			if (objFireMain == GameScreen.player)
			{
				GameScreen.setIsMoveEff(ismove: false);
			}
		}
		if (f < 7)
		{
			if (Dir == 0)
			{
				objFireMain.vx = -objFireMain.vMax * 3;
			}
			else
			{
				objFireMain.vx = objFireMain.vMax * 3;
			}
		}
		else
		{
			objFireMain.vx = 0;
		}
		if (f == 7)
		{
			setAva(1, objBeFireMain);
			int num = 20;
			if (Dir == 0)
			{
				num = -20;
			}
			GameScreen.addEffectEnd(1, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
			if (isAddSound)
			{
				mSound.playSound(3, mSound.volumeSound);
			}
			GameScreen.addEffectEnd(93, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
			GameScreen.addEffectEnd(0, 0, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
		}
	}

	public void updateLuffy_S2_L2()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			removeEff();
			if (objFireMain == GameScreen.player)
			{
				GameScreen.setIsMoveEff(ismove: false);
			}
			return;
		}
		if (f < 6)
		{
			if (Dir == 0)
			{
				objFireMain.vx = -objFireMain.vMax * 3;
			}
			else
			{
				objFireMain.vx = objFireMain.vMax * 3;
			}
			if (f % 2 == 1)
			{
				Point o = new Point(objFireMain.x - objFireMain.vx / 2, objFireMain.y);
				VecEff.addElement(o);
			}
		}
		else
		{
			objFireMain.vx = 0;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.f++;
			if (point.f / 2 >= 3)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
		if (f == 6)
		{
			if (isAddSound)
			{
				mSound.playSound(3, mSound.volumeSound);
			}
			setAva(2, objBeFireMain);
			int num = 20;
			if (Dir == 0)
			{
				num = -20;
			}
			GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(0, 0, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
		}
	}

	public void updateNami5()
	{
		if (!checkNullObject(1))
		{
			if (objFireMain.Dir == 0)
			{
				x = x1000 - 20;
			}
			else
			{
				x = x1000 + 20;
			}
		}
		if (f > 5 && (typeEffect == 55 || typeEffect == 31 || f >= 10) && f % 3 == 0 && f <= fRemove)
		{
			if (indexObjBefire < vecObjsBeFire.size())
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
				indexObjBefire++;
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						int num = mainObject.hOne / 2;
						if (typeEffect == 56 || typeEffect == 191 || typeEffect == 223)
						{
							num = mainObject.hOne + 20;
						}
						int xdich = mainObject.x - x;
						int ydich = mainObject.y - num - y;
						Point_Focus p = new Point_Focus();
						p = create_Speed(xdich, ydich, p);
						p.objMain = mainObject;
						VecEff.addElement(p);
					}
				}
			}
			else if (typeEffect == 223 && !GameCanvas.lowGraphic)
			{
				int xdich2 = CRes.random_Am_0(100);
				int ydich2 = -50 + CRes.random_Am_0(60);
				Point_Focus p2 = new Point_Focus();
				p2 = create_Speed(xdich2, ydich2, p2);
				VecEff.addElement(p2);
			}
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f < point_Focus.fRe)
			{
				continue;
			}
			if (isAddSound)
			{
				mSound.playSound(19, mSound.volumeSound);
			}
			if (typeEffect == 31)
			{
				GameScreen.addEffectEnd(38, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
			}
			else if (typeEffect == 55)
			{
				GameScreen.addEffectEnd(41, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
			}
			else if (typeEffect == 56 || typeEffect == 191 || typeEffect == 191 || typeEffect == 223)
			{
				if (isAddSound)
				{
					mSound.playSound(17, mSound.volumeSound);
				}
				addVir(5, 5, 10, isPlayer: true);
				sbyte subtype = 0;
				if (typeEffect == 191)
				{
					subtype = 1;
				}
				else if (typeEffect == 223)
				{
					subtype = 2;
				}
				if (point_Focus.objMain == null)
				{
					GameScreen.addEffectEnd(39, subtype, point_Focus.x, point_Focus.y, Dir, objMainEff);
				}
				else
				{
					GameScreen.addEffectEnd_ObjTo(39, subtype, point_Focus.objMain.x, point_Focus.objMain.y - point_Focus.objMain.hOne - 20, point_Focus.objMain.ID, point_Focus.objMain.typeObject, 0, objMainEff);
				}
			}
			GameScreen.addEffectEnd(93, 1, point_Focus.x, point_Focus.y, Dir, objMainEff);
			VecEff.removeElement(point_Focus);
			i--;
		}
		if (f > fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateNami6()
	{
		if (!checkNullObject(1))
		{
			if (objFireMain.Dir == 0)
			{
				x = x1000 - 20;
			}
			else
			{
				x = x1000 + 20;
			}
		}
		if (f == 10)
		{
			int num = objFireMain.hOne + 50;
			int xdich = 0;
			int ydich = -num;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			VecEff.addElement(p);
		}
		if (f >= 10 && f <= 19)
		{
			int num2 = objFireMain.hOne + 50;
			int num3 = 100 * CRes.getcos((f - 10) * 360 / 10) / 1000;
			int num4 = -num2 + 30 * CRes.getsin((f - 10) * 360 / 10) / 1000;
			Point_Focus p2 = new Point_Focus();
			p2 = create_Speed(num3, num4, p2, x, y, num3 - x, num4 - y);
			p2.objMain = objFireMain;
			VecEff.addElement(p2);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				if (isAddSound)
				{
					mSound.playSound(19, mSound.volumeSound);
				}
				if (isAddSound)
				{
					mSound.playSound(17, mSound.volumeSound);
				}
				addVir(5, 5, 10, isPlayer: true);
				sbyte subtype = 2;
				if (point_Focus.objMain == null)
				{
					subtype = 3;
				}
				GameScreen.addEffectEnd(39, subtype, point_Focus.x, point_Focus.y, Dir, objMainEff);
				GameScreen.addEffectEnd(93, 1, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f <= fRemove || VecEff.size() != 0)
		{
			return;
		}
		for (int j = 0; j < vecObjsBeFire.size(); j++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
			if (object_Effect_Skill != null)
			{
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					GameScreen.addEffectEnd(42, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
					GameScreen.addEffectEnd(41, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
					GameScreen.addEffectEnd(8, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
					GameScreen.addEffectEnd(108, 8, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
				}
			}
		}
		removeEff();
	}

	public void update_Nami_S3_L7()
	{
		if (!checkNullObject(1))
		{
			if (objFireMain.Dir == 0)
			{
				x = x1000 - 20;
			}
			else
			{
				x = x1000 + 20;
			}
		}
		if (f == 10)
		{
			int num = objFireMain.hOne + 50;
			int xdich = 0;
			int ydich = -num;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			VecEff.addElement(p);
		}
		if (f >= 10 && f <= 19)
		{
			int num2 = objFireMain.hOne + 25;
			int num3 = 100 * CRes.getcos((f - 10) * 360 / 10) / 1000;
			int num4 = -num2 + 30 * CRes.getsin((f - 10) * 360 / 10) / 1000;
			Point_Focus p2 = new Point_Focus();
			p2 = create_Speed(num3, num4, p2, x, y, num3 - x, num4 - y);
			p2.objMain = objFireMain;
			p2.frame = 0;
			VecEff.addElement(p2);
			int num5 = num2 + 25;
			num3 = 150 * CRes.getcos((f - 10) * 360 / 10) / 1000;
			num4 = -num5 + 30 * CRes.getsin((f - 10) * 360 / 10) / 1000;
			p2 = new Point_Focus();
			p2 = create_Speed(num3, num4, p2, x, y, num3 - x, num4 - y);
			p2.frame = 1;
			p2.objMain = objFireMain;
			VecEff.addElement(p2);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				if (isAddSound)
				{
					mSound.playSound(19, mSound.volumeSound);
				}
				if (isAddSound)
				{
					mSound.playSound(17, mSound.volumeSound);
				}
				addVir(5, 5, 10, isPlayer: true);
				sbyte subtype = 2;
				if (point_Focus.objMain == null)
				{
					subtype = 3;
				}
				if (point_Focus.frame == 0)
				{
					GameScreen.addEffectEnd(39, subtype, point_Focus.x, point_Focus.y, Dir, objMainEff);
				}
				else
				{
					GameScreen.addEffectEnd(185, subtype, point_Focus.x, point_Focus.y, Dir, objMainEff);
				}
				GameScreen.addEffectEnd(93, 1, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f <= fRemove || VecEff.size() != 0)
		{
			return;
		}
		for (int j = 0; j < vecObjsBeFire.size(); j++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
			if (object_Effect_Skill != null)
			{
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					GameScreen.addEffectEnd(42, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
					GameScreen.addEffectEnd(41, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
					GameScreen.addEffectEnd(8, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
					GameScreen.addEffectEnd(108, 8, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, mainObject);
				}
			}
		}
		removeEff();
	}

	public void updateNami4()
	{
		if (f == 8 && isAddSound)
		{
			mSound.playSound(10, mSound.volumeSound);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
		else if (f > 4 && f % 5 == 0 && !checkNullObject(2))
		{
			if (typeEffect == 16)
			{
				setAva(0, objBeFireMain);
				GameScreen.addEffectEnd(3, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
			}
			else if (typeEffect == 51)
			{
				setAva(1, objBeFireMain);
				GameScreen.addEffectEnd(1, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
			}
			GameScreen.addEffectEnd(93, 1, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
		}
	}

	public void updateZoro8()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			if (!checkNullObject(1))
			{
				objFireMain.vx = 0;
				objFireMain.vy = 0;
				objFireMain.isTanHinh = false;
				if (objFireMain.plashNow != null)
				{
					objFireMain.plashNow.setIsNextf(0);
				}
			}
			int num = 30;
			if (Dir == 0)
			{
				num = -30;
			}
			if (!checkNullObject(1))
			{
				if (isAddSound)
				{
					mSound.playSound(9, mSound.volumeSound);
				}
				if (typeEffect == 29)
				{
					setAva(2, objBeFireMain);
					GameScreen.addEffectEnd(26, 0, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
				}
				else
				{
					GameScreen.addEffectEnd(19, 0, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
					setAva(1, objBeFireMain);
				}
				GameScreen.addEffectEnd(93, 0, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
			}
			removeEff();
		}
		else
		{
			objFireMain.vx = vx;
			objFireMain.vy = vy;
			if (LoadMap.Tile_Stand(GameCanvas.loadmap.getTile(objFireMain.x + objFireMain.vx, objFireMain.y + objFireMain.vy)))
			{
				objFireMain.vx = 0;
				objFireMain.vy = 0;
			}
		}
	}

	public void updateUssopSkill1_Lv3()
	{
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
		if ((f == 0 || f == 3) && isAddSound)
		{
			mSound.playSound(21, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				if (isAddSound)
				{
					mSound.playSound(19, mSound.volumeSound);
				}
				if (point_Focus.frame == 0)
				{
					GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(20), toY + CRes.random_Am_0(20), Dir, objMainEff);
					GameScreen.addEffectEnd(35, 0, toX, toY, Dir, objMainEff);
				}
				else if (point_Focus.frame == 1)
				{
					GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(20), toY + CRes.random_Am_0(20), Dir, objMainEff);
					GameScreen.addEffectEnd(35, 0, toX, toY, Dir, objMainEff);
				}
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 3)
		{
			int xdich = toX - x;
			int ydich = toY - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			p.frame = 1;
			GameScreen.addEffectEnd(1, 0, x, y, Dir, objMainEff);
			VecEff.addElement(p);
		}
	}

	public void updateUssopSkill1_Lv3_New()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			removeEff();
		}
		if ((f == 0 || f == 3 || f == 10 || f == 13 || f == 20 || f == 23) && isAddSound)
		{
			mSound.playSound(21, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				addVir(5, 5, 10, isPlayer: true);
				if (point_Focus.frame == 0)
				{
					GameScreen.addEffectEnd(1, 0, point_Focus.toX + CRes.random_Am_0(20), point_Focus.toY + CRes.random_Am_0(20), Dir, objMainEff);
					GameScreen.addEffectEnd(35, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				}
				else if (point_Focus.frame == 1)
				{
					GameScreen.addEffectEnd(1, 0, point_Focus.toX + CRes.random_Am_0(20), point_Focus.toY + CRes.random_Am_0(20), Dir, objMainEff);
					GameScreen.addEffectEnd(35, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				}
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 5 || f == 15)
		{
			objFireMain.isTanHinh = true;
		}
		if (f == 7)
		{
			objFireMain.x -= am_duong * 10;
			objFireMain.y += CRes.random_Am(1, 2) * 20;
			y = objFireMain.y - objFireMain.hOne / 2;
			x = objFireMain.x;
			y -= 6;
			if (Dir == 0)
			{
				x -= 30;
			}
			else
			{
				x += 30;
			}
		}
		if (f == 9 || f == 19)
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 17)
		{
			objFireMain.x = xArchor;
			objFireMain.y = yArchor;
			y = objFireMain.y - objFireMain.hOne / 2;
			x = objFireMain.x;
			y -= 6;
			if (Dir == 0)
			{
				x -= 30;
			}
			else
			{
				x += 30;
			}
		}
		if ((f == 3 || f == 10 || f == 13 || f == 20 || f == 13) && !checkNullObject(3))
		{
			int num = 30;
			if (Dir == 0)
			{
				num = -30;
			}
			int xdich = objBeFireMain.x - (objFireMain.x + num);
			int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - (objFireMain.y - objFireMain.hOne / 2);
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2);
			p.frame = 1;
			GameScreen.addEffectEnd(1, 0, x, y, Dir, objMainEff);
			VecEff.addElement(p);
		}
	}

	public void updateUssopSkill1_Lv3_SHORT()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			removeEff();
		}
		if ((f == 0 || f == 3 || f == 10 || f == 13) && isAddSound)
		{
			mSound.playSound(21, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f < point_Focus.fRe)
			{
				continue;
			}
			addVir(5, 5, 10, isPlayer: true);
			if (typeEffect == 192)
			{
				GameScreen.addEffectEnd(25, 4, point_Focus.toX + CRes.random_Am_0(20), point_Focus.toY + CRes.random_Am_0(20), Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
			}
			else
			{
				GameScreen.addEffectEnd(1, 0, point_Focus.toX + CRes.random_Am_0(20), point_Focus.toY + CRes.random_Am_0(20), Dir, objMainEff);
				if (point_Focus.frame == 2)
				{
					GameScreen.addEffectEnd(108, 5, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				}
			}
			GameScreen.addEffectEnd(35, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
			VecEff.removeElement(point_Focus);
			i--;
		}
		if (f == 5 || f == 14)
		{
			objFireMain.isTanHinh = true;
		}
		if (f == 7)
		{
			objFireMain.x -= am_duong * 10;
			objFireMain.y += CRes.random_Am(1, 2) * 20;
			y = objFireMain.y - objFireMain.hOne / 2;
			x = objFireMain.x;
			y -= 6;
			if (Dir == 0)
			{
				x -= 30;
			}
			else
			{
				x += 30;
			}
		}
		if (f == 15)
		{
			objFireMain.x = xArchor;
			objFireMain.y = yArchor;
			y = objFireMain.y - objFireMain.hOne / 2;
			x = objFireMain.x;
			y -= 6;
			if (Dir == 0)
			{
				x -= 30;
			}
			else
			{
				x += 30;
			}
		}
		if (f == 9 || f == 15)
		{
			objFireMain.isTanHinh = false;
		}
		if ((f == 3 || f == 10 || f == 13) && !checkNullObject(3))
		{
			int num = 30;
			if (Dir == 0)
			{
				num = -30;
			}
			int xdich = objBeFireMain.x - (objFireMain.x + num);
			int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - (objFireMain.y - objFireMain.hOne / 2);
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2);
			p.frame = 1;
			if (f == 10)
			{
				p.frame = 2;
			}
			GameScreen.addEffectEnd(1, 0, x, y, Dir, objMainEff);
			VecEff.addElement(p);
		}
	}

	public void update_Ussop_S1_L5()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			if (objFireMain != null)
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
		if ((f == 2 || f == 6 || f == 10 || f == 14) && isAddSound)
		{
			mSound.playSound(21, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				addVir(5, 5, 10, isPlayer: true);
				sbyte subtype = 4;
				if (point_Focus.frame == 2)
				{
					subtype = 3;
				}
				GameScreen.addEffectEnd(25, subtype, point_Focus.toX + CRes.random_Am_0(20), point_Focus.toY + CRes.random_Am_0(20), Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				GameScreen.addEffectEnd(35, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 2)
		{
			objFireMain.isTanHinh = true;
		}
		else if (f == 15)
		{
			objFireMain.isTanHinh = false;
		}
		if (f > 3 && f % 2 == 0 && !checkNullObject(3))
		{
			int num = 25;
			int num2 = (f - 2) / 2;
			if (num2 >= mframeSuper.Length)
			{
				return;
			}
			if (Dir == 0)
			{
				num = -25;
			}
			int xdich = objFireMain.x + mframeSuper[num2][0] - objBeFireMain.x + num;
			int ydich = objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0] - (objBeFireMain.y - objBeFireMain.hOne / 2);
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p, objFireMain.x + mframeSuper[num2][0] + num, objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0], objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2);
			p.frame = 1;
			if (f == 8 || f == 14)
			{
				p.frame = 2;
			}
			GameScreen.addEffectEnd(1, 0, objFireMain.x + mframeSuper[num2][0] + num, objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0] - 10, Dir, objMainEff);
			VecEff.addElement(p);
		}
		if (typeEffect != 301)
		{
			return;
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			if (f > j * 4)
			{
				((Point_Focus)VecSubEff.elementAt(j)).update_Vx_Vy();
			}
		}
	}

	public void update_Ussop_S1_L7()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			if (objFireMain != null)
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
		if ((f == 2 || f == 6 || f == 10 || f == 14) && isAddSound)
		{
			mSound.playSound(21, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				addVir(5, 5, 10, isPlayer: true);
				sbyte subtype = 4;
				if (point_Focus.frame == 2)
				{
					subtype = 3;
				}
				GameScreen.addEffectEnd(25, subtype, point_Focus.toX + CRes.random_Am_0(20), point_Focus.toY + CRes.random_Am_0(20), Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				GameScreen.addEffectEnd(35, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 2)
		{
			objFireMain.isTanHinh = true;
		}
		else if (f == 15)
		{
			objFireMain.isTanHinh = false;
		}
		if (f > 3 && f % 2 == 0 && !checkNullObject(3))
		{
			int num = 25;
			int num2 = (f - 2) / 2;
			if (num2 >= mframeSuper.Length)
			{
				return;
			}
			if (Dir == 0)
			{
				num = -25;
			}
			int xdich = objFireMain.x + mframeSuper[num2][0] - objBeFireMain.x + num;
			int ydich = objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0] - (objBeFireMain.y - objBeFireMain.hOne / 2);
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p, objFireMain.x + mframeSuper[num2][0] + num, objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0], objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2);
			p.frame = 1;
			if (f == 8 || f == 14)
			{
				p.frame = 2;
			}
			GameScreen.addEffectEnd(1, 0, objFireMain.x + mframeSuper[num2][0] + num, objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0] - 10, Dir, objMainEff);
			VecEff.addElement(p);
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			if (f > j * 4)
			{
				((Point_Focus)VecSubEff.elementAt(j)).update_Vx_Vy();
			}
		}
	}

	public void updateUssopS1_L6()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			if (objFireMain != null)
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
		if ((f == 2 || f == 6 || f == 10 || f == 14) && isAddSound)
		{
			mSound.playSound(21, mSound.volumeSound);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				addVir(5, 5, 10, isPlayer: true);
				sbyte subtype = 4;
				if (point_Focus.frame == 2)
				{
					subtype = 3;
				}
				GameScreen.addEffectEnd(25, subtype, point_Focus.toX + CRes.random_Am_0(20), point_Focus.toY + CRes.random_Am_0(20), Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				GameScreen.addEffectEnd(35, 0, point_Focus.toX, point_Focus.toY, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 2)
		{
			objFireMain.isTanHinh = true;
		}
		else if (f == 15)
		{
			objFireMain.isTanHinh = false;
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			if (f > j * 4)
			{
				((Point_Focus)VecSubEff.elementAt(j)).update_Vx_Vy();
			}
		}
		if (f <= 3 || f % 2 != 0 || checkNullObject(3))
		{
			return;
		}
		int num = 25;
		int num2 = (f - 2) / 2;
		if (num2 < mframeSuper.Length)
		{
			if (Dir == 0)
			{
				num = -25;
			}
			int xdich = objFireMain.x + mframeSuper[num2][0] - objBeFireMain.x + num;
			int ydich = objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0] - (objBeFireMain.y - objBeFireMain.hOne / 2);
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p, objFireMain.x + mframeSuper[num2][0] + num, objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0], objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2);
			p.frame = 1;
			if (f == 8 || f == 14)
			{
				p.frame = 2;
			}
			GameScreen.addEffectEnd(1, 0, objFireMain.x + mframeSuper[num2][0] + num, objFireMain.y - objFireMain.hOne / 2 + mframeSuper[num2][0] - 10, Dir, objMainEff);
			VecEff.addElement(p);
		}
	}

	public void update_Nami_S1_L3()
	{
		if (f >= fRemove)
		{
			removeEff();
		}
		else
		{
			if (isAddSound && f == 8)
			{
				mSound.playSound(10, mSound.volumeSound);
			}
			if ((f == 5 || f == 15) && isAddSound)
			{
				mSound.playSound(17, mSound.volumeSound);
			}
			if (f == 5 && typeEffect == 311)
			{
				GameScreen.addEffectEnd(174, 0, objBeFireMain.x, objBeFireMain.y, Dir, objBeFireMain);
			}
			if (f > 4 && f % 5 == 0 && objBeFireMain != null)
			{
				addVir(5, 5, 10, isPlayer: true);
				setAva(1, objBeFireMain);
				sbyte subtype = 1;
				if ((typeEffect == 221 || typeEffect == 311) && CRes.random(2) == 0)
				{
					subtype = 3;
				}
				GameScreen.addEffectEnd(38, subtype, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
				if (f == 10 || typeEffect == 221 || typeEffect == 311)
				{
					subtype = 3;
					if ((typeEffect == 221 || typeEffect == 311) && CRes.random(2) == 0)
					{
						subtype = 8;
					}
					GameScreen.addEffectEnd(108, subtype, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
				}
			}
			if ((typeEffect == 189 || typeEffect == 221 || typeEffect == 311) && f > 4 && f % 3 == 0 && objBeFireMain != null)
			{
				short type = 38;
				if ((typeEffect == 221 || typeEffect == 311) && CRes.random(2) == 0)
				{
					type = 138;
				}
				GameScreen.addEffectEnd(type, 2, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
			}
			if (objFireMain != null && !GameCanvas.lowGraphic)
			{
				int num = x - 20;
				if (Dir == 0)
				{
					num = x + 20;
				}
				int a = 25;
				if (objFireMain.hOne > 1)
				{
					a = objFireMain.hOne / 2;
				}
				Point point = new Point(num + CRes.random_Am_0(20), y + CRes.random_Am_0(a));
				if ((typeEffect == 221 || typeEffect == 311) && CRes.random(2) == 0)
				{
					point.frame = 1;
				}
				VecEff.addElement(point);
			}
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point2 = (Point)VecEff.elementAt(i);
			point2.f++;
			if (point2.f >= 4)
			{
				VecEff.removeElement(point2);
				i--;
			}
		}
	}

	public void update_Nami_S1_L7()
	{
		if (f >= fRemove)
		{
			removeEff();
		}
		else
		{
			if (isAddSound && f == 8)
			{
				mSound.playSound(10, mSound.volumeSound);
			}
			if ((f == 5 || f == 15) && isAddSound)
			{
				mSound.playSound(17, mSound.volumeSound);
			}
			if (f == 4)
			{
				GameScreen.addEffectEnd(138, 0, objBeFireMain.x, objBeFireMain.y, Dir, objBeFireMain);
			}
			else if (f == 3)
			{
				GameScreen.addEffectEnd(38, 2, objBeFireMain.x, objBeFireMain.y, Dir, objBeFireMain);
			}
			else if (f == 2)
			{
				GameScreen.addEffectEnd(38, 1, objBeFireMain.x, objBeFireMain.y, Dir, objBeFireMain);
			}
			else if (f == 1)
			{
				GameScreen.addEffectEnd(38, 3, objBeFireMain.x, objBeFireMain.y, Dir, objBeFireMain);
			}
			else if (f == 6 || f == 10 || f == 14 || f == 18)
			{
				objBeFireMain.x += am_duong * 18;
				setAva(2, objBeFireMain);
			}
			if (f > 4 && f % 5 == 0 && objBeFireMain != null)
			{
				addVir(5, 5, 10, isPlayer: true);
				setAva(2, objBeFireMain);
				sbyte subtype = 1;
				if (CRes.random(2) == 0)
				{
					subtype = 3;
				}
				GameScreen.addEffectEnd(38, subtype, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
				subtype = 3;
				if (CRes.random(2) == 0)
				{
					subtype = 8;
				}
				GameScreen.addEffectEnd(108, subtype, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
			}
			if (f > 4 && f % 3 == 0 && objBeFireMain != null)
			{
				short type = 38;
				if (CRes.random(2) == 0)
				{
					type = 138;
				}
				GameScreen.addEffectEnd(type, 2, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
			}
			if (objFireMain != null && !GameCanvas.lowGraphic)
			{
				int num = x - 20;
				if (Dir == 0)
				{
					num = x + 20;
				}
				int a = 25;
				if (objFireMain.hOne > 1)
				{
					a = objFireMain.hOne / 2;
				}
				Point point = new Point(num + CRes.random_Am_0(20), y + CRes.random_Am_0(a));
				if (CRes.random(2) == 0)
				{
					point.frame = 1;
				}
				VecEff.addElement(point);
			}
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point2 = (Point)VecEff.elementAt(i);
			point2.f++;
			if (point2.f >= 4)
			{
				VecEff.removeElement(point2);
				i--;
			}
		}
	}

	public void updateSanjiSkill3_Lv1()
	{
		if (f >= 4)
		{
			if (isAddSound)
			{
				mSound.playSound(13, mSound.volumeSound);
			}
			if (objFireMain != null && CRes.random(2) == 0)
			{
				objFireMain.dx = CRes.random_Am_0(2);
				xplus = objFireMain.dx;
			}
			if (f % 2 == 0 && indexObjBefire < vecObjsBeFire.size())
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
				indexObjBefire++;
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						int xdich = mainObject.x - x;
						int ydich = mainObject.y - objFireMain.hOne / 2 - y;
						Point_Focus p = new Point_Focus();
						p = create_Speed(xdich, ydich, p);
						p.frame = CRes.random(6);
						VecEff.addElement(p);
					}
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			if (objFireMain != null)
			{
				objFireMain.dx = 0;
			}
			removeEff();
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				if (typeEffect == 49)
				{
					GameScreen.addEffectEnd(1, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				}
				else if (typeEffect == 50)
				{
					GameScreen.addEffectEnd(35, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				}
				GameScreen.addEffectEnd(93, 2, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
	}

	public void updateLuffyS1()
	{
		if (objBeFireMain != null && objBeFireMain.hOne > 0 && f % 5 == 0)
		{
			sbyte b = 0;
			if (typeEffect == 33)
			{
				b = 2;
				if (!checkNullObject(2))
				{
					GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				}
			}
			setAva(b, objBeFireMain);
			if (!checkNullObject(1))
			{
				int num = 28;
				if (objFireMain.Dir == 0)
				{
					num = -28;
				}
				if (isAddSound)
				{
					mSound.playSound(2, mSound.volumeSound);
				}
				if (typeEffect == 176)
				{
					GameScreen.addEffectEnd(114, 0, objFireMain.x + num - am_duong * 8, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 5, (sbyte)objFireMain.Dir, objMainEff);
					GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
				}
				else
				{
					GameScreen.addEffectEnd(25, b, objFireMain.x + num, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
					GameScreen.addEffectEnd(93, 0, objFireMain.x + num, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				}
			}
		}
		if (f >= fRemove)
		{
			if (typeEffect == 176 && !checkNullObject(1))
			{
				GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			removeEff();
		}
	}

	public void updateLuffyS1_NEW()
	{
		if (f < 20 && f % 5 == 0)
		{
			if (isAddSound)
			{
				mSound.playSound(2, mSound.volumeSound);
			}
			int num = 28;
			if (Dir == 0)
			{
				num = -28;
			}
			if (!checkNullObject(2))
			{
				setDy(-6, objBeFireMain);
				if (objBeFireMain.typeObject == 1 && objBeFireMain.Action != 4)
				{
					objBeFireMain.Action = 3;
				}
				GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(25, 2, objFireMain.x + num, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
		}
		if (f == 20)
		{
			if (isAddSound)
			{
				mSound.playSound(6, mSound.volumeSound);
			}
			int num2 = -15;
			if (Dir == 0)
			{
				num2 = 15;
			}
			GameScreen.addEffectEnd(171, 0, x + num2, y, 450, Dir, objMainEff);
		}
		if (f == 32)
		{
			if (isAddSound)
			{
				mSound.playSound(5, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			setAva(2, objBeFireMain);
			int num3 = 28;
			if (Dir == 0)
			{
				num3 = -28;
			}
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(54, 2, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateLuffyS1_L3_SHORT()
	{
		if (f == 4)
		{
			if (isAddSound)
			{
				mSound.playSound(2, mSound.volumeSound);
			}
			int num = 28;
			if (Dir == 0)
			{
				num = -28;
			}
			if (!checkNullObject(2))
			{
				setDy(-6, objBeFireMain);
				if (objBeFireMain.typeObject == 1 && objBeFireMain.Action != 4)
				{
					objBeFireMain.Action = 3;
				}
				GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(25, 2, objFireMain.x + num, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
		}
		if (f == 5)
		{
			if (isAddSound)
			{
				mSound.playSound(6, mSound.volumeSound);
			}
			int num2 = -15;
			if (Dir == 0)
			{
				num2 = 15;
			}
			GameScreen.addEffectEnd(30, 0, x + num2, y, 150, Dir, objMainEff);
		}
		if (typeEffect == 83)
		{
			if (f == 15)
			{
				if (isAddSound)
				{
					mSound.playSound(5, mSound.volumeSound);
				}
				addVir(5, 5, 10, isPlayer: true);
				setAva(2, objBeFireMain);
				int num3 = 28;
				if (Dir == 0)
				{
					num3 = -28;
				}
				if (!checkNullObject(2))
				{
					GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
					GameScreen.addEffectEnd(108, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
				}
				if (!checkNullObject(1))
				{
					GameScreen.addEffectEnd(25, 2, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
					GameScreen.addEffectEnd(54, 0, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				}
			}
		}
		else if (typeEffect == 180)
		{
			if (f == 13 || f == 17)
			{
				if (isAddSound)
				{
					mSound.playSound(5, mSound.volumeSound);
				}
				addVir(5, 5, 10, isPlayer: true);
				setAva(2, objBeFireMain);
				int num4 = 28;
				if (Dir == 0)
				{
					num4 = -28;
				}
				if (!checkNullObject(2))
				{
					GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
					GameScreen.addEffectEnd(108, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
				}
				if (!checkNullObject(1))
				{
					GameScreen.addEffectEnd(25, 2, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
					GameScreen.addEffectEnd(54, (f == 13) ? 7 : 6, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				}
			}
		}
		else if (typeEffect == 212)
		{
			for (int i = 0; i < VecSubEff.size(); i++)
			{
				Point point = (Point)VecSubEff.elementAt(i);
				point.update();
				if (point.f >= point.fRe)
				{
					VecSubEff.removeElement(point);
					i--;
				}
			}
			if (f < fRemove && f % 3 == 0 && !GameCanvas.lowGraphic)
			{
				Point point2 = new Point();
				point2.x = x + CRes.random_Am_0(15);
				point2.y = y + 15 + CRes.random_Am_0(5);
				point2.vx = CRes.random_Am_0(2);
				point2.vy = -CRes.random(1, 4);
				point2.fRe = CRes.random(10, 14);
				VecSubEff.addElement(point2);
			}
			if (f == 13 || f == 17)
			{
				if (isAddSound)
				{
					mSound.playSound(5, mSound.volumeSound);
				}
				addVir(5, 5, 10, isPlayer: true);
				setAva(2, objBeFireMain);
				int num5 = 28;
				if (Dir == 0)
				{
					num5 = -28;
				}
				if (!checkNullObject(2))
				{
					GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
					GameScreen.addEffectEnd(108, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
				}
				if (!checkNullObject(1))
				{
					GameScreen.addEffectEnd(25, 2, objFireMain.x + num5, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
					GameScreen.addEffectEnd(54, (f == 13) ? 7 : 9, objFireMain.x + num5, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				}
			}
		}
		if (f >= fRemove && (typeEffect != 212 || VecSubEff.size() == 0))
		{
			removeEff();
		}
	}

	public void update_Luffy_S1_L6()
	{
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			removeEff();
		}
		if (f == 1)
		{
			if (isAddSound)
			{
				mSound.playSound(6, mSound.volumeSound);
			}
			int num = -15;
			if (Dir == 0)
			{
				num = 15;
			}
			GameScreen.addEffectEnd(171, 0, x + num, y, 450, Dir, objMainEff);
		}
		if (f == 10)
		{
			if (isAddSound)
			{
				mSound.playSound(5, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			setAva(2, objBeFireMain);
			int num2 = 28;
			if (Dir == 0)
			{
				num2 = -28;
			}
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 5, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(54, 5, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
			GameScreen.addEffectEnd(119, 3, objFireMain.x + am_duong * 20, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
		}
		if (f == 14)
		{
			int num3 = 28;
			if (Dir == 0)
			{
				num3 = -28;
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(54, 6, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		if (f < fRemove && f % 3 == 0 && !GameCanvas.lowGraphic)
		{
			Point point2 = new Point();
			point2.x = x + CRes.random_Am_0(15);
			point2.y = y + 15 + CRes.random_Am_0(5);
			point2.vx = CRes.random_Am_0(2);
			point2.vy = -CRes.random(1, 4);
			point2.fRe = CRes.random(10, 14);
			VecSubEff.addElement(point2);
		}
	}

	public void update_Luffy_S1_L7()
	{
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			removeEff();
		}
		if (f == 0)
		{
			if (isAddSound)
			{
				mSound.playSound(6, mSound.volumeSound);
			}
			int num = -15;
			if (Dir == 0)
			{
				num = 15;
			}
			GameScreen.addEffectEnd(171, 1, x + num, y, 450, Dir, objMainEff);
		}
		if (f == 10)
		{
			if (isAddSound)
			{
				mSound.playSound(5, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			setAva(2, objBeFireMain);
			int num2 = 28;
			if (Dir == 0)
			{
				num2 = -28;
			}
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 5, objBeFireMain.x, objBeFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(54, 5, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
			GameScreen.addEffectEnd(182, 3, objFireMain.x + am_duong * 20, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
		}
		if (f == 14)
		{
			int num3 = 28;
			if (Dir == 0)
			{
				num3 = -28;
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(54, 6, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecSubEff.removeElement(point);
				i--;
			}
		}
		if (f < fRemove && f % 3 == 0 && !GameCanvas.lowGraphic)
		{
			Point point2 = new Point();
			point2.x = x + CRes.random_Am_0(15);
			point2.y = y + 15 + CRes.random_Am_0(5);
			point2.vx = CRes.random_Am_0(2);
			point2.vy = -CRes.random(1, 4);
			point2.fRe = CRes.random(10, 14);
			VecSubEff.addElement(point2);
		}
	}

	public void updateXaPhong()
	{
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			removeEff();
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			point.update();
			if (point.f < point.fRe)
			{
				continue;
			}
			int num = CRes.random(3) + 1;
			if (typeEffect == 274)
			{
				num = 2;
			}
			if (num == 2)
			{
				GameScreen.addEffectEnd(71, 0, point.x, point.y, Dir, objMainEff);
				if (CRes.random(4) == 0)
				{
					GameScreen.addEffectEnd(108, 4, point.x, point.y, Dir, objMainEff);
				}
			}
			else
			{
				GameScreen.addEffectEnd(38, num, point.x, point.y, Dir, objMainEff);
				if (CRes.random(4) == 0)
				{
					if (num == 1)
					{
						GameScreen.addEffectEnd(108, 3, point.x, point.y, Dir, objMainEff);
					}
					else
					{
						GameScreen.addEffectEnd(108, 8, point.x, point.y, Dir, objMainEff);
					}
				}
			}
			VecSubEff.removeElement(point);
			i--;
		}
		if (f < fRemove && !GameCanvas.lowGraphic)
		{
			for (int j = 0; j < 2; j++)
			{
				Point point2 = new Point();
				point2.x = objBeFireMain.x + CRes.random_Am_0(15);
				point2.y = objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10);
				point2.vx = CRes.random_Am_0(4);
				point2.vy = CRes.random_Am_0(5);
				point2.fRe = CRes.random(10, 14);
				VecSubEff.addElement(point2);
			}
		}
	}

	public void updateMorgan_1()
	{
		if (f < fRemove)
		{
			return;
		}
		if (!checkNullObject(1))
		{
			int num = -10;
			int num2 = 20;
			if (Dir == 0)
			{
				num2 = -20;
			}
			GameScreen.addEffectEnd(16, 1, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num, Dir, objMainEff);
			num2 = 13;
			if (Dir == 0)
			{
				num2 = -13;
			}
			GameScreen.addEffectEnd(16, 1, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num, Dir, objMainEff);
			num2 = 5;
			if (Dir == 0)
			{
				num2 = -5;
			}
			GameScreen.addEffectEnd(16, 1, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num, Dir, objMainEff);
		}
		if (!checkNullObject(2))
		{
			addVir(3, 5, 10, isPlayer: false);
			setAva(1, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
		}
		removeEff();
	}

	public void updateMorgan_2()
	{
		if (f < fRemove)
		{
			return;
		}
		if (!checkNullObject(1))
		{
			int num = -10;
			int num2 = 20;
			if (Dir == 0)
			{
				num2 = -20;
			}
			GameScreen.addEffectEnd(16, 0, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num, Dir, objMainEff);
			num2 = 15;
			num += 3;
			if (Dir == 0)
			{
				num2 = -13;
			}
			GameScreen.addEffectEnd(16, 0, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num, Dir, objMainEff);
			num2 = 10;
			num += 3;
			if (Dir == 0)
			{
				num2 = -5;
			}
			GameScreen.addEffectEnd(16, 0, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num, Dir, objMainEff);
		}
		if (!checkNullObject(2))
		{
			setAva(1, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
		}
		removeEff();
	}

	public void updateZoroS2_New()
	{
		int num = 5;
		if (f >= fRemove || checkNullObject(3))
		{
			if (isAddSound)
			{
				mSound.playSound(9, mSound.volumeSound);
			}
			if (!checkNullObject(2))
			{
				setAva(2, objBeFireMain);
				GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				_ = Dir;
				int subtype = 0;
				if (typeEffect == 184)
				{
					subtype = 2;
				}
				else if (typeEffect == 216)
				{
					subtype = 3;
				}
				GameScreen.addEffectEnd(26, subtype, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
				objFireMain.vx = 0;
				objFireMain.vy = 0;
				objFireMain.toX = objFireMain.x;
				objFireMain.toY = objFireMain.y;
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		if ((f > 12 && f < 20) || (f > 22 && f < 26) || (f > 28 && f < 32) || (f > 34 && f < 38))
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 17)
		{
			objFireMain.y = objBeFireMain.y;
			giatocFly = 8;
		}
		if (f < 20 && f >= 17)
		{
			objBeFireMain.dy += giatocFly;
			giatocFly /= 2;
		}
		if (f >= 20 && f < 26)
		{
			giatocFly = 0;
			objBeFireMain.dy = 20;
			objFireMain.dy = 15;
		}
		if (f == 20)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			int num2 = 2;
			if (Dir == 0)
			{
				num2 = -2;
			}
			objFireMain.x = toX - num2;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			int subtype2 = 1;
			if (typeEffect == 184 || typeEffect == 216)
			{
				subtype2 = -1;
				num2 = 10;
				if (Dir == 0)
				{
					num2 = -10;
				}
			}
			GameScreen.addEffectEnd(16, subtype2, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
			if (typeEffect == 216)
			{
				GameScreen.addEffectEnd(136, 0, objFireMain.x + num2, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
			}
		}
		if (f >= 26 && f < 32)
		{
			objBeFireMain.dy = 30;
			objFireMain.dy = 25;
		}
		if (f == 26)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			changeDir();
			objFireMain.Dir = Dir;
			int num3 = 30;
			if (Dir == 0)
			{
				num3 = -30;
			}
			objFireMain.x = toX - num3;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			int subtype3 = 2;
			if (typeEffect == 184 || typeEffect == 216)
			{
				subtype3 = -2;
				num3 = 15;
				if (Dir == 0)
				{
					num3 = -15;
				}
			}
			GameScreen.addEffectEnd(16, subtype3, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num, Dir, objMainEff);
		}
		if (f == 32)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			changeDir();
			objFireMain.Dir = Dir;
			int num4 = 20;
			if (Dir == 0)
			{
				num4 = -20;
			}
			objFireMain.x = toX - num4;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			objBeFireMain.dy = 40;
			objFireMain.dy = 35;
			GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			int subtype4 = 1;
			if (typeEffect == 184 || typeEffect == 216)
			{
				subtype4 = -1;
				num4 = 10;
				if (Dir == 0)
				{
					num4 = -10;
				}
			}
			GameScreen.addEffectEnd(16, subtype4, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
		}
		if (f == 38)
		{
			changeDir();
			objFireMain.Dir = Dir;
			Point_Focus point_Focus = new Point_Focus();
			int num5 = 20;
			if (Dir == 0)
			{
				num5 = -20;
			}
			int num6 = toX;
			int num7 = toY;
			toX = x;
			toY = y;
			x = num6;
			y = num7;
			int xdich = toX - (x - num5);
			int ydich = toY - y;
			objFireMain.x = x - num5;
			objFireMain.y = y;
			objFireMain.dy = 0;
			objBeFireMain.dy = 0;
			create_Speed(xdich, ydich, point_Focus);
			objFireMain.vx = point_Focus.vx;
			objFireMain.vy = -point_Focus.vy;
			objFireMain.toX = point_Focus.toX;
			objFireMain.toY = point_Focus.toY;
		}
		if (f > 38 && MainObject.getDistance(objFireMain.x, objFireMain.y, objFireMain.toX, objFireMain.toY) < vMax)
		{
			objFireMain.vx = 0;
			objFireMain.vy = 0;
			objFireMain.toX = objFireMain.x;
			objFireMain.toY = objFireMain.y;
		}
	}

	public void update_Zoro_S2_L6()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (isAddSound)
			{
				mSound.playSound(9, mSound.volumeSound);
			}
			if (!checkNullObject(2))
			{
				setAva(2, objBeFireMain);
				GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				_ = Dir;
				int num = 0;
				num = 4;
				GameScreen.addEffectEnd(26, num, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
				objFireMain.vx = 0;
				objFireMain.vy = 0;
				objFireMain.toX = objFireMain.x;
				objFireMain.toY = objFireMain.y;
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		if ((f > 12 && f < 20) || (f > 22 && f < 26) || (f > 28 && f < 32) || (f > 34 && f < 38))
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 17)
		{
			objFireMain.y = objBeFireMain.y;
			giatocFly = 8;
		}
		if (f < 20 && f >= 17)
		{
			objBeFireMain.dy += giatocFly;
			giatocFly /= 2;
		}
		if (f >= 20 && f < 26)
		{
			giatocFly = 0;
			objBeFireMain.dy = 20;
			objFireMain.dy = 15;
		}
		if (f == 10)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			int num2 = 20;
			if (Dir == 0)
			{
				num2 = -20;
			}
			objFireMain.x = toX - num2;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			createSkillZoro2(1, toX + CRes.random_Am_0(5), toY - 20, 2);
		}
		if (f >= 16 && f < 22)
		{
			objBeFireMain.dy = 30;
			objFireMain.dy = 25;
		}
		if (f == 16)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			changeDir();
			objFireMain.Dir = Dir;
			int num3 = 30;
			if (Dir == 0)
			{
				num3 = -30;
			}
			objFireMain.x = toX - num3;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			createSkillZoro2(0, toX + CRes.random_Am_0(5), toY - 20, 2);
		}
		if (f == 22)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			changeDir();
			objFireMain.Dir = Dir;
			int num4 = 20;
			if (Dir == 0)
			{
				num4 = -20;
			}
			objFireMain.x = toX - num4;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			objBeFireMain.dy = 40;
			objFireMain.dy = 35;
			GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			createSkillZoro2(1, toX + CRes.random_Am_0(5), toY - 20, 2);
		}
		if (f == 28)
		{
			changeDir();
			objFireMain.Dir = Dir;
			Point_Focus point_Focus = new Point_Focus();
			int num5 = 20;
			if (Dir == 0)
			{
				num5 = -20;
			}
			int num6 = toX;
			int num7 = toY;
			toX = x;
			toY = y;
			x = num6;
			y = num7;
			int xdich = toX - (x - num5);
			int ydich = toY - y;
			objFireMain.x = x - num5;
			objFireMain.y = y;
			objFireMain.dy = 0;
			objBeFireMain.dy = 0;
			create_Speed(xdich, ydich, point_Focus);
			objFireMain.vx = point_Focus.vx;
			objFireMain.vy = -point_Focus.vy;
			objFireMain.toX = point_Focus.toX;
			objFireMain.toY = point_Focus.toY;
		}
		if (f > 28 && MainObject.getDistance(objFireMain.x, objFireMain.y, objFireMain.toX, objFireMain.toY) < vMax)
		{
			objFireMain.vx = 0;
			objFireMain.vy = 0;
			objFireMain.toX = objFireMain.x;
			objFireMain.toY = objFireMain.y;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
	}

	public void update_Zoro_S2_L7()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (isAddSound)
			{
				mSound.playSound(9, mSound.volumeSound);
			}
			if (!checkNullObject(2))
			{
				setAva(2, objBeFireMain);
				GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				_ = Dir;
				GameScreen.addEffectEnd(26, 5, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
				objFireMain.vx = 0;
				objFireMain.vy = 0;
				objFireMain.toX = objFireMain.x;
				objFireMain.toY = objFireMain.y;
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		if (mframe[f] > -2)
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 14)
		{
			objFireMain.y = objBeFireMain.y;
			giatocFly = 8;
		}
		if (f < 18 && f >= 14)
		{
			objBeFireMain.dy += giatocFly;
			giatocFly /= 2;
		}
		if (f >= 18 && f < 26)
		{
			giatocFly = 0;
			objBeFireMain.dy = 20;
			objFireMain.dy = 15;
		}
		if (f == 10)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			int num = 20;
			if (Dir == 0)
			{
				num = -20;
			}
			objFireMain.x = toX - num;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			createSkillZoro2(1, toX + CRes.random_Am_0(5), toY - 20, 2);
		}
		if (f >= 14 && f < 22)
		{
			objBeFireMain.dy = 30;
			objFireMain.dy = 25;
		}
		if (f == 16)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			changeDir();
			objFireMain.Dir = Dir;
			int num2 = 30;
			if (Dir == 0)
			{
				num2 = -30;
			}
			objFireMain.x = toX - num2;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			createSkillZoro2(0, toX + CRes.random_Am_0(5), toY - 20, 2);
		}
		if (f == 22)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			changeDir();
			objFireMain.Dir = Dir;
			int num3 = 20;
			if (Dir == 0)
			{
				num3 = -20;
			}
			objFireMain.x = toX - num3;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			objBeFireMain.dy = 40;
			objFireMain.dy = 35;
			GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			createSkillZoro2(1, toX + CRes.random_Am_0(5), toY - 20, 2);
		}
		if (f == 28)
		{
			changeDir();
			objFireMain.Dir = Dir;
			Point_Focus point_Focus = new Point_Focus();
			int num4 = 20;
			if (Dir == 0)
			{
				num4 = -20;
			}
			int num5 = toX;
			int num6 = toY;
			toX = x;
			toY = y;
			x = num5;
			y = num6;
			int xdich = toX - (x - num4);
			int ydich = toY - y;
			objFireMain.x = x - num4;
			objFireMain.y = y;
			objFireMain.dy = 0;
			objBeFireMain.dy = 0;
			create_Speed(xdich, ydich, point_Focus);
			objFireMain.vx = point_Focus.vx;
			objFireMain.vy = -point_Focus.vy;
			objFireMain.toX = point_Focus.toX;
			objFireMain.toY = point_Focus.toY;
		}
		if (f > 28 && MainObject.getDistance(objFireMain.x, objFireMain.y, objFireMain.toX, objFireMain.toY) < vMax)
		{
			objFireMain.vx = 0;
			objFireMain.vy = 0;
			objFireMain.toX = objFireMain.x;
			objFireMain.toY = objFireMain.y;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.update();
			if (point.f >= point.fRe)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
		if (f > 30)
		{
			for (int j = 0; j < VecSubEff.size(); j++)
			{
				((Point_Focus)VecSubEff.elementAt(j)).update_Vx_Vy();
			}
		}
	}

	public void addShadow_Zoro_S1_L6()
	{
		Point point = new Point(objFireMain.x, objFireMain.y + 2);
		point.frame = (f - 12) / 2;
		if (point.frame >= fraImgSubEff.nFrame)
		{
			point.frame = fraImgSubEff.nFrame - 1;
		}
		VecSubEff.addElement(point);
		if (Dir == 0)
		{
			objFireMain.vx = -objFireMain.vMax * 4;
		}
		else
		{
			objFireMain.vx = objFireMain.vMax * 4;
		}
	}

	public void updateZoroS2_New_SHORT()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(1))
			{
				int num = 30;
				if (Dir == 0)
				{
					num = -30;
				}
				int subtype = 0;
				if (typeEffect == 184)
				{
					subtype = 2;
				}
				if (typeEffect == 482)
				{
					subtype = 5;
				}
				GameScreen.addEffectEnd(26, subtype, objFireMain.x + num, objFireMain.y - 5, Dir, objMainEff);
				GameScreen.addEffectEnd(26, subtype, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 3, objFireMain.x + num, objFireMain.y - 35, Dir, objMainEff);
				objFireMain.vx = 0;
				objFireMain.vy = 0;
				objFireMain.toX = objFireMain.x;
				objFireMain.toY = objFireMain.y;
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		if ((f > 2 && f < 10) || (f > 12 && f < 16) || (f > 18 && f < 22))
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		int num2 = 5;
		if (f == 7)
		{
			objFireMain.y = objBeFireMain.y;
			giatocFly = 8;
		}
		if (f < 10 && f >= 7)
		{
			objBeFireMain.dy += giatocFly;
			giatocFly /= 2;
		}
		if (f >= 10 && f < 16)
		{
			giatocFly = 0;
			objBeFireMain.dy = 20;
			objFireMain.dy = 15;
		}
		if (f == 10)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			int num3 = 20;
			if (Dir == 0)
			{
				num3 = -20;
			}
			objFireMain.x = toX - num3;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(16, 1, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num2, Dir, objMainEff);
		}
		if (f >= 16 && f < 22)
		{
			objBeFireMain.dy = 30;
			objFireMain.dy = 25;
		}
		if (f == 16)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			changeDir();
			objFireMain.Dir = Dir;
			int num4 = 30;
			if (Dir == 0)
			{
				num4 = -30;
			}
			objFireMain.x = toX - num4;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(16, 2, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num2, Dir, objMainEff);
		}
		if (f == 22)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			changeDir();
			objFireMain.Dir = Dir;
			int num5 = 20;
			if (Dir == 0)
			{
				num5 = -20;
			}
			objFireMain.x = toX - num5;
			objFireMain.y = toY + objBeFireMain.hOne / 2;
			setAva(0, objBeFireMain);
			objBeFireMain.dy = 40;
			objFireMain.dy = 35;
			GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(16, 1, objFireMain.x + num5, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num2, Dir, objMainEff);
			if (isAddSound)
			{
				mSound.playSound(9, mSound.volumeSound);
			}
			if (!checkNullObject(2))
			{
				setAva(2, objBeFireMain);
				GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
		}
		if (f > 23 && MainObject.getDistance(objFireMain.x, objFireMain.y, objFireMain.toX, objFireMain.toY) < vMax)
		{
			objFireMain.vx = 0;
			objFireMain.vy = 0;
			objFireMain.toX = objFireMain.x;
			objFireMain.toY = objFireMain.y;
		}
	}

	public void updateZoroS1_New()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(1))
			{
				objFireMain.vx = 0;
				objFireMain.dy = 0;
			}
			removeEff();
			return;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f == point_Focus.fRe + 1)
			{
				point_Focus.vx = 0;
				point_Focus.vy = 0;
				point_Focus.x = objBeFireMain.x;
				point_Focus.y = objBeFireMain.y;
			}
			if (point_Focus.f > point_Focus.fRe + 10)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		int num = 5;
		if (f <= fRemove)
		{
			if (f == 21)
			{
				giatocFly = 12;
			}
			if (f >= 21 && f <= 26)
			{
				objBeFireMain.dy += giatocFly;
				giatocFly -= 2;
			}
			if (f > 26)
			{
				giatocFly = 0;
				setAva(-1, objBeFireMain);
				objFireMain.y = objBeFireMain.y;
				objFireMain.vx = 0;
				objFireMain.dy = 40;
				objBeFireMain.dy = 45;
			}
			else if (f == 24)
			{
				setAva(-1, objBeFireMain);
				int num2 = objBeFireMain.x - 10;
				if (Dir == 0)
				{
					num2 = objBeFireMain.x + 10;
				}
				int num3 = num2 - objFireMain.x;
				objFireMain.vx = num3 / 4;
			}
			else if (f >= 22)
			{
				setAva(-1, objBeFireMain);
			}
		}
		if (f == 5 || f == 37)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			if (f == 5)
			{
				setAva(0, objBeFireMain);
			}
			int num4 = 20;
			if (Dir == 0)
			{
				num4 = -20;
			}
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(16, 1, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
		}
		if (f == 10 || f == 42)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			if (f == 10)
			{
				setAva(0, objBeFireMain);
			}
			int num5 = 30;
			if (Dir == 0)
			{
				num5 = -30;
			}
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(16, 2, objFireMain.x + num5, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 + num, Dir, objMainEff);
		}
		if (f == 47)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			int num6 = 20;
			if (Dir == 0)
			{
				num6 = -20;
			}
			GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(16, 1, objFireMain.x + num6, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
		}
		if (f == 12)
		{
			GameScreen.addEffectEnd(30, 0, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, 300, Dir, objMainEff);
		}
		if (f == 22)
		{
			if (isAddSound)
			{
				mSound.playSound(8, mSound.volumeSound);
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			int num7 = 20;
			if (Dir == 0)
			{
				num7 = -20;
			}
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(19, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(16, 1, x + num7, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
			int xdich = objBeFireMain.x - x;
			int ydich = objBeFireMain.y - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			VecEff.addElement(p);
		}
	}

	public void updateZoro_S1_L3_SHORT()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(1))
			{
				objFireMain.vx = 0;
				objFireMain.dy = 0;
			}
			removeEff();
			return;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f == point_Focus.fRe + 1)
			{
				point_Focus.vx = 0;
				point_Focus.vy = 0;
				if (typeEffect == 183 || typeEffect == 215)
				{
					point_Focus.vy = -4;
				}
				point_Focus.x = objBeFireMain.x;
				point_Focus.y = objBeFireMain.y;
			}
			if (typeEffect == 183 || typeEffect == 215)
			{
				if (point_Focus.f > point_Focus.fRe + 7)
				{
					VecEff.removeElement(point_Focus);
					i--;
				}
			}
			else if (point_Focus.f > point_Focus.fRe + 5)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		int num = 5;
		if (f <= 14)
		{
			if (f == 1)
			{
				giatocFly = 12;
			}
			if (f >= 1 && f <= 6)
			{
				objBeFireMain.dy += giatocFly;
				giatocFly -= 2;
			}
			if (f > 6)
			{
				giatocFly = 0;
				setAva(-1, objBeFireMain);
				objFireMain.y = objBeFireMain.y;
				objFireMain.vx = 0;
				objFireMain.dy = 40;
				objBeFireMain.dy = 45;
			}
			else if (f == 4)
			{
				setAva(-1, objBeFireMain);
				int num2 = objBeFireMain.x - 10;
				if (Dir == 0)
				{
					num2 = objBeFireMain.x + 10;
				}
				int num3 = num2 - objFireMain.x;
				objFireMain.vx = num3 / 4;
			}
			else if (f >= 2)
			{
				setAva(-1, objBeFireMain);
			}
		}
		if (f == 8 || f == 12)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			setAva(1, objBeFireMain);
			int num4 = 20;
			if (Dir == 0)
			{
				num4 = -20;
			}
			int subtype = 1;
			if (typeEffect == 183)
			{
				subtype = -1;
			}
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(108, 2, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
			if (typeEffect == 215)
			{
				GameScreen.addEffectEnd(135, 0, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 5 + num, Dir, objMainEff);
			}
			else
			{
				GameScreen.addEffectEnd(16, subtype, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
			}
		}
		if (f == 1)
		{
			int xdich = objBeFireMain.x - x;
			int ydich = objBeFireMain.y - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			VecEff.addElement(p);
		}
	}

	public void update_Zoro_S1_L6()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(1))
			{
				objFireMain.vx = 0;
				objFireMain.dy = 0;
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f == point_Focus.fRe + 1)
			{
				point_Focus.vx = 0;
				point_Focus.vy = -4;
			}
			if (point_Focus.f > point_Focus.fRe + 7)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		int num = 5;
		if (f <= 14)
		{
			if (f == 1)
			{
				giatocFly = 12;
			}
			if (f >= 1 && f <= 6)
			{
				objBeFireMain.dy += giatocFly;
				giatocFly -= 2;
			}
			if (f > 6)
			{
				giatocFly = 0;
				setAva(-1, objBeFireMain);
				objFireMain.y = objBeFireMain.y;
				objFireMain.vx = 0;
				objFireMain.dy = 40;
				objBeFireMain.dy = 45;
			}
			else if (f == 4)
			{
				setAva(-1, objBeFireMain);
				int num2 = objBeFireMain.x - 10;
				if (Dir == 0)
				{
					num2 = objBeFireMain.x + 10;
				}
				_ = objFireMain.x;
			}
			else if (f >= 2)
			{
				setAva(-1, objBeFireMain);
			}
		}
		if (f == 8 || f == 12)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			setAva(1, objBeFireMain);
			int num3 = 20;
			if (Dir == 0)
			{
				num3 = -20;
			}
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(108, 2, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
		}
		if (f == 1)
		{
			int xdich = 0;
			int ydich = objBeFireMain.y - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			p.x = objBeFireMain.x;
			p.y = objBeFireMain.y;
			VecEff.addElement(p);
			int num4 = ((Dir == 0) ? 5 : (-5));
			GameScreen.addEffectEnd(170, 0, objFireMain.x + num4, objFireMain.y + 22, Dir, objMainEff);
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			if (f > 8 + j * 4)
			{
				((Point_Focus)VecSubEff.elementAt(j)).update_Vx_Vy();
			}
		}
	}

	public void update_Zoro_S1_L7()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(1))
			{
				objFireMain.vx = 0;
				objFireMain.dy = 0;
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f == point_Focus.fRe + 1)
			{
				point_Focus.vx = 0;
				point_Focus.vy = -4;
			}
			if (point_Focus.f > point_Focus.fRe + 7)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		int num = 5;
		if (f <= 14)
		{
			if (f == 1)
			{
				giatocFly = 12;
			}
			if (f >= 1 && f <= 6)
			{
				objBeFireMain.dy += giatocFly;
				giatocFly -= 2;
			}
			if (f > 6)
			{
				giatocFly = 0;
				setAva(-1, objBeFireMain);
				objFireMain.y = objBeFireMain.y;
				objFireMain.vx = 0;
				objFireMain.dy = 40;
				objBeFireMain.dy = 45;
			}
			else if (f == 4)
			{
				setAva(-1, objBeFireMain);
				int num2 = objBeFireMain.x - 10;
				if (Dir == 0)
				{
					num2 = objBeFireMain.x + 10;
				}
				_ = objFireMain.x;
			}
			else if (f >= 2)
			{
				setAva(-1, objBeFireMain);
			}
		}
		if (f == 8 || f == 12)
		{
			if (isAddSound)
			{
				mSound.playSound(7, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			setAva(1, objBeFireMain);
			int num3 = 20;
			if (Dir == 0)
			{
				num3 = -20;
			}
			GameScreen.addEffectEnd(10, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			GameScreen.addEffectEnd(108, 2, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 2 - 10 + num, Dir, objMainEff);
		}
		if (f == 1)
		{
			int xdich = 0;
			int ydich = objBeFireMain.y - y;
			Point_Focus p = new Point_Focus();
			p = create_Speed(xdich, ydich, p);
			p.x = objBeFireMain.x;
			p.y = objBeFireMain.y;
			VecEff.addElement(p);
			int num4 = ((Dir == 0) ? 5 : (-5));
			GameScreen.addEffectEnd(181, 0, objFireMain.x + num4, objFireMain.y + 22, Dir, objMainEff);
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			if (f > 8 + j * 4)
			{
				((Point_Focus)VecSubEff.elementAt(j)).update_Vx_Vy();
			}
		}
	}

	public void updateLuffyS3_New()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			removeEff();
			return;
		}
		if (f == 5)
		{
			int num = 30;
			if (Dir == 2)
			{
				num = -30;
			}
			Point o = new Point(objFireMain.x + num, objFireMain.y);
			VecSubEff.addElement(o);
		}
		if (f == 10)
		{
			int num2 = -10;
			if (Dir == 2)
			{
				num2 = 10;
			}
			Point o2 = new Point(objFireMain.x + num2, objFireMain.y - 35);
			VecSubEff.addElement(o2);
		}
		if (f == 15)
		{
			int num3 = -10;
			if (Dir == 2)
			{
				num3 = 10;
			}
			Point o3 = new Point(objFireMain.x + num3, objFireMain.y + 35);
			VecSubEff.addElement(o3);
		}
		if ((f == 22 || f == 25) && isAddSound)
		{
			mSound.playSound(4, mSound.volumeSound);
		}
		if (f >= 18)
		{
			if (f % 3 == 0)
			{
				if (indexObjBefire < vecObjsBeFire.size())
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
					indexObjBefire++;
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							sbyte dir = 0;
							if (objFireMain.x < mainObject.x)
							{
								dir = 2;
							}
							int num4 = 12;
							if (Dir == 0)
							{
								num4 = -12;
							}
							int num5 = 2;
							if (typeEffect == 182)
							{
								num5 = 3;
							}
							GameScreen.addEffectEnd_ObjTo(13, num5, objFireMain.x + num4, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, mainObject.ID, mainObject.typeObject, dir, objMainEff);
							for (int i = 0; i < VecSubEff.size(); i++)
							{
								Point point = (Point)VecSubEff.elementAt(i);
								int num6 = -20;
								if (Dir == 2)
								{
									num6 = 20;
								}
								GameScreen.addEffectEnd_ObjTo(13, num5, point.x + num4 + num6, point.y - objFireMain.hOne / 2, mainObject.ID, mainObject.typeObject, dir, objMainEff);
							}
						}
					}
				}
				else
				{
					int num7 = 12;
					if (Dir == 0)
					{
						num7 = -12;
					}
					int num8 = 0;
					if (typeEffect == 182)
					{
						num8 = 3;
					}
					if (CRes.random(3) == 0)
					{
						int xTo = objFireMain.x + num7 + am_duong * 120 + CRes.random_Am_0(20);
						int yTo = objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10 - objFireMain.hOne / 2 + CRes.random_Am_0(80);
						GameScreen.addEffectEnd_ToX_ToY(13, (sbyte)num8, objFireMain.x + num7, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, xTo, yTo, Dir, objMainEff);
					}
					for (int j = 0; j < VecSubEff.size(); j++)
					{
						Point point2 = (Point)VecSubEff.elementAt(j);
						if (CRes.random(3) == 0)
						{
							int num9 = -20;
							if (Dir == 2)
							{
								num9 = 20;
							}
							int xTo2 = point2.x + num7 + num9 + am_duong * 120 + CRes.random_Am_0(20);
							int yTo2 = point2.y - objFireMain.hOne / 2 + CRes.random_Am_0(80);
							GameScreen.addEffectEnd_ToX_ToY(13, (sbyte)num8, point2.x + num7 + num9, point2.y - objFireMain.hOne / 2, xTo2, yTo2, Dir, objMainEff);
						}
					}
				}
			}
			addVir(15, 5, 10, isPlayer: true);
		}
		for (int k = 0; k < VecSubEff.size(); k++)
		{
			((Point)VecSubEff.elementAt(k)).f++;
		}
		for (int l = 0; l < VecEff.size(); l++)
		{
			Point point3 = (Point)VecEff.elementAt(l);
			point3.f++;
			if (point3.f >= 3)
			{
				VecEff.removeElement(point3);
				l--;
			}
		}
	}

	public void updateLuffyS3_L5()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			removeEff();
			return;
		}
		if (f < 4)
		{
			objFireMain.vx = -(am_duong * 7);
			objFireMain.dy += 20 - f * 3;
		}
		else if (f < fRemove - 3)
		{
			objFireMain.dy = 60;
			objFireMain.vx = 0;
		}
		else
		{
			if (objFireMain.dy <= 10)
			{
				objFireMain.dy = 0;
			}
			if (objFireMain.dy != 0)
			{
				objFireMain.dy /= 3;
			}
		}
		if (f == 5)
		{
			int num = 40;
			if (Dir == 2)
			{
				num = -40;
			}
			Point o = new Point(objFireMain.x + num, objFireMain.y - objFireMain.dy);
			VecSubEff.addElement(o);
		}
		if (typeEffect == 273)
		{
			if (f == 10)
			{
				int num2 = 15;
				if (Dir == 2)
				{
					num2 = -15;
				}
				Point o2 = new Point(objFireMain.x + num2, objFireMain.y - objFireMain.dy - 45);
				VecSubEff.addElement(o2);
				Point o3 = new Point(objFireMain.x + num2, objFireMain.y - objFireMain.dy + 45);
				VecSubEff.addElement(o3);
			}
			if (f == 15)
			{
				int num3 = 40;
				if (Dir == 2)
				{
					num3 = -40;
				}
				Point o4 = new Point(objFireMain.x + num3, objFireMain.y - objFireMain.dy - 90);
				VecSubEff.addElement(o4);
				Point o5 = new Point(objFireMain.x + num3, objFireMain.y - objFireMain.dy + 90);
				VecSubEff.addElement(o5);
			}
		}
		else
		{
			if (f == 10)
			{
				int num4 = 15;
				if (Dir == 2)
				{
					num4 = -15;
				}
				Point o6 = new Point(objFireMain.x + num4, objFireMain.y - objFireMain.dy - 45);
				VecSubEff.addElement(o6);
			}
			if (f == 15)
			{
				int num5 = 15;
				if (Dir == 2)
				{
					num5 = -15;
				}
				Point o7 = new Point(objFireMain.x + num5, objFireMain.y - objFireMain.dy + 45);
				VecSubEff.addElement(o7);
			}
		}
		if ((f == 22 || f == 25) && isAddSound)
		{
			mSound.playSound(4, mSound.volumeSound);
		}
		if (f >= 18)
		{
			if (f % 3 == 0)
			{
				if (indexObjBefire < vecObjsBeFire.size())
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
					indexObjBefire++;
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							sbyte dir = 0;
							if (objFireMain.x < mainObject.x)
							{
								dir = 2;
							}
							int num6 = 12;
							if (Dir == 0)
							{
								num6 = -12;
							}
							int num7 = 4;
							if (typeEffect == 273)
							{
								num7 = 5;
							}
							GameScreen.addEffectEnd_ObjTo(13, num7, objFireMain.x + num6, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, mainObject.ID, mainObject.typeObject, dir, objMainEff);
							for (int i = 0; i < VecSubEff.size(); i++)
							{
								Point point = (Point)VecSubEff.elementAt(i);
								int num8 = -20;
								if (Dir == 2)
								{
									num8 = 20;
								}
								GameScreen.addEffectEnd_ObjTo(13, num7, point.x + num6 + num8, point.y - objFireMain.hOne / 2, mainObject.ID, mainObject.typeObject, dir, objMainEff);
							}
						}
					}
				}
				else if (!GameCanvas.lowGraphic)
				{
					int num9 = 12;
					if (Dir == 0)
					{
						num9 = -12;
					}
					int num10 = 4;
					if (typeEffect == 273)
					{
						num10 = 5;
					}
					if (CRes.random(3) == 0)
					{
						int xTo = objFireMain.x + num9 + am_duong * 120 + CRes.random_Am_0(20);
						int yTo = objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10 - objFireMain.hOne / 2 + CRes.random_Am_0(80);
						GameScreen.addEffectEnd_ToX_ToY(13, (sbyte)num10, objFireMain.x + num9, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, xTo, yTo, Dir, objMainEff);
					}
					for (int j = 0; j < VecSubEff.size(); j++)
					{
						Point point2 = (Point)VecSubEff.elementAt(j);
						if (CRes.random(3) == 0)
						{
							int num11 = -20;
							if (Dir == 2)
							{
								num11 = 20;
							}
							int xTo2 = point2.x + num9 + num11 + am_duong * 120 + CRes.random_Am_0(20);
							int yTo2 = point2.y - objFireMain.hOne / 2 + CRes.random_Am_0(80);
							GameScreen.addEffectEnd_ToX_ToY(13, (sbyte)num10, point2.x + num9 + num11, point2.y - objFireMain.hOne / 2, xTo2, yTo2, Dir, objMainEff);
						}
					}
				}
			}
			addVir(15, 5, 10, isPlayer: true);
		}
		for (int k = 0; k < VecSubEff.size(); k++)
		{
			((Point)VecSubEff.elementAt(k)).f++;
		}
		for (int l = 0; l < VecEff.size(); l++)
		{
			Point point3 = (Point)VecEff.elementAt(l);
			point3.f++;
			if (point3.f >= 3)
			{
				VecEff.removeElement(point3);
				l--;
			}
		}
	}

	public void update_Luffy_S3_L7()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			removeEff();
			return;
		}
		if (f < 4)
		{
			objFireMain.vx = -(am_duong * 7);
			objFireMain.dy += 20 - f * 3;
		}
		else if (f < fRemove - 3)
		{
			objFireMain.dy = 60;
			objFireMain.vx = 0;
		}
		else
		{
			if (objFireMain.dy <= 10)
			{
				objFireMain.dy = 0;
			}
			if (objFireMain.dy != 0)
			{
				objFireMain.dy /= 3;
			}
		}
		if (f == 3)
		{
			int num = -150;
			int dir = 2;
			if (Dir == 2)
			{
				num = 150;
				dir = 0;
			}
			Point point = new Point(objFireMain.x + num, objFireMain.y - objFireMain.dy);
			point.dir = dir;
			VecSubEff.addElement(point);
		}
		if (f == 5)
		{
			int num2 = 40;
			if (Dir == 2)
			{
				num2 = -40;
			}
			int num3 = -150;
			int dir2 = 2;
			if (Dir == 2)
			{
				num3 = 150;
				dir2 = 0;
			}
			Point point2 = new Point(objFireMain.x + num2, objFireMain.y - objFireMain.dy);
			point2.dir = Dir;
			VecSubEff.addElement(point2);
			point2 = new Point(objFireMain.x - num2 + num3, objFireMain.y - objFireMain.dy);
			point2.dir = dir2;
			VecSubEff.addElement(point2);
		}
		if (f == 10)
		{
			int num4 = 15;
			if (Dir == 2)
			{
				num4 = -15;
			}
			Point point3 = new Point(objFireMain.x + num4, objFireMain.y - objFireMain.dy - 45);
			point3.dir = Dir;
			VecSubEff.addElement(point3);
			point3 = new Point(objFireMain.x + num4, objFireMain.y - objFireMain.dy + 45);
			point3.dir = Dir;
			VecSubEff.addElement(point3);
			int num5 = -150;
			int dir3 = 2;
			if (Dir == 2)
			{
				num5 = 150;
				dir3 = 0;
			}
			point3 = new Point(objFireMain.x - num4 + num5, objFireMain.y - objFireMain.dy - 45);
			point3.dir = dir3;
			VecSubEff.addElement(point3);
			point3 = new Point(objFireMain.x - num4 + num5, objFireMain.y - objFireMain.dy + 45);
			point3.dir = dir3;
			VecSubEff.addElement(point3);
		}
		if ((f == 22 || f == 25) && isAddSound)
		{
			mSound.playSound(4, mSound.volumeSound);
		}
		if (f >= 18)
		{
			if (f % 3 == 0)
			{
				if (indexObjBefire < vecObjsBeFire.size())
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
					indexObjBefire++;
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							sbyte b = 0;
							if (objFireMain.x < mainObject.x)
							{
								b = 2;
							}
							int num6 = 12;
							if (Dir == 0)
							{
								num6 = -12;
							}
							int num7 = 5;
							int num8 = -150;
							int num9 = 2;
							if (b == 2)
							{
								num8 = 150;
								num9 = 0;
							}
							GameScreen.addEffectEnd_ObjTo(13, num7, objFireMain.x + num6, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, mainObject.ID, mainObject.typeObject, b, objMainEff);
							GameScreen.addEffectEnd_ObjTo(13, num7, objFireMain.x + num6 + num8, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, mainObject.ID, mainObject.typeObject, (sbyte)num9, objMainEff);
							for (int i = 0; i < VecSubEff.size(); i++)
							{
								Point point4 = (Point)VecSubEff.elementAt(i);
								int num10 = -20;
								if (Dir == 2)
								{
									num10 = 20;
								}
								GameScreen.addEffectEnd_ObjTo(13, num7, point4.x + num6 + num10, point4.y - objFireMain.hOne / 2, mainObject.ID, mainObject.typeObject, (sbyte)point4.dir, objMainEff);
							}
						}
					}
				}
				else if (!GameCanvas.lowGraphic)
				{
					int num11 = -150;
					int num12 = 2;
					if (Dir == 2)
					{
						num11 = 150;
						num12 = 0;
					}
					int num13 = 12;
					if (Dir == 0)
					{
						num13 = -12;
					}
					int num14 = 5;
					if (CRes.random(3) == 0)
					{
						int num15 = objFireMain.x + num13 + am_duong * 120 + CRes.random_Am_0(20);
						int yTo = objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10 - objFireMain.hOne / 2 + CRes.random_Am_0(80);
						GameScreen.addEffectEnd_ToX_ToY(13, (sbyte)num14, objFireMain.x + num13, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, num15, yTo, Dir, objMainEff);
						GameScreen.addEffectEnd_ToX_ToY(13, (sbyte)num14, objFireMain.x - num13 + num11, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, num15 + num11, yTo, (sbyte)num12, objMainEff);
					}
					for (int j = 0; j < VecSubEff.size(); j++)
					{
						Point point5 = (Point)VecSubEff.elementAt(j);
						if (CRes.random(3) == 0)
						{
							int num16 = -20;
							if (Dir == 2)
							{
								num16 = 20;
							}
							int num17 = point5.x + num13 + num16 + am_duong * 120 + CRes.random_Am_0(20);
							int yTo2 = point5.y - objFireMain.hOne / 2 + CRes.random_Am_0(80);
							GameScreen.addEffectEnd_ToX_ToY(13, (sbyte)num14, point5.x + num13 + num16 + num11, point5.y - objFireMain.hOne / 2, num17 + num11, yTo2, (sbyte)point5.dir, objMainEff);
						}
					}
				}
			}
			addVir(15, 5, 10, isPlayer: true);
		}
		for (int k = 0; k < VecSubEff.size(); k++)
		{
			((Point)VecSubEff.elementAt(k)).f++;
		}
		for (int l = 0; l < VecEff.size(); l++)
		{
			Point point6 = (Point)VecEff.elementAt(l);
			point6.f++;
			if (point6.f >= 3)
			{
				VecEff.removeElement(point6);
				l--;
			}
		}
	}

	public void updateLuffyS2_NEW()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			removeEff();
			if (objFireMain == GameScreen.player)
			{
				GameScreen.setIsMoveEff(ismove: true);
			}
			return;
		}
		if (f >= 12 && f <= 20)
		{
			objFireMain.isTanHinh = true;
			if (objFireMain == GameScreen.player)
			{
				Player.isSendMove = false;
			}
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 16)
		{
			int num = 220;
			if (Dir == 0)
			{
				num = -220;
			}
			objFireMain.x += num;
			x = objFireMain.x;
			Dir = (sbyte)((Dir == 0) ? 2 : 0);
			objFireMain.Dir = Dir;
		}
		if (f == 12)
		{
			setDy(-10, objBeFireMain);
			int num2 = 20;
			if (Dir == 0)
			{
				num2 = -20;
			}
			GameScreen.addEffectEnd(0, 0, objFireMain.x + num2, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
			if (isAddSound)
			{
				mSound.playSound(3, mSound.volumeSound);
			}
		}
		if (f == 29)
		{
			addVir(5, 5, 10, isPlayer: true);
			setAva(2, objBeFireMain);
			int num3 = 20;
			if (Dir == 0)
			{
				num3 = -20;
			}
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			GameScreen.addEffectEnd(0, 0, objFireMain.x + num3, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
			if (isAddSound)
			{
				mSound.playSound(3, mSound.volumeSound);
			}
		}
		if (f <= 20)
		{
			return;
		}
		if (f < 27)
		{
			if (Dir == 0)
			{
				objFireMain.vx = -objFireMain.vMax * 4;
			}
			else
			{
				objFireMain.vx = objFireMain.vMax * 4;
			}
			if (f % 2 == 0 || typeEffect == 35)
			{
				Point o = new Point(objFireMain.x - objFireMain.vx / 2, objFireMain.y);
				VecEff.addElement(o);
			}
		}
		else
		{
			if (objFireMain == GameScreen.player)
			{
				Player.isSendMove = true;
			}
			objFireMain.vx = 0;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			point.f++;
			if (point.f / 2 >= 3)
			{
				VecEff.removeElement(point);
				i--;
			}
		}
	}

	public void updateLuffyS2_NEW_SHORT()
	{
		if ((f >= fRemove && (typeEffect != 213 || typeEffect == 272 || VecSubEff.size() == 0)) || checkNullObject(1))
		{
			removeEff();
			if (objFireMain == GameScreen.player)
			{
				GameScreen.setIsMoveEff(ismove: true);
			}
			return;
		}
		if (f >= 4 && f <= 11)
		{
			objFireMain.isTanHinh = true;
			if (objFireMain == GameScreen.player)
			{
				Player.isSendMove = false;
			}
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 7)
		{
			int num = 320;
			if (Dir == 0)
			{
				num = -320;
			}
			objFireMain.x += num;
			x = objFireMain.x;
			Dir = (sbyte)((Dir == 0) ? 2 : 0);
			objFireMain.Dir = Dir;
		}
		if (f == 3)
		{
			setDy(-10, objBeFireMain);
			int num2 = 20;
			if (Dir == 0)
			{
				num2 = -20;
			}
			GameScreen.addEffectEnd(0, 0, objFireMain.x + num2, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
			if (isAddSound)
			{
				mSound.playSound(3, mSound.volumeSound);
			}
		}
		if (f == 20)
		{
			addVir(5, 5, 10, isPlayer: true);
			setAva(2, objBeFireMain);
			int num3 = 20;
			if (Dir == 0)
			{
				num3 = -20;
			}
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				int subtype = 5;
				if (typeEffect == 272)
				{
					subtype = 3;
				}
				GameScreen.addEffectEnd(108, subtype, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(0, 0, objFireMain.x + num3, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
				if (typeEffect == 181 || typeEffect == 213 || typeEffect == 272)
				{
					num3 = 10;
					if (Dir == 0)
					{
						num3 = -10;
					}
					int subtype2 = 0;
					if (typeEffect == 213)
					{
						subtype2 = 3;
					}
					else if (typeEffect == 272)
					{
						subtype2 = 4;
					}
					GameScreen.addEffectEnd(119, subtype2, objFireMain.x + num3, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
				}
			}
			if (isAddSound)
			{
				mSound.playSound(3, mSound.volumeSound);
			}
		}
		if (f == 22 && typeEffect == 272)
		{
			int num4 = 10;
			if (Dir == 0)
			{
				num4 = -10;
			}
			GameScreen.addEffectEnd(173, 0, objFireMain.x + num4, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
		}
		if (f <= 11)
		{
			return;
		}
		if (f < 18)
		{
			if (Dir == 0)
			{
				objFireMain.vx = -objFireMain.vMax * 4;
			}
			else
			{
				objFireMain.vx = objFireMain.vMax * 4;
			}
			if (f % 2 == 0 || typeEffect == 35)
			{
				Point o = new Point(objFireMain.x - objFireMain.vx / 2, objFireMain.y);
				VecEff.addElement(o);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(109, 0, objFireMain.x, objFireMain.y + 5, Dir, objMainEff);
			}
			if (typeEffect == 213 || typeEffect == 272)
			{
				Point point = new Point(objFireMain.x, objFireMain.y + 2);
				point.frame = (f - 12) / 2;
				if (point.frame >= fraImgSub3Eff.nFrame)
				{
					point.frame = fraImgSub3Eff.nFrame - 1;
				}
				VecSubEff.addElement(point);
			}
		}
		else
		{
			if (objFireMain == GameScreen.player)
			{
				Player.isSendMove = true;
			}
			objFireMain.vx = 0;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point2 = (Point)VecEff.elementAt(i);
			point2.f++;
			if (point2.f / 2 >= 3)
			{
				VecEff.removeElement(point2);
				i--;
			}
		}
		if (typeEffect != 213 && typeEffect != 272)
		{
			return;
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point obj = (Point)VecSubEff.elementAt(j);
			obj.f++;
			if (obj.f >= 5)
			{
				VecSubEff.removeElementAt(j);
				j--;
			}
		}
	}

	public void update_Luffy_S2_L7()
	{
		if ((f >= fRemove && VecSubEff.size() == 0) || checkNullObject(1))
		{
			removeEff();
			if (objFireMain == GameScreen.player)
			{
				GameScreen.setIsMoveEff(ismove: true);
			}
			return;
		}
		if (f >= 4 && f <= 11)
		{
			objFireMain.isTanHinh = true;
			if (objFireMain == GameScreen.player)
			{
				Player.isSendMove = false;
			}
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 7)
		{
			int num = 120;
			if (Dir == 0)
			{
				num = -120;
			}
			objFireMain.x += num;
			x = objFireMain.x;
			Dir = (sbyte)((Dir == 0) ? 2 : 0);
			objFireMain.Dir = Dir;
		}
		if (f == 3)
		{
			setDy(-10, objBeFireMain);
			int num2 = 20;
			if (Dir == 0)
			{
				num2 = -20;
			}
			GameScreen.addEffectEnd(0, 1, objFireMain.x + num2, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
			if (isAddSound)
			{
				mSound.playSound(3, mSound.volumeSound);
			}
		}
		if (f == 20)
		{
			addVir(5, 5, 10, isPlayer: true);
			setAva(2, objBeFireMain);
			int num3 = 20;
			if (Dir == 0)
			{
				num3 = -20;
			}
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(8, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
				int subtype = 3;
				GameScreen.addEffectEnd(108, subtype, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(0, 1, objFireMain.x + num3, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
				num3 = 10;
				if (Dir == 0)
				{
					num3 = -10;
				}
				int subtype2 = 4;
				GameScreen.addEffectEnd(119, subtype2, objFireMain.x + num3, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
			}
			if (isAddSound)
			{
				mSound.playSound(3, mSound.volumeSound);
			}
		}
		if (f == 22)
		{
			int num4 = 10;
			if (Dir == 0)
			{
				num4 = -10;
			}
			GameScreen.addEffectEnd(173, 1, objFireMain.x + num4, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
		}
		if (f <= 11)
		{
			return;
		}
		if (f < 18)
		{
			if (Dir == 0)
			{
				objFireMain.vx = -objFireMain.vMax * 4;
			}
			else
			{
				objFireMain.vx = objFireMain.vMax * 4;
			}
			if (f % 2 == 0 || typeEffect == 35)
			{
				Point o = new Point(objFireMain.x - objFireMain.vx / 2, objFireMain.y);
				VecEff.addElement(o);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(109, 0, objFireMain.x, objFireMain.y + 5, Dir, objMainEff);
			}
			Point point = new Point(objFireMain.x, objFireMain.y + 2);
			point.frame = (f - 12) / 2;
			if (point.frame >= fraImgSub3Eff.nFrame)
			{
				point.frame = fraImgSub3Eff.nFrame - 1;
			}
			VecSubEff.addElement(point);
		}
		else
		{
			if (objFireMain == GameScreen.player)
			{
				Player.isSendMove = true;
			}
			objFireMain.vx = 0;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point2 = (Point)VecEff.elementAt(i);
			point2.f++;
			if (point2.f / 2 >= 3)
			{
				VecEff.removeElement(point2);
				i--;
			}
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point obj = (Point)VecSubEff.elementAt(j);
			obj.f++;
			if (obj.f >= 5)
			{
				VecSubEff.removeElementAt(j);
				j--;
			}
		}
	}

	public void updateMon11()
	{
		if (!checkNullObject(1))
		{
			if (f < 2)
			{
				objFireMain.vx = vx1000;
			}
			else if (f < 5)
			{
				objFireMain.vx = -vx1000;
			}
			else
			{
				objFireMain.vx = 0;
			}
		}
		if (f == fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.vx = 0;
			}
			if (typeEffect == 144)
			{
				GameScreen.addEffectEnd(11, 0, toX + CRes.random_Am_0(5), toY + CRes.random_Am_0(10), Dir, objMainEff);
				setAva(0, objBeFireMain);
			}
			else
			{
				GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
			}
			if (fRemove >= 4)
			{
				if (!checkNullObject(1))
				{
					objFireMain.vx = 0;
				}
				removeEff();
			}
		}
		if (fRemove < 4 && f == 4)
		{
			if (!checkNullObject(1))
			{
				objFireMain.vx = 0;
			}
			removeEff();
		}
	}

	public void updateMon10()
	{
		if (!checkNullObject(1))
		{
			if (f < 2)
			{
				objFireMain.vx = vx;
			}
			else
			{
				objFireMain.vx = -vx;
			}
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.vx = 0;
			}
			if (typeEffect == 149)
			{
				GameScreen.addEffectEnd(8, 0, toX + CRes.random_Am_0(5), toY + CRes.random_Am_0(10), Dir, objMainEff);
				setAva(0, objBeFireMain);
			}
			else if (typeEffect == 143)
			{
				GameScreen.addEffectEnd(11, 0, toX + CRes.random_Am_0(5), toY + CRes.random_Am_0(10), Dir, objMainEff);
				setAva(0, objBeFireMain);
			}
			else
			{
				GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
			}
			removeEff();
		}
	}

	public void updateAlvida2()
	{
		if (f == 7)
		{
			addSound(14);
			int num = x;
			num = ((Dir != 0) ? (num + 15) : (num - 15));
			GameScreen.addEffectEnd(89, 0, num, y + 20, Dir, objMainEff);
		}
		if (f >= 7)
		{
			vy = 6;
		}
		if (f < fRemove)
		{
			return;
		}
		addVir(3, 5, 10, isPlayer: false);
		for (int i = 0; i < vecObjsBeFire.size(); i++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
			if (object_Effect_Skill != null)
			{
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject != null)
				{
					GameScreen.addEffectEnd(52, 0, mainObject.x, mainObject.y + 10, Dir, objMainEff);
					GameScreen.addEffectEnd(8, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, objMainEff);
				}
			}
		}
		removeEff();
	}

	public void update_Ussop_S3_L5()
	{
		if (f == 1)
		{
			mframeSuper = new int[4][]
			{
				new int[3] { 34, -30, 1 },
				new int[3] { 67, -44, 1 },
				new int[3] { 100, -42, 2 },
				new int[3] { 126, -17, 1 }
			};
		}
		if (f == 10 && !checkNullObject(3))
		{
			int num = toX - x;
			int num2 = toY - objBeFireMain.hOne - y - 50;
			create_Speed(num, num2, null);
			int frameAngle = CRes.angle(num, num2);
			frame = setFrameAngle(frameAngle);
			fRemove += 10;
		}
		if (f == fRemove)
		{
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(120, 0, x, y, Dir, objMainEff);
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(93, 2, mainObject.x + CRes.random_Am_0(10), mainObject.y - mainObject.hOne + CRes.random_Am_0(10), Dir, objMainEff);
					}
				}
			}
			indexObjBefire = 0;
			vx = 0;
			vy = 0;
		}
		if (f <= fRemove || f % 2 != 1)
		{
			return;
		}
		if (!GameCanvas.lowGraphic)
		{
			for (int j = 0; j < 2; j++)
			{
				GameScreen.addEffectEnd(120, mframeSuper[indexObjBefire][2], x + mframeSuper[indexObjBefire][0] * ((j == 0) ? 1 : (-1)), y + mframeSuper[indexObjBefire][1], Dir, objMainEff);
			}
		}
		indexObjBefire++;
		if (indexObjBefire >= mframeSuper.Length)
		{
			removeEff();
		}
	}

	public void update_Ussop_S3_L6()
	{
		if (f == 1)
		{
			mframeSuper = new int[4][]
			{
				new int[3]
				{
					40,
					-60,
					CRes.random(2, 4)
				},
				new int[3]
				{
					80,
					-25,
					CRes.random(1, 3)
				},
				new int[3]
				{
					120,
					-60,
					CRes.random(2, 4)
				},
				new int[3]
				{
					160,
					-25,
					CRes.random(2, 4)
				}
			};
		}
		if (f == 10 && !checkNullObject(3))
		{
			int num = toX - x;
			int num2 = toY - objBeFireMain.hOne - y - 50;
			create_Speed(num, num2, null);
			int frameAngle = CRes.angle(num, num2);
			frame = setFrameAngle(frameAngle);
			fRemove += 10;
			vMax = 14;
			rocket1 = create_Speed(toX + 80 - x, num2, new Point_Focus());
			rocket2 = create_Speed(toX - 80 - x, num2, new Point_Focus());
			frameAngle = CRes.angle(toX + 80 - x, num2);
			frame1 = setFrameAngle(frameAngle);
			frameAngle = CRes.angle(toX - 80 - x, num2);
			frame2 = setFrameAngle(frameAngle);
		}
		if (f > 10 && f < fRemove)
		{
			rocket1.update_Vx_Vy();
			rocket2.update_Vx_Vy();
		}
		if (f == fRemove)
		{
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(168, 1, x, y, Dir, objMainEff);
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(93, 2, mainObject.x + CRes.random_Am_0(10), mainObject.y - mainObject.hOne + CRes.random_Am_0(10), Dir, objMainEff);
					}
				}
			}
			indexObjBefire = 0;
			vx = 0;
			vy = 0;
		}
		if (f <= fRemove || f % 2 != 1)
		{
			return;
		}
		if (!GameCanvas.lowGraphic)
		{
			for (int j = 0; j < 2; j++)
			{
				GameScreen.addEffectEnd(168, mframeSuper[indexObjBefire][2], x + mframeSuper[indexObjBefire][0] * ((j == 0) ? 1 : (-1)), y + mframeSuper[indexObjBefire][1], Dir, objMainEff);
			}
		}
		indexObjBefire++;
		if (indexObjBefire >= mframeSuper.Length)
		{
			removeEff();
		}
	}

	public void update_Ussop_S3_L7()
	{
		if (f == 1)
		{
			mframeSuper = new int[4][]
			{
				new int[3]
				{
					40,
					-60,
					CRes.random(2, 4)
				},
				new int[3]
				{
					80,
					-25,
					CRes.random(1, 3)
				},
				new int[3]
				{
					120,
					-60,
					CRes.random(2, 4)
				},
				new int[3]
				{
					160,
					-25,
					CRes.random(2, 4)
				}
			};
		}
		if (f == 10 && !checkNullObject(3))
		{
			int num = toX - x;
			int num2 = toY - objBeFireMain.hOne - y - 50;
			create_Speed(num, num2, null);
			int frameAngle = CRes.angle(num, num2);
			frame = setFrameAngle(frameAngle);
			fRemove += 10;
			vMax = 14;
			rocket1 = create_Speed(toX + 80 - x, num2, new Point_Focus());
			rocket2 = create_Speed(toX - 80 - x, num2, new Point_Focus());
			frameAngle = CRes.angle(toX + 80 - x, num2);
			frame1 = setFrameAngle(frameAngle);
			frameAngle = CRes.angle(toX - 80 - x, num2);
			frame2 = setFrameAngle(frameAngle);
		}
		if (f > 10 && f < fRemove)
		{
			rocket1.update_Vx_Vy();
			rocket2.update_Vx_Vy();
		}
		if (f == 15)
		{
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(168, 1, x, y, Dir, objMainEff);
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(183, 0, mainObject.x, mainObject.y - mainObject.hOne / 3, Dir, objMainEff);
						GameScreen.addEffectEnd(93, 2, mainObject.x + CRes.random_Am_0(10), mainObject.y - mainObject.hOne + CRes.random_Am_0(10), Dir, objMainEff);
					}
				}
			}
			indexObjBefire = 0;
			vx = 0;
			vy = 0;
		}
		if (f == fRemove)
		{
			GameScreen.addEffectEnd(183, 1, objMainEff.x, objMainEff.y - objMainEff.hOne / 3, Dir, objMainEff);
		}
		if (f <= fRemove || f % 2 != 1)
		{
			return;
		}
		if (!GameCanvas.lowGraphic)
		{
			for (int j = 0; j < 2; j++)
			{
				GameScreen.addEffectEnd(168, mframeSuper[indexObjBefire][2], x + mframeSuper[indexObjBefire][0] * ((j == 0) ? 1 : (-1)), y + mframeSuper[indexObjBefire][1], Dir, objMainEff);
			}
		}
		indexObjBefire++;
		if (indexObjBefire >= mframeSuper.Length)
		{
			removeEff();
		}
	}

	public void update_Ussop_S3_L1()
	{
		if (f == 10 && !checkNullObject(3))
		{
			int num = toX - x;
			int num2 = toY - objBeFireMain.hOne - y - 30;
			create_Speed(num, num2, null);
			int frameAngle = CRes.angle(num, num2);
			frame = setFrameAngle(frameAngle);
			if (typeEffect != 69 && typeEffect != 194)
			{
				GameScreen.addEffectEnd(5, 0, x, y, Dir, objMainEff);
			}
			fRemove += 10;
		}
		if (f < fRemove)
		{
			return;
		}
		sbyte subtype = 0;
		if (typeEffect == 68)
		{
			subtype = 1;
		}
		else if (typeEffect == 69)
		{
			addVir(5, 5, 10, isPlayer: true);
			subtype = 2;
			GameScreen.addEffectEnd(48, 0, x - 30 + CRes.random_Am_0(10), y - 30 + CRes.random_Am_0(10), Dir, objMainEff);
			GameScreen.addEffectEnd(48, 0, x + 30 + CRes.random_Am_0(10), y - 30 + CRes.random_Am_0(10), Dir, objMainEff);
		}
		else if (typeEffect == 194)
		{
			addVir(5, 5, 10, isPlayer: true);
			subtype = 2;
			GameScreen.addEffectEnd(120, 0, x - 30 + CRes.random_Am_0(10), y - 30 + CRes.random_Am_0(10), Dir, objMainEff);
			GameScreen.addEffectEnd(120, 0, x + 30 + CRes.random_Am_0(10), y - 30 + CRes.random_Am_0(10), Dir, objMainEff);
			GameScreen.addEffectEnd(120, 0, x - 60 + CRes.random_Am_0(10), y + CRes.random_Am_0(10), Dir, objMainEff);
			GameScreen.addEffectEnd(120, 0, x + 60 + CRes.random_Am_0(10), y + CRes.random_Am_0(10), Dir, objMainEff);
		}
		GameScreen.addEffectEnd(48, subtype, x, y, Dir, objMainEff);
		for (int i = 0; i < vecObjsBeFire.size(); i++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
			if (object_Effect_Skill == null)
			{
				continue;
			}
			MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
			if (mainObject == null)
			{
				continue;
			}
			if (typeEffect == 67)
			{
				if (isAddSound)
				{
					mSound.playSound(14, mSound.volumeSound);
				}
				setAva(0, mainObject);
				GameScreen.addEffectEnd(1, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, objMainEff);
			}
			else if (typeEffect == 68)
			{
				if (isAddSound)
				{
					mSound.playSound(14, mSound.volumeSound);
				}
				setAva(1, mainObject);
				GameScreen.addEffectEnd(1, 0, mainObject.x + CRes.random_Am_0(10), mainObject.y - mainObject.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
				GameScreen.addEffectEnd(1, 0, mainObject.x + CRes.random_Am_0(10), mainObject.y - mainObject.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			}
			else if (typeEffect == 69)
			{
				if (isAddSound)
				{
					mSound.playSound(15, mSound.volumeSound);
				}
				if (i == 0)
				{
					setAva(2, mainObject);
				}
				else
				{
					GameScreen.addEffectEnd_ObjTo(49, 0, x, y, mainObject.ID, mainObject.typeObject, Dir, objMainEff);
				}
			}
			GameScreen.addEffectEnd(93, 2, mainObject.x + CRes.random_Am_0(10), mainObject.y - mainObject.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
		}
		removeEff();
	}

	public void updateMohji_1()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		if ((f >= 3 && f <= 11) || (f >= 26 && f <= 30))
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 8)
		{
			int num = 20;
			if (Dir == 2)
			{
				num = -20;
			}
			objFireMain.x = toX + num;
			objFireMain.y = toY;
		}
		if (f == 12 || f == 16)
		{
			addSound(7);
		}
		if (f == 12)
		{
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(10), toY - 5 + CRes.random_Am_0(10), Dir, objMainEff);
		}
		if (f == 20)
		{
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(10), toY - 5 + CRes.random_Am_0(10), Dir, objMainEff);
			setAva(0, objBeFireMain);
		}
		if (f == 30)
		{
			objFireMain.x = x;
			objFireMain.y = y;
		}
	}

	public void updateMohji_2()
	{
		if (f == 2)
		{
			addSound(7);
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(10), toY + CRes.random_Am_0(10), Dir, objMainEff);
			setAva(0, objBeFireMain);
		}
		if (f == 6)
		{
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(10), toY + CRes.random_Am_0(10), Dir, objMainEff);
			setAva(0, objBeFireMain);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateBuggy_1()
	{
		if (f == 2)
		{
			addSound(19);
			if (!checkNullObject(1) && objFireMain.plashNow != null)
			{
				objFireMain.plashNow.setIsNextf(1);
			}
			Point_Focus point_Focus = new Point_Focus();
			int xdich = toX - x;
			int ydich = toY - y;
			point_Focus.Dir = Dir;
			point_Focus.frame = 1;
			create_Speed(xdich, ydich, point_Focus);
			VecEff.addElement(point_Focus);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecEff.elementAt(i);
			point_Focus2.update_Vx_Vy();
			if (point_Focus2.f >= point_Focus2.fRe)
			{
				if (point_Focus2.frame == 1)
				{
					addSound(7);
					Point_Focus point_Focus3 = new Point_Focus();
					GameScreen.addEffectEnd(1, 0, toX, toY, Dir, objMainEff);
					setAva(1, objBeFireMain);
					int num = x;
					int num2 = y;
					x = toX;
					y = toY;
					toX = num;
					toY = num2;
					int xdich2 = toX - x;
					int ydich2 = toY - y;
					point_Focus3.Dir = Dir;
					point_Focus3.frame = 2;
					create_Speed(xdich2, ydich2, point_Focus3);
					VecEff.addElement(point_Focus3);
				}
				VecEff.removeElement(point_Focus2);
				i--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			if (!checkNullObject(1) && objFireMain.plashNow != null)
			{
				objFireMain.plashNow.setIsNextf(0);
			}
			removeEff();
		}
	}

	public void updateBuggy_2()
	{
		if (f == 18)
		{
			GameScreen.addEffectEnd(30, 0, x1000, y1000, 300, Dir, objMainEff);
		}
		if (f == 28)
		{
			addSound(15);
			addVir(2, 6, 10, isPlayer: false);
			Point_Focus p = new Point_Focus();
			int xdich = -260;
			if (Dir == 2)
			{
				xdich = 260;
			}
			p = create_Speed(xdich, 0, p);
			p.y = y1000;
			VecEff.addElement(p);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f <= 28)
		{
			return;
		}
		addSound(19);
		if (f % 2 != 0 || indexObjBefire >= vecObjsBeFire.size())
		{
			return;
		}
		Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
		indexObjBefire++;
		if (object_Effect_Skill != null)
		{
			MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
			if (mainObject != null)
			{
				setAva(1, mainObject);
				GameScreen.addEffectEnd(48, 1, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, objMainEff);
			}
		}
	}

	public void updateCabaji_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(1, 0, point_Focus.objMain.x + CRes.random_Am_0(5), point_Focus.objMain.y - point_Focus.objMain.hOne / 2 + CRes.random_Am_0(5), Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 2)
		{
			addSound(18);
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus p = new Point_Focus();
						int xdich = mainObject.x - x;
						int ydich = mainObject.y - mainObject.hOne / 2 - y;
						p = create_Speed(xdich, ydich, p);
						p.objMain = mainObject;
						p.frame = CRes.random(2);
						VecEff.addElement(p);
					}
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateDonKrieg_3()
	{
		if (f == 18)
		{
			addSound(15);
			int num = -45;
			if (Dir == 2)
			{
				num = 45;
			}
			GameScreen.addEffectEnd(57, 0, x + num, y + 12, Dir, objMainEff);
		}
		if (f > 18 && f < 28)
		{
			if (f == 20 || f == 26)
			{
				addSound(14);
			}
			if (f % 2 == 1)
			{
				int num2 = -40 - ((f - 18) / 2 + 1) * 30;
				if (Dir == 2)
				{
					num2 = 40 + ((f - 18) / 2 + 1) * 30;
				}
				GameScreen.addEffectEnd(58, 0, x + num2, y + 30, Dir, objMainEff);
				GameScreen.addEffectEnd(59, 0, x + num2, y + 30, Dir, objMainEff);
				addVir(2, 5, 10, isPlayer: false);
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateDonKrieg_2()
	{
		if (f == 2)
		{
			xArchor += x1000;
		}
		if (f == 10)
		{
			addSound(32);
			if (!checkNullObject(2))
			{
				addVir(3, 5, 10, isPlayer: false);
				Point_Focus p = new Point_Focus();
				int xdich = objBeFireMain.x - x;
				int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - y;
				p = create_Speed(xdich, ydich, p);
				GameScreen.addEffectEnd(12, 1, x, y, Dir, objMainEff);
				VecEff.addElement(p);
			}
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f < point_Focus.fRe)
			{
				continue;
			}
			x = point_Focus.x;
			y = point_Focus.y;
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						vMax = 8 + CRes.random(5);
						Point_Focus p2 = new Point_Focus();
						int xdich2 = mainObject.x - x;
						int ydich2 = mainObject.y - mainObject.hOne / 2 - y;
						p2 = create_Speed(xdich2, ydich2, p2);
						p2.objMain = mainObject;
						VecSubEff.addElement(p2);
					}
				}
			}
			GameScreen.addEffectEnd(57, 0, x, y, Dir, objMainEff);
			if (VecSubEff.size() < 8)
			{
				for (int k = 0; k < 8 - VecEff.size(); k++)
				{
					vMax = 8 + CRes.random(5);
					Point_Focus p3 = new Point_Focus();
					int xdich3 = CRes.random_Am_0(120);
					int ydich3 = CRes.random_Am_0(50);
					p3 = create_Speed(xdich3, ydich3, p3);
					VecSubEff.addElement(p3);
				}
			}
			VecEff.removeElement(point_Focus);
			i--;
		}
		for (int l = 0; l < VecSubEff.size(); l++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecSubEff.elementAt(l);
			point_Focus2.update_Vx_Vy();
			if (point_Focus2.f == point_Focus2.fRe && point_Focus2.objMain != null)
			{
				GameScreen.addEffectEnd(1, 0, point_Focus2.x + CRes.random_Am_0(5), point_Focus2.y + CRes.random_Am_0(5), Dir, objMainEff);
				setAva(0, point_Focus2.objMain);
			}
			if (point_Focus2.f > point_Focus2.fRe + 8)
			{
				VecSubEff.removeElement(point_Focus2);
				l--;
			}
		}
		if (f >= fRemove && VecSubEff.size() == 0 && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateDonKrieg_1()
	{
		if (f == 2)
		{
			x += x1000;
			x1000 = x;
			y1000 = y;
		}
		if (f == 10)
		{
			addSound(32);
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill == null)
				{
					continue;
				}
				MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
				if (mainObject == null)
				{
					continue;
				}
				for (int j = 0; j < 2; j++)
				{
					Point_Focus p = new Point_Focus();
					if (Dir == 0)
					{
						x += CRes.random(10);
					}
					else
					{
						x -= CRes.random(10);
					}
					y += CRes.random_Am_0(25);
					int num = mainObject.x - x;
					int num2 = mainObject.y - mainObject.hOne / 2 - y;
					p = create_Speed(num, num2, p);
					int frameAngle = CRes.angle(num, num2);
					p.frame = setFrameAngle(frameAngle);
					GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
					if (j == 0)
					{
						p.objMain = mainObject;
					}
					VecEff.addElement(p);
					x = x1000;
					y = y1000;
				}
			}
			if (VecEff.size() < 8)
			{
				for (int k = 0; k < 8 - VecEff.size(); k++)
				{
					Point_Focus p2 = new Point_Focus();
					if (Dir == 0)
					{
						x += CRes.random(10);
					}
					else
					{
						x -= CRes.random(10);
					}
					y += CRes.random_Am_0(25);
					int num3 = 120 + CRes.random_Am_0(30);
					int num4 = CRes.random_Am_0(50);
					if (Dir == 0)
					{
						num3 = -num3;
					}
					p2 = create_Speed(num3, num4, p2);
					int frameAngle2 = CRes.angle(num3, num4);
					p2.frame = setFrameAngle(frameAngle2);
					GameScreen.addEffectEnd(3, 0, x, y, Dir, objMainEff);
					VecEff.addElement(p2);
					x = x1000;
					y = y1000;
				}
			}
		}
		for (int l = 0; l < VecEff.size(); l++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(l);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f == point_Focus.fRe && point_Focus.objMain != null)
			{
				GameScreen.addEffectEnd(1, 0, point_Focus.x + CRes.random_Am_0(5), point_Focus.y + CRes.random_Am_0(5), Dir, objMainEff);
				setAva(0, point_Focus.objMain);
			}
			if (point_Focus.f > point_Focus.fRe + 10)
			{
				VecEff.removeElement(point_Focus);
				l--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateKuro_2()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(1))
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
		if (f == 10 || f == 24)
		{
			addSound(10);
		}
		if (f == 32 || f == 18)
		{
			addSound(7);
		}
		if (f <= 8)
		{
			if (f == 0 || f == 4)
			{
				objFireMain.x = x + 3;
			}
			if (f == 2 || f == 8)
			{
				objFireMain.x = x - 3;
			}
		}
		else if (f < fRemove)
		{
			if (f == 10)
			{
				createSkillKuro(0, toX + CRes.random_Am_0(30), toY - 10 + CRes.random_Am_0(30), CRes.random(2, 5));
				setAva(0, objBeFireMain);
			}
			else if (f % 4 == 0)
			{
				createSkillKuro(CRes.random(4), toX + CRes.random_Am_0(30), toY - 10 + CRes.random_Am_0(30), CRes.random(2, 5));
				setAva(0, objBeFireMain);
			}
			if (objFireMain.isTanHinh)
			{
				if (CRes.random(5) == 0)
				{
					objFireMain.isTanHinh = false;
				}
			}
			else if (CRes.random(3) == 0)
			{
				objFireMain.isTanHinh = true;
				objFireMain.x = toX + CRes.random_Am_0(30);
				objFireMain.y = toY + CRes.random_Am_0(30);
			}
			if (CRes.random(5) == 0)
			{
				Point point = new Point();
				point.x = toX + CRes.random_Am_0(30);
				point.y = toY + CRes.random_Am_0(30);
				point.frame = 4;
				point.fRe = 3;
				point.dis = ((CRes.random(2) != 0) ? 2 : 0);
				VecEff.addElement(point);
				addVir(3, 5, 10, isPlayer: false);
			}
		}
		if (f == fRemove - 2)
		{
			Point point2 = new Point();
			point2.x = x1000;
			point2.y = y1000;
			point2.frame = 4;
			point2.fRe = 2;
			point2.dis = Dir;
			VecEff.addElement(point2);
		}
		if (f == fRemove)
		{
			objFireMain.isTanHinh = false;
			objFireMain.x = x1000;
			objFireMain.y = y1000;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point3 = (Point)VecEff.elementAt(i);
			point3.update();
			if (point3.f >= point3.fRe)
			{
				VecEff.removeElement(point3);
				i--;
			}
		}
	}

	public void createSkillKuro(int type, int x, int y, int size)
	{
		switch (type)
		{
		case 0:
		{
			for (int l = 0; l < size; l++)
			{
				Point point4 = new Point();
				point4.y = y;
				if (Dir == 2)
				{
					point4.x = x + 7 * l;
				}
				else
				{
					point4.x = x - 7 * l;
				}
				point4.vy = -7;
				point4.frame = 2;
				point4.fRe = 5;
				point4.dis = ((CRes.random(2) != 0) ? 2 : 0);
				VecEff.addElement(point4);
			}
			break;
		}
		case 1:
		{
			for (int j = 0; j < size; j++)
			{
				Point point2 = new Point();
				point2.y = y + j * 7;
				point2.x = x;
				point2.vx = -5;
				point2.frame = 3;
				point2.fRe = 5;
				point2.dis = ((CRes.random(2) != 0) ? 2 : 0);
				VecEff.addElement(point2);
			}
			break;
		}
		case 2:
		{
			for (int k = 0; k < size; k++)
			{
				Point point3 = new Point();
				point3.y = y + k * 7;
				point3.x = x;
				point3.vx = -3;
				if (Dir == 0)
				{
					point3.vx = 3;
				}
				point3.frame = 0;
				point3.fRe = 4;
				point3.dis = ((CRes.random(2) != 0) ? 2 : 0);
				VecEff.addElement(point3);
			}
			break;
		}
		case 3:
		{
			for (int i = 0; i < size; i++)
			{
				Point point = new Point();
				point.y = y + i * 7;
				point.x = x;
				point.vx = -3;
				if (Dir == 0)
				{
					point.vx = 3;
				}
				point.frame = 1;
				point.fRe = 4;
				point.dis = ((CRes.random(2) != 0) ? 2 : 0);
				VecEff.addElement(point);
			}
			break;
		}
		}
	}

	public void createSkillZoro2(int type, int x, int y, int size)
	{
		switch (type)
		{
		case 0:
		{
			for (int j = 0; j < size; j++)
			{
				Point point2 = new Point();
				point2.y = y;
				if (Dir == 2)
				{
					point2.x = x + 15 * j;
				}
				else
				{
					point2.x = x - 15 * j;
				}
				point2.vy = -7;
				point2.frame = 2;
				point2.fRe = 4;
				point2.dis = ((CRes.random(2) != 0) ? 2 : 0);
				VecEff.addElement(point2);
			}
			break;
		}
		case 1:
		{
			for (int i = 0; i < size; i++)
			{
				Point point = new Point();
				point.y = y + i * 15;
				point.x = x;
				point.vx = -5;
				point.frame = 3;
				point.fRe = 4;
				point.dis = ((CRes.random(2) != 0) ? 2 : 0);
				VecEff.addElement(point);
			}
			break;
		}
		}
	}

	public void updateKuro_1()
	{
		if (f == 2)
		{
			int num = 14;
			if (Dir == 2)
			{
				num = -14;
			}
			x = toX + num;
			y = toY - objFireMain.hOne / 2;
			objFireMain.x = x;
			objFireMain.y = toY;
		}
		if (f == 4)
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 5)
		{
			int num2 = -14;
			if (Dir == 2)
			{
				num2 = 14;
			}
			x += num2;
			for (int i = 0; i < 3; i++)
			{
				Point point = new Point();
				point.y = y;
				if (Dir == 2)
				{
					point.x = x + 7 * i;
				}
				else
				{
					point.x = x - 7 * i;
				}
				point.vy = -10;
				point.frame = 2;
				point.fRe = 5;
				VecEff.addElement(point);
			}
		}
		if (f > 5)
		{
			if (f < 11)
			{
				objFireMain.dy = 10 * (f - 6);
				objBeFireMain.dy = 12 * (f - 6);
			}
			else if (f < 15)
			{
				objFireMain.dy = 50;
				objBeFireMain.dy = 60;
				objFireMain.vx = -5;
				if (Dir == 0)
				{
					objFireMain.vx = 5;
				}
			}
		}
		if (f == 8)
		{
			addSound(7);
			setAva(0, objBeFireMain);
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point point2 = (Point)VecEff.elementAt(j);
			point2.update();
			if (point2.f >= point2.fRe)
			{
				VecEff.removeElement(point2);
				j--;
			}
		}
		if (f == 13)
		{
			addSound(7);
			setAva(1, objBeFireMain);
			for (int k = 0; k < 3; k++)
			{
				Point point3 = new Point();
				point3.y = y - objFireMain.dy + k * 7;
				point3.x = x;
				point3.vx = -3;
				if (Dir == 0)
				{
					point3.vx = 3;
				}
				point3.frame = 0;
				point3.fRe = 4;
				VecEff.addElement(point3);
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			addVir(3, 5, 10, isPlayer: false);
			objFireMain.dy = 0;
			removeEff();
		}
	}

	public void updateNyaban_2()
	{
		if (f > fRemove || checkNullObject(1))
		{
			if (!checkNullObject(1))
			{
				objFireMain.toX = x;
				objFireMain.isTanHinh = false;
				objFireMain.dy = 0;
			}
			setAva(0, objBeFireMain);
			removeEff();
			return;
		}
		if (f == 1)
		{
			addSound(3);
		}
		if (f < 5)
		{
			objFireMain.dy = 40 * f;
			objFireMain.vx = vx;
		}
		else if (f < 8)
		{
			objFireMain.isTanHinh = true;
		}
		if (f == 5)
		{
			objFireMain.x = toX;
			objFireMain.vx = 0;
			objFireMain.dy = 100;
		}
		if (f == 6)
		{
			addSound(14);
			addVir(2, 5, 10, isPlayer: false);
			objFireMain.dy = 0;
			setAva(1, objBeFireMain);
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(5), objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(5), Dir, objMainEff);
			GameScreen.addEffectEnd(90, 0, toX, objBeFireMain.y + 10, Dir, objMainEff);
		}
	}

	public void updateNyaban_3()
	{
		if (f > fRemove || checkNullObject(1))
		{
			if (!checkNullObject(1))
			{
				objFireMain.toX = x;
				objFireMain.dy = 0;
				objFireMain.vx = 0;
			}
			removeEff();
		}
		if (f < 3)
		{
			objFireMain.dy = 10 * f;
			objFireMain.vx = vx;
		}
		else if (f < 6)
		{
			objFireMain.dy = 10 * (6 - f);
			objFireMain.vx = vx;
		}
		if (f == 6)
		{
			objFireMain.dy = 0;
			objFireMain.vx = 0;
		}
		if (f == 17)
		{
			objFireMain.Dir = ((objFireMain.Dir == 0) ? 2 : 0);
			vx = 20;
			if (objFireMain.Dir == 0)
			{
				vx = -20;
			}
			setAva(0, objBeFireMain);
		}
		if (f == 8 || f == 13)
		{
			addSound(7);
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(5), objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(5), Dir, objMainEff);
		}
		if (f > 17)
		{
			if (f < 22)
			{
				objFireMain.dy = 5 * (f - 17);
				objFireMain.vx = vx;
			}
			else if (f < 26)
			{
				objFireMain.dy = 5 * (25 - f);
				objFireMain.vx = vx;
			}
		}
	}

	public void updateJango_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			Point point = new Point(point_Focus.x, point_Focus.y);
			point.frame = CRes.random(fraImgSubEff.nFrame);
			VecSubEff.addElement(point);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(1, 0, point_Focus.objMain.x + CRes.random_Am_0(5), point_Focus.objMain.y - point_Focus.objMain.hOne / 2 + CRes.random_Am_0(5), Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point point2 = (Point)VecSubEff.elementAt(j);
			point2.f++;
			if (point2.f >= 2)
			{
				VecSubEff.removeElement(point2);
				j--;
			}
		}
		if (f == 2)
		{
			addSound(18);
			for (int k = 0; k < vecObjsBeFire.size(); k++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus p = new Point_Focus();
						int xdich = mainObject.x - x;
						int ydich = mainObject.y - mainObject.hOne / 2 - y;
						p = create_Speed(xdich, ydich, p);
						p.objMain = mainObject;
						VecEff.addElement(p);
					}
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateCabaji_2()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			if (!checkNullObject(1))
			{
				objFireMain.toX = x;
			}
			removeEff();
			return;
		}
		if (f == 1)
		{
			addSound(3);
		}
		if (f < 5)
		{
			objFireMain.dy = 70 * f;
		}
		else if (f >= 5 && f <= 10)
		{
			objFireMain.dy = 330;
		}
		else if (f <= 13)
		{
			objFireMain.dy = (13 - f) * 110;
		}
		if (f == 10)
		{
			objFireMain.x = toX;
			objFireMain.y = toY;
		}
		if (f == 13)
		{
			addSound(15);
			addVir(3, 5, 10, isPlayer: false);
			objFireMain.dy = 0;
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(5), objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(5), Dir, objMainEff);
			}
			GameScreen.addEffectEnd(9, 0, toX, toY, Dir, objMainEff);
			setAva(2, objBeFireMain);
			if (typeEffect == 22)
			{
				GameScreen.addEffectEnd(45, 0, toX, toY + 20, Dir, objMainEff);
			}
		}
	}

	public void updateArlong_3()
	{
		if (f == 12)
		{
			addSound(15);
			if (vecObjsBeFire.size() > 1)
			{
				for (int i = 0; i < vecObjsBeFire.size(); i++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							setAva(1, mainObject);
						}
					}
				}
			}
			else
			{
				setAva(1, objBeFireMain);
			}
			GameScreen.addEffectEnd(8, 0, toX, toY, Dir, objMainEff);
			Point point = new Point(x + plusxy[4][0], y + 30);
			point.vx = -10;
			if (Dir == 2)
			{
				point.vx = 10;
			}
			point.fRe = 12;
			VecEff.addElement(point);
		}
		if (f == 18 || f == 22)
		{
			addSound(14);
		}
		if (f == 13)
		{
			addVir(1, 6, 12, isPlayer: false);
			int num = -10;
			if (Dir == 2)
			{
				num = 10;
			}
			GameScreen.addEffectEnd_ToX_ToY(62, 0, x + plusxy[4][0], y + 30, x + plusxy[4][0] + num * 12, y + 30, Dir, objMainEff);
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point point2 = (Point)VecEff.elementAt(j);
			point2.update();
			if (point2.f < point2.fRe - 2)
			{
				point2.frame = CRes.random(2);
			}
			else
			{
				point2.frame = 2;
			}
			if (f % 3 == 0)
			{
				GameScreen.addEffectEnd(59, 0, point2.x, point2.y, Dir, objMainEff);
			}
			if (point2.f >= point2.fRe)
			{
				VecEff.removeElement(point2);
				j--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateArlong_2()
	{
		if ((f >= fRemove && VecEff.size() == 0) || checkNullObject(3))
		{
			if (!checkNullObject(1) && objFireMain.plashNow != null)
			{
				objFireMain.plashNow.setIsNextf(0);
			}
			removeEff();
		}
		if (f == 2 || f == 12 || f == 22)
		{
			addSound(19);
		}
		if (f == 2)
		{
			addVir(3, 5, 10, isPlayer: false);
			objFireMain.isTanHinh = true;
			if (objFireMain.plashNow != null)
			{
				objFireMain.plashNow.setIsNextf(1);
			}
			Point_Focus point_Focus = new Point_Focus();
			int xdich = objBeFireMain.x - x;
			int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - y;
			point_Focus.objMain = objBeFireMain;
			point_Focus = create_Speed(xdich, ydich, point_Focus);
			point_Focus.frame = 0;
			point_Focus.dis = Dir;
			VecEff.addElement(point_Focus);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecEff.elementAt(i);
			point_Focus2.update_Vx_Vy();
			if (point_Focus2.f >= point_Focus2.fRe)
			{
				if (point_Focus2.frame == 2)
				{
					objFireMain.isTanHinh = false;
					if (objFireMain.plashNow != null)
					{
						objFireMain.plashNow.setIsNextf(0);
					}
					VecEff.removeElement(point_Focus2);
					i--;
				}
				else if (point_Focus2.f == point_Focus2.fRe)
				{
					GameScreen.addEffectEnd(8, 0, toX, toY, Dir, objMainEff);
					setAva(1, objBeFireMain);
				}
			}
			if (point_Focus2.frame == 0 && point_Focus2.f >= 8)
			{
				Point_Focus point_Focus3 = new Point_Focus();
				int xdich2 = objBeFireMain.x - point_Focus2.x;
				int ydich2 = objBeFireMain.y - objBeFireMain.hOne / 2 - point_Focus2.y;
				point_Focus3.objMain = objBeFireMain;
				point_Focus3 = create_Speed(xdich2, ydich2, point_Focus3, point_Focus2.x, point_Focus2.y, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2);
				point_Focus3.frame = 1;
				point_Focus3.dis = ((Dir == 0) ? 2 : 0);
				VecEff.addElement(point_Focus3);
				VecEff.removeElement(point_Focus2);
				i--;
			}
			else if (point_Focus2.f >= 22 && point_Focus2.frame == 1)
			{
				vMax = 20;
				Point_Focus p = new Point_Focus();
				int xdich3 = x - point_Focus2.x;
				int ydich3 = y - point_Focus2.y;
				p = create_Speed(xdich3, ydich3, p, point_Focus2.x, point_Focus2.y, x, y);
				p.frame = 2;
				p.dis = Dir;
				VecEff.addElement(p);
				VecEff.removeElement(point_Focus2);
				i--;
			}
		}
	}

	public void updateArlong_1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			((Point)VecEff.elementAt(i)).f++;
		}
		if (f == 6)
		{
			addSound(33);
			addVir(3, 5, 10, isPlayer: false);
			if (vecObjsBeFire.size() > 1)
			{
				for (int j = 0; j < vecObjsBeFire.size(); j++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
					if (object_Effect_Skill != null)
					{
						MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
						if (mainObject != null)
						{
							GameScreen.addEffectEnd(8, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, objMainEff);
							setAva(1, mainObject);
						}
					}
				}
			}
			else
			{
				GameScreen.addEffectEnd(8, 0, toX, toY, Dir, objMainEff);
				setAva(1, objBeFireMain);
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateKurobi_2()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			removeEff();
			return;
		}
		if (f >= 10 && f <= 16)
		{
			if (f < 13)
			{
				objFireMain.dy = 10 * (f - 10);
				objFireMain.vx = vx;
			}
			else if (f < 16)
			{
				objFireMain.dy = 10 * (16 - f);
				objFireMain.vx = vx;
			}
			if (f == 16)
			{
				if (!checkNullObject(2))
				{
					GameScreen.addEffectEnd(25, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				}
				objFireMain.dy = 0;
				objFireMain.vx = 0;
				setAva(1, objBeFireMain);
			}
		}
		if (f == 18)
		{
			GameScreen.addEffectEnd(30, 0, x, y + 10, 200, Dir, objMainEff);
		}
		if (f == 26)
		{
			addSound(5);
			if (!checkNullObject(2))
			{
				addVir(2, 5, 10, isPlayer: false);
				GameScreen.addEffectEnd(25, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 3 * 2 + 5, Dir, objMainEff);
			}
			objFireMain.dy = 0;
			objFireMain.vx = 0;
			setAva(1, objBeFireMain);
		}
	}

	public void updateKurobi_1()
	{
		if (f == 12 || f == 27)
		{
			addSound(13);
			if (!checkNullObject(2))
			{
				addVir(2, 5, 10, isPlayer: false);
				GameScreen.addEffectEnd(25, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				setAva(0, objBeFireMain);
			}
		}
		if (f == 15)
		{
			GameScreen.addEffectEnd(30, 0, x, y, 300, Dir, objMainEff);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateChu_2()
	{
		if (f >= 10 && f < fRemove && f % 4 == 0)
		{
			if (f % 8 == 0 && indexObjBefire < vecObjsBeFire.size())
			{
				addSound(21);
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
				indexObjBefire++;
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						Point_Focus point_Focus = new Point_Focus();
						int xdich = mainObject.x - x;
						int ydich = mainObject.y - mainObject.hOne / 2 - y;
						point_Focus.objMain = mainObject;
						point_Focus = create_Speed(xdich, ydich, point_Focus);
						VecEff.addElement(point_Focus);
					}
				}
				addVir(3, 5, 10, isPlayer: false);
			}
			else
			{
				for (int i = 0; i < 2; i++)
				{
					Point_Focus p = new Point_Focus();
					int num = 120 + CRes.random_Am_0(30);
					int ydich2 = CRes.random_Am_0(50);
					if (Dir == 0)
					{
						num = -num;
					}
					p = create_Speed(num, ydich2, p);
					VecEff.addElement(p);
				}
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecEff.elementAt(j);
			point_Focus2.update_Vx_Vy();
			if (point_Focus2.f >= point_Focus2.fRe)
			{
				GameScreen.addEffectEnd(61, 0, point_Focus2.x, point_Focus2.y, Dir, objMainEff);
				if (point_Focus2.objMain != null)
				{
					setAva(0, point_Focus2.objMain);
				}
				VecEff.removeElement(point_Focus2);
				j--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateChu_1()
	{
		if (f == 10 || f == 14 || f == 18)
		{
			addSound(21);
			if (!checkNullObject(2))
			{
				Point_Focus p = new Point_Focus();
				int xdich = objBeFireMain.x - x;
				int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - y;
				p = create_Speed(xdich, ydich, p);
				VecEff.addElement(p);
			}
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(61, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				if (CRes.random(3) == 0)
				{
					setAva(0, objBeFireMain);
				}
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateHachi_2()
	{
		if (f == fRemove - 4 && !checkNullObject(2))
		{
			Point_Focus p = new Point_Focus();
			int xdich = objBeFireMain.x - x;
			int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - y;
			p = create_Speed(xdich, ydich, p);
			VecEff.addElement(p);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			sbyte b = 0;
			if (typeEffect == 150)
			{
				b = 1;
			}
			else if (typeEffect != 113)
			{
				b = 2;
			}
			if (point_Focus.f >= point_Focus.fRe)
			{
				if (b < 2)
				{
					GameScreen.addEffectEnd(60, b, point_Focus.x, point_Focus.y, Dir, objMainEff);
				}
				else
				{
					GameScreen.addEffectEnd(34, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
					GameScreen.addEffectEnd(87, frame, point_Focus.x, point_Focus.y, Dir, objMainEff);
				}
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateHachi_1()
	{
		if (f == 1)
		{
			addSound(4);
		}
		if (f < 10 && f % 3 == 0)
		{
			setAva(0, objBeFireMain);
			GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(15), toY + CRes.random_Am_0(15), Dir, objMainEff);
			addVir(3, 5, 10, isPlayer: false);
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateGhin_2()
	{
		if (f == 1 || f == 8)
		{
			addSound(10);
		}
		if (f == 4)
		{
			for (int i = 0; i < VecEff.size(); i++)
			{
				((Point)VecEff.elementAt(i)).dis = 2;
			}
		}
		if (f == 12)
		{
			for (int j = 0; j < VecEff.size(); j++)
			{
				Point point = (Point)VecEff.elementAt(j);
				if (!checkNullObject(1))
				{
					if (Dir == 0)
					{
						point.x = objFireMain.x + 20;
					}
					else
					{
						point.x = objFireMain.x - 20;
					}
				}
				else
				{
					point.x = x;
				}
				point.y = objFireMain.y - 28 + 4 * j;
				point.vx = vx;
			}
		}
		for (int k = 0; k < VecEff.size(); k++)
		{
			Point point2 = (Point)VecEff.elementAt(k);
			point2.update();
			if (!checkNullObject(2) && k == 0 && f % 4 == 0 && CRes.abs(point2.x - objBeFireMain.x) < 30)
			{
				GameScreen.addEffectEnd(1, 0, objBeFireMain.x + CRes.random_Am_0(5), objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(5), Dir, objMainEff);
				setAva(0, objBeFireMain);
			}
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isPaintWeapon = true;
				objFireMain.vx = 0;
			}
			removeEff();
		}
		else if (f >= 12 && !checkNullObject(1))
		{
			objFireMain.vx = vx;
		}
	}

	public void updatePearl_2()
	{
		if (f > 10 && f < fRemove && f % 4 == 0)
		{
			addSound(19);
			if (indexObjBefire < vecObjsBeFire.size())
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
				indexObjBefire++;
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					int xdich = mainObject.x - x;
					int ydich = mainObject.y - mainObject.hOne / 2 - y;
					Point_Focus p = new Point_Focus();
					p = create_Speed(xdich, ydich, p);
					p.frame = CRes.random(3);
					p.objMain = mainObject;
					VecSubEff.addElement(p);
				}
			}
			else if (!checkNullObject(2))
			{
				int xdich2 = objBeFireMain.x + CRes.random_Am_0(30) - x;
				int ydich2 = objBeFireMain.y + CRes.random_Am_0(30) - objBeFireMain.hOne / 2 - y;
				Point_Focus p2 = new Point_Focus();
				p2 = create_Speed(xdich2, ydich2, p2);
				p2.frame = CRes.random(3);
				VecSubEff.addElement(p2);
			}
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecSubEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				if (point_Focus.objMain != null)
				{
					GameScreen.addEffectEnd_ObjTo(55, 0, point_Focus.objMain.x, point_Focus.objMain.y - point_Focus.objMain.hOne / 2, point_Focus.objMain.ID, point_Focus.objMain.typeObject, Dir, objMainEff);
				}
				else
				{
					GameScreen.addEffectEnd(55, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				}
				VecSubEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f >= fRemove && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	public void updateUrgot3()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			if ((point.vy > 0 && point.y >= 0) || (point.vy < 0 && point.y <= -30))
			{
				point.vy = -point.vy;
			}
			point.y += point.vy;
		}
		if (f == 30 && !checkNullObject(1))
		{
			objFireMain.x = toX;
			objFireMain.y = toY;
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updatexerath3()
	{
		vy1000 += xplus;
		x1000 += vx1000;
		y1000 += vy1000;
		if (f == fRemove)
		{
			x1000 = toX * 1000;
			y1000 = toY;
			GameScreen.addEffectEnd(68, 0, toX, toY + 10, Dir, objMainEff);
		}
		if (f >= fRemove)
		{
			if (VecEff.size() == 0)
			{
				removeEff();
			}
		}
		else
		{
			for (int i = 0; i < 1; i++)
			{
				Point point = new Point();
				point.x = x1000 / 1000;
				point.y = y1000;
				if (CRes.random(3) == 0)
				{
					point.fraImgEff = fraImgSubEff;
				}
				else
				{
					point.fraImgEff = fraImgSub2Eff;
				}
				VecEff.addElement(point);
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point point2 = (Point)VecEff.elementAt(j);
			point2.f++;
			if (point2.f >= 8)
			{
				VecEff.removeElement(point2);
				j--;
			}
		}
	}

	public void updateXerath2()
	{
		if (f > 10)
		{
			for (int i = 0; i < vecPos.size(); i++)
			{
				((Point_Focus)vecPos.elementAt(i)).f++;
			}
		}
		if (GameCanvas.timeNow - timeBegin >= timeEnd)
		{
			removeEff();
		}
	}

	public void updateXerath1()
	{
		if (f == 5 && !checkNullObject(1))
		{
			GameScreen.addEffectEnd(30, 2, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, (short)(timeEnd - (GameCanvas.timeNow - timeBegin) - 200), Dir, objMainEff);
		}
		if (GameCanvas.timeNow - timeBegin >= timeEnd)
		{
			removeEff();
		}
	}

	public void updateNoTheoHuong_1()
	{
		if (f == 5 && !checkNullObject(1))
		{
			GameScreen.addEffectEnd(30, 2, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, (short)(timeEnd - (GameCanvas.timeNow - timeBegin) - 200), Dir, objMainEff);
			x = objFireMain.x;
			y = objFireMain.y;
		}
		if (f == 20 || f == 40)
		{
			for (int i = 0; i < 4; i++)
			{
				Point point = new Point();
				point.x = x + am_duong * 20;
				point.y = y - 30 + i * 20;
				point.vx = am_duong * 40;
				VecEff.addElement(point);
			}
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point point2 = (Point)VecSubEff.elementAt(j);
			point2.f++;
			if (point2.f > 6 && point2.f % 2 == 0)
			{
				point2.frame++;
			}
			if (point2.frame > 2)
			{
				VecSubEff.removeElement(point2);
				j--;
			}
		}
		for (int k = 0; k < VecEff.size(); k++)
		{
			Point point3 = (Point)VecEff.elementAt(k);
			point3.f++;
			if (point3.f % 3 == 1)
			{
				Point point4 = new Point();
				point4.x = point3.x;
				point4.y = point3.y;
				VecSubEff.addElement(point4);
				point3.x += point3.vx;
			}
			if (point3.f > 13)
			{
				VecEff.removeElement(point3);
			}
		}
		if (GameCanvas.timeNow - timeBegin >= timeEnd)
		{
			removeEff();
		}
	}

	public void updateNoTheoHuong_2()
	{
		if (f < 30 && f % 6 == 3)
		{
			addVir(3, 5, 10, isPlayer: false);
			for (int i = 0; i < 4; i++)
			{
				GameScreen.addEffectEnd(52, 0, x + am_duong * 20 + am_duong * (f / 6) * 40, y - 30 + i * 20, Dir, objMainEff);
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updateNoNangLuong3()
	{
		for (int i = 0; i < vecPos.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)vecPos.elementAt(i);
			if (point_Focus.f == 0)
			{
				addVir(2, 6, 10, isPlayer: false);
				GameScreen.addEffectEnd(63, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				GameScreen.addEffectEnd(59, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
			}
			point_Focus.f++;
			if (point_Focus.f >= 8)
			{
				vecPos.removeElement(point_Focus);
				i--;
			}
		}
		if (f >= fRemove && vecPos.size() == 0)
		{
			removeEff();
		}
	}

	public void updateNoNangLuong2()
	{
		if (f > 25)
		{
			for (int i = 0; i < vecPos.size(); i++)
			{
				((Point_Focus)vecPos.elementAt(i)).f++;
			}
		}
		if (GameCanvas.timeNow - timeBegin >= timeEnd)
		{
			removeEff();
		}
	}

	public void updateNoNangLuong1()
	{
		if (f == 5)
		{
			GameScreen.addEffectEnd(30, 2, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, (short)(timeEnd - (GameCanvas.timeNow - timeBegin) - 200), Dir, objMainEff);
		}
		if (CRes.random(6) == 0)
		{
			for (int i = indexObjBefire; i < GameScreen.vecPlayers.size(); i++)
			{
				MainObject mainObject = (MainObject)GameScreen.vecPlayers.elementAt(i);
				if (indexObjBefire == GameScreen.vecPlayers.size() - 1)
				{
					indexObjBefire = 0;
				}
				if (mainObject != objFireMain && MainObject.getDistance(objFireMain.x, objFireMain.y, mainObject.x, mainObject.y) <= 220)
				{
					indexObjBefire = i + 1;
					if (indexObjBefire >= GameScreen.vecPlayers.size())
					{
						indexObjBefire = 0;
					}
					GameScreen.addEffectEnd_ObjTo(22, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, objFireMain.ID, objFireMain.typeObject, (sbyte)objFireMain.Dir, objMainEff);
					break;
				}
			}
		}
		if (GameCanvas.timeNow - timeBegin >= timeEnd || checkNullObject(1))
		{
			removeEff();
		}
	}

	public void updateGalio2()
	{
		if (f == 2)
		{
			int num = 0;
			for (int i = 0; i < 8; i++)
			{
				num %= 360;
				Point point = new Point(x + CRes.getcos(num) * 43 / 1000, y + CRes.getsin(num) * 23 / 1000);
				VecEff.addElement(point);
				GameScreen.addEffectEnd(66, 0, point.x, point.y, Dir, objMainEff);
				num += 45;
			}
		}
		if (f == 8)
		{
			int num2 = 22;
			for (int j = 0; j < 12; j++)
			{
				num2 %= 360;
				Point point2 = new Point(x + CRes.getcos(num2) * 65 / 1000, y + CRes.getsin(num2) * 40 / 1000);
				VecEff.addElement(point2);
				GameScreen.addEffectEnd(66, 0, point2.x, point2.y, Dir, objMainEff);
				num2 += 30;
			}
		}
		if (f == 14)
		{
			int num3 = 45;
			addVir(2, 6, 12, isPlayer: false);
			for (int k = 0; k < 16; k++)
			{
				num3 %= 360;
				Point point3 = new Point(x + CRes.getcos(num3) * 100 / 1000, y + CRes.getsin(num3) * 65 / 1000);
				VecEff.addElement(point3);
				GameScreen.addEffectEnd(66, 0, point3.x, point3.y, Dir, objMainEff);
				num3 += 22;
			}
		}
		for (int l = 0; l < VecEff.size(); l++)
		{
			Point point4 = (Point)VecEff.elementAt(l);
			point4.f++;
			if (point4.f >= 8)
			{
				VecEff.removeElement(point4);
				l--;
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	public void updatePan2()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			removeEff();
			return;
		}
		if (f < 6)
		{
			objFireMain.dy = f * 40;
		}
		if (f >= 6 && f <= 12)
		{
			objFireMain.dy = 480;
			objFireMain.isTanHinh = true;
		}
		if (f == 13)
		{
			objFireMain.isTanHinh = false;
			objFireMain.x = toX;
			objFireMain.y = toY;
		}
		if (f > 13 && f < 18)
		{
			objFireMain.dy = (17 - f) * 120;
		}
		if (f >= 18)
		{
			objFireMain.dy = 0;
		}
		if (f == 18)
		{
			addVir(2, 6, 10, isPlayer: false);
			GameScreen.addEffectEnd(65, 0, objFireMain.x, objFireMain.y + 22, Dir, objMainEff);
			GameScreen.addEffectEnd(59, 0, objFireMain.x + CRes.random_Am_0(10), objFireMain.y, Dir, objMainEff);
			GameScreen.addEffectEnd(59, 0, objFireMain.x + CRes.random_Am_0(10), objFireMain.y, Dir, objMainEff);
		}
	}

	public void update_Pan1()
	{
		if (f == 15)
		{
			Point o = new Point(toX, toY);
			VecEff.addElement(o);
		}
		if (GameCanvas.timeNow - timeBegin >= timeEnd)
		{
			removeEff();
		}
		if (f == 17)
		{
			Point point = new Point(toX, toY);
			point.frame = 2;
			VecEff.addElement(point);
		}
		if (f != 19 && f != 21 && f <= 25)
		{
			return;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point2 = (Point)VecEff.elementAt(i);
			if (f >= 25)
			{
				point2.f++;
			}
			else if (point2.frame == 2 || point2.frame == 4)
			{
				point2.frame += 2;
			}
		}
	}

	public void update_Zoro_S3_L3()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			if (isAddSound)
			{
				mSound.playSound(9, mSound.volumeSound);
			}
			addVir(10, 5, 10, isPlayer: true);
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point point = (Point)VecEff.elementAt(i);
				int subtype = 0;
				if (typeEffect == 185)
				{
					subtype = 1;
				}
				GameScreen.addEffectEnd(64, subtype, point.x, point.y, Dir, objMainEff);
				setAva(1, point.obj);
			}
			removeEff();
			return;
		}
		x1000 += vx;
		if (f == 6)
		{
			vx = 8;
			if (Dir == 0)
			{
				vx = -8;
			}
			objFireMain.vx = vx;
		}
		if (f == 10 && typeEffect == 217 && !GameCanvas.lowGraphic)
		{
			GameScreen.addEffectEnd(137, 0, objFireMain.x, objFireMain.y + 10, Dir, objMainEff);
		}
		if (f == 12)
		{
			objFireMain.isTanHinh = true;
		}
		if (f == 14)
		{
			vx = 0;
			objFireMain.vx = vx;
			objFireMain.isTanHinh = true;
		}
		if (f == 20)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(108, 1, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, objMainEff);
						GameScreen.addEffectEnd_ObjTo(24, 0, mainObject.x, mainObject.y, mainObject.ID, mainObject.typeObject, 0, null);
					}
				}
			}
		}
		if (f >= 16 && f % 3 == 0 && indexObjBefire < vecObjsBeFire.size())
		{
			Object_Effect_Skill object_Effect_Skill2 = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
			indexObjBefire++;
			if (object_Effect_Skill2 != null)
			{
				MainObject mainObject2 = MainObject.get_Object(object_Effect_Skill2.ID, object_Effect_Skill2.tem);
				if (mainObject2 != null)
				{
					Point point2 = new Point(mainObject2.x, mainObject2.y - mainObject2.hOne / 2);
					point2.obj = mainObject2;
					VecEff.addElement(point2);
				}
			}
		}
		for (int k = 0; k < VecEff.size(); k++)
		{
			Point point3 = (Point)VecEff.elementAt(k);
			point3.f++;
			point3.x = point3.obj.x;
			point3.y = point3.obj.y - point3.obj.hOne / 2;
		}
		if (f == 24)
		{
			changeDir();
			objFireMain.Dir = Dir;
			objFireMain.x = xArchor;
			objFireMain.y = yArchor;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			x1000 = x - 15;
			y1000 = objFireMain.y - 22;
			int num = -15;
			if (Dir == 2)
			{
				num = 15;
				x1000 = x - 63;
			}
			x += num;
			y -= 5;
		}
		if (f == 26)
		{
			objFireMain.isTanHinh = false;
		}
	}

	public void update_Zoro_S3_L6()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			if (isAddSound)
			{
				mSound.playSound(9, mSound.volumeSound);
			}
			addVir(10, 5, 10, isPlayer: true);
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
				int subtype = 2;
				GameScreen.addEffectEnd(64, subtype, point_Focus.x, point_Focus.y - point_Focus.objMain.hOne / 2, Dir, objMainEff);
				setAva(1, point_Focus.objMain);
			}
			removeEff();
			return;
		}
		x1000 += vx;
		if (f == 6)
		{
			vx = 8;
			if (Dir == 0)
			{
				vx = -8;
			}
			objFireMain.vx = vx;
		}
		if (f == 10 && !GameCanvas.lowGraphic)
		{
			GameScreen.addEffectEnd(137, 1, objFireMain.x, objFireMain.y + 10, Dir, objMainEff);
		}
		if (f == 12)
		{
			objFireMain.isTanHinh = true;
		}
		if (f == 14)
		{
			vx = 0;
			objFireMain.vx = vx;
			objFireMain.isTanHinh = true;
		}
		if (f == 20)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(108, 1, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, objMainEff);
					}
				}
			}
		}
		if (f >= 10 && f % 3 == 0 && indexObjBefire < vecObjsBeFire.size())
		{
			Object_Effect_Skill object_Effect_Skill2 = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
			indexObjBefire++;
			if (object_Effect_Skill2 != null)
			{
				MainObject mainObject2 = MainObject.get_Object(object_Effect_Skill2.ID, object_Effect_Skill2.tem);
				if (mainObject2 != null)
				{
					Point_Focus p = new Point_Focus();
					int num = y;
					if (!checkNullObject(1))
					{
						num = objFireMain.y;
					}
					int xdich = mainObject2.x - x;
					int ydich = mainObject2.y - num;
					p = create_Speed(xdich, ydich, p, x, num, mainObject2.x, mainObject2.y);
					p.objMain = mainObject2;
					VecEff.addElement(p);
				}
			}
		}
		for (int k = 0; k < VecEff.size(); k++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecEff.elementAt(k);
			point_Focus2.update_Vx_Vy();
			if (point_Focus2.f == point_Focus2.fRe)
			{
				setAva(1, point_Focus2.objMain);
				point_Focus2.vx = 0;
				point_Focus2.vy = 0;
				point_Focus2.x = point_Focus2.objMain.x;
				point_Focus2.y = point_Focus2.objMain.y;
			}
			if (point_Focus2.f > point_Focus2.fRe)
			{
				point_Focus2.objMain.dy = CRes.random(20, 30);
				if (point_Focus2.f < point_Focus2.fRe + 4)
				{
					setAva(-1, point_Focus2.objMain);
				}
			}
		}
		if (f == 24)
		{
			changeDir();
			objFireMain.Dir = Dir;
			objFireMain.x = xArchor;
			objFireMain.y = yArchor;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			x1000 = x - 15;
			y1000 = objFireMain.y - 22;
			int num2 = -15;
			if (Dir == 2)
			{
				num2 = 15;
				x1000 = x - 63;
			}
			x += num2;
			y -= 5;
		}
		if (f == 26)
		{
			objFireMain.isTanHinh = false;
		}
	}

	public void update_Zoro_S3_L7()
	{
		if (f >= fRemove || checkNullObject(1))
		{
			if (isAddSound)
			{
				mSound.playSound(9, mSound.volumeSound);
			}
			addVir(10, 5, 10, isPlayer: true);
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
				GameScreen.addEffectEnd(64, 3, point_Focus.x, point_Focus.y - point_Focus.objMain.hOne / 2, Dir, objMainEff);
				setAva(1, point_Focus.objMain);
			}
			removeEff();
			return;
		}
		x1000 += vx;
		if (f == 6)
		{
			vx = 8;
			if (Dir == 0)
			{
				vx = -8;
			}
			objFireMain.vx = vx;
		}
		if (f == 10 && !GameCanvas.lowGraphic)
		{
			GameScreen.addEffectEnd(137, 2, objFireMain.x, objFireMain.y + 10, Dir, objMainEff);
		}
		if (f == 12)
		{
			objFireMain.isTanHinh = true;
		}
		if (f == 14)
		{
			vx = 0;
			objFireMain.vx = vx;
			objFireMain.isTanHinh = true;
		}
		if (f == 20)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd(108, 1, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, objMainEff);
					}
				}
			}
		}
		if (f >= 10 && f % 3 == 0 && indexObjBefire < vecObjsBeFire.size())
		{
			Object_Effect_Skill object_Effect_Skill2 = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
			indexObjBefire++;
			if (object_Effect_Skill2 != null)
			{
				MainObject mainObject2 = MainObject.get_Object(object_Effect_Skill2.ID, object_Effect_Skill2.tem);
				if (mainObject2 != null)
				{
					Point_Focus p = new Point_Focus();
					int num = y;
					if (!checkNullObject(1))
					{
						num = objFireMain.y;
					}
					int xdich = mainObject2.x - x;
					int ydich = mainObject2.y - num;
					p = create_Speed(xdich, ydich, p, x, num, mainObject2.x, mainObject2.y);
					p.objMain = mainObject2;
					VecEff.addElement(p);
				}
			}
		}
		for (int k = 0; k < VecEff.size(); k++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecEff.elementAt(k);
			point_Focus2.update_Vx_Vy();
			if (point_Focus2.f == point_Focus2.fRe)
			{
				setAva(1, point_Focus2.objMain);
				point_Focus2.vx = 0;
				point_Focus2.vy = 0;
				point_Focus2.x = point_Focus2.objMain.x;
				point_Focus2.y = point_Focus2.objMain.y;
			}
			if (point_Focus2.f > point_Focus2.fRe)
			{
				point_Focus2.objMain.dy = CRes.random(20, 30);
				if (point_Focus2.f < point_Focus2.fRe + 4)
				{
					setAva(-1, point_Focus2.objMain);
				}
			}
		}
		if (f == 24)
		{
			changeDir();
			objFireMain.Dir = Dir;
			objFireMain.x = xArchor;
			objFireMain.y = yArchor;
			x = objFireMain.x;
			y = objFireMain.y - objFireMain.hOne / 2;
			x1000 = x - 15;
			y1000 = objFireMain.y - 22;
			int num2 = -15;
			if (Dir == 2)
			{
				num2 = 15;
				x1000 = x - 63;
			}
			x += num2;
			y -= 5;
		}
		if (f == 26)
		{
			objFireMain.isTanHinh = false;
		}
	}

	public void changeDir()
	{
		Dir = (sbyte)((Dir != 2) ? 2 : 0);
	}

	public void addVir(int ran, int min, int max, bool isPlayer)
	{
		if ((!isPlayer || (!checkNullObject(1) && objFireMain == GameScreen.player)) && CRes.random(ran) == 0)
		{
			LoadMap.timeVibrateScreen = CRes.random(min, max);
		}
	}

	public void sendMove(int x, int xto, int y, int yto)
	{
		if (objFireMain != GameScreen.player)
		{
			objFireMain.x = xto;
			objFireMain.y = yto;
			return;
		}
		if (MainObject.getDistance(x, y, x, y) <= 30)
		{
			objFireMain.x = xto;
			objFireMain.y = yto;
		}
		int num = CRes.abs(x - xto);
		if (num < CRes.abs(y - yto))
		{
			num = CRes.abs(y - yto);
		}
		int num2 = num / 20;
		if (num2 == 0)
		{
			num2 = 1;
		}
		int num3 = (xto - x) / num2;
		int num4 = (xto - x) / num2;
		for (int i = 0; i < num2; i++)
		{
			objFireMain.x += num3;
			objFireMain.y += num4;
			GlobalService.gI().Obj_Move((short)objFireMain.x, (short)objFireMain.y);
		}
		objFireMain.x = xto;
		objFireMain.y = yto;
		GlobalService.gI().Obj_Move((short)objFireMain.x, (short)objFireMain.y);
	}

	private void update_Zoro_S3_L2()
	{
		if ((f == 13 || f == 20) && isAddSound)
		{
			mSound.playSound(10, mSound.volumeSound);
		}
		if (f == 15)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd_ObjTo(24, 0, mainObject.x, mainObject.y, mainObject.ID, mainObject.typeObject, 0, null);
					}
				}
			}
		}
		if (f > 20 && f % 3 == 0 && indexObjBefire < vecObjsBeFire.size())
		{
			Object_Effect_Skill object_Effect_Skill2 = (Object_Effect_Skill)vecObjsBeFire.elementAt(indexObjBefire);
			indexObjBefire++;
			if (object_Effect_Skill2 != null)
			{
				MainObject mainObject2 = MainObject.get_Object(object_Effect_Skill2.ID, object_Effect_Skill2.tem);
				if (mainObject2 != null)
				{
					Point_Focus p = new Point_Focus();
					int num = y;
					if (!checkNullObject(1))
					{
						num = objFireMain.y;
					}
					int xdich = mainObject2.x - x;
					int ydich = mainObject2.y - num;
					p = create_Speed(xdich, ydich, p, x, num, mainObject2.x, mainObject2.y);
					p.objMain = mainObject2;
					VecEff.addElement(p);
				}
			}
		}
		for (int j = 0; j < VecEff.size(); j++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(j);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f == point_Focus.fRe)
			{
				setAva(1, point_Focus.objMain);
				point_Focus.vx = 0;
				point_Focus.vy = 0;
				point_Focus.x = point_Focus.objMain.x;
				point_Focus.y = point_Focus.objMain.y;
			}
			if (point_Focus.f > point_Focus.fRe)
			{
				point_Focus.objMain.dy = CRes.random(20, 30);
				if (point_Focus.f < point_Focus.fRe + 4)
				{
					setAva(-1, point_Focus.objMain);
				}
				if (point_Focus.f >= point_Focus.fRe + 8)
				{
					VecEff.removeElement(point_Focus);
					j--;
				}
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void update_Zoro_S3_L1()
	{
		if ((f == 13 || f == 20) && isAddSound)
		{
			mSound.playSound(10, mSound.volumeSound);
		}
		if (f == 15)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (object_Effect_Skill != null)
				{
					MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
					if (mainObject != null)
					{
						GameScreen.addEffectEnd_ObjTo(24, 0, mainObject.x, mainObject.y, mainObject.ID, mainObject.typeObject, 0, null);
					}
				}
			}
		}
		if (f == 23)
		{
			for (int j = 0; j < vecObjsBeFire.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill2 = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
				if (object_Effect_Skill2 != null)
				{
					MainObject mainObject2 = MainObject.get_Object(object_Effect_Skill2.ID, object_Effect_Skill2.tem);
					if (mainObject2 != null)
					{
						GameScreen.addEffectEnd(11, 0, mainObject2.x, mainObject2.y - mainObject2.hOne / 2, Dir, objMainEff);
						setAva(0, mainObject2);
					}
				}
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void updateLuffyMon16_17()
	{
		if (f == 1 || f == 5 || f == 10)
		{
			int num = 20;
			if (Dir == 0)
			{
				num = -20;
			}
			setAva(0, objBeFireMain);
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(35, 0, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			if (!checkNullObject(1))
			{
				GameScreen.addEffectEnd(72, (f != 5) ? 1 : 2, x + num, objFireMain.y - objFireMain.hOne / 2, Dir, objMainEff);
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void updateLuffySea1()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(93, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				GameScreen.addEffectEnd(8, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f == 3 || f == 11)
		{
			if (isAddSound)
			{
				mSound.playSound(11, mSound.volumeSound);
			}
			if (!checkNullObject(2))
			{
				for (int j = 0; j < 2; j++)
				{
					Point_Focus p = new Point_Focus(x, y);
					int xdich = objBeFireMain.x - x;
					int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - y;
					p = create_Speed(xdich, ydich, p);
					p.frame = CRes.random(3);
					p.dis = Dir;
					VecEff.addElement(p);
				}
			}
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			removeEff();
		}
	}

	private void updateLuffySea2()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				GameScreen.addEffectEnd(8, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 4, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
			else if (typeEffect == 135)
			{
				Point o = new Point(point_Focus.x, point_Focus.y);
				VecSubEff.addElement(o);
			}
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point point = (Point)VecSubEff.elementAt(j);
			point.f++;
			if (point.f >= 4)
			{
				VecSubEff.removeElement(point);
				j--;
			}
		}
		if (f >= 10 && f <= 13 && !checkNullObject(1))
		{
			objFireMain.dy = (f - 9) * 8;
		}
		if (f >= 14 && f <= 16 && !checkNullObject(1))
		{
			objFireMain.dy = 32;
		}
		if (f >= 17 && f <= 20 && !checkNullObject(1))
		{
			objFireMain.dy = (20 - f) * 8;
		}
		if (f == 3 || f == 6)
		{
			if (isAddSound)
			{
				mSound.playSound(11, mSound.volumeSound);
			}
			if (!checkNullObject(2))
			{
				for (int k = 0; k < 2; k++)
				{
					Point_Focus p = new Point_Focus(x, y);
					int xdich = objBeFireMain.x - x;
					int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - y;
					p = create_Speed(xdich, ydich, p);
					p.frame = CRes.random(3);
					p.dis = Dir;
					if (typeEffect == 135)
					{
						p.maxdis = 1;
					}
					VecEff.addElement(p);
				}
			}
		}
		if (f == 12 && isAddSound)
		{
			mSound.playSound(6, mSound.volumeSound);
		}
		if (f == 15 && !checkNullObject(3))
		{
			Point_Focus p2 = new Point_Focus(x, y);
			int xdich2 = objBeFireMain.x - x;
			int ydich2 = objBeFireMain.y - objBeFireMain.hOne / 2 - (y - objFireMain.dy);
			p2 = create_Speed(xdich2, ydich2, p2, x, y - objFireMain.dy, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2);
			p2.maxdis = 1;
			p2.frame = CRes.random(4);
			p2.dis = Dir;
			VecEff.addElement(p2);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			if (!checkNullObject(1))
			{
				objFireMain.dy = 0;
			}
			removeEff();
		}
	}

	private void updateLuffySea3()
	{
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			point_Focus.update_Vx_Vy();
			if (point_Focus.f >= point_Focus.fRe)
			{
				addVir(5, 5, 10, isPlayer: true);
				GameScreen.addEffectEnd(8, 0, point_Focus.x, point_Focus.y, Dir, objMainEff);
				VecEff.removeElement(point_Focus);
				i--;
			}
		}
		if (f >= 22 && f <= 25 && !checkNullObject(1))
		{
			objFireMain.dy = (f - 21) * 10;
		}
		if (f >= 26 && f <= 35 && !checkNullObject(1))
		{
			objFireMain.dy = 40;
		}
		if (f >= 36 && f <= 39 && !checkNullObject(1))
		{
			objFireMain.dy = (39 - f) * 10;
		}
		if (f == 10 || f == 20 || f == 34)
		{
			if (isAddSound)
			{
				mSound.playSound(6, mSound.volumeSound);
			}
			if (!checkNullObject(3))
			{
				Point_Focus p = new Point_Focus(x, y);
				int xdich = objBeFireMain.x - x;
				int ydich = objBeFireMain.y - objBeFireMain.hOne / 2 - (y - objFireMain.dy);
				p = create_Speed(xdich, ydich, p, x, y - objFireMain.dy, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2);
				p.frame = CRes.random(4);
				p.dis = Dir;
				VecEff.addElement(p);
			}
		}
		if (f == 1 || f == 11 || f == 26)
		{
			GameScreen.addEffectEnd(30, 0, x, y - objFireMain.dy, 250, Dir, objMainEff);
		}
		if (f >= fRemove && VecEff.size() == 0)
		{
			if (!checkNullObject(1))
			{
				objFireMain.dy = 0;
			}
			removeEff();
		}
	}

	private void updateSanjiSea1()
	{
		if (f <= 4 || (f >= 11 && f <= 15))
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = true;
			}
		}
		else if (!checkNullObject(1))
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 2 && !checkNullObject(3))
		{
			int num = objBeFireMain.x - 20;
			if (Dir == 0)
			{
				num = objBeFireMain.x + 20;
			}
			objFireMain.x = num;
			objFireMain.y = objBeFireMain.y;
		}
		if (f == 12 && !checkNullObject(1))
		{
			objFireMain.x = x;
			objFireMain.y = y;
		}
		if (objBeFireMain != null && objBeFireMain.hOne > 0 && (f == 6 || f == 9))
		{
			if (isAddSound && f == 6)
			{
				mSound.playSound(2, mSound.volumeSound);
			}
			if (!checkNullObject(2))
			{
				GameScreen.addEffectEnd(1, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
				GameScreen.addEffectEnd(93, 2, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
			}
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.x = x;
				objFireMain.y = y;
			}
			removeEff();
		}
	}

	private void updateSanjiSea2()
	{
		if (f <= 4 || (f >= 8 && f <= 13) || f == 19)
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = true;
			}
		}
		else if (!checkNullObject(1))
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 2 && !checkNullObject(3))
		{
			int num = objBeFireMain.x - 20;
			if (Dir == 0)
			{
				num = objBeFireMain.x + 20;
			}
			objFireMain.x = num;
			objFireMain.y = objBeFireMain.y;
			objFireMain.Dir = Dir;
		}
		if (f == 12 && !checkNullObject(3))
		{
			int num2 = objBeFireMain.x + 20;
			if (Dir == 0)
			{
				num2 = objBeFireMain.x - 20;
			}
			objFireMain.x = num2;
			objFireMain.y = objBeFireMain.y;
			objFireMain.Dir = ((Dir == 0) ? 2 : 0);
		}
		if (f == 19 && !checkNullObject(1))
		{
			objFireMain.x = x;
			objFireMain.y = y;
			objFireMain.Dir = Dir;
		}
		if (objBeFireMain != null && objBeFireMain.hOne > 0 && (f == 4 || f == 6 || f == 14 || f == 16))
		{
			if (isAddSound && (f == 4 || f == 14))
			{
				mSound.playSound(2, mSound.volumeSound);
			}
			if (!checkNullObject(3))
			{
				if (objFireMain.hOne > 0)
				{
					int num3 = 25;
					if (objFireMain.Dir == 0)
					{
						num3 = -25;
					}
					GameScreen.addEffectEnd(36, 0, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				}
				if (typeEffect == 137)
				{
					GameScreen.addEffectEnd(1, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3), Dir, objMainEff);
				}
				else
				{
					GameScreen.addEffectEnd(4, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3) - 10, Dir, objMainEff);
					addVir(5, 5, 10, isPlayer: true);
				}
			}
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.x = x;
				objFireMain.y = y;
				objFireMain.Dir = Dir;
				objFireMain.isTanHinh = false;
			}
			removeEff();
		}
	}

	private void updateSanjiSea3()
	{
		if ((f >= 4 && f <= 8) || (f >= 19 && f <= 23) || (f >= 35 && f <= 39))
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = true;
			}
		}
		else if (!checkNullObject(1))
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 6 && !checkNullObject(3))
		{
			int num = objBeFireMain.x - 25;
			if (Dir == 0)
			{
				num = objBeFireMain.x + 25;
			}
			objFireMain.x = num;
			objFireMain.y = objBeFireMain.y;
			objFireMain.Dir = Dir;
		}
		if (f == 21 && !checkNullObject(3))
		{
			int num2 = objBeFireMain.x + 25;
			if (Dir == 0)
			{
				num2 = objBeFireMain.x - 25;
			}
			objFireMain.x = num2;
			objFireMain.y = objBeFireMain.y;
			objFireMain.Dir = ((Dir == 0) ? 2 : 0);
		}
		if (f == 37 && !checkNullObject(1))
		{
			objFireMain.x = x;
			objFireMain.y = y;
			objFireMain.Dir = Dir;
		}
		if (objBeFireMain != null && objBeFireMain.hOne > 0 && (f == 11 || f == 15 || f == 25 || f == 29))
		{
			if (isAddSound && (f == 11 || f == 25))
			{
				mSound.playSound(14, mSound.volumeSound);
			}
			if (!checkNullObject(3))
			{
				if (objFireMain.hOne > 0)
				{
					int num3 = 30;
					if (objFireMain.Dir == 0)
					{
						num3 = -30;
					}
					GameScreen.addEffectEnd(36, 0, objFireMain.x + num3, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				}
				GameScreen.addEffectEnd(4, 0, objBeFireMain.x + CRes.random_Am_0(15), objBeFireMain.y - CRes.random(0, objBeFireMain.hOne / 4 * 3) - 10, Dir, objMainEff);
				addVir(5, 5, 10, isPlayer: true);
			}
		}
		if (f >= fRemove)
		{
			if (!checkNullObject(1))
			{
				objFireMain.x = x;
				objFireMain.y = y;
				objFireMain.Dir = Dir;
			}
			removeEff();
		}
	}

	private void updateMonster_DanhTron()
	{
		if (f == 1 && !checkNullObject(1))
		{
			for (int i = 0; i < GameScreen.vecPlayers.size(); i++)
			{
				MainObject mainObject = (MainObject)GameScreen.vecPlayers.elementAt(i);
				if (mainObject.typeObject == 0 && MainObject.getDistance(mainObject.x, mainObject.y, objFireMain.x, objFireMain.y) <= 60)
				{
					setAva(-1, mainObject);
					GameScreen.addEffectEnd(3, 0, mainObject.x, mainObject.y - mainObject.hOne / 2, Dir, objMainEff);
				}
			}
		}
		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void updateUssop_S2_L3_New()
	{
		if (f >= fRemove)
		{
			removeEff();
			return;
		}
		if (f == 15)
		{
			if (isAddSound)
			{
				mSound.playSound(23, mSound.volumeSound);
			}
			toX = objBeFireMain.x;
			toY = objBeFireMain.y - objBeFireMain.hOne / 2;
			y -= 6;
			if (Dir == 0)
			{
				x -= 30;
			}
			else
			{
				x += 30;
			}
			if (toX > x)
			{
				vx = 12;
			}
			else
			{
				vx = -12;
			}
			if (toY > y)
			{
				vy = 2;
			}
			else
			{
				vy = -2;
			}
			setAngle();
			GameScreen.addEffectEnd(57, 0, x, y, Dir, objMainEff);
			addVir(5, 5, 10, isPlayer: true);
		}
		if (f > 15 && f < fRemove && typeEffect == 225 && !GameCanvas.lowGraphic)
		{
			GameScreen.addEffectEnd(140, 0, x, y + 40, Dir, objMainEff);
		}
		if ((typeEffect != 225 && f == fRemove - 10) || (typeEffect == 225 && f == fRemove - 16))
		{
			setAva(2, objBeFireMain);
			if (!checkNullObject(2))
			{
				int a = 12;
				GameScreen.addEffectEnd(4, 2, objBeFireMain.x + CRes.random_Am_0(a), objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(a), Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
		}
	}

	private void updateUssop_S2_L6()
	{
		if (f >= fRemove)
		{
			objFireMain.isTanHinh = false;
			removeEff();
			return;
		}
		if (f == 15)
		{
			if (isAddSound)
			{
				mSound.playSound(23, mSound.volumeSound);
			}
			toX = objBeFireMain.x;
			toY = objBeFireMain.y - objBeFireMain.hOne / 2;
			y -= 6;
			if (Dir == 0)
			{
				x -= 30;
			}
			else
			{
				x += 30;
			}
			if (toX > x)
			{
				vx = 12;
			}
			else
			{
				vx = -12;
			}
			if (toY > y)
			{
				vy = 2;
			}
			else
			{
				vy = -2;
			}
			setAngle();
			GameScreen.addEffectEnd(168, 2, x, y, Dir, objMainEff);
			addVir(5, 5, 10, isPlayer: true);
		}
		if (f > 15 && f < fRemove && !GameCanvas.lowGraphic)
		{
			GameScreen.addEffectEnd(167, 0, x, y + 40, Dir, objMainEff);
		}
		if (f == fRemove - 10 || f == fRemove - 16)
		{
			setAva(2, objBeFireMain);
			if (!checkNullObject(2))
			{
				int a = 12;
				GameScreen.addEffectEnd(4, 2, objBeFireMain.x + CRes.random_Am_0(a), objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(a), Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
		}
		if (f > 2 && f < 6)
		{
			((Point_Focus)VecEff.elementAt(0)).update();
		}
		if (f > 2 && f < 15)
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
	}

	private void update_Ussop_S2_L7()
	{
		if (f >= fRemove)
		{
			objFireMain.isTanHinh = false;
			removeEff();
			return;
		}
		if (f == 15)
		{
			if (isAddSound)
			{
				mSound.playSound(23, mSound.volumeSound);
			}
			toX = objBeFireMain.x;
			toY = objBeFireMain.y - objBeFireMain.hOne / 2;
			y -= 6;
			if (Dir == 0)
			{
				x -= 30;
			}
			else
			{
				x += 30;
			}
			if (toX > x)
			{
				vx = 12;
			}
			else
			{
				vx = -12;
			}
			if (toY > y)
			{
				vy = 2;
			}
			else
			{
				vy = -2;
			}
			setAngle();
			int num = 40;
			int num2 = 20;
			if (Dir == 2)
			{
				num = -40;
				num2 = 20;
			}
			GameScreen.addEffectEnd(168, 2, x, y, Dir, objMainEff);
			GameScreen.addEffectEnd(168, 2, x + num, y - num2, Dir, objMainEff);
			addVir(5, 5, 10, isPlayer: true);
		}
		if (f > 15 && f < fRemove && !GameCanvas.lowGraphic)
		{
			int num3 = 40;
			int num4 = 20;
			if (Dir == 2)
			{
				num3 = -40;
				num4 = 20;
			}
			GameScreen.addEffectEnd(167, 0, x, y + 40, Dir, objMainEff);
			GameScreen.addEffectEnd(167, 0, x + num3, y - num4 + 40, Dir, objMainEff);
		}
		if (f == fRemove - 10 || f == fRemove - 16)
		{
			setAva(2, objBeFireMain);
			if (!checkNullObject(2))
			{
				int a = 12;
				GameScreen.addEffectEnd(4, 2, objBeFireMain.x + CRes.random_Am_0(a), objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(a), Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
		}
		if (f > 2 && f < 6)
		{
			((Point_Focus)VecEff.elementAt(0)).update();
		}
		if (f > 2 && f < 15)
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
	}

	private void updateUssop_S2_L6_old()
	{
		if (f >= fRemove)
		{
			if (objFireMain != null)
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		if (f == 1 || f == 10)
		{
			GameScreen.addEffectEnd(5, 0, x, y, Dir, objMainEff);
		}
		if (f == 2)
		{
			objFireMain.isTanHinh = true;
		}
		else if (f == fPre)
		{
			objFireMain.isTanHinh = false;
		}
		if (VecEff.size() > 0 && f < fPre)
		{
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
				int num = f - fPre * i / 3;
				if (num > 0 && num < point_Focus.fRe)
				{
					point_Focus.update();
				}
				int a = 12;
				if (num == point_Focus.fRe)
				{
					GameScreen.addEffectEnd(1, 0, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
					GameScreen.addEffectEnd(4, 2, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
					GameScreen.addEffectEnd(93, 2, toX + CRes.random_Am_0(a), toY + CRes.random_Am_0(a), Dir, objMainEff);
				}
			}
		}
		if (f == 8 + fPre)
		{
			if (isAddSound)
			{
				mSound.playSound(23, mSound.volumeSound);
			}
			toX = objBeFireMain.x;
			toY = objBeFireMain.y - objBeFireMain.hOne / 2;
			y = objFireMain.y - objFireMain.hOne / 2;
			if (Dir == 0)
			{
				x = objFireMain.x - 30;
			}
			else
			{
				x = objFireMain.x + 30;
			}
			if (toX > x)
			{
				vx = 12;
			}
			else
			{
				vx = -12;
			}
			if (toY > y)
			{
				vy = 2;
			}
			else
			{
				vy = -2;
			}
			setAngle();
			GameScreen.addEffectEnd(57, 0, x, y, Dir, objMainEff);
			addVir(5, 5, 10, isPlayer: true);
		}
		if (f > 8 + fPre && f < fRemove && !GameCanvas.lowGraphic)
		{
			GameScreen.addEffectEnd(167, 0, x, y + 40, Dir, objMainEff);
		}
		if (f == fRemove - 16)
		{
			setAva(2, objBeFireMain);
			if (!checkNullObject(2))
			{
				int a2 = 12;
				GameScreen.addEffectEnd(4, 2, objBeFireMain.x + CRes.random_Am_0(a2), objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(a2), Dir, objMainEff);
				GameScreen.addEffectEnd(108, 7, objBeFireMain.x, objBeFireMain.y - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
		}
	}

	private void updateMonster_Chay_Thang()
	{
		if (f > 12 && f % 12 == 0)
		{
			Point point = new Point();
			point.vx = am_duong * 15;
			if (!checkNullObject(1))
			{
				point.y = objFireMain.y;
				point.x = objFireMain.x + point.vx;
			}
			else
			{
				point.y = y;
				point.x = x + point.vx;
			}
			VecEff.addElement(point);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point2 = (Point)VecEff.elementAt(i);
			point2.update();
			if (point2.f > 6)
			{
				point2.frame++;
			}
			if (point2.frame >= 3)
			{
				VecEff.removeElement(point2);
				i--;
			}
		}
		if (GameCanvas.timeNow - timeBegin >= timeEnd)
		{
			removeEff();
		}
	}

	private void updateSanji_S2_L3_New()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		if ((f % 10 > 9 || f % 10 <= 1) && f > 5 && f < 35)
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 10 || f == 20 || f == 30)
		{
			if (f > 10)
			{
				changeDir();
				am_duong = -1;
				if (Dir == 2)
				{
					am_duong = 1;
				}
				objFireMain.Dir = Dir;
			}
			objFireMain.x = objBeFireMain.x - am_duong * 30;
			objFireMain.y = objBeFireMain.y;
		}
		if (f < 40 && f >= 10 && (f % 10 == 2 || f % 10 == 7))
		{
			if (isAddSound && f % 10 == 2)
			{
				mSound.playSound(14, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(36, 0, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			GameScreen.addEffectEnd(25, 0, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			setAva(0, objBeFireMain);
		}
		if (f == 42)
		{
			objFireMain.isTanHinh = true;
			changeDir();
			am_duong = -1;
			if (Dir == 2)
			{
				am_duong = 1;
			}
			objFireMain.Dir = Dir;
			objFireMain.x = x;
			objFireMain.y = y;
		}
	}

	private void updateSanji_S2_L3_New_SHORT()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(1))
			{
				objFireMain.isTanHinh = false;
			}
			removeEff();
			return;
		}
		if ((f % 10 > 9 || f % 10 <= 1) && f > 5 && f < 25)
		{
			objFireMain.isTanHinh = true;
		}
		else
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 1 || f == 10 || f == 20)
		{
			changeDir();
			am_duong = -1;
			if (Dir == 2)
			{
				am_duong = 1;
			}
			objFireMain.Dir = Dir;
			objFireMain.x = objBeFireMain.x - am_duong * 30;
			objFireMain.y = objBeFireMain.y;
		}
		if (f < 24 && (f % 10 == 2 || f % 10 == 7))
		{
			if (isAddSound && f % 10 == 2)
			{
				mSound.playSound(14, mSound.volumeSound);
			}
			if (f % 10 == 2 || typeEffect == 187)
			{
				GameScreen.addEffectEnd(108, 7, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			}
			addVir(5, 5, 10, isPlayer: true);
			GameScreen.addEffectEnd(36, 0, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			int subtype = 0;
			if (typeEffect == 187)
			{
				subtype = 4;
			}
			GameScreen.addEffectEnd(25, subtype, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
			if (typeEffect == 187)
			{
				GameScreen.addEffectEnd(119, 2, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.hOne / 2 + 2, (sbyte)objFireMain.Dir, objMainEff);
			}
			setAva(0, objBeFireMain);
		}
		if (f == 22)
		{
			objFireMain.isTanHinh = true;
			changeDir();
			am_duong = -1;
			if (Dir == 2)
			{
				am_duong = 1;
			}
			objFireMain.Dir = Dir;
			objFireMain.x = x;
			objFireMain.y = y;
		}
	}

	private void updateSanji_S1_L3_New()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(3))
			{
				objFireMain.isTanHinh = false;
				objBeFireMain.vx = 0;
			}
			removeEff();
			return;
		}
		if (f == 10)
		{
			objFireMain.x = objBeFireMain.x - am_duong * 30;
			objFireMain.y = objBeFireMain.y;
		}
		if (f == 12 || f == 17)
		{
			if (isAddSound)
			{
				mSound.playSound(2, mSound.volumeSound);
			}
			if (typeEffect == 177)
			{
				GameScreen.addEffectEnd(19, 0, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 1, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
			else
			{
				GameScreen.addEffectEnd(36, 0, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
			GameScreen.addEffectEnd(25, 0, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
		}
		if (f == 20)
		{
			vy1000 = 35;
			objFireMain.isTanHinh = true;
		}
		if (f >= 20 && f <= 27)
		{
			objBeFireMain.dy = yplus;
			yplus += vy1000;
			if (vy1000 > 0)
			{
				vy1000 -= 5;
			}
			setAva(-1, objBeFireMain);
		}
		if (f == 25)
		{
			objFireMain.isTanHinh = false;
		}
		if (f == 23)
		{
			objFireMain.dy = 105;
			changeDir();
			am_duong = -1;
			if (Dir == 2)
			{
				am_duong = 1;
			}
			objFireMain.Dir = Dir;
			objFireMain.x = objBeFireMain.x - am_duong * 30;
			GameScreen.addEffectEnd(30, 0, objFireMain.x - am_duong * 5, objFireMain.y - objFireMain.hOne / 2 - objFireMain.dy, 400, Dir, objMainEff);
		}
		if (f >= 23 && f <= 40)
		{
			objFireMain.dy = 105;
			if (f >= 27)
			{
				objBeFireMain.dy = 105;
			}
			setAva(-1, objBeFireMain);
		}
		if (f == 40)
		{
			if (isAddSound)
			{
				mSound.playSound(15, mSound.volumeSound);
			}
			addVir(5, 5, 10, isPlayer: true);
			if (typeEffect == 177)
			{
				GameScreen.addEffectEnd(19, 0, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				GameScreen.addEffectEnd(108, 1, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			}
			else
			{
				GameScreen.addEffectEnd(36, 0, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
				GameScreen.addEffectEnd(35, 0, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.dy - objBeFireMain.hOne / 2, Dir, objMainEff);
			}
			vy1000 = 10;
			yplus = 120;
		}
		if (f >= 41 && f <= 46)
		{
			yplus -= vy1000;
			objBeFireMain.dy = yplus;
			vy1000 += 5;
			if (objBeFireMain.dy < 0)
			{
				objBeFireMain.dy = 0;
			}
			objBeFireMain.vx = am_duong * 15;
			objFireMain.updateDy();
		}
		if (f > 47)
		{
			objBeFireMain.vx = 0;
		}
	}

	private void updateSanji_S1_L3_SHORT()
	{
		if (f >= fRemove || checkNullObject(3))
		{
			if (!checkNullObject(3))
			{
				objFireMain.isTanHinh = false;
				objBeFireMain.vx = 0;
			}
			removeEff();
			return;
		}
		if (f == 1)
		{
			objFireMain.x = objBeFireMain.x - am_duong * 30;
			objFireMain.y = objBeFireMain.y;
		}
		if (f != 7 && f != 10 && f != 13)
		{
			return;
		}
		if (isAddSound)
		{
			mSound.playSound(2, mSound.volumeSound);
		}
		setAva(0, objBeFireMain);
		sbyte subtype = 1;
		if (typeEffect != 218 || f == 10)
		{
			subtype = 0;
		}
		GameScreen.addEffectEnd(36, subtype, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
		subtype = 0;
		if (typeEffect == 186)
		{
			subtype = 4;
		}
		GameScreen.addEffectEnd(25, subtype, objBeFireMain.x - am_duong * 5, objBeFireMain.y - objBeFireMain.hOne / 2 + CRes.random_Am_0(10), Dir, objMainEff);
		if (f == 10)
		{
			GameScreen.addEffectEnd(108, 7, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.dy - objFireMain.hOne / 3 * 2 + 10, Dir, objMainEff);
			if (typeEffect == 186)
			{
				GameScreen.addEffectEnd(119, 1, objFireMain.x + am_duong * 25, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
			}
			if (typeEffect == 218)
			{
				GameScreen.addEffectEnd(119, 4, objFireMain.x + am_duong * 20, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, objMainEff);
			}
		}
	}

	private void beginPaint()
	{
	}

	public void addSound(sbyte idS)
	{
		if (isAddSound)
		{
			mSound.playSound(idS, mSound.volumeSound);
		}
	}

	public void addSoundBuff()
	{
		if (objFireMain.clazz == 1)
		{
			addSound(6);
		}
		else if (objFireMain.clazz == 2)
		{
			addSound(8);
		}
		else if (objFireMain.clazz == 3)
		{
			addSound(16);
		}
		else if (objFireMain.clazz == 4)
		{
			addSound(22);
		}
		else if (objFireMain.clazz == 5)
		{
			addSound(34);
		}
	}

	public void addSoundBuffShort()
	{
		if (objFireMain.clazz == 1)
		{
			addSound(44);
		}
		else if (objFireMain.clazz == 2)
		{
			addSound(45);
		}
		else if (objFireMain.clazz == 3)
		{
			addSound(46);
		}
		else if (objFireMain.clazz == 4)
		{
			addSound(22);
		}
		else if (objFireMain.clazz == 5)
		{
			addSound(34);
		}
	}

	public void paintKurobi_2(mGraphics g)
	{
		if (f >= 15 && f <= 20)
		{
			fraImgEff.drawFrame((f - 11) / 3, objFireMain.x + x1000, objFireMain.y + y1000, Dir, 3, g);
		}
		else if (f >= 25 && f <= 30)
		{
			fraImgEff.drawFrame((f - 25) / 3, objFireMain.x + x1000, objFireMain.y + y1000 + 10, Dir, 3, g);
		}
	}

	public void paintDonKrieg_3(mGraphics g)
	{
		if (f > 10 && f < 18)
		{
			int num = x + plusxy[1][0];
			int num2 = y + plusxy[1][1];
			int idx = 1;
			if (f < 13)
			{
				num = x + plusxy[0][0];
				num2 = y + plusxy[0][1];
				idx = 0;
			}
			fraImgEff.drawFrame(idx, num, num2, Dir, 3, g);
		}
	}

	public void paintDonKrieg_2(mGraphics g)
	{
		if (f < fRemove)
		{
			int num = xArchor;
			int num2 = f / 2;
			if (f > 16)
			{
				num2 = 22 - f;
				if (f > 20)
				{
					num = xArchor + xplus * 2;
				}
				else if (f > 18)
				{
					num = xArchor + xplus;
				}
			}
			else if (f >= 4)
			{
				num2 = 2;
			}
			if (num2 == 2)
			{
				num2 = 3;
			}
			if (f >= 10 && f <= 12)
			{
				num = xArchor + xplus;
			}
			fraImgEff.drawFrame(num2, num, y, Dir, 3, g);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			fraImgSubEff.drawFrame(0, point_Focus.x, point_Focus.y, Dir, 3, g);
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point_Focus point_Focus2 = (Point_Focus)VecSubEff.elementAt(j);
			fraImgSub2Eff.drawFrame(point_Focus2.f % fraImgSub2Eff.nFrame, point_Focus2.x, point_Focus2.y, Dir, 3, g);
		}
	}

	public void paintDonKrieg_1(mGraphics g)
	{
		if (f < fRemove)
		{
			int num = x;
			int idx = f / 2;
			if (f > 16)
			{
				idx = 22 - f;
				if (f > 20)
				{
					num = x + xplus * 2;
				}
				else if (f > 18)
				{
					num = x + xplus;
				}
			}
			else if (f >= 4)
			{
				idx = 2;
			}
			if (f >= 10 && f <= 12)
			{
				num = x + xplus;
			}
			fraImgEff.drawFrame(idx, num, y, Dir, 3, g);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			paint_Bullet(g, fraImgSubEff, point_Focus.frame, point_Focus.x, point_Focus.y, isMore: false, 0);
		}
	}

	public void paintBuggy_2(mGraphics g)
	{
		int num = 0;
		int num2 = 5;
		if (f == 28)
		{
			num = -6;
		}
		else if (f > 28)
		{
			num = 0;
			num2 *= 2;
		}
		else
		{
			num2 = 0;
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
			if (Dir == 2)
			{
				g.setColor(15956504);
				g.fillRect(x1000 - num2, y1000 - 7, CRes.abs(point_Focus.x - x1000) + num2, 14);
				g.setColor(15985419);
				g.fillRect(x1000 - num2, y1000 - 6, CRes.abs(point_Focus.x - x1000) + num2, 12);
				g.setColor(16645629);
				g.fillRect(x1000 - num2, y1000 - 4, CRes.abs(point_Focus.x - x1000) + num2, 8);
			}
			else
			{
				g.setColor(15956504);
				g.fillRect(x1000 - CRes.abs(point_Focus.x - x1000), y1000 - 7, CRes.abs(point_Focus.x - x1000) + num2, 14);
				g.setColor(15985419);
				g.fillRect(x1000 - CRes.abs(point_Focus.x - x1000), y1000 - 6, CRes.abs(point_Focus.x - x1000) + num2, 12);
				g.setColor(16645629);
				g.fillRect(x1000 - CRes.abs(point_Focus.x - x1000), y1000 - 4, CRes.abs(point_Focus.x - x1000) + num2, 8);
			}
			fraImgSub3Eff.drawFrame(0, point_Focus.x, point_Focus.y, Dir, 3, g);
		}
		if (f > 8 && f < 42)
		{
			if (Dir == 2)
			{
				num2 = -num2;
			}
			int idx = 0;
			if (f < 16)
			{
				idx = 2;
			}
			else if (f == 16 || f == 17)
			{
				idx = 1;
			}
			fraImgSubEff.drawFrame(idx, x + num2, y + 38 + num, Dir, 33, g);
		}
		if (f >= 18 && f <= 20)
		{
			fraImgSub2Eff.drawFrame(f % fraImgSub2Eff.nFrame, x1000, y1000, Dir, 3, g);
		}
		if (f < 12)
		{
			int idx2 = 1 + f / 2 % 2;
			if (f < 2 || f > 9)
			{
				idx2 = 0;
			}
			fraImgEff.drawFrame(idx2, x, y, Dir, mGraphics.TOP | mGraphics.HCENTER, g);
		}
		if (f > 40)
		{
			int idx3 = 1 + f / 2 % 2;
			if (f > 46)
			{
				idx3 = 0;
			}
			fraImgEff.drawFrame(idx3, x, y, Dir, mGraphics.TOP | mGraphics.HCENTER, g);
		}
	}

	public void paintLuffy_New3(mGraphics g)
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			if (point.f < 3)
			{
				fraImgSub2Eff.drawFrame(2 - point.f, point.x, point.y, Dir, 33, g);
			}
			else
			{
				objFireMain.paintBody(g, point.x, point.y, objFireMain.frame, objFireMain.Dir, isEye: true);
			}
			int num = -20;
			if (Dir == 2)
			{
				num = 20;
			}
			if (f > 20)
			{
				fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, point.x + num, point.y - objFireMain.hOne / 2, Dir, 3, g);
			}
		}
		if (f > 20)
		{
			fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, x, y, Dir, 3, g);
		}
	}

	public void paint_Luffy_S3_L7(mGraphics g)
	{
		mSystem.outz("vestsub size   = " + VecSubEff.size());
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			if (point.f < 3)
			{
				fraImgSub2Eff.drawFrame(2 - point.f, point.x, point.y, point.dir, 33, g);
			}
			else
			{
				objFireMain.paintBody(g, point.x, point.y, objFireMain.frame, point.dir, isEye: true);
			}
			int num = -20;
			if (Dir == 2)
			{
				num = 20;
			}
			if (f > 20)
			{
				fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, point.x + num, point.y - objFireMain.hOne / 2, point.dir, 3, g);
			}
		}
		if (f > 20)
		{
			fraImgEff.drawFrame(f / numNextFrame % fraImgEff.nFrame, x, y, Dir, 3, g);
		}
	}

	public void paintLuffy_New2(mGraphics g)
	{
		if (objFireMain != null)
		{
			if (f == 1)
			{
				fraImgSubEff.drawFrame(0, x, y + objFireMain.hOne / 2, Dir, 33, g);
			}
			if ((f >= 9 && f <= 11) || (f > 25 && f < 33))
			{
				int num = 16;
				if (Dir == 0)
				{
					num = -16;
				}
				fraImgEff.drawFrame(2, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2 + 2, Dir, 3, g);
			}
		}
		if (f >= 12 && f <= 15)
		{
			fraImgSubEff.drawFrame((f - 12) / 2, x, y, Dir, 3, g);
		}
		if (f >= 17 && f <= 20)
		{
			fraImgSub2Eff.drawFrame((f - 17) / 2, x, y, Dir, 3, g);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			fraImgSubEff.drawFrame(point.f / 2, point.x, point.y, Dir, 33, g);
		}
	}

	public void paintLuffy_New2_SHORT(mGraphics g)
	{
		if (objFireMain != null && ((f >= 2 && f <= 3) || (f > 16 && f < 22)))
		{
			int num = 16;
			if (Dir == 0)
			{
				num = -16;
			}
			fraImgEff.drawFrame(2, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2 + 2 - objFireMain.dy, Dir, 3, g);
		}
		if (f >= 3 && f <= 6)
		{
			fraImgSubEff.drawFrame((f - 12) / 2, x, y, Dir, 3, g);
		}
		if (f >= 8 && f <= 11)
		{
			fraImgSub2Eff.drawFrame((f - 17) / 2, x, y, Dir, 3, g);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			fraImgSubEff.drawFrame(point.f / 2, point.x, point.y, Dir, 33, g);
		}
		if (typeEffect == 213 || typeEffect == 272)
		{
			for (int j = 0; j < VecSubEff.size(); j++)
			{
				Point point2 = (Point)VecSubEff.elementAt(j);
				fraImgSub3Eff.drawFrame(point2.frame, point2.x, point2.y, Dir, 33, g);
			}
		}
	}

	public void paint_Luffy_S2_L7(mGraphics g)
	{
		if (objFireMain != null && ((f >= 2 && f <= 3) || (f > 16 && f < 22)))
		{
			int num = 16;
			if (Dir == 0)
			{
				num = -16;
			}
			fraImgEff.drawFrame(2, objFireMain.x + num, objFireMain.y - objFireMain.hOne / 2 + 2 - objFireMain.dy, Dir, 3, g);
		}
		if (f >= 3 && f <= 6)
		{
			fraImgSubEff.drawFrame((f - 12) / 2, x, y, Dir, 3, g);
		}
		if (f >= 8 && f <= 11)
		{
			fraImgSub2Eff.drawFrame((f - 17) / 2, x, y, Dir, 3, g);
		}
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			fraImgSubEff.drawFrame(point.f / 2, point.x, point.y, Dir, 33, g);
		}
		for (int j = 0; j < VecSubEff.size(); j++)
		{
			Point point2 = (Point)VecSubEff.elementAt(j);
			fraImgSub3Eff.drawFrame(point2.frame, point2.x, point2.y, Dir, 33, g);
		}
	}

	public void paintSanji_3(mGraphics g)
	{
		if (f >= 4 && f < fRemove)
		{
			fraImgEff.drawFrame((f - 4) / numNextFrame % fraImgEff.nFrame, x - xplus, y, Dir, 3, g);
		}
		if (typeEffect == 49 || typeEffect == 50)
		{
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point_Focus point_Focus = (Point_Focus)VecEff.elementAt(i);
				if (fraImgSubEff != null)
				{
					fraImgSubEff.drawFrame(point_Focus.f % fraImgSubEff.nFrame, point_Focus.x, point_Focus.y, Dir, 3, g);
				}
				fraImgSub2Eff.drawFrame((point_Focus.f + point_Focus.frame) % fraImgSub2Eff.nFrame, point_Focus.x, point_Focus.y, Dir, 3, g);
			}
			return;
		}
		if (typeEffect == 220 || typeEffect == 293)
		{
			if (f > 1 && f < fRemove - 1 && fraImgSubEff != null)
			{
				fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x1000, y1000 + 5, Dir, 33, g);
			}
			for (int j = 0; j < VecSubEff.size(); j++)
			{
				Point point = (Point)VecSubEff.elementAt(j);
				if (point.f == 0)
				{
					fraImgSub2Eff.drawFrame(point.frame, point.x + am_duong * 5, point.y + 4, Dir, 3, g);
				}
				else if (point.frame == 0)
				{
					fraImgSub3Eff.drawFrame(point.f, point.x, point.y, Dir, 3, g);
				}
				else
				{
					fraImgSub4Eff.drawFrame(point.f, point.x, point.y, Dir, 3, g);
				}
			}
			return;
		}
		if (f > 1 && f < fRemove - 1 && fraImgSubEff != null)
		{
			fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x1000, y1000 + 5, Dir, 33, g);
		}
		for (int k = 0; k < VecSubEff.size(); k++)
		{
			Point point2 = (Point)VecSubEff.elementAt(k);
			if (point2.f == 0)
			{
				fraImgSub2Eff.drawFrame(point2.f, point2.x, point2.y, Dir, 3, g);
			}
			else
			{
				fraImgSub3Eff.drawFrame(point2.f, point2.x, point2.y, Dir, 3, g);
			}
		}
	}

	public void paint_Sanji_S3_L7(mGraphics g)
	{
		if (f >= 4 && f < fRemove)
		{
			fraImgEff.drawFrame((f - 4) / numNextFrame % fraImgEff.nFrame, x - xplus, y, Dir, 3, g);
		}
		if (f > 1 && f < fRemove - 1 && fraImgSubEff != null)
		{
			fraImgSubEff.drawFrame(f / 2 % fraImgSubEff.nFrame, x1000, y1000 + 5, Dir, 33, g);
		}
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point point = (Point)VecSubEff.elementAt(i);
			if (point.f == 0)
			{
				fraImgSub2Eff.drawFrame(point.frame, point.x + am_duong * 5, point.y + 4, point.dis, 3, g);
			}
			else if (point.frame == 0)
			{
				fraImgSub3Eff.drawFrame(point.f, point.x, point.y, Dir, 3, g);
			}
			else
			{
				fraImgSub4Eff.drawFrame(point.f, point.x, point.y, Dir, 3, g);
			}
		}
	}

	public void paintGalio_1(mGraphics g)
	{
		int num = 2;
		numNextFrame = 4;
		if (f > 40)
		{
			num = fraImgEff.nFrame;
			numNextFrame = 2;
		}
		fraImgEff.drawFrame(f / numNextFrame % num, objFireMain.x, objFireMain.y - objFireMain.hOne / 2, Dir, 3, g);
	}

	public void paintPan_1(mGraphics g)
	{
		int num = 3;
		if (f > 20)
		{
			num = fraImgEff.nFrame;
		}
		fraImgEff.drawFrame(f / 2 % num, objFireMain.x, objFireMain.y, Dir, 3, g);
		for (int i = 0; i < VecEff.size(); i++)
		{
			Point point = (Point)VecEff.elementAt(i);
			fraImgSubEff.drawFrame(point.frame + point.f / 3 % 2, point.x, point.y, 0, mGraphics.BOTTOM | mGraphics.RIGHT, g);
			fraImgSubEff.drawFrame(point.frame + point.f / 3 % 2, point.x, point.y, 2, mGraphics.BOTTOM | mGraphics.LEFT, g);
			fraImgSubEff.drawFrame(point.frame + point.f / 3 % 2, point.x, point.y, 1, mGraphics.TOP | mGraphics.RIGHT, g);
			fraImgSubEff.drawFrame(point.frame + point.f / 3 % 2, point.x, point.y, 3, 0, g);
		}
	}

	public override void replaceHP(mVector vec)
	{
		for (int i = 0; i < vecObjsBeFire.size(); i++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
			if (object_Effect_Skill == null)
			{
				continue;
			}
			for (int j = 0; j < vec.size(); j++)
			{
				Object_Effect_Skill object_Effect_Skill2 = (Object_Effect_Skill)vec.elementAt(i);
				if (object_Effect_Skill2 != null && object_Effect_Skill.ID == object_Effect_Skill2.ID)
				{
					if (GameScreen.isShowTextTab)
					{
						GameCanvas.chatTabScr.addNewChat(T.tabTestAdmin, "+DAM: ", object_Effect_Skill.hpShow.ToString() ?? "", 1, isFocus: false, -1, ChatDetail.CAT_SYSTEM);
					}
					object_Effect_Skill.hpShow = object_Effect_Skill2.hpShow;
					object_Effect_Skill.hpMagic = object_Effect_Skill2.hpMagic;
					object_Effect_Skill.mEffTypePlus = new int[object_Effect_Skill2.mEffTypePlus.Length];
					object_Effect_Skill.mEff_HP_Plus = new int[object_Effect_Skill2.mEffTypePlus.Length];
					object_Effect_Skill.mEff_Time_Plus = new int[object_Effect_Skill2.mEffTypePlus.Length];
					for (int k = 0; k < object_Effect_Skill.mEffTypePlus.Length; k++)
					{
						object_Effect_Skill.mEffTypePlus[k] = object_Effect_Skill2.mEffTypePlus[k];
						object_Effect_Skill.mEff_HP_Plus[k] = object_Effect_Skill2.mEff_HP_Plus[k];
						object_Effect_Skill.mEff_Time_Plus[k] = object_Effect_Skill2.mEff_Time_Plus[k];
					}
					break;
				}
			}
		}
	}

	public static void setHP_New(mVector vec, MainObject objFire, bool isAdd)
	{
		for (int i = 0; i < vec.size(); i++)
		{
			Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vec.elementAt(i);
			MainObject mainObject = MainObject.get_Object(object_Effect_Skill.ID, object_Effect_Skill.tem);
			if (mainObject == null)
			{
				vec.removeElement(object_Effect_Skill);
				i--;
			}
			else
			{
				if (mainObject.Action == 4)
				{
					continue;
				}
				if (object_Effect_Skill.hpLast < mainObject.Hp)
				{
					mainObject.Hp = object_Effect_Skill.hpLast;
				}
				if (!isAdd)
				{
					continue;
				}
				bool flag = setAddEffPlus(object_Effect_Skill, mainObject, objFire, objFire);
				sbyte typeColor = 15;
				int num = object_Effect_Skill.hpShow;
				if (objFire == GameScreen.player)
				{
					typeColor = 13;
				}
				if (objFire.typeObject == 1)
				{
					typeColor = 14;
					num = -num;
				}
				if (objFire == GameScreen.player && GameScreen.isShowTextTab)
				{
					GameCanvas.chatTabScr.addNewChat(T.tabTestAdmin, "+DAM: ", object_Effect_Skill.hpShow.ToString() ?? "", 1, isFocus: false, -1, ChatDetail.CAT_SYSTEM);
				}
				if (objFire == GameScreen.player || mainObject == GameScreen.player || !GameCanvas.lowGraphic)
				{
					if (object_Effect_Skill.hpShow == 0)
					{
						GameScreen.addEffectNumBig_NEW_AP(num, object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, 17);
					}
					else
					{
						if (flag)
						{
							typeColor = 16;
						}
						GameScreen.addEffectNumBig_NEW_AP(num, object_Effect_Skill.hpMagic, mainObject.x, mainObject.y - mainObject.hOne, typeColor);
					}
				}
				if (mainObject.Hp <= 0)
				{
					mainObject.beginDie(objFire);
				}
			}
		}
	}

	public void setFlyFire(int begin, int end, int maxfly, int speedfly)
	{
		if (objFireMain != null && !objFireMain.returnAction() && f >= begin && f < end)
		{
			objFireMain.dy += speedfly;
			if (objFireMain.dy > maxfly)
			{
				objFireMain.dy = maxfly;
			}
		}
	}

	public void setDownFire(int begin, int end, int speedfly)
	{
		if (objFireMain != null && !objFireMain.returnAction() && f >= begin && f < end)
		{
			objFireMain.dy -= speedfly;
			if (objFireMain.dy < 0)
			{
				objFireMain.dy = 0;
			}
		}
	}

	public static bool setAddEffPlus(Object_Effect_Skill objEff, MainObject obj, MainObject objFire, MainObject OBJMainEff)
	{
		if (objEff == null || obj == null || objFire == null)
		{
			return false;
		}
		bool result = false;
		for (int i = 0; i < objEff.mEffTypePlus.Length; i++)
		{
			switch (objEff.mEffTypePlus[i])
			{
			case 1010:
			case 1058:
				if (objEff.hpShow <= 1)
				{
					return false;
				}
				GameScreen.addEffectEnd(20, 0, obj.x, obj.y - obj.hOne / 2, (sbyte)obj.Dir, OBJMainEff);
				result = true;
				break;
			case 1013:
				GameScreen.addEffectEnd(21, 0, obj.x, obj.y - obj.hOne / 2, (sbyte)obj.Dir, OBJMainEff);
				break;
			case 1014:
				GameScreen.addEffectEnd_ToX_ToY(23, 0, obj.x, obj.y - obj.hOne / 2, objFire.x, objFire.y - objFire.hOne / 2, (sbyte)obj.Dir, OBJMainEff);
				break;
			case 1021:
			case 1022:
				GameScreen.addEffectEnd_ObjTo(22, (objEff.mEffTypePlus[i] == 1021) ? ((sbyte)1) : ((sbyte)0), obj.x, obj.y - obj.hOne / 2, objFire.ID, objFire.typeObject, (sbyte)objFire.Dir, OBJMainEff);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 15:
			case 16:
			case 17:
				obj.addEffSpec((short)objEff.mEffTypePlus[i], (short)objEff.mEff_Time_Plus[i]);
				break;
			case 12:
				GameScreen.addEffectNum(objEff.hpShow + T.chuan, obj.x, obj.y - obj.hOne, 11);
				break;
			}
		}
		return result;
	}

	public void CreateSpeedEff(int xS, int yS, int xToS, int yToS, int time, int vyBegin)
	{
		int num = (xToS - xS) * 1000 / time;
		int num2 = CRes.abs(vyBegin / (time / 2));
		speedEff = new int[time][];
		int num3 = yS;
		for (int i = 0; i < speedEff.Length - 1; i++)
		{
			speedEff[i] = new int[2];
			speedEff[i][0] = xS + num * i / 1000;
			speedEff[i][1] = num3 + vyBegin / 1000;
			vyBegin += num2;
			num3 = speedEff[i][1];
		}
		speedEff[time - 1][0] = xToS;
		speedEff[time - 1][1] = yToS;
	}

	public void CreateSuperFrameCausu1()
	{
		mframeSuper = new int[6][]
		{
			new int[2] { 1, 0 },
			new int[3] { 1, 1, 0 },
			new int[3] { 1, 1, 0 },
			new int[3] { 1, 1, 0 },
			new int[2] { 1, 0 },
			new int[2] { 1, 2 }
		};
	}

	public static int HasHapThuEffPlus(Object_Effect_Skill objEff, MainObject obj, MainObject objFire, MainObject OBJMainEff)
	{
		if (objEff == null || obj == null || objFire == null)
		{
			return -1;
		}
		for (int i = 0; i < objEff.mEffTypePlus.Length; i++)
		{
			if (objEff.mEffTypePlus[i] == 1058)
			{
				GameScreen.addEffectEnd(20, 0, obj.x, obj.y - obj.hOne / 2, (sbyte)obj.Dir, OBJMainEff);
				return i;
			}
		}
		return -1;
	}

	private static FrameImage s_nikaEff1;
	private static FrameImage s_nikaEff2;

	private void createNikaJump(int variant)
	{
		fraImgEff = s_nikaEff1 ?? (s_nikaEff1 = new FrameImage(356, 40, 80));
		fraImgSubEff = s_nikaEff2 ?? (s_nikaEff2 = new FrameImage(183, 20, 54));
		if (objBeFireMain != null)
		{
			toY = objBeFireMain.y;
			toX = objBeFireMain.x;
		}
		fRemove = (variant == NIKA_VARIANT_ACTIVE_2) ? 52 : 30;
		step = 0;

		if (variant == NIKA_VARIANT_ACTIVE_2)
		{
			GameScreen.addEffectEnd(30, 0, x, y, 250, Dir, objMainEff);
		}
	}

	private void paintNikaJump(mGraphics g)
	{
		if (checkNullObject(1))
		{
			return;
		}

		if (f >= 3 && f <= 6 && fraImgSubEff != null && fraImgSubEff.nFrame > 0 && objFireMain != null)
		{
			fraImgSubEff.drawFrame(0, objFireMain.x, objFireMain.y - objFireMain.dy, 0, 33, g);
		}

		if (f >= 10 && f <= 20 && fraImgEff != null && fraImgEff.nFrame > 0 && objFireMain != null)
		{
			fraImgEff.drawFrame(f / 2 % fraImgEff.nFrame, objFireMain.x, objFireMain.y - objFireMain.dy, 0, 33, g);
		}
	}

	private void updateNikaJump(int variant)
	{
		if (checkNullObject(3))
		{
			if (objFireMain != null)
			{
				objFireMain.dy = 0;
				objFireMain.isTanHinh = false;
			}
			finishNikaEffect();
			return;
		}

		if (f >= 0 && f <= 8)
		{
			objFireMain.dy += 60;
			if (f == 4)
			{
				addSound(51);
			}
		}

		bool activeSkill2 = variant == NIKA_VARIANT_ACTIVE_2;
		bool level5 = variant == NIKA_VARIANT_ACTIVE_1_LEVEL5;

		if (activeSkill2 && f == 0 && step == 0)
		{
			GameScreen.addHightDataeff(NIKA_DATA_ACTIVE_2, objFireMain.x, objFireMain.y);
			step = 1;
		}
		else if (!activeSkill2 && f == 0 && step == 0)
		{
			if (objBeFireMain != null)
			{
				GameScreen.addHightDataeff(level5 ? NIKA_DATA_LEVEL5_START : NIKA_DATA_LEVEL1_START, objBeFireMain.x, objBeFireMain.y);
			}
			step = 1;
		}
		else if (!activeSkill2 && f == 5 && step == 1)
		{
			if (objBeFireMain != null)
			{
				GameScreen.addHightDataeff(level5 ? NIKA_DATA_LEVEL5_JUMP : NIKA_DATA_LEVEL1_JUMP, objBeFireMain.x, objBeFireMain.y);
			}
			step = 2;
		}

		if (f == 9)
		{
			objFireMain.dy = 480;
		}

		if (f >= 10 && f <= 20 && objFireMain.dy >= 0)
		{
			objFireMain.dy -= 60;
		}

		if (f == 12 && objBeFireMain != null && MainObject.getDistance(objFireMain.x, objFireMain.y, objBeFireMain.x, objBeFireMain.y) < 260)
		{
			objFireMain.x = objBeFireMain.x;
			objFireMain.y = objBeFireMain.y + 5;
		}

		if (f == 21)
		{
			objFireMain.dy = 0;
			addSound(5);
			if (objBeFireMain != null)
			{
				setAva(1, objBeFireMain);
				GameScreen.addEffectEnd(148, 0, objBeFireMain.x, objBeFireMain.y, Dir, objMainEff);
				GameScreen.addEffectEnd(45, 0, objBeFireMain.x, objBeFireMain.y + 25, Dir, objMainEff);
			}
			LoadMap.timeVibrateScreen = CRes.random(2, 6);
		}

		if (!activeSkill2 && f == 25 && step == 2)
		{
			if (objBeFireMain != null)
			{
				GameScreen.addHightDataeff(level5 ? NIKA_DATA_LEVEL5_LANDING_LEFT : NIKA_DATA_LEVEL1_LANDING_LEFT, objBeFireMain.x - 30, objBeFireMain.y);
				GameScreen.addHightDataeff(level5 ? NIKA_DATA_LEVEL5_LANDING_RIGHT : NIKA_DATA_LEVEL1_LANDING_RIGHT, objBeFireMain.x + 30, objBeFireMain.y);
			}
			step = 3;
		}

		if (f > fRemove)
		{
			if (objFireMain != null)
			{
				objFireMain.dy = 0;
				objFireMain.isTanHinh = false;
			}
			finishNikaEffect();
		}
	}

	private void createNikaBuff()
	{
		fRemove = 2;
		levelPaint = 1;
	}

	private void updateNikaBuff()
	{
		if (f == 0 && !checkNullObject(1))
		{
			objFireMain.addDataEff(NIKA_DATA_BUFF, 25000, 0, 0);
			addSound(30);
		}

		if (f > fRemove)
		{
			finishNikaEffect();
		}
	}

	private void createLightActive1Level5()
	{
		if (objFireMain != null)
		{
			objFireMain.addDataEff(LIGHT_LEVEL5_CAST, 0, (sbyte)0, (sbyte)0);
		}

		VecSubEff.removeAllElements();
		int maxTravelFrame = 0;

		if (vecObjsBeFire != null)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (targetInfo == null)
				{
					continue;
				}

				MainObject target = MainObject.get_Object(targetInfo.ID, targetInfo.tem);
				Point_Focus projectile = new Point_Focus(0, 0);
				projectile.objMain = target;
				projectile.Dir = Dir;
				projectile.x = x;
				projectile.y = y - 25;
				int targetX = target == null ? toX : target.x;
				int targetY = target == null ? toY : target.y - (target.hOne / 2);
				int distance = CRes.abs(targetX - projectile.x);
				int travelFrame = distance / 20;
				if (travelFrame < 1)
				{
					travelFrame = 1;
				}

				projectile.fRe = travelFrame;
				projectile.f = 0;
				projectile.vy = (targetY - projectile.y) / travelFrame;
				VecSubEff.addElement(projectile);
				if (travelFrame > maxTravelFrame)
				{
					maxTravelFrame = travelFrame;
				}
			}
		}

		fRemove = (short)(maxTravelFrame + 20);
	}

	private void updateLightActive1Level5()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point_Focus projectile = (Point_Focus)VecSubEff.elementAt(i);
			if (projectile == null) continue;
			projectile.x += projectile.Dir == 2 ? 20 : -20;
			projectile.y += projectile.vy;
			GameScreen.addHightDataeff(
				LIGHT_LEVEL5_PROJECTILE,
				projectile.x,
				projectile.y
			);
			projectile.f++;

			if (projectile.f >= projectile.fRe)
			{
				int impactX = projectile.objMain == null ? projectile.x : projectile.objMain.x;
				int impactY = projectile.objMain == null ? projectile.y : projectile.objMain.y - (projectile.objMain.hOne / 2);
				GameScreen.addHightDataeff(LIGHT_LEVEL5_IMPACT, impactX, impactY);
				LoadMap.timeVibrateScreen = CRes.random(6, 15);
				VecSubEff.removeElement(projectile);
				i--;
			}
		}

		if (f >= fRemove || VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void createLightActive2Level5()
	{
		int nFrame = 5;
		mframe = new int[nFrame];
		mframe[0] = 0;

		for (short i = 1; i < nFrame; i++)
		{
			DataSkillEff data = new DataSkillEff(
				(short)(LIGHT_LEVEL5_ACTIVE_2_START + i - 1),
				0
			);
			mframe[i] = (data.sequence != null ? data.sequence.Length : 10) + mframe[i - 1] + 1;
		}

		VecSubEff.removeAllElements();
		if (vecObjsBeFire != null)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (targetInfo == null)
				{
					continue;
				}

				MainObject target = MainObject.get_Object(targetInfo.ID, targetInfo.tem);
				if (target != null)
				{
					Point_Focus sequence = new Point_Focus(0, 0);
					sequence.objMain = target;
					sequence.dis = (short)i;
					sequence.f = (short)(-i * 5);
					sequence.fRe = (short)mframe[nFrame - 1];
					VecSubEff.addElement(sequence);
				}
			}
		}
		fRemove = (short)(mframe[nFrame - 1] + 20);
	}

	private void updateLightActive2Level5()
	{
		if (mframe == null)
		{
			removeEff();
			return;
		}
		int nFrame = mframe.Length;
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point_Focus sequence = (Point_Focus)VecSubEff.elementAt(i);
			if (sequence == null || sequence.objMain == null)
			{
				VecSubEff.removeElement(sequence);
				i--;
				continue;
			}

			for (short frameIndex = 0; frameIndex < nFrame - 1; frameIndex++)
			{
				if (sequence.f == mframe[frameIndex])
				{
					GameScreen.addHightDataeff(
						(short)(LIGHT_LEVEL5_ACTIVE_2_START + frameIndex),
						sequence.objMain.x,
						sequence.objMain.y
					);
					if (frameIndex == 1)
					{
						GameScreen.addEffectEnd(
							110,
							0,
							sequence.objMain.x + CRes.random_Am_0(15),
							sequence.objMain.y + CRes.random_Am_0(5),
							Dir,
							sequence.objMain
						);
					}
					if (frameIndex == 1 || frameIndex == 3)
					{
						GameScreen.addEffectEnd(
							112,
							0,
							sequence.objMain.x,
							sequence.objMain.y,
							Dir,
							sequence.objMain
						);
						LoadMap.timeVibrateScreen = (frameIndex == 1 ? CRes.random(1, 5) : CRes.random(6, 20));
					}
				}
			}

			if (sequence.f > mframe[1])
			{
				if (sequence.f % 3 == 0)
				{
					GameScreen.addEffectEnd(
						108,
						5,
						sequence.objMain.x + CRes.random_Am_0(10),
						sequence.objMain.y - CRes.random(240),
						Dir,
						sequence.objMain
					);
					addSound(17);
				}

				if (sequence.f > mframe[nFrame - 2] + 4)
				{
					GameScreen.addEffectEnd(
						108,
						5,
						sequence.objMain.x + CRes.random_Am_0(60),
						sequence.objMain.y - CRes.random(30),
						Dir,
						sequence.objMain
					);
				}
			}

			sequence.f++;
			if (sequence.f >= sequence.fRe)
			{
				VecSubEff.removeElement(sequence);
				i--;
			}
		}

		if (VecSubEff.size() == 0 || (fRemove > 0 && f >= fRemove))
		{
			removeEff();
		}
	}

	private void createLoveActive2Level5()
	{
		int nFrame = 5;
		mframe = new int[nFrame];
		mframe[0] = 0;
		for (short i = 1; i < nFrame; i++)
		{
			DataSkillEff data = new DataSkillEff((short)(30 + i - 1), 0);
			mframe[i] = (data.sequence != null ? data.sequence.Length : 10) + mframe[i - 1] + 1;
		}
		fRemove = (short)(mframe[nFrame - 1] + 10);
	}

	private void updateLoveActive2Level5()
	{
		if (objFireMain == null || f > fRemove)
		{
			removeEff();
			return;
		}

		if (mframe != null)
		{
			for (short i = 0; i < mframe.Length; i++)
			{
				if (f == mframe[i])
				{
					objFireMain.addDataEff((short)(30 + i), 0, (sbyte)0, (sbyte)0);
					if (i == mframe.Length - 1)
					{
						objFireMain.addDataEff(
							LOVE_LEVEL5_FINISH_ATTACHED,
							0,
							(sbyte)0,
							(sbyte)0
						);
						if (objBeFireMain != null)
						{
							GameScreen.addHightDataeff(
								LOVE_LEVEL5_FINISH_IMPACT,
								objBeFireMain.x,
								objBeFireMain.y
							);
						}
					}
				}
			}
		}
	}

	private void finishNikaEffect()
	{
		removeEff();
	}

	private void createSkillBuff(short timeBuff)
	{
		fRemove = 2;
		levelPaint = 1;
	}

	private void updateSkillBuff()
	{
		if (f == 0 && !checkNullObject(1))
		{
			objFireMain.addDataEff((short)35, (int)timeBegin, (sbyte)0, (sbyte)0);
			addSoundBuffShort();
		}
		if (f > fRemove)
		{
			finishNikaEffect();
		}
	}

	private void createNikyuActive1()
	{
		if (objFireMain != null)
		{
			objFireMain.addDataEff(NIKYU_PROJECTILE, 0, (sbyte)0, (sbyte)0);
		}

		VecSubEff.removeAllElements();
		int maxTravelFrame = 0;

		if (vecObjsBeFire != null)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (targetInfo == null) continue;

				MainObject target = MainObject.get_Object(targetInfo.ID, targetInfo.tem);
				Point_Focus projectile = new Point_Focus(0, 0);
				projectile.objMain = target;
				projectile.Dir = Dir;
				projectile.x = x;
				projectile.y = y - 25;
				int targetX = target == null ? toX : target.x;
				int targetY = target == null ? toY : target.y - (target.hOne / 2);
				int distance = CRes.abs(targetX - projectile.x);
				int travelSpeed = 24;
				int travelFrame = distance / travelSpeed;
				if (travelFrame < 1) travelFrame = 1;

				projectile.fRe = travelFrame;
				projectile.f = 0;
				projectile.vy = (targetY - projectile.y) / travelFrame;
				VecSubEff.addElement(projectile);
				if (travelFrame > maxTravelFrame) maxTravelFrame = travelFrame;
			}
		}
		fRemove = (short)(maxTravelFrame + 25);
	}

	private void updateNikyuActive1()
	{
		for (int i = 0; i < VecSubEff.size(); i++)
		{
			Point_Focus projectile = (Point_Focus)VecSubEff.elementAt(i);
			projectile.x += projectile.Dir == 2 ? 24 : -24;
			projectile.y += projectile.vy;
			GameScreen.addHightDataeff(
				NIKYU_PROJECTILE,
				projectile.x,
				projectile.y,
				projectile.Dir == 2
			);
			projectile.f++;

			if (projectile.f >= projectile.fRe)
			{
				int impactX = projectile.objMain == null ? projectile.x : projectile.objMain.x;
				int impactY = projectile.objMain == null ? projectile.y : projectile.objMain.y - (projectile.objMain.hOne / 2);
				GameScreen.addHightDataeff(NIKYU_IMPACT, impactX, impactY, projectile.Dir == 2);
				LoadMap.timeVibrateScreen = CRes.random(8, 18);
				VecSubEff.removeElement(projectile);
				i--;
			}
		}

		if (f >= fRemove && VecSubEff.size() == 0)
		{
			removeEff();
		}
	}

	private void createNikyuActive2()
	{
		if (objFireMain != null)
		{
			GameScreen.addHightDataeff(NIKYU_DASH, objFireMain.x, objFireMain.y, Dir == 2);
		}

		VecSubEff.removeAllElements();
		if (vecObjsBeFire != null)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (targetInfo == null) continue;

				MainObject target = MainObject.get_Object(targetInfo.ID, targetInfo.tem);
				if (target != null)
				{
					Point_Focus repulsion = new Point_Focus(0, 0);
					repulsion.objMain = target;
					repulsion.Dir = Dir;
					repulsion.f = 0;
					repulsion.fRe = 24;
					VecSubEff.addElement(repulsion);
				}
			}
		}
		fRemove = 30;
	}

	private void updateNikyuActive2()
	{
		if (f == 2)
		{
			for (int i = 0; i < VecSubEff.size(); i++)
			{
				Point_Focus repulsion = (Point_Focus)VecSubEff.elementAt(i);
				if (repulsion.objMain != null)
				{
					GameScreen.addHightDataeff(NIKYU_REPULSION, repulsion.objMain.x, repulsion.objMain.y, repulsion.Dir == 2);
					LoadMap.timeVibrateScreen = CRes.random(10, 20);
					int pushDist = (repulsion.Dir == 2) ? 100 : -100;
					repulsion.objMain.x += pushDist;
				}
			}
		}

		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void createNikyuBuff()
	{
		fRemove = 2;
		levelPaint = 1;
	}

	private void updateNikyuBuff()
	{
		if (f == 0 && !checkNullObject(1))
		{
			objFireMain.addDataEff(NIKYU_BUFF_EFF, (int)timeBegin, (sbyte)0, (sbyte)0);
			addSoundBuffShort();
		}
		if (f > fRemove)
		{
			finishNikaEffect();
		}
	}

private static FrameImage[][] s_ttFrames;

	private static void ensureThanTrangFrames()
	{
		if (s_ttFrames != null) return;
		s_ttFrames = new FrameImage[17][];
		s_ttFrames[1] = new FrameImage[] {
			new FrameImage(101, 40, 47), new FrameImage(240, 30, 73, 1), new FrameImage(183, 3), new FrameImage(239, 40, 40), new FrameImage(406, 30, 30)
		};
		s_ttFrames[2] = new FrameImage[] {
			// 4002 Đại Phún Hỏa Volcano _X (Akainu Magma Eruption) — 10 Authentic Fire & Magma Textures
			new FrameImage(271, 130, 80, 3),          // [0] 271: Đại Phún Hỏa Cự Quyền / Colossal Magma Wave Fist (130x80, 3f)
			new FrameImage(336, 74, 30, 3),           // [1] 336: Dòng Dung Nham Cánh Tay / Magma Arm Stream (74x30, 3f)
			new FrameImage(254, 30, 40),              // [2] 254: Phản Lực Hỏa Diễm / Fiery Jet Trail (30x40, 1f)
			new FrameImage(252, 62, 64, 4),           // [3] 252: Vụ Nổ Dung Nham Bộc Phát / Magma Impact Blast (62x64, 4f)
			new FrameImage(238, 30, 73),              // [4] 238: Cột Nham Thạch Phun Trào / Molten Magma Pillar (30x73, 1f)
			new FrameImage(240, 30, 73, 1),           // [5] 240: Cột Lửa Vút Trời Thẳng Đứng / Rising Flame Column (30x73, 1f)
			new FrameImage(239, 38, 22),              // [6] 239: Hồ Dung Nham Sôi Sùng Sục / Boiling Magma Pool (38x22, 1f)
			new FrameImage(246, 49, 21, 4),           // [7] 246: Vết Rạn Nứt Núi Lửa Phun Trào / Volcanic Ground Fissure (49x21, 4f)
			new FrameImage(78, 22, 28, 5),            // [8] 78: Hạt Tàn Lửa Đỏ Li Ti / Crimson Sparks (22x28, 5f)
			new FrameImage(272, 50, 24)               // [9] 272: Vết Cháy Sém Mặt Đất / Scorched Earth Ground Shadow (50x24, 1f)
		};
		s_ttFrames[3] = new FrameImage[] {
			new FrameImage(37, 31, 74),          // [0] 37: Cột Băng Đao / Colossal Ice Pillar (31x74)
			new FrameImage(40, 63, 20),          // [1] 40: Thềm Băng Mặt Đất / Permafrost Ground Patch (63x20)
			new FrameImage(41, 40, 40),          // [2] 41: Hoa Tuyết & Bụi Băng Tinh Thể / Diamond Snow Crystal (40x40)
			new FrameImage(43, 84, 110),         // [3] 43: Đại Băng Trĩ / Glacial Avalanche Wave (84x110)
			new FrameImage(89, 28, 44),          // [4] 89: Gai Băng Nhọn Tủa / Jagged Ground Icicles (28x44)
			new FrameImage(46, 70, 100, 49, 70), // [5] 46: Hàn Băng Xung Kích / Glacial Shockwave (70x100)
			new FrameImage(39, 53, 28),          // [6] 39: Khối Băng Vĩnh Cửu / Eternal Permafrost Block (53x28)
			new FrameImage(41, 40, 40),          // [7] 41: Hoa Tuyết & Kim Cương Băng Tinh (40x40)
			new FrameImage(152, 25, 21)          // [8] 152: Khói Bụi Sương Lạnh / Sub-Zero Frost Fog (25x21)
		};
		s_ttFrames[4] = new FrameImage[] {
			new FrameImage(255, 42, 50, 3), new FrameImage(254, 30, 40)
		};
		s_ttFrames[5] = new FrameImage[] {
			// 4005 Hắc Ám Thôn Phệ Vô Tận _X (Marshall D. Teach Black Hole Abyss Singularity) — 14 Master VFX Assets
			new FrameImage(480, 120, 100, 8),        // [0] 480: Caster Void Aura (120x100, 8f, anchor 33)
			new FrameImage(481, 240, 180, 8),        // [1] 481: Singularity Gate Cast (240x180, 8f, anchor 33)
			new FrameImage(482, 180,  80, 8),        // [2] 482: Void Wave Projectile (180x80, 8f, anchor 3)
			new FrameImage(483, 240, 240, 10),       // [3] 483: Colossal Black Hole Vortex (240x240, 10f, anchor 3)
			new FrameImage(484, 280, 200, 12),       // [4] 484: Cataclysmic Singularity Collapse (280x200, 12f, anchor 3)
			new FrameImage(485,  90, 140, 8),        // [5] 485: Target Void Spire Particles (90x140, 8f, anchor 33)
			new FrameImage(272,  50,  24),           // [6] 272: Vết Cháy Sém / Hồ Hư Vô Mặt Đất (50x24)
			new FrameImage(246,  49,  21, 4),        // [7] 246: Vết Rạn Nứt Hư Vô Địa Chấn (49x21, 4f)
			new FrameImage(285, 111,  90),           // [8] 285: Sóng Không Gian Biến Dạng Hư Vô (111x90, 3f)
			new FrameImage(394, 126,  41),           // [9] 394: Vành Đai Trọng Lực Sóng Xung Kích (126x41, 3f)
			new FrameImage(104,  30,  30),           // [10] 104: Chớp Sao Bụi Hắc Ám / Dark Starburst Sparks (30x30, 3f)
			new FrameImage(152,  25,  21),           // [11] 152: Khói Bụi Va Chạm Mặt Đất (25x21)
			new FrameImage(92,   64, 126, 45, 89, 1),// [12] 92: Sét Hư Vô Tím Đen / Cosmic Void Lightning (64x126)
			new FrameImage(175,  40,  40)            // [13] 175: Vòng Nén Trọng Lực Hư Vô (40x40)
		};
		s_ttFrames[6] = new FrameImage[] {
			// 4006 Enel 200M Volt El Thor — 8 Pure Authentic Lightning Assets
			new FrameImage(243, 36, 39),          // [0] 243: Cầu lôi tụ điện (36x39) — High-voltage plasma sphere
			new FrameImage(244, 20, 37, 3),       // [1] 244: Sét chéo dội trần (20x37, 3f) — Sky diagonal thunderbolt
			new FrameImage(240, 30, 73, 1),       // [2] 240: Cột sét dọc (30x73, 1f) — Vertical lightning bolt column
			new FrameImage(241, 40, 27, 2),       // [3] 241: Điện quang mặt đất (40x27, 2f) — Ground spiderweb crackle
			new FrameImage(242, 49, 28, 2),       // [4] 242: Vành đai điện xả (49x28, 2f) — Ground discharge ring
			new FrameImage(104, 30, 30),          // [5] 104: Chớp sao bùng nổ hồ quang (30x30) — Starburst spark flash
			new FrameImage(152, 25, 21),          // [6] 152: Khói bụi tiếp đất (25x21) — Ground impact dust
			new FrameImage(92, 64, 126, 45, 89, 1)// [7] 92:  Cung sét khổng lồ / Hồ quang cao thế (64x126, 1f)
		};
		s_ttFrames[7] = new FrameImage[] {
			new FrameImage(310, 73, 59), new FrameImage(312, 121, 77)
		};
		s_ttFrames[8] = new FrameImage[] {
			// 4008 ROOM Gamma Knife (Law - Phẫu thuật Ope Ope no Mi)
			new FrameImage(393, 1), // [0] 393: ROOM sphere quanh nhân vật (1 frame 110x110)
			new FrameImage(394, 3), // [1] 394: Vòng sáng chân nhân vật (3 frame dọc chuẩn)
			new FrameImage(391, 1), // [2] 391: Hiệu ứng nửa trên mục tiêu dính (1 frame 28x13)
			new FrameImage(392, 3), // [3] 392: Vòng sáng chân mục tiêu dính (3 frame dọc chuẩn tương tự 394)
			new FrameImage(358, 3)  // [4] 358: Vết chém nhỏ ngẫu nhiên ở mục tiêu (3 frame dọc 51x22)
		};
		s_ttFrames[9] = new FrameImage[] {
			// 4009 Eustass Kid: Từ Trường Bộc Phá Đại Pháo _X (Damned Punk Railgun)
			new FrameImage(243, 36, 39),        // [0] Lõi plasma từ trường quay cuồng (36x39)
			new FrameImage(92, 40, 40),         // [1] Tia sét hồ quang điện từ (40x40)
			new FrameImage(104, 30, 30),        // [2] Tia lửa ma sát kim loại & điểm nổ (30x30)
			new FrameImage(175, 40, 40),        // [3] Vòng sóng nén từ trường (40x40)
			new FrameImage(152, 25, 21),        // [4] Khói bụi va chạm mặt đất (25x21)
			new FrameImage(240, 30, 73, 1),     // [5] Cột năng lượng ánh sáng thẳng đứng (30x73, 1f)
			new FrameImage(238, 110, 50),       // [6] Sóng chấn địa chấn (110x50)
			new FrameImage(402, 120, 60),       // [7] Luồng phản lực Railgun (120x60)
			new FrameImage(358, 51, 22),        // [8] Mảnh kim loại & tia chém (51x22)
			new FrameImage(272, 50, 24)         // [9] Vòng định vị mục tiêu mặt đất (50x24)
		};
		s_ttFrames[10] = new FrameImage[] {
			// 4010 Cổ Độc Phán Quyết Venom _X — texIDs 467-473 (Cinematic VFX)
			new FrameImage(467, 8),
			new FrameImage(468, 11),
			new FrameImage(469, 6),
			new FrameImage(470, 13),
			new FrameImage(471, 8),
			new FrameImage(472, 8),
			new FrameImage(473, 4)
		};
		s_ttFrames[11] = new FrameImage[] {
			new FrameImage(266, 80, 100, 64, 80, 2), new FrameImage(254, 30, 40)
		};
		s_ttFrames[12] = new FrameImage[] {
			// 4012 Phượng Hoàng Bất Tử Bộc Phá _X (Marco Blue Phoenix Climax) — 12 Authentic VFX Assets
			new FrameImage(474, 120, 100, 8),  // [0] Lam Hỏa Caster Aura (120x100, 8f, anchor 33)
			new FrameImage(475, 240, 180, 8),  // [1] Phượng Hoàng Thức Tỉnh Cast (240x180, 8f, anchor 33)
			new FrameImage(476, 180,  80, 8),  // [2] Phượng Hoàng Phi Thiên Projectile (180x80, 8f, anchor 3)
			new FrameImage(477, 240, 240, 10), // [3] Lam Hỏa Đại Bộc Phá _X Impact (240x240, 10f, anchor 3)
			new FrameImage(478, 280, 200, 12), // [4] Tung Cánh Phượng Hoàng Finisher AOE (280x200, 12f, anchor 33)
			new FrameImage(479,  90, 140, 8),  // [5] Cột Lam Hỏa Thiêu Đốt Particles (90x140, 8f, anchor 33)
			new FrameImage(243, 36, 39),         // [6] Swirling Solar Blue Flame Core (36x39)
			new FrameImage(242, 49, 28, 2),      // [7] Ground Flame Shockwave Ring (49x28, 2f)
			new FrameImage(241, 40, 27, 2),      // [8] Radiating Blue Ground Fire Sparks (40x27, 2f)
			new FrameImage(224, 22, 28, 5),      // [9] Sacred Rebirth Feathers & Embers (22x28, 5f)
			new FrameImage(272, 50, 24),         // [10] Ground Tracking Shadow (50x24)
			new FrameImage(104, 30, 30)          // [11] Starburst Blue Sparks & Flash (30x30, 3f)
		};
		s_ttFrames[13] = new FrameImage[] {
			// 4013 Đại Phật Sóng Xung Kích _X (Sengoku Daibutsu Golden Shockwave) — 15 Authentic Textures
			new FrameImage(416, 78, 40),              // [0] 416: Kim Cương Phật Chưởng / Palm Thrust Wave (78x40, 4f)
			new FrameImage(171, 153, 84),             // [1] 171: Sóng Xung Kích Hoàng Kim / Traveling Shockwave Ring (153x84, 4f)
			new FrameImage(453, 169, 126),            // [2] 453: Đại Bộc Phá Cực Đại / Colossal Mega Shockwave (169x126, 3f)
			new FrameImage(394, 126, 41),             // [3] 394: Vành Đai Địa Chấn / Ground Shockwave Ring (126x41, 3f)
			new FrameImage(357, 100, 100, 2),         // [4] 357: Pháp Luân Kim Quang / Sacred Dharma Wheel Nimbus (100x100, 4f)
			new FrameImage(315, 77, 54, 3),           // [5] 315: Khai Hoa Kim Liên / Divine Lotus Bloom (77x54, 6f)
			new FrameImage(174, 40, 40, 4),           // [6] 174: Thái Dương Quang Cầu / Condensed Palm Energy Core (40x40, 8f)
			new FrameImage(335, 80, 80, 2),           // [7] 335: Phật Chưởng Bộc Phá / Palm Blast Burst Cone (80x80, 10f)
			new FrameImage(300, 80, 25, 3),           // [8] 300: Địa Chấn Thổ Bụi / Seismic Dust Upheaval (80x25, 9f)
			new FrameImage(246, 49, 21, 4),           // [9] 246: Vết Rạn Nứt Địa Chấn / Ground Seismic Fissure Crevasse (49x21, 8f)
			new FrameImage(285, 111, 90),             // [10] 285: Sóng Không Gian Biến Dạng / Radial Distortion Wave (111x90, 3f)
			new FrameImage(66, 75, 55),               // [11] 66: Lõi Bạch Kim Thiểm Quang / Divine White-Gold Core Flash (75x55, 1f)
			new FrameImage(104, 30, 30),              // [12] 104: Chớp Sao Kim Cương / Diamond Starburst Sparks (30x30, 3f)
			new FrameImage(267, 47, 53),              // [13] 267: Kim Sắc Hộ Thể Linh Khí / Golden Transformation Aura (47x53, 1f)
			new FrameImage(224, 22, 28, 5)            // [14] 224: Hạt Kim Quang Linh Khí / Sacred Floating Nirvana Embers (22x28, 20f)
		};
		s_ttFrames[14] = new FrameImage[] {
			new FrameImage(291, 47, 48), new FrameImage(295, 34, 24)
		};
		s_ttFrames[15] = new FrameImage[] {
			new FrameImage(101, 40, 47), new FrameImage(240, 30, 73, 1)
		};
		s_ttFrames[16] = new FrameImage[] {
			new FrameImage(404, 40, 40), new FrameImage(408, 30, 30), new FrameImage(447, 40, 40), new FrameImage(456, 30, 73, 1),
			new FrameImage(254, 30, 40), new FrameImage(247, 40, 20), new FrameImage(285, 30, 30), new FrameImage(108, 30, 30),
			new FrameImage(100, 20, 20), new FrameImage(224, 22, 28), new FrameImage(152, 25, 21), new FrameImage(272, 30, 30)
		};
	}

	private void createThanTrangSkill(int typeEff)
	{
		this.VecSubEff.removeAllElements();
		int setId = (typeEff == 4017 || typeEff == 4010) ? 10 : ((typeEff >= 4001 && typeEff <= 4016) ? (typeEff - 4000) : ((typeEff >= 4201 && typeEff <= 4216) ? (typeEff - 4200) : ((typeEff >= 4501 && typeEff <= 4516) ? (typeEff - 4500) : (((typeEff - 4001) / 5) + 1))));
		if (setId < 1 || setId > 16) setId = 1;
		ensureThanTrangFrames();
		FrameImage[] arr = s_ttFrames[setId];
		if (arr != null)
		{
			if (arr.Length > 0) fraImgEff = arr[0];
			if (arr.Length > 1) fraImgSubEff = arr[1];
			if (arr.Length > 2) fraImgSub2Eff = arr[2];
			if (arr.Length > 3) fraImgSub3Eff = arr[3];
			if (arr.Length > 4) fraImgSub4Eff = arr[4];
			if (arr.Length > 5) fraImgSub5Eff = arr[5];
			if (arr.Length > 6) fraImgSub6Eff = arr[6];
		}

		// Play Sound Effect on cast
		switch (setId)
		{
			case 1:
				this.addSound(5);
				this.addSound(51);
				break;
			case 7:
				this.addSound(51);
				this.addSound(14);
				break;
			case 2:
				this.addSound(5);
				this.addSound(51);
				break;
			case 8:
				this.addSound(10);
				this.addSound(18);
				break;
			case 12: this.addSound(10); break;
			case 15: this.addSound(5); break;
			case 10: case 14: this.addSound(14); break;
			case 3: this.addSound(2); break;
			case 4: case 11: case 13: this.addSound(10); break;
			case 6: case 9: this.addSound(18); break;
			case 5: this.addSound(4); break;
			default: this.addSound(5); break;
		}

		// Ground casting ripple & celestial array under caster
		if (objFireMain != null)
		{
			switch (setId)
			{
				case 1:
					GameScreen.addHightDataeff(33, x, y);
					GameScreen.addEffectEnd(63, 0, x, y, (sbyte)Dir, objFireMain);
					GameScreen.addEffectEnd(175, 0, x, y - 15, (sbyte)Dir, objFireMain);
					break;
				case 2:
					GameScreen.addHightDataeff(33, x, y);
					GameScreen.addEffectEnd(111, 0, x, y, (sbyte)Dir, objFireMain);
					GameScreen.addEffectEnd(63, 0, x, y, (sbyte)Dir, objFireMain);
					GameScreen.addEffectEnd(112, 0, x, y, (sbyte)Dir, objFireMain);
					break;
				case 7:
					GameScreen.addEffectEnd(133, 0, x, y, (sbyte)Dir, objFireMain);
					GameScreen.addEffectEnd(110, 0, x, y, (sbyte)Dir, objFireMain);
					GameScreen.addEffectEnd(92, 0, x, y, (sbyte)Dir, objFireMain);
					GameScreen.addHightDataeff(33, x, y);
					break;
				case 8:
					break;
				case 15: GameScreen.addEffectEnd(63, 0, x, y, (sbyte)Dir, objFireMain); break;
				case 3: GameScreen.addEffectEnd(35, 0, x, y, (sbyte)Dir, objFireMain); break;
				case 4: GameScreen.addEffectEnd(175, 0, x, y - 20, (sbyte)Dir, objFireMain); break;
				case 13: GameScreen.addHightDataeff(33, x, y); break;
				case 5: GameScreen.addEffectEnd(108, 7, x, y, (sbyte)Dir, objFireMain); break;
				case 6: GameScreen.addEffectEnd(40, 0, x, y, (sbyte)Dir, objFireMain); break;
				case 9: GameScreen.addEffectEnd(92, 0, x, y, (sbyte)Dir, objFireMain); break;
				case 10: GameScreen.addEffectEnd(108, 7, x, y, (sbyte)Dir, objFireMain); break;
				case 11: GameScreen.addEffectEnd(175, 0, x, y, (sbyte)Dir, objFireMain); break;
				case 12:
					this.marcoWaveHitMask = 0;
					GameScreen.addEffectEnd(63, 0, x, y, (sbyte)Dir, objFireMain);
					GameScreen.addEffectEnd(175, 0, x, y - 15, (sbyte)Dir, objFireMain);
					break;
				case 14: GameScreen.addEffectEnd(50, 0, x, y, (sbyte)Dir, objFireMain); break;
			}
		}

		int maxTravelFrame = 0;
		if (setId != 2 && setId != 3 && setId != 5 && setId != 6 && setId != 7 && setId != 8 && setId != 9 && setId != 10 && setId != 12 && setId != 13 && setId != 16)
		{
			if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				for (int i = 0; i < vecObjsBeFire.size(); i++)
				{
					Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
					if (object_Effect_Skill != null)
					{
						MainObject target = MainObject.get_Object((int)object_Effect_Skill.ID, (sbyte)object_Effect_Skill.tem);
						Point_Focus point_Focus = new Point_Focus(0, 0);
						point_Focus.objMain = target;
						point_Focus.Dir = (sbyte)Dir;
						point_Focus.x = x;
						point_Focus.y = y - ((objFireMain != null) ? (objFireMain.hOne / 2) : 20);
						int targetX = (target == null) ? toX : target.x;
						int targetY = (target == null) ? toY : (target.y - target.hOne / 2);
						int distance = Math.Abs(targetX - point_Focus.x);
						int travelSpeed = 22;
						int travelFrame = distance / travelSpeed;
						if (travelFrame < 1)
						{
							travelFrame = 1;
						}
						point_Focus.fRe = travelFrame;
						point_Focus.f = 0;
						point_Focus.vy = (targetY - point_Focus.y) / travelFrame;
						point_Focus.toX = targetX;
						point_Focus.toY = targetY;
						this.VecSubEff.addElement(point_Focus);
						if (travelFrame > maxTravelFrame)
						{
							maxTravelFrame = travelFrame;
						}
					}
				}
			}
			else
			{
				Point_Focus point_Focus = new Point_Focus(0, 0);
				point_Focus.objMain = null;
				point_Focus.Dir = (sbyte)Dir;
				point_Focus.x = x;
				point_Focus.y = y - ((objFireMain != null) ? (objFireMain.hOne / 2) : 20);
				int targetX = x + ((Dir == 2) ? 160 : -160);
				int targetY = y - 20;
				int travelFrame = 7;
				point_Focus.fRe = travelFrame;
				point_Focus.f = 0;
				point_Focus.vy = (targetY - point_Focus.y) / travelFrame;
				point_Focus.toX = targetX;
				point_Focus.toY = targetY;
				this.VecSubEff.addElement(point_Focus);
				maxTravelFrame = travelFrame;
			}
		}

		if (setId == 2)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (objFireMain != null) ? objFireMain.x : x;
			int casterY = (objFireMain != null) ? objFireMain.y : y;
			int facingSign = (Dir == 2) ? 1 : -1;

			int centerX = toX;
			int centerY = toY;
			if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
				if (tInfo != null)
				{
					MainObject t0 = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
					if (t0 != null && !t0.isDie && t0.Hp > 0 && !t0.isRemove)
					{
						centerX = t0.x;
						centerY = t0.y - (t0.hOne / 2);
					}
				}
			}
			if (centerX == 0 && centerY == 0)
			{
				centerX = casterX + facingSign * 160;
				centerY = casterY - 15;
			}

			// Clamp distance to range = 220
			int dist = Math.Abs(centerX - casterX);
			if (dist > 220)
			{
				centerX = casterX + facingSign * 220;
			}
			toX = centerX;
			toY = centerY;

			// Khởi tạo 12 hạt tàn lửa nham thạch bắn tung tóe quanh tâm chấn
			for (int i = 0; i < 12; i++)
			{
				Point ep = new Point();
				int angle = (i * 30) % 360;
				int speed = 3 + (i % 4);
				ep.x = centerX;
				ep.y = centerY - 10;
				ep.vx = (CRes.getcos(angle) * speed) >> 10;
				ep.vy = -(3 + (i % 4));
				ep.f = 0;
				ep.fRe = 24;
				ep.color = (i % 2 == 0) ? 0 : 2;
				this.VecSubEff.addElement(ep);
			}

			fRemove = 70;
		}
		else if (setId == 3)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int centerX = toX;
			int centerY = toY;
			if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill t0 = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
				if (t0 != null)
				{
					MainObject target = MainObject.get_Object((int)t0.ID, (sbyte)t0.tem);
					if (target != null)
					{
						centerX = target.x;
						centerY = target.y;
					}
				}
			}
			if (centerX == 0 && centerY == 0)
			{
				centerX = x + (Dir == 2 ? 140 : -140);
				centerY = y - 15;
			}
			toX = centerX;
			toY = centerY;

			// 1. SEVEN COLOSSAL PERMAFROST PILLARS & 6 INTERMEDIATE JAGGED SPIKES
			// Final Unified Structure ("cuối vẫn ra 1 hình"):
			// 7 Colossal Ice Spires (dis = max height) & 6 Ground Icicles forming the majestic Glacial Peak
			int pattern = CRes.random(6);
			int[] spireDelays = new int[7];
			int[] spikeDelays = new int[6];
			int[] jitterX = new int[7];
			int[] jitterY = new int[7];

			switch (pattern)
			{
				case 0: // Pattern 0: Băng Tâm Bộc Phát (Epicenter Bloom / Inside-Out Surge)
					spireDelays[0] = 14; // Center pinnacle erupts first!
					spireDelays[1] = 16; spireDelays[2] = 17; // Inner flank
					spireDelays[3] = 19; spireDelays[4] = 20; // Mid flank
					spireDelays[5] = 22; spireDelays[6] = 23; // Outer perimeter
					spikeDelays[4] = 15; spikeDelays[5] = 15;
					spikeDelays[0] = 18; spikeDelays[1] = 18;
					spikeDelays[2] = 21; spikeDelays[3] = 21;
					break;

				case 1: // Pattern 1: Gọng Kìm Băng Phổ (Glacial Pincer / Outside-In Converge)
					spireDelays[5] = 14; spireDelays[6] = 14; // Outer flanks seal perimeter first!
					spireDelays[3] = 17; spireDelays[4] = 17; // Mid flank
					spireDelays[1] = 20; spireDelays[2] = 20; // Inner flank
					spireDelays[0] = 23; // Grand center pinnacle climax!
					spikeDelays[2] = 15; spikeDelays[3] = 15;
					spikeDelays[0] = 18; spikeDelays[1] = 18;
					spikeDelays[4] = 21; spikeDelays[5] = 21;
					break;

				case 2: // Pattern 2: Hàn Băng Thần Triều Thuận (Tidal Avalanche Wave Left-to-Right Surge)
					spireDelays[5] = 14; // Left Outer
					spireDelays[3] = 16; // Left Mid
					spireDelays[1] = 18; // Left Inner
					spireDelays[0] = 20; // Center
					spireDelays[2] = 22; // Right Inner
					spireDelays[4] = 24; // Right Mid
					spireDelays[6] = 26; // Right Outer
					spikeDelays[2] = 15; spikeDelays[0] = 17; spikeDelays[4] = 19;
					spikeDelays[5] = 21; spikeDelays[1] = 23; spikeDelays[3] = 25;
					break;

				case 3: // Pattern 3: Hàn Băng Thần Triều Nghịch (Tidal Avalanche Wave Right-to-Left Surge)
					spireDelays[6] = 14; // Right Outer
					spireDelays[4] = 16; // Right Mid
					spireDelays[2] = 18; // Right Inner
					spireDelays[0] = 20; // Center
					spireDelays[1] = 22; // Left Inner
					spireDelays[3] = 24; // Left Mid
					spireDelays[5] = 26; // Left Outer
					spikeDelays[3] = 15; spikeDelays[1] = 17; spikeDelays[5] = 19;
					spikeDelays[4] = 21; spikeDelays[0] = 23; spikeDelays[2] = 25;
					break;

				case 4: // Pattern 4: Hàn Băng Tinh Khắc Đan Chéo (Criss-Cross Zigzag Eruption)
					spireDelays[5] = 14; // Left Outer
					spireDelays[6] = 15; // Right Outer
					spireDelays[4] = 17; // Right Mid
					spireDelays[3] = 18; // Left Mid
					spireDelays[1] = 20; // Left Inner
					spireDelays[2] = 21; // Right Inner
					spireDelays[0] = 23; // Center Pinnacle Climax
					spikeDelays[2] = 15; spikeDelays[3] = 16;
					spikeDelays[1] = 18; spikeDelays[0] = 19;
					spikeDelays[4] = 21; spikeDelays[5] = 22;
					break;

				default: // Pattern 5: Băng Tách Hỗn Mang Tự Nhiên (Organic Shuffled Fracture with Natural Micro-Jitter)
					int[] baseDelays = { 14, 15, 17, 18, 20, 22, 23 };
					for (int s = 6; s > 0; s--)
					{
						int r = CRes.random(s + 1);
						int tmp = baseDelays[s];
						baseDelays[s] = baseDelays[r];
						baseDelays[r] = tmp;
					}
					for (int s = 0; s < 7; s++)
					{
						spireDelays[s] = baseDelays[s];
						jitterX[s] = CRes.random_Am_0(5);
						jitterY[s] = CRes.random_Am_0(3);
					}
					for (int k = 0; k < 6; k++)
					{
						spikeDelays[k] = 15 + CRes.random(8);
					}
					break;
			}

			Point p0 = new Point(); p0.x = centerX + jitterX[0]; p0.y = centerY + jitterY[0]; p0.f = 0; p0.fSmall = spireDelays[0]; p0.subType = 0; p0.dis = 135; p0.color = 0; this.VecEff.addElement(p0);
			Point p1 = new Point(); p1.x = centerX - 42 + jitterX[1]; p1.y = centerY - 5 + jitterY[1]; p1.f = 0; p1.fSmall = spireDelays[1]; p1.subType = 0; p1.dis = 110; p1.color = 2; this.VecEff.addElement(p1);
			Point p2 = new Point(); p2.x = centerX + 42 + jitterX[2]; p2.y = centerY - 4 + jitterY[2]; p2.f = 0; p2.fSmall = spireDelays[2]; p2.subType = 0; p2.dis = 110; p2.color = 0; this.VecEff.addElement(p2);
			Point p3 = new Point(); p3.x = centerX - 85 + jitterX[3]; p3.y = centerY + 2 + jitterY[3]; p3.f = 0; p3.fSmall = spireDelays[3]; p3.subType = 0; p3.dis = 88; p3.color = 2; this.VecEff.addElement(p3);
			Point p4 = new Point(); p4.x = centerX + 85 + jitterX[4]; p4.y = centerY + 3 + jitterY[4]; p4.f = 0; p4.fSmall = spireDelays[4]; p4.subType = 0; p4.dis = 88; p4.color = 0; this.VecEff.addElement(p4);
			Point p5 = new Point(); p5.x = centerX - 135 + jitterX[5]; p5.y = centerY + 6 + jitterY[5]; p5.f = 0; p5.fSmall = spireDelays[5]; p5.subType = 0; p5.dis = 68; p5.color = 2; this.VecEff.addElement(p5);
			Point p6 = new Point(); p6.x = centerX + 135 + jitterX[6]; p6.y = centerY + 8 + jitterY[6]; p6.f = 0; p6.fSmall = spireDelays[6]; p6.subType = 0; p6.dis = 68; p6.color = 0; this.VecEff.addElement(p6);

			int[][] subOffsets = new int[][] {
				new int[] {-65, 10},
				new int[] {65, 12},
				new int[] {-110, -8},
				new int[] {110, -6},
				new int[] {-20, 14},
				new int[] {20, 15}
			};
			for (int i = 0; i < subOffsets.Length; i++)
			{
				Point sp = new Point();
				sp.x = centerX + subOffsets[i][0];
				sp.y = centerY + subOffsets[i][1];
				sp.f = 0; sp.fSmall = spikeDelays[i]; sp.subType = 1; sp.dis = 40; sp.color = (i % 2 == 0) ? 0 : 2;
				this.VecEff.addElement(sp);
			}

			// 2. 24 BLIZZARD VORTEX PARTICLES
			for (int b = 0; b < 24; b++)
			{
				Point bp = new Point();
				bp.x = centerX;
				bp.y = centerY;
				bp.frame = b * 15;
				bp.dis = 30 + (b * 5);
				bp.fSmall = b;
				bp.subType = (b % 3 == 0) ? 1 : 0;
				this.VecSubEff.addElement(bp);
			}

			fRemove = 65;
		}
		else if (setId == 5)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (objFireMain != null) ? objFireMain.x : x;
			int casterY = (objFireMain != null) ? objFireMain.y : y;
			int facingSign = (Dir == 2) ? 1 : -1;

			int impactX = toX;
			int impactY = toY;
			if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill t0 = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
				if (t0 != null)
				{
					MainObject target = MainObject.get_Object((int)t0.ID, (sbyte)t0.tem);
					if (target != null)
					{
						impactX = target.x;
						impactY = target.y;
					}
				}
			}
			if (impactX == 0 && impactY == 0)
			{
				impactX = casterX + facingSign * 160;
				impactY = casterY;
			}
			toX = impactX;
			toY = impactY;

			// Hướng mặt Caster về phía mục tiêu và vào pose tụ lực hắc ám
			if (objFireMain != null)
			{
				Dir = (sbyte)((impactX >= casterX) ? 2 : 0);
				objFireMain.type_left_right = Dir;
				objFireMain.Dir = Dir;
				objFireMain.Action = 2; // Windup tụ năng lượng Hắc Ám
				objFireMain.f = 0;
			}

			// Khởi tạo 16 hạt vật chất tối xoay quanh đĩa bồi tụ chân trời sự kiện (this.VecSubEff)
			for (int b = 0; b < 16; b++)
			{
				Point bp = new Point();
				bp.x = impactX;
				bp.y = impactY;
				bp.frame = b * 22;                  // Góc xoay quỹ đạo ban đầu
				bp.dis = 45 + (b * 6);              // Bán kính xoay (45px -> 140px)
				bp.fSmall = b;                      // Seed nhấp nháy
				bp.subType = (b % 3 == 0) ? 1 : 0;  // 0 = điểm sáng tím, 1 = đốm sao hắc ám
				bp.color = (b % 2 == 0) ? 0x9C27B0 : 0x4A148C; // Màu tím hư vô / tím đậm
				this.VecSubEff.addElement(bp);
			}

			// Khởi tạo 6 điểm nứt hư vô mặt đất quanh tâm chấn (this.VecEff)
			int[][] riftOffsets = new int[][] {
				new int[] { 0, 4 }, new int[] { -45, 2 }, new int[] { 45, 3 }, new int[] { -85, 5 }, new int[] { 85, 4 }, new int[] { 0, -10 }
			};
			for (int r = 0; r < riftOffsets.Length; r++)
			{
				Point rp = new Point();
				rp.x = impactX + riftOffsets[r][0];
				rp.y = impactY + riftOffsets[r][1];
				rp.f = 0;
				rp.fSmall = 16 + r * 3; // Delay xuất hiện rạn nứt
				rp.subType = (r % 2 == 0) ? 0 : 2; // Lật hình ngẫu nhiên
				this.VecEff.addElement(rp);
			}

			this.addSound((sbyte)10);
			fRemove = 76;
		}
		else if (setId == 7)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			int centerX = toX;
			int centerY = toY;
			if (centerX == 0 && centerY == 0)
			{
				centerX = x + (Dir == 2 ? 120 : -120);
				centerY = y - 20;
			}

			// 1. SCREEN-SPACE SPATIAL SHATTER NODES (subType = 2: Pinned to viewport screen percentage)
			// Center Screen Master Fracture
			Point sc0 = new Point(); sc0.x = 50; sc0.y = 45; sc0.f = 0; sc0.fSmall = 2; sc0.subType = 2; sc0.color = 1; this.VecEff.addElement(sc0);
			// Staggered screen-space fractures across the viewport
			Point sc1 = new Point(); sc1.x = 24; sc1.y = 28; sc1.f = 0; sc1.fSmall = 8; sc1.subType = 2; sc1.color = 0; this.VecEff.addElement(sc1);
			Point sc2 = new Point(); sc2.x = 76; sc2.y = 25; sc2.f = 0; sc2.fSmall = 12; sc2.subType = 2; sc2.color = 1; this.VecEff.addElement(sc2);
			Point sc3 = new Point(); sc3.x = 18; sc3.y = 72; sc3.f = 0; sc3.fSmall = 18; sc3.subType = 2; sc3.color = 1; this.VecEff.addElement(sc3);
			Point sc4 = new Point(); sc4.x = 82; sc4.y = 74; sc4.f = 0; sc4.fSmall = 22; sc4.subType = 2; sc4.color = 0; this.VecEff.addElement(sc4);

			// 2. WIDELY SPACED & ORGANIC RANDOMIZED GROUND SEISMIC RUPTURE NODES (subType = 0 / 1)
			// Guaranteed non-overlapping spread across wide battlefield range (-220px to +220px)
			int[][] seismicOffsets = new int[][] {
				new int[] { 0, 5, 4, 1 },
				new int[] { -55, -8, 8, 0 },
				new int[] { 60, -12, 10, 1 },
				new int[] { -115, 12, 16, 1 },
				new int[] { 125, 8, 20, 0 },
				new int[] { -175, -5, 26, 0 },
				new int[] { 185, -10, 30, 1 },
				new int[] { -225, 10, 36, 1 },
				new int[] { 230, 14, 40, 0 }
			};

			for (int i = 0; i < seismicOffsets.Length; i++)
			{
				Point p = new Point();
				p.x = centerX + seismicOffsets[i][0] + CRes.random_Am_0(12);
				p.y = centerY + 15 + seismicOffsets[i][1] + CRes.random_Am_0(6);
				p.f = 0;
				p.fSmall = seismicOffsets[i][2];
				p.subType = seismicOffsets[i][3];
				p.color = (i % 2);
				this.VecEff.addElement(p);
			}

			// 3. TARGET-ANCHORED SHATTER NODES (Only for valid living targets, spaced out)
			if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (tInfo != null)
					{
						MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (tObj != null && !tObj.isDie && tObj.Hp > 0 && !tObj.isRemove)
						{
							Point tn = new Point();
							tn.x = tObj.x;
							tn.y = tObj.y - (tObj.hOne / 2);
							tn.f = 0;
							tn.fSmall = 12 + k * 5;
							tn.subType = (k % 2);
							tn.color = 1;
							this.VecEff.addElement(tn);
						}
					}
				}
			}
			fRemove = Math.Max(maxTravelFrame + 75, 85);
		}
		else if (setId == 6)
		{
			// Enel 200M Volt El Thor — Full Procedural Lightning Matrix & Random Aerial Teleport
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (objFireMain != null) ? objFireMain.x : x;
			int casterY = (objFireMain != null) ? objFireMain.y : y;
			int facingSign = (Dir == 2) ? 1 : -1;

			this.enelOrigX = casterX;
			this.enelOrigY = casterY;
			if (objFireMain != null && objFireMain == GameScreen.player)
			{
				Player.isBlock = true;
			}

			// Epicenter determination
			int impactX = toX;
			int impactY = toY;
			int nTargets = (vecObjsBeFire != null) ? vecObjsBeFire.size() : 0;
			if (nTargets > 0)
			{
				int sumX = 0;
				int sumY = 0;
				int validCount = 0;
				for (int k = 0; k < nTargets; k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (tInfo != null)
					{
						MainObject t = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (t != null && !t.isDie && t.Hp > 0)
						{
							sumX += t.x;
							sumY += t.y;
							validCount++;
						}
					}
				}
				if (validCount > 0)
				{
					impactX = sumX / validCount;
					impactY = sumY / validCount;
				}
			}
			if (impactX == 0 && impactY == 0)
			{
				impactX = casterX + facingSign * 140;
				impactY = casterY;
			}
			toX = impactX;
			toY = impactY;

			// ─── 1. MATRIX NODES GENERATION (Stored in this.VecEff) ───
			// Node 0: Epicenter Master Node (subType = 1: Colossal 200M Volt El Thor Beam, strike at f = 36)
			Point nodeCenter = new Point();
			nodeCenter.x = impactX;
			nodeCenter.y = impactY;
			nodeCenter.f = 0;
			nodeCenter.fSmall = 36;
			nodeCenter.subType = 1;
			nodeCenter.color = 0;
			nodeCenter.dis = 120;
			this.VecEff.addElement(nodeCenter);

			// Nodes 1..N: Locked Target Nodes (subType = 0: Cascading Sky Bolts)
			for (int i = 0; i < nTargets; i++)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (tInfo != null)
				{
					MainObject t = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
					if (t != null && !t.isDie)
					{
						Point pTarget = new Point();
						pTarget.x = t.x;
						pTarget.y = t.y;
						pTarget.f = 0;
						pTarget.fSmall = 16 + i * 4; // Staggered: 16, 20, 24, 28, 32
						pTarget.subType = 0;
						pTarget.color = (i % 2 == 0 ? 0 : 2);
						pTarget.dis = 30;
						this.VecEff.addElement(pTarget);
					}
				}
			}

			// Nodes: Inner Geometric Matrix Ring (6 Nodes at r = 65px, staggered strikes)
			int rInner = 65;
			for (int a = 0; a < 6; a++)
			{
				int angle = a * 60 + 15;
				Point pInner = new Point();
				pInner.x = impactX + (rInner * CRes.getcos(angle) >> 10);
				pInner.y = impactY + ((rInner * 3 / 5) * CRes.getsin(angle) >> 10);
				pInner.f = 0;
				pInner.fSmall = 14 + (a * 3); // 14, 17, 20, 23, 26, 29
				pInner.subType = 0;
				pInner.color = (a % 2 == 0 ? 0 : 2);
				pInner.dis = 45;
				this.VecEff.addElement(pInner);
			}

			// Nodes: Outer Perimeter Grid Nodes (6 Nodes at r = 150px)
			int rOuter = 150;
			for (int b = 0; b < 6; b++)
			{
				int angle = b * 60 + 45;
				Point pOuter = new Point();
				int perturbR = rOuter + CRes.random_Am_0(15);
				pOuter.x = impactX + (perturbR * CRes.getcos(angle) >> 10);
				pOuter.y = impactY + ((perturbR * 3 / 5) * CRes.getsin(angle) >> 10);
				pOuter.f = 0;
				pOuter.fSmall = 22 + (b * 4); // 22, 26, 30, 34, 38, 42
				pOuter.subType = (b % 2 == 0 ? 0 : 2);
				pOuter.color = (b % 2 == 0 ? 2 : 0);
				pOuter.dis = 60;
				this.VecEff.addElement(pOuter);
			}

			// Nodes: Residual Overdrive Aftershock Nodes (Landing at f = 46, 50, 54, 58)
			int[][] aftershockOffsets = new int[][] {
				new int[] { -55, -8, 46, 2, 0 },
				new int[] { 60, -10, 50, 2, 2 },
				new int[] { -85, 12, 54, 2, 0 },
				new int[] { 90, 8, 58, 2, 2 }
			};
			for (int s = 0; s < aftershockOffsets.Length; s++)
			{
				Point pAfter = new Point();
				pAfter.x = impactX + aftershockOffsets[s][0];
				pAfter.y = impactY + aftershockOffsets[s][1];
				pAfter.f = 0;
				pAfter.fSmall = aftershockOffsets[s][2];
				pAfter.subType = aftershockOffsets[s][3];
				pAfter.color = aftershockOffsets[s][4];
				this.VecEff.addElement(pAfter);
			}

			// ─── 2. MATRIX INTERCONNECTION EDGES / LIGHTNING ARCS (Stored in this.VecSubEff) ───
			// Interconnect nodes to draw the glowing celestial lightning web / matrix lines!
			int totalNodes = this.VecEff.size();
			if (totalNodes >= 7)
			{
				// A. Center-to-Inner Spokes (Connecting Node 0 to Inner Ring Nodes)
				for (int i = 1; i <= 6 && i < totalNodes; i++)
				{
					Point targetNode = (Point)this.VecEff.elementAt(i);
					Point arc = new Point();
					arc.x = nodeCenter.x;
					arc.y = nodeCenter.y - 10;
					arc.x2 = targetNode.x;
					arc.y2 = targetNode.y;
					arc.fSmall = 10 + i * 2; // Active from f=12..38
					arc.fRe = 28; // Duration
					arc.subType = 0; // 0 = standard lightning arc
					arc.frame = i * 7; // jitter seed
					arc.color = (i % 2 == 0 ? 0x00B0FF : 0x33B5E5);
					this.VecSubEff.addElement(arc);
				}

				// B. Inner Ring Perimeter Polygon (Connecting adjacent inner nodes)
				for (int i = 1; i <= 6 && i < totalNodes; i++)
				{
					int nextIdx = (i == 6) ? 1 : (i + 1);
					Point n1 = (Point)this.VecEff.elementAt(i);
					Point n2 = (Point)this.VecEff.elementAt(nextIdx);
					Point arc = new Point();
					arc.x = n1.x;
					arc.y = n1.y;
					arc.x2 = n2.x;
					arc.y2 = n2.y;
					arc.fSmall = 12 + i * 2;
					arc.fRe = 26;
					arc.subType = 0;
					arc.frame = i * 11;
					arc.color = 0x80D8FF;
					this.VecSubEff.addElement(arc);
				}

				// C. Outer Ring Polygon & Spokes to Inner Ring
				for (int i = 7; i <= 12 && i < totalNodes; i++)
				{
					Point nOuter = (Point)this.VecEff.elementAt(i);
					int innerIdx = 1 + (i - 7) % 6;
					Point nInner = (Point)this.VecEff.elementAt(innerIdx);
					// Ray connecting inner to outer
					Point arcRay = new Point();
					arcRay.x = nInner.x;
					arcRay.y = nInner.y;
					arcRay.x2 = nOuter.x;
					arcRay.y2 = nOuter.y;
					arcRay.fSmall = 16 + (i - 7) * 3;
					arcRay.fRe = 24;
					arcRay.subType = 0;
					arcRay.frame = i * 13;
					arcRay.color = 0x00E5FF;
					this.VecSubEff.addElement(arcRay);

					// Outer loop edge
					int nextOuter = (i == 12) ? 7 : (i + 1);
					if (nextOuter < totalNodes)
					{
						Point nOuterNext = (Point)this.VecEff.elementAt(nextOuter);
						Point arcOuter = new Point();
						arcOuter.x = nOuter.x;
						arcOuter.y = nOuter.y;
						arcOuter.x2 = nOuterNext.x;
						arcOuter.y2 = nOuterNext.y;
						arcOuter.fSmall = 18 + (i - 7) * 2;
						arcOuter.fRe = 22;
						arcOuter.subType = 0;
						arcOuter.frame = i * 17;
						arcOuter.color = 0xB1EDFC;
						this.VecSubEff.addElement(arcOuter);
					}
				}

				// D. Sky-to-Ground Feeder Arcs (From high altitude stormcloud down to ground nodes)
				for (int sky = 0; sky < 5; sky++)
				{
					Point arcSky = new Point();
					arcSky.x = impactX - 120 + sky * 60 + CRes.random_Am_0(15);
					arcSky.y = impactY - 260; // High in the heavens
					int groundTargetIdx = (sky * 2) % totalNodes;
					Point groundNode = (Point)this.VecEff.elementAt(groundTargetIdx);
					arcSky.x2 = groundNode.x;
					arcSky.y2 = groundNode.y;
					arcSky.fSmall = 14 + sky * 4;
					arcSky.fRe = 26;
					arcSky.subType = 1; // 1 = Vertical Sky Lightning Feeder
					arcSky.frame = sky * 19;
					arcSky.color = 0xFFFFFF;
					this.VecSubEff.addElement(arcSky);
				}
			}

			fRemove = 78;
		}
		else if (setId == 8)
		{
			if (this.VecEff != null) this.VecEff.removeAllElements();
			if (this.VecSubEff != null) this.VecSubEff.removeAllElements();
			fRemove = 74;
		}
		else if (setId == 12)
		{
			// 4012 Phượng Hoàng Bất Tử Bộc Phá _X (Marco Blue Phoenix Climax)
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (objFireMain != null) ? objFireMain.x : x;
			int casterY = (objFireMain != null) ? objFireMain.y : y;
			this.phoenixOrigX = casterX;
			this.phoenixOrigY = casterY;

			int facingSign = (Dir == 2) ? 1 : -1;
			int targetX = toX;
			int targetY = toY;
			if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
				if (tInfo != null)
				{
					MainObject t0 = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
					if (t0 != null && !t0.isDie && t0.Hp > 0 && !t0.isRemove)
					{
						targetX = t0.x;
						targetY = t0.y - (t0.hOne / 2);
					}
				}
			}
			if (targetX == 0 && targetY == 0)
			{
				targetX = casterX + facingSign * 160;
				targetY = casterY - 15;
			}

			// Clamp distance to range = 220
			int dist = Math.Abs(targetX - casterX);
			if (dist > 220)
			{
				targetX = casterX + facingSign * 220;
			}
			this.phoenixTargetX = targetX;
			this.phoenixTargetY = targetY;
			this.marcoWaveHitMask = 0;

			// Movement Lock: Không cho player di chuyển khi đang tung tuyệt chiêu
			if (objFireMain != null && objFireMain == GameScreen.player)
			{
				Player.isBlock = true;
				GameScreen.player.Action = 0;
				GameScreen.player.toX = GameScreen.player.x;
				GameScreen.player.toY = GameScreen.player.y;
			}

			// Sinh 16 hạt lông vũ linh thiêng & tàn lam hỏa (Sacred Feathers & Blue Embers)
			for (int i = 0; i < 16; i++)
			{
				Point p = new Point();
				p.x = casterX + CRes.random_Am_0(25);
				p.y = casterY - 10 + CRes.random_Am_0(20);
				p.vx = CRes.random_Am_0(3);
				p.vy = -(2 + CRes.random(4));
				p.f = 0;
				p.fRe = 20 + CRes.random(15);
				p.subType = (i % 2); // 0: feather (p12[9]), 1: spark (p12[8])
				p.color = (i % 2 == 0) ? 0 : 2;
				this.VecSubEff.addElement(p);
			}

			this.addSound(10);
			LoadMap.timeVibrateScreen = 6;
			fRemove = 68;
		}
		else if (setId == 13)
		{
			// 4013 Đại Phật Sóng Xung Kích _X (Sengoku Daibutsu Golden Shockwave) - 44-tick 5-phase cinematic animation
			if (this.VecEff != null) this.VecEff.removeAllElements();
			if (this.VecSubEff != null) this.VecSubEff.removeAllElements();

			int casterX = (objFireMain != null) ? objFireMain.x : x;
			int casterY = (objFireMain != null) ? objFireMain.y : y;
			this.daibutsuCasterX = casterX;
			this.daibutsuCasterY = casterY;

			int facingSign = (Dir == 2) ? 1 : -1;
			int targetX = toX;
			int targetY = toY;
			if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
				if (tInfo != null)
				{
					MainObject t0 = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
					if (t0 != null && !t0.isDie && t0.Hp > 0 && !t0.isRemove)
					{
						targetX = t0.x;
						targetY = t0.y - (t0.hOne / 2);
					}
				}
			}
			if (targetX == 0 && targetY == 0)
			{
				targetX = casterX + facingSign * 180;
				targetY = casterY - 15;
			}
			// Clamp to max skill range = 220
			int dist = Math.Abs(targetX - casterX);
			if (dist > 220)
			{
				targetX = casterX + facingSign * 220;
			}
			this.daibutsuImpactX = targetX;
			this.daibutsuImpactY = targetY;

			fRemove = 44;
		}
		else if (setId == 9)
		{
			// 4009 Eustass Kid: Từ Trường Bộc Phá Đại Pháo _X (Damned Punk Railgun)
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();
			if (this.VecSubEff == null) this.VecSubEff = new mVector();
			this.VecSubEff.removeAllElements();

			int casterX = (objFireMain != null) ? objFireMain.x : x;
			int casterY = (objFireMain != null) ? objFireMain.y : y;
			int facingSign = (Dir == 2) ? 1 : -1;

			// Root fixed casting coordinates
			x = casterX;
			y = casterY;
			if (objFireMain != null)
			{
				objFireMain.Action = 2; // Đổi pose gồng nhẹ khi bắt đầu tụ lực
				objFireMain.f = 0;
			}
			fRemove = 48;

			// Lock target coordinates (tối đa range = 220)
			int targetX = toX;
			int targetY = toY;
			if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
				if (tInfo != null)
				{
					MainObject t0 = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
					if (t0 != null && !t0.isDie && t0.Hp > 0 && !t0.isRemove)
					{
						targetX = t0.x;
						targetY = t0.y - (t0.hOne / 2);
					}
				}
			}
			if (targetX == 0 && targetY == 0)
			{
				targetX = casterX + facingSign * 180;
				targetY = casterY - 15;
			}
			// Clamp range to 220px theo chuẩn skill data
			int dist = Math.Abs(targetX - casterX);
			if (dist > 220)
			{
				targetX = casterX + facingSign * 220;
			}
			this.kidCannonImpactX = targetX;
			this.kidCannonImpactY = targetY;

			// Master Pool of 48 authentic metallic weapon icons in HTTH
			int[] baseWeaponPool = {
				// Swords & Sabers (11)
				2, 26, 50, 74, 98, 122, 146, 170, 194, 218, 257,
				// Daggers, Cleavers & Great Blades (11)
				3, 27, 51, 75, 99, 123, 147, 171, 195, 219, 252,
				// Staves, Spears, Tridents & Polearms (11)
				4, 28, 52, 76, 100, 124, 148, 172, 196, 220, 256,
				// Pistols, Rifles, Flintlocks & Cannons (11)
				5, 29, 53, 77, 101, 125, 149, 173, 197, 221, 269,
				// Iron Claws & Spiked Gauntlets (4)
				49, 169, 193, 255
			};

			// Shuffle using Fisher-Yates to guarantee 100% unique weapons on every cast (NO DUPLICATES)
			int[] shuffledPool = new int[baseWeaponPool.Length];
			Array.Copy(baseWeaponPool, 0, shuffledPool, 0, baseWeaponPool.Length);
			for (int s = shuffledPool.Length - 1; s > 0; s--)
			{
				int r = CRes.random(s + 1);
				int tmp = shuffledPool[s];
				shuffledPool[s] = shuffledPool[r];
				shuffledPool[r] = tmp;
			}

			// Scan nearby mobs & players within 220px to pull weapons from
			mVector nearbyEntities = new mVector();
			if (GameScreen.vecPlayers != null)
			{
				for (int k = 0; k < GameScreen.vecPlayers.size(); k++)
				{
					MainObject obj = (MainObject)GameScreen.vecPlayers.elementAt(k);
					if (obj != null && obj != objFireMain && !obj.isDie && obj.Hp > 0 && !obj.isRemove)
					{
						int d = MainObject.getDistance(casterX, casterY, obj.x, obj.y);
						if (d <= 220)
						{
							nearbyEntities.addElement(obj);
						}
					}
				}
			}

			int totalWeapons = 28;
			for (int i = 0; i < totalWeapons; i++)
			{
				Point p = new Point();
				p.subType = shuffledPool[i];

				if (i < nearbyEntities.size())
				{
					MainObject ent = (MainObject)nearbyEntities.elementAt(i);
					p.AZ = ent;
					p.x2 = ent.x + CRes.random_Am_0(10);
					p.y2 = ent.y - (ent.hOne / 2) + CRes.random_Am_0(8);
				}
				else
				{
					p.AZ = null;
					int pullAngle = (i * (360 / totalWeapons) + CRes.random_Am_0(16) + 360) % 360;
					int pullDist = 140 + CRes.random(0, 100);
					p.x2 = casterX + (pullDist * CRes.getcos(pullAngle)) / 1000;
					p.y2 = casterY + (pullDist * CRes.getsin(pullAngle)) / 1000;
				}

				p.x = p.x2;
				p.y = p.y2;

				// Relative offsets in cannon assembly formation (Twin-Rail Damned Punk Great Cannon)
				// 0..7: Upper Rail, 8..15: Lower Rail, 16..21: Breech & Coils, 22..27: Muzzle Converters
				int rx = 0;
				int ry = 0;
				int depth = 0;
				if (i < 8)
				{
					// Upper Magnetic Rail
					rx = facingSign * (-10 + i * 7);
					ry = -14 + (i % 2) * 2;
					depth = 1;
				}
				else if (i < 16)
				{
					// Lower Magnetic Rail
					int k = i - 8;
					rx = facingSign * (-10 + k * 7);
					ry = 14 - (k % 2) * 2;
					depth = 0;
				}
				else if (i < 22)
				{
					// Breech & Magnetic Accelerator Coils
					int k = i - 16;
					rx = facingSign * (-22 + (k % 3) * 8);
					ry = -8 + (k / 3) * 16;
					depth = (k % 2 == 0) ? 0 : 1;
				}
				else
				{
					// Muzzle Converters & Rail Stabilizers
					int k = i - 22;
					rx = facingSign * (36 + (k % 2) * 8);
					ry = -10 + k * 4;
					depth = 1;
				}

				p.AK = rx;
				p.AL = ry;
				p.color = depth; // 0 = back layer, 1 = front layer
				p.frame = CRes.random(8);

				p.fSmall = CRes.random(0, 4); // Takeoff delay
				p.fRe = 10 + CRes.random(0, 4); // Flight duration: lands at cannon by frame 10..14

				p.f = (i + CRes.random(4)) % 4; // Flight style
				p.dis = 35 + CRes.random(0, 35); // Curvature amplitude

				// Dispersal explosion velocities (for Phase 5)
				int disperseAngle = (i * (360 / totalWeapons) + CRes.random_Am_0(20) + 360) % 360;
				int disperseSpeed = 16 + CRes.random(0, 16);
				p.vx = (disperseSpeed * CRes.getcos(disperseAngle)) / 1000;
				p.vy = (disperseSpeed * CRes.getsin(disperseAngle)) / 1000 - 4;

				this.VecEff.addElement(p);
			}

			this.addSound((sbyte)18);
		}
		else if (setId == 10)
		{
			if (this.VecEff == null) this.VecEff = new mVector();
			this.VecEff.removeAllElements();

			// 1. Ưu tiên tuyệt đối tìm mục tiêu chuẩn:
			MainObject target = null;
			if (GameScreen.objFocus != null && !GameScreen.objFocus.isDie && !GameScreen.objFocus.isRemove && GameScreen.objFocus != objFireMain)
			{
				target = GameScreen.objFocus;
			}
			else if (this.objBeFireMain != null && this.objBeFireMain != objFireMain && !this.objBeFireMain.isDie && !this.objBeFireMain.isRemove)
			{
				target = this.objBeFireMain;
			}
			else if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill t0 = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (t0 != null)
					{
						MainObject mob = MainObject.get_Object((int)t0.ID, (sbyte)t0.tem);
						if (mob != null && !mob.isDie && !mob.isRemove && mob != objFireMain)
						{
							target = mob;
							break;
						}
					}
				}
			}

			int centerX = 0;
			int centerY = 0;
			if (target != null)
			{
				this.objBeFireMain = target;
				centerX = target.x;
				centerY = target.y;
				toX = centerX;
				toY = centerY;
				if (objFireMain != null)
				{
					Dir = (sbyte)((target.x >= objFireMain.x) ? 2 : 0);
					objFireMain.type_left_right = Dir;
					objFireMain.Dir = Dir;
				}
			}
			else if (toX != 0 || toY != 0)
			{
				centerX = toX;
				centerY = toY;
			}
			else
			{
				centerX = x + (Dir == 2 ? 140 : -140);
				centerY = y;
				toX = centerX;
				toY = centerY;
			}

			Point targetCenter = new Point();
			targetCenter.x = centerX;
			targetCenter.y = centerY;
			targetCenter.obj = target;
			this.VecEff.addElement(targetCenter);
			fRemove = 72;

			if (levelPaint >= 0)
			{
				Effect_Skill groundPool = new Effect_Skill();
				groundPool.typeEffect = typeEff;
				groundPool.levelPaint = -1; // ONTOP = 0: DƯỚI MỤC TIÊU & NHÂN VẬT
				groundPool.x = centerX;
				groundPool.y = centerY;
				groundPool.toX = centerX;
				groundPool.toY = centerY;
				groundPool.Dir = Dir;
				groundPool.objFireMain = objFireMain;
				groundPool.objBeFireMain = target;
				groundPool.vecObjsBeFire = vecObjsBeFire;
				groundPool.fRemove = 72;
				groundPool.f = 0;
				groundPool.subType = 1004;
				if (groundPool.VecEff == null) groundPool.VecEff = new mVector();
				groundPool.VecEff.addElement(new Point(centerX, centerY));
				GameScreen.VecEffect.addElement(groundPool);
			}
		}
		else
		{
			fRemove = maxTravelFrame + 25;
		}
	}

	private void updateThanTrangSkill(int typeEff)
	{
		int setId = (typeEff == 4017 || typeEff == 4010) ? 10 : ((typeEff >= 4001 && typeEff <= 4016) ? (typeEff - 4000) : (((typeEff - 4001) / 5) + 1));
		if (setId < 1 || setId > 16) setId = 1;

		// Continuous Volcanic Screen Heat Vibration & Sound for Set 2 (Dung Nham - Volcano)
		if (setId == 2 && f <= fRemove)
		{
			if (f == 2)
			{
				this.addSound(5);
				LoadMap.timeVibrateScreen = 6;
			}
			else if (f == 12)
			{
				this.addSound(5);
			}
			else if (f == 24)
			{
				// VA CHẠM CỰC ĐẠI - IMPACT & VOLCANIC EXPLOSION!
				this.addSound(51);
				this.addSound(18);
				this.addSound(14);
				LoadMap.timeVibrateScreen = 28;

				// 1 HIT DUY NHẤT (nKick = 1): Kích ứng quái trúng đòn giật lùi & chớp trắng
				if (vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size() && k < 5; k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (targetInfo != null)
						{
							MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
							if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
							{
								this.setAva(2, target);
								target.dy = -8;
							}
						}
					}
				}
			}
			else if (f == 36)
			{
				this.addSound(51);
				LoadMap.timeVibrateScreen = 10;
			}
		}

		// Cập nhật vị trí hạt tàn lửa nham thạch (VecSubEff) sau va chạm (f >= 24)
		if (setId == 2)
		{
			if (f >= 24 && this.VecSubEff != null)
			{
				int impactY = toY;
				for (int k = 0; k < this.VecSubEff.size(); k++)
				{
					Point p = (Point)this.VecSubEff.elementAt(k);
					if (p != null)
					{
						p.f++;
						if (p.f < 20)
						{
							p.x += p.vx;
							p.y += p.vy;
							p.vy++;
							if (p.y > impactY + 12)
							{
								p.y = impactY + 12;
								p.vx = 0;
								p.vy = 0;
							}
						}
					}
				}
			}

			// Clean finish
			if (f >= fRemove)
			{
				if (vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (targetInfo != null)
						{
							MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
							if (target != null) target.dy = 0;
						}
					}
				}
			}
		}

		// ─── setId 3: Kỷ Băng Hà Tuyệt Đối X (Aokiji Ice Age) ───
		if (setId == 3)
		{
			// 1. Screen Freezing Audio & Glacial Creak Milestones
			if (f == 4)
			{
				this.addSound(2);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 14;
			}
			else if (f == 14)
			{
				this.addSound(14);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 28;
			}
			else if (f == 28)
			{
				this.addSound(2);
				this.addSound(18);
				LoadMap.timeVibrateScreen = 16;
			}
			else if (f == 42)
			{
				this.addSound(10);
				LoadMap.timeVibrateScreen = 10;
			}

			// 2. Target Freezing & Sub-Zero Damage Pulses (Hit at f = 14, 28, 42)
			if (f == 14 || f == 28 || f == 42)
			{
				if (vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (targetInfo == null) continue;
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;

						this.setAva(2, target);
						target.dy = 0;
						// Pure sub-zero ice burst at target feet (Effect 35: Jagged Ice 89)
						GameScreen.addEffectEnd((short)35, 0, target.x, target.y, (sbyte)0, null);
					}
				}
			}

			// 3. 24 Blizzard Snow Crystal Vortex Orbiting Simulation (this.VecSubEff)
			if (this.VecSubEff != null)
			{
				for (int i = 0; i < this.VecSubEff.size(); i++)
				{
					Point bp = (Point)this.VecSubEff.elementAt(i);
					if (bp != null)
					{
						bp.frame = (bp.frame + 12) % 360;
						if (f < 14)
						{
							bp.dis = Math.Min(135, bp.dis + 7);
						}
						else if (f > 45)
						{
							bp.dis = Math.Max(0, bp.dis - 5);
						}
					}
				}
			}

			// 4. Glacial Pillars & Icicles Eruption Age Advance (this.VecEff)
			if (this.VecEff != null)
			{
				for (int i = 0; i < this.VecEff.size(); i++)
				{
					Point sp = (Point)this.VecEff.elementAt(i);
					if (sp != null)
					{
						sp.f++;
					}
				}
			}

			// 5. Clean up targets on end
			if (f >= fRemove && vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (targetInfo == null) continue;
					MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
					if (target != null) target.dy = 0;
				}
			}
		}

		// ─── setId 5: Hắc Ám Thôn Phệ Vô Tận _X (Marshall D. Teach Black Hole Abyss Singularity) ───
		if (setId == 5)
		{
			int impactX = toX;
			int impactY = toY;

			// TUYỆT ĐỐI KHÔNG LÀM TỐI MAP VÀ TRỜI (Bản đồ và bầu trời giữ nguyên 100% tự nhiên)
			GameCanvas.gameScr.isFullScreen = false;

			// 1. Audio Milestones & Dynamic Screen Shake (chuẩn nhịp chiến đấu hoành tráng)
			if (f == 2)
			{
				this.addSound((sbyte)10); // Tiếng rền trầm hư vô tích tụ
				LoadMap.timeVibrateScreen = 4;
			}
			else if (f == 14)
			{
				// Caster xuất chiêu phóng hắc tinh cầu xé toạc không gian
				this.addSound((sbyte)4);
				this.addSound((sbyte)51);
				LoadMap.timeVibrateScreen = 8;
				if (objFireMain != null)
				{
					objFireMain.Action = 2;
					objFireMain.f = 0; // Đổi thế xuất chiêu oanh tạc
				}
			}
			else if (f == 22)
			{
				// Hố đen khai mở gầm vang
				this.addSound((sbyte)10);
				this.addSound((sbyte)51);
				LoadMap.timeVibrateScreen = 14;
			}
			else if (f == 34 || f == 42)
			{
				// Nhịp co thắt trọng lực
				this.addSound((sbyte)10);
				LoadMap.timeVibrateScreen = 6;
			}
			else if (f == 50)
			{
				// Vụ Nổ Đại Hư Vô Sụp Đổ Cực Đại & Giải Phóng
				this.addSound((sbyte)14);
				this.addSound((sbyte)51);
				this.addSound((sbyte)18);
				LoadMap.timeVibrateScreen = 28;
			}

			// 2. Caster Action Transition:
			// Sau khi giải phóng hắc tinh cầu (f >= 28), Caster tự do di chuyển chiến đấu bình thường
			if (objFireMain != null)
			{
				if (f == 28)
				{
					objFireMain.Action = 0;
					if (objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
					}
				}
			}

			// 3. Phản Ứng Chiến Đấu Của Mục Tiêu (Hiện mục tiêu bình thường, dính đòn rung giật tự nhiên)
			if (vecObjsBeFire != null)
			{
				// Các nhịp hút chấn thương (f = 22, 30, 38, 46)
				if (f == 22 || f == 30 || f == 38 || f == 46)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (targetInfo == null) continue;
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
						this.setAva(2, target);
						target.dy = -4; // Nhịp giật sát thương nhẹ
					}
				}
				// Cú nổ sụp đổ hố đen cực đại tại f = 50: Giật lùi mạnh
				else if (f == 50)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (targetInfo == null) continue;
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
						this.setAva(2, target);
						target.dy = -10; // Hất tung nhẹ khỏi mặt đất khi hố đen phát nổ
					}
				}
				// Hồi phục tiếp đất mượt mà sau khi bị hất tung
				else if (f > 50 && f <= 58)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (targetInfo == null) continue;
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
						if (target != null && target.dy < 0)
						{
							target.dy += 2;
						}
					}
				}
			}

			// 4. Cập nhật 16 Hạt Vật Chất Tối Xoay Xoắn Ốc Quỹ Đạo Hố Đen (this.VecSubEff)
			if (this.VecSubEff != null)
			{
				for (int i = 0; i < this.VecSubEff.size(); i++)
				{
					Point bp = (Point)this.VecSubEff.elementAt(i);
					if (bp != null)
					{
						int rotSpd = (f < 22) ? 8 : ((f < 48) ? 16 : 24);
						bp.frame = (bp.frame + rotSpd) % 360;
						if (f >= 20 && f <= 48)
						{
							// Xoáy logarithmic thu dần bán kính vào tâm hố đen
							if (bp.dis > 15) bp.dis -= 1;
						}
						else if (f > 50)
						{
							// Bắn tung tỏa ra ngoài khi hố đen sụp đổ
							bp.dis += 8;
						}
					}
				}
			}

			// 5. Cập nhật tiến trình rạn nứt mặt đất (this.VecEff)
			if (this.VecEff != null)
			{
				for (int i = 0; i < this.VecEff.size(); i++)
				{
					Point rp = (Point)this.VecEff.elementAt(i);
					if (rp != null)
					{
						rp.f++;
					}
				}
			}

			// 6. Hoàn tất chiêu & Giải phóng an toàn
			if (f >= fRemove)
			{
				GameCanvas.gameScr.isFullScreen = false;
				if (objFireMain != null && objFireMain == GameScreen.player)
				{
					Player.isBlock = false;
				}
				if (vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (target != null)
						{
							target.dy = 0;
							target.Action = 0;
							if (target == GameScreen.player)
							{
								Player.isBlock = false;
							}
						}
					}
				}
			}
		}

		// ─── setId 6: 200 Triệu Volt Thần Lôi (Enel El Thor) — Full Random Aerial Teleport, Flight & Matrix Update ───
		if (setId == 6)
		{
			int casterOrigX = this.enelOrigX;
			int casterOrigY = this.enelOrigY;
			int impactX = toX;
			int impactY = toY;
			int facingSign = (Dir == 2) ? 1 : -1;

			// 1. RANDOM AERIAL TELEPORT & LEVITATION FLIGHT CHOREOGRAPHY
			if (objFireMain != null)
			{
				if (f < 6)
				{
					// Ground charging stage
					objFireMain.dy = 0;
					objFireMain.isTanHinh = false;
				}
				else if (f == 6)
				{
					// ─── TELEPORT 1: Ground to Sky Ascent ───
					GameScreen.addEffectEnd(40, 0, objFireMain.x, objFireMain.y, (sbyte)0, null);
					GameScreen.addEffectEnd(42, 0, objFireMain.x, objFireMain.y, (sbyte)0, null);
					this.addSound(18);
					objFireMain.x = casterOrigX + facingSign * 50;
					objFireMain.dy = -50;
				}
				else if (f > 6 && f < 19)
				{
					// Aerial hovering 1 with electric tremor
					int tremor = (f % 2 == 0) ? -1 : 1;
					objFireMain.dy = -50 + tremor;
				}
				else if (f == 19)
				{
					// ─── TELEPORT 2: Aerial Lightning Blitz Dash ───
					GameScreen.addEffectEnd(40, 0, objFireMain.x, objFireMain.y + objFireMain.dy, (sbyte)0, null);
					GameScreen.addEffectEnd(42, 0, objFireMain.x, objFireMain.y + objFireMain.dy, (sbyte)0, null);
					this.addSound(18);
					// Teleport to random aerial vantage over targets
					objFireMain.x = impactX - facingSign * 55 + CRes.random_Am_0(25);
					objFireMain.dy = -60;
				}
				else if (f > 19 && f < 32)
				{
					// Aerial hovering 2 with matrix invocation
					int tremor = (f % 2 == 0) ? -1 : 1;
					objFireMain.dy = -60 + tremor;
				}
				else if (f == 32)
				{
					// ─── TELEPORT 3: Zenith Climax Apex (Over the epicenter) ───
					GameScreen.addEffectEnd(40, 0, objFireMain.x, objFireMain.y + objFireMain.dy, (sbyte)0, null);
					GameScreen.addEffectEnd(42, 0, objFireMain.x, objFireMain.y + objFireMain.dy, (sbyte)0, null);
					this.addSound(51);
					objFireMain.x = impactX;
					objFireMain.dy = -75;
				}
				else if (f > 32 && f < 48)
				{
					// Zenith levitation unleashing the 200M volt beam
					int tremor = (f % 2 == 0) ? -2 : 2;
					objFireMain.dy = -75 + tremor;
				}
				else if (f == 48)
				{
					// ─── TELEPORT 4: Flank Descending Overdrive ───
					GameScreen.addEffectEnd(40, 0, objFireMain.x, objFireMain.y + objFireMain.dy, (sbyte)0, null);
					objFireMain.x = impactX + facingSign * 70;
					objFireMain.dy = -38;
				}
				else if (f > 48 && f < 68)
				{
					// Gliding down smoothly
					int tDown = f - 48; // 0..20
					objFireMain.dy = -38 + (38 * tDown / 20);
				}
				else if (f >= 68)
				{
					// ─── TELEPORT 5: Return to Origin (Perfect Safety) ───
					objFireMain.x = casterOrigX;
					objFireMain.y = casterOrigY;
					objFireMain.dy = 0;
					objFireMain.isTanHinh = false;
					if (objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
					}
				}
			}

			// 2. AUDIO & SCREEN SHAKE MILESTONES
			if (f == 2)
			{
				this.addSound(10); // High-voltage charge hum
				LoadMap.timeVibrateScreen = 6;
			}
			else if (f == 14)
			{
				this.addSound(18); // Matrix activation
				LoadMap.timeVibrateScreen = 10;
			}
			else if (f == 26)
			{
				this.addSound(10);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 16;
			}
			else if (f == 36)
			{
				// CLIMAX CATACLYSM: 200M Volt El Thor mega impact!
				this.addSound(51);
				this.addSound(14);
				this.addSound(18);
				LoadMap.timeVibrateScreen = 38; // Violent full-screen quake
			}
			else if (f == 46 || f == 54)
			{
				this.addSound(51);
				LoadMap.timeVibrateScreen = 12;
			}

			// 3. TARGET PARALYSIS, AIRBORNE POP & DAMAGE SYNCHRONIZATION
			if (vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (targetInfo == null) continue;
					MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
					if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;

					// Intermediate matrix shocks (f = 20, 28)
					if (f == 20 || f == 28)
					{
						this.setAva(2, target);
						GameScreen.addEffectEnd(40, 0, target.x, target.y - target.hOne / 2, (sbyte)0, null);
					}

					// Mega Climax Strike at f = 36: Popped high into air, paralyzed!
					if (f == 36)
					{
						this.setAva(2, target);
						target.dy = -24; // High pop
						GameScreen.addEffectEnd(40, 0, target.x, target.y, (sbyte)0, null);
						GameScreen.addEffectEnd(42, 0, target.x, target.y, (sbyte)0, null);
					}

					// Lateral paralyzed electric shaking while suspended (f = 37..50)
					if (f >= 37 && f <= 50)
					{
						int wobble = (f % 2 == 0) ? 3 : -3;
						target.x += wobble;
					}

					// Smooth gravity recovery
					if (f >= 48 && f <= 66)
					{
						if (target.dy < 0)
						{
							target.dy += 2;
						}
					}

					// Final cleanup
					if (f >= fRemove)
					{
						target.dy = 0;
					}
				}
			}

			// Skill lifecycle safety finish
			if (f >= fRemove)
			{
				if (objFireMain != null)
				{
					objFireMain.x = casterOrigX;
					objFireMain.y = casterOrigY;
					objFireMain.dy = 0;
					objFireMain.isTanHinh = false;
					if (objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
					}
				}
			}
		}

		// Smooth Camera Vibration & Audio Beats for Set 7 (Chấn Thiên)
		if (setId == 7 && f <= fRemove)
		{
			if (f == 4)
			{
				this.addSound(51);
				this.addSound(14);
				LoadMap.timeVibrateScreen = 20;
			}
			else if (f == 16)
			{
				this.addSound(51);
				this.addSound(18);
				LoadMap.timeVibrateScreen = 24;
			}
			else if (f == 28)
			{
				this.addSound(51);
				this.addSound(14);
				LoadMap.timeVibrateScreen = 20;
			}
			else if (f == 42)
			{
				this.addSound(5);
				LoadMap.timeVibrateScreen = 14;
			}
		}

		// Multi-Pulse Target Damage & Flinch Reaction at Primary Seismic Waves
		if (setId == 7 && (f == 14 || f == 26 || f == 38 || f == 52))
		{
			if (vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (targetInfo != null)
					{
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
						if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
						{
							this.setAva(2, target);
							GameScreen.addEffectEnd(134, (sbyte)(k % 2), target.x, target.y, (sbyte)Dir, objMainEff);
							GameScreen.addEffectEnd(110, 0, target.x, target.y, 0, null);
						}
					}
				}
			}
		}

		// Progressive Organic Seismic Ruptures Jumping Across Terrain
		if (setId == 7 && this.VecEff != null)
		{
			int camX = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.xCam : 0;
			int camY = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.yCam : 0;
			int screenW = GameCanvas.w > 0 ? GameCanvas.w : MotherCanvas.w;
			int screenH = GameCanvas.h > 0 ? GameCanvas.h : MotherCanvas.h;

			for (int k = 0; k < this.VecEff.size(); k++)
			{
				Point node = (Point)this.VecEff.elementAt(k);
				if (node != null)
				{
					node.f++;
					int age = node.f - node.fSmall;
					int posX = (node.subType == 2) ? (camX + screenW * node.x / 100) : node.x;
					int posY = (node.subType == 2) ? (camY + screenH * node.y / 100) : node.y;

					if (age == 1)
					{
						// Phase 1: Seismic ground cracking / shockwave ring
						if (node.subType == 2)
						{
							GameScreen.addEffectEnd(133, 0, posX, posY, 0, null);
							GameScreen.addHightDataeff(33, posX, posY);
						}
						else
						{
							GameScreen.addEffectEnd(110, 0, posX, posY, 0, null);
							GameScreen.addEffectEnd(133, (sbyte)node.color, posX, posY + 4, 0, null);
							GameScreen.addHightDataeff(33, posX, posY);
						}
					}
					else if (age == 10)
					{
						// Phase 2: Atmospheric spatial rupture & dust upheaval
						if (node.subType != 2)
						{
							GameScreen.addEffectEnd(134, 0, posX, posY - 5, 0, null);
							GameScreen.addEffectEnd(92, 0, posX, posY, 0, null);
							GameScreen.addEffectEnd(50, 0, posX, posY - 4, 0, null);
							GameScreen.addHightDataeff(33, posX, posY);
						}
					}
				}
			}
		}

		// 1. Smooth Airborne Levitation for Set 8 (Law Takt: ascent 0→72px easing-out, hover, landing easing-in)
		if (setId == 8 && f <= 74)
		{
			if (vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (targetInfo != null)
					{
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
						if (target == null) continue;
						if (target.isDie || target.Hp <= 0 || target.isRemove)
						{
							target.dy = 0;
							continue;
						}
						int targetDy = 0;
						if (f >= 3 && f <= 22)
						{
							// Easing-out quadratic ascent: fast at start, slow near peak (0→72px over 20 frames)
							int t = f - 3; // 0..19
							int tScaled = (19 - t) * 100 / 19;
							int remaining = 100 - (tScaled * tScaled / 100);
							targetDy = 72 * remaining / 100;
						}
						else if (f > 22 && f <= 56)
						{
							targetDy = 72; // Suspended in air inside ROOM sphere
						}
						else if (f > 56 && f <= 70)
						{
							// Easing-in quadratic landing: slow at start, accelerates down (72→0px over 14 frames)
							int t = f - 56; // 0..14
							int tScaled = (14 - t) * 100 / 14;
							targetDy = 72 * tScaled * tScaled / 10000;
						}
						else
						{
							targetDy = 0;
						}
						target.dy = targetDy;
					}
				}
			}
		}

		// 2. Sound cues for Set 8
		if (setId == 8)
		{
			if (f == 2)
			{
				this.addSound(10);
			}
			else if (f == 14)
			{
				this.addSound(51);
			}
			else if (f == 30)
			{
				this.addSound(18); // Mid-ROOM ambient pulse
			}
		}

		// 3. Periodic Hit Feedback on Targets while levitating (f = 8..56 every 8 frames)
		if (setId == 8 && (f % 8 == 0) && f >= 8 && f <= 56)
		{
			this.addSound(18);
			if (vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (targetInfo != null)
					{
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
						if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
						{
							this.setAva(2, target);
							int targetCenterY = target.y - target.dy - (target.hOne / 2);
							GameScreen.addEffectEnd(10, 0, target.x, targetCenterY, (sbyte)Dir, objMainEff);
						}
					}
				}
			}
		}

		// 4. Final Clean Reset for Set 8 on completion
		if (setId == 8 && f >= fRemove)
		{
			if (vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (targetInfo != null)
					{
						MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
						if (target != null)
						{
							target.dy = 0;
						}
					}
				}
			}
		}

		// ─── setId 12: Phượng Hoàng Bất Tử Bộc Phá _X (Marco Blue Phoenix Climax) ───
		if (setId == 12)
		{
			// Caster state management
			if (objFireMain != null)
			{
				objFireMain.isTanHinh = false;
				objFireMain.dy = 0;
			}

			// Sound and screenshake cues
			if (f == 4)
			{
				this.addSound(18); // Tiếng ngọn lửa bùng phát
				LoadMap.timeVibrateScreen = 6;
			}
			else if (f == 16)
			{
				this.addSound(5);  // Tiếng phượng hoàng cất cánh rít gió
				this.addSound(51);
				LoadMap.timeVibrateScreen = 8;
			}
			else if (f == 32)
			{
				// ─── CLIMAX IMPACT X ───
				this.addSound(14); // Tiếng nổ oanh tạc cực lớn
				this.addSound(51);
				this.addSound(18);
				LoadMap.timeVibrateScreen = 32; // Rung màn hình cực đại 32 tick

				// Spawn các vụ nổ và hiệu ứng phụ tại tâm chấn
				GameScreen.addEffectEnd(118, 0, this.phoenixTargetX, this.phoenixTargetY, (sbyte)0, null);
				GameScreen.addEffectEnd(54, 0, this.phoenixTargetX, this.phoenixTargetY - 10, (sbyte)0, null);
				GameScreen.addEffectEnd(104, 0, this.phoenixTargetX, this.phoenixTargetY - 25, (sbyte)0, null);

				// Kích nổ lan ra nTarget = 5 mục tiêu, nảy lên không trung dy = -18
				if (vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size() && k < 5; k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
						if (Math.Abs(target.x - this.phoenixTargetX) <= 140)
						{
							this.setAva(2, target);
							target.dy = -18; // Pop airborne
							GameScreen.addEffectEnd(118, 0, target.x, target.y - 20, (sbyte)0, null);
						}
					}
				}
			}
			else if (f == 40)
			{
				// Finisher Wings Sweep Sound
				this.addSound(10);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 14;
			}
			else if (f == 46)
			{
				this.addSound(18); // Tiếng lam hỏa thiêu đốt bốc lên dưới chân mục tiêu
				if (vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size() && k < 5; k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
						if (Math.Abs(target.x - this.phoenixTargetX) <= 140)
						{
							this.setAva(2, target); // Giật chớp trắng lần 2
							GameScreen.addEffectEnd(54, 0, target.x, target.y - 10, (sbyte)0, null);
						}
					}
				}
			}

			// Target gravity recovery after pop (f = 42..62)
			if (f >= 42 && f <= 62)
			{
				if (vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (target != null && target.dy < 0)
						{
							target.dy += 3;
							if (target.dy > 0) target.dy = 0;
						}
					}
				}
			}

			// Update floating particles (VecSubEff)
			if (this.VecSubEff != null)
			{
				for (int i = 0; i < this.VecSubEff.size(); i++)
				{
					Point p = (Point)this.VecSubEff.elementAt(i);
					if (p != null)
					{
						p.f++;
						p.x += p.vx;
						p.y += p.vy;
						if (p.f % 4 == 0)
						{
							p.vx = CRes.random_Am_0(2);
						}
						if (p.f >= p.fRe)
						{
							// Tái sinh hạt ở quanh tâm chấn nếu f >= 32
							if (f >= 32 && f < 60)
							{
								p.x = this.phoenixTargetX + CRes.random_Am_0(70);
								p.y = this.phoenixTargetY - 10 + CRes.random_Am_0(30);
								p.vx = CRes.random_Am_0(3);
								p.vy = -(2 + CRes.random(4));
								p.f = 0;
								p.fRe = 15 + CRes.random(10);
							}
						}
					}
				}
			}

			// End skill cleanup (f >= fRemove)
			if (f >= fRemove)
			{
				if (objFireMain != null)
				{
					objFireMain.isTanHinh = false;
					objFireMain.dy = 0;
					if (objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
					}
				}
				if (vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (target != null) target.dy = 0;
					}
				}
				if (GameScreen.typePaintGameScreen == 1)
				{
					GameScreen.isPaintNormal();
				}
				this.removeEff();
			}
		}

		// ─── setId 9: Eustass Kid — Từ Trường Bộc Phá Đại Pháo _X (Damned Punk Railgun Laser & Colossal Pillar) ───
		if (setId == 9)
		{
			// Toạ độ xuất chiêu cố định - nhân vật giữ nguyên x, y không bị lệch khi di chuyển
			int casterX = x;
			int casterY = y;
			int facingSign = (Dir == 2) ? 1 : -1;
			int impactX = (this.kidCannonImpactX != 0) ? this.kidCannonImpactX : toX;
			int impactY = (this.kidCannonImpactY != 0) ? this.kidCannonImpactY : toY;

			int cannonBaseX = casterX + facingSign * 35;
			int cannonBaseY = casterY - 26;

			// Phase 1: Hút vũ khí & Pose gồng nhẹ (f: 0..14)
			if (f < 14)
			{
				if (f == 2 || f == 8)
				{
					this.addSound((sbyte)18); // Tiếng từ trường hút kim loại
				}
			}

			// Phase 2: Ra hiệu ứng bắn laze -> Mở khóa di chuyển ngay lập tức (f == 14)
			if (f == 14)
			{
				if (objFireMain == GameScreen.player)
				{
					Player.isBlock = false;
				}
				if (objFireMain != null && objFireMain.Action == 2)
				{
					objFireMain.Action = 0; // Trở về pose bình thường, nhân vật tự do di chuyển ngay
				}
				this.addSound((sbyte)14); // Tiếng nổ khai hỏa đại pháo
				LoadMap.timeVibrateScreen = 8;
			}

			// Phase 3 & 4: Laser bắn trúng & Cột sáng nổ đi (f == 18)
			if (f == 18)
			{
				// Vết nứt đất phát sáng ở tầng levelPaint = -1 (dưới chân char/mob)
				GameScreen.addEffectEnd((short)133, 0, impactX, impactY, (sbyte)0, null);

				this.addSound((sbyte)14);
				LoadMap.timeVibrateScreen = 20;

				// Tính sát thương & ĐẨY LÙI mục tiêu chính + mục tiêu lân cận (tối đa nTarget = 5, bán kính rangeLan = 140)
				int hitCount = 0;
				// 1. Đẩy lùi mục tiêu chính
				if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
					if (tInfo != null)
					{
						MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
						{
							target.x += facingSign * 28; // Đẩy lùi mạnh về sau theo hướng bắn
							this.setAva(2, target);
							hitCount++;
						}
					}
				}

				// 2. Đẩy lùi các mục tiêu lân cận trong vecObjsBeFire
				if (vecObjsBeFire != null)
				{
					for (int k = 1; k < vecObjsBeFire.size() && hitCount < 5; k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (tInfo != null)
						{
							MainObject other = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (other != null && !other.isDie && other.Hp > 0 && !other.isRemove)
							{
								int dist = MainObject.getDistance(impactX, impactY, other.x, other.y);
								if (dist <= 140)
								{
									other.x += facingSign * 22; // Đẩy lùi mục tiêu lân cận
									this.setAva(2, other);
									hitCount++;
								}
							}
						}
					}
				}

				// 3. Đẩy lùi các quái/mục tiêu lân cận trên bản đồ (nếu chưa đủ nTarget = 5)
				if (hitCount < 5 && GameScreen.vecPlayers != null)
				{
					for (int k = 0; k < GameScreen.vecPlayers.size() && hitCount < 5; k++)
					{
						MainObject other = (MainObject)GameScreen.vecPlayers.elementAt(k);
						if (other != null && other != objFireMain && !other.isDie && other.Hp > 0 && !other.isRemove)
						{
							int dist = MainObject.getDistance(impactX, impactY, other.x, other.y);
							if (dist <= 140)
							{
								bool alreadyHit = false;
								if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
								{
									Object_Effect_Skill t0 = (Object_Effect_Skill)vecObjsBeFire.elementAt(0);
									if (t0 != null && t0.ID == other.ID && t0.tem == other.typeObject)
									{
										alreadyHit = true;
									}
								}
								if (!alreadyHit)
								{
									other.x += facingSign * 22;
									this.setAva(2, other);
									hitCount++;
								}
							}
						}
					}
				}
			}

			// Cập nhật toạ độ 28 vũ khí kim loại trong VecEff
			if (this.VecEff != null)
			{
				for (int i = 0; i < this.VecEff.size(); i++)
				{
					Point p = (Point)this.VecEff.elementAt(i);
					if (p == null) continue;

					p.frame = (p.frame + 1) % 8;

					if (f < 14)
					{
						// Bay xoáy từ ngoài vào tụ thành nòng pháo
						if (f >= p.fSmall)
						{
							int dur = Math.Max(1, p.fRe - p.fSmall);
							int elapsed = Math.Min(dur, f - p.fSmall);
							int targetX = cannonBaseX + p.AK;
							int targetY = cannonBaseY + p.AL;

							int style = p.f % 4;
							if (style == 3)
							{
								int hyperEase = (elapsed * elapsed * elapsed * 100) / (dur * dur * dur);
								p.x = p.x2 + ((targetX - p.x2) * hyperEase) / 100;
								p.y = p.y2 + ((targetY - p.y2) * hyperEase) / 100;
							}
							else
							{
								int easePercent = (elapsed * elapsed * 100) / (dur * dur);
								int baseX = p.x2 + ((targetX - p.x2) * easePercent) / 100;
								int baseY = p.y2 + ((targetY - p.y2) * easePercent) / 100;

								if (style == 0)
								{
									int arcMid = (elapsed * (dur - elapsed) * 300) / (dur * dur);
									p.x = baseX;
									p.y = baseY - arcMid;
								}
								else if (style == 1)
								{
									int waveX = (CRes.getsin((elapsed * 180) / dur) * p.dis) / 1000;
									p.x = baseX + waveX;
									p.y = baseY;
								}
								else
								{
									int zig = ((elapsed % 4) < 2) ? 6 : -6;
									p.x = baseX + zig;
									p.y = baseY - zig;
								}
							}
						}
					}
					else if (f < 32)
					{
						// Khóa chặt vị trí nòng pháo phát sáng
						p.x = cannonBaseX + p.AK;
						p.y = cannonBaseY + p.AL;
					}
					else
					{
						// Phase 5: Tán xạ ra xa khi năng lượng giải phóng xong
						p.x += p.vx;
						p.y += p.vy;
						p.vy += 1;
					}
				}
			}
			return;
		}

		// ─── setId 10: Cổ Độc Phán Quyết Venom _X (Magellan Hell's Judgment) — UPDATE TIMELINE ───
		if (setId == 10)
		{
			// Vũng axit độc ngầm cố định 1 vị trí tại tâm mục tiêu đã chọn, không di chuyển theo quái
			if (this.subType == 1004)
			{
				if (f >= fRemove)
				{
					this.removeEff();
				}
				return;
			}

			int targetX = (this.VecEff != null && this.VecEff.size() > 0) ? ((Point)this.VecEff.elementAt(0)).x : toX;
			int targetY = (this.VecEff != null && this.VecEff.size() > 0) ? ((Point)this.VecEff.elementAt(0)).y : toY;

			// Sound & Screen Vibration Timeline
			if (f == 5)
			{
				this.addSound((sbyte)10); // Toxic charge hiss
			}
			else if (f == 12)
			{
				this.addSound((sbyte)14); // Hydra 3-headed roar
				LoadMap.timeVibrateScreen = 8;
			}
			else if (f == 24)
			{
				this.addSound((sbyte)5);  // Venom dragon projectile blast
			}
			else if (f == 35)
			{
				// ZERO HOUR: VENOM IMPACT X DETONATION!
				this.addSound((sbyte)14);
				this.addSound((sbyte)51);
				this.addSound((sbyte)18);
				LoadMap.timeVibrateScreen = 28;

				// Flash & Flinch targets
			if (vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size() && k < 6; k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (tInfo != null)
					{
						MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
						{
							this.setAva(2, target);
						}
					}
				}
			}

			}
			else if (f == 46)
			{
				// Acid pool active bubbling sound
				this.addSound((sbyte)10);
			}
			else if (f == 52)
			{
				// Geyser eruption sound & screen tremor
				this.addSound((sbyte)51);
				this.addSound((sbyte)14);
				LoadMap.timeVibrateScreen = 16;
			}

			// Target flinch release at f44
			if (f == 44 && vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size() && k < 6; k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (tInfo != null)
					{
						MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (target != null)
						{
							target.isPaintSpec = false;
						}
					}
				}
			}

			// Release player block when skill completes
			if (f >= fRemove)
			{
				if (objFireMain != null)
				{
					objFireMain.isPaintSpec = false;
					if (objFireMain == GameScreen.player)
					{
						Player.isBlock = false;
						if (objFireMain.Action == 2)
						{
							objFireMain.Action = 0;
						}
					}
				}
				GameScreen.isPaintNormal();
			}
		}

		// ─── setId 13: Đại Phật Sóng Xung Kích _X (Sengoku Daibutsu) – 5-Phase Timing & Hit Frame Sync ───
		if (setId == 13)
		{
			// Phase 1: Divine Charge Chime & Pre-Tremor
			if (f == 2)
			{
				this.addSound(10);
			}
			if (f == 6)
			{
				LoadMap.timeVibrateScreen = 6;
			}
			// Phase 2: Palm Thrust Sonic Boom at tick 10
			if (f == 10)
			{
				this.addSound(51);
				LoadMap.timeVibrateScreen = 16;
			}
			// Phase 4: OFFICIAL HIT FRAME at tick 21 (Impact detonation on all targets, damage tick, knockback recoil & screen shake)
			if (f == 21)
			{
				this.addSound(14);
				this.addSound(51);
				LoadMap.timeVibrateScreen = 38;
				int facingSign = (Dir == 2) ? 1 : -1;
				int impX = (this.daibutsuImpactX != 0) ? this.daibutsuImpactX : (x + facingSign * 180);
				int impY = (this.daibutsuImpactY != 0) ? this.daibutsuImpactY : (y - 15);

				// Engine shockwave ring & detonations at epicenter
				GameScreen.addHightDataeff(238, impX, impY, Dir == 2);
				GameScreen.addHightDataeff(33, impX, impY + 10);
				GameScreen.addEffectEnd(92, 0, impX - 25, impY - 20, 0, null);
				GameScreen.addEffectEnd(92, 0, impX + 25, impY - 20, 2, null);
				GameScreen.addEffectEnd(110, 0, impX, impY, 0, null);

				if (vecObjsBeFire != null)
				{
					for (int j = 0; j < vecObjsBeFire.size(); j++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
						if (tInfo != null)
						{
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (tObj != null && !tObj.isDie && tObj.Hp > 0 && !tObj.isRemove)
							{
								this.setAva(2, tObj);
								tObj.x += facingSign * 22;
								tObj.dy = -14;
								GameScreen.addEffectEnd(63, 0, tObj.x, tObj.y, 0, null);
							}
						}
					}
				}
			}
			// Airborne recoil recovery for targets (ticks 22..29)
			if (f >= 22 && f <= 29)
			{
				if (vecObjsBeFire != null)
				{
					for (int j = 0; j < vecObjsBeFire.size(); j++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
						if (tInfo != null)
						{
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (tObj != null && tObj.dy < 0)
							{
								tObj.dy += 2;
							}
						}
					}
				}
			}
			// Secondary Lotus Bloom Resonance at tick 26
			if (f == 26)
			{
				int facingSign = (Dir == 2) ? 1 : -1;
				int impX = (this.daibutsuImpactX != 0) ? this.daibutsuImpactX : (x + facingSign * 180);
				int impY = (this.daibutsuImpactY != 0) ? this.daibutsuImpactY : (y - 15);
				GameScreen.addHightDataeff(33, impX, impY + 8);
				this.addSound(10);
				LoadMap.timeVibrateScreen = 14;
			}
			// Clean reset at end of skill
			if (f >= fRemove)
			{
				if (vecObjsBeFire != null)
				{
					for (int j = 0; j < vecObjsBeFire.size(); j++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(j);
						if (tInfo != null)
						{
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (tObj != null)
							{
								tObj.dy = 0;
							}
						}
					}
				}
			}
		}

		if (setId != 2 && setId != 3 && setId != 5 && setId != 6 && setId != 7 && setId != 8 && setId != 9 && setId != 10 && setId != 12 && setId != 13 && setId != 16)
		{
			for (int i = 0; i < this.VecSubEff.size(); i++)
			{
				Point_Focus point_Focus = (Point_Focus)this.VecSubEff.elementAt(i);
				if (point_Focus == null) continue;
			point_Focus.x += ((point_Focus.Dir == 2) ? 22 : -22);
			point_Focus.y += point_Focus.vy;
			point_Focus.f++;

			// Dynamic Trailing Particles along trajectory
			if (point_Focus.f % 2 == 0)
			{
				if (setId == 1)
				{
					GameScreen.addEffectEnd(63, 0, point_Focus.x, point_Focus.y, 0, null);
				}
				else if (setId == 7)
				{
					GameScreen.addEffectEnd(133, 1, point_Focus.x, point_Focus.y + 8, (sbyte)point_Focus.Dir, null);
				}
			}

			// Impact & Explosion Phase
			if (point_Focus.f >= point_Focus.fRe)
			{
				int impactX = (point_Focus.objMain == null) ? point_Focus.toX : point_Focus.objMain.x;
				int impactY = (point_Focus.objMain == null) ? point_Focus.toY : (point_Focus.objMain.y - point_Focus.objMain.hOne / 2);
				if (point_Focus.objMain != null && !point_Focus.objMain.isDie && point_Focus.objMain.Hp > 0 && !point_Focus.objMain.isRemove)
				{
					this.setAva(2, point_Focus.objMain);
				}

				// Supreme Ultimate Impact for 16 Sets
				switch (setId)
				{
					case 1: // Entei Flame Emperor Supreme Cataclysm
						GameScreen.addHightDataeff(70, impactX, impactY - 45, false);
						GameScreen.addHightDataeff(70, impactX, impactY - 15, true);
						GameScreen.addEffectEnd(110, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX - 35, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX + 35, impactY, 0, null);
						GameScreen.addEffectEnd(175, 0, impactX, impactY - 25, 0, null);
						LoadMap.timeVibrateScreen = 30;
						this.addSound(14);
						this.addSound(51);
						break;
					case 2: // Ryusei Kazan Magma Volcano
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(111, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(112, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(113, 0, impactX, impactY - 20, 0, null);
						GameScreen.addEffectEnd(85, 0, impactX, impactY - 15, 0, null);
						GameScreen.addEffectEnd(60, 2, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(110, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 27;
						this.addSound(51);
						this.addSound(5);
						break;
					case 3: // Ice Age Glacial Burst
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(35, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(17, 30, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 22;
						break;
					case 4: // Yasakani no Magatama Light Rain
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(175, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 25;
						break;
					case 5: // Black Hole & Dark Liberation
						GameScreen.addHightDataeff(8, impactX, impactY);
						GameScreen.addEffectEnd(108, 8, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 25;
						break;
					case 6: // 200M Volts Enel El Thor — Divine Thunder Impact
						GameScreen.addEffectEnd(40, 0, impactX, impactY, 0, null);          // electric burst
						GameScreen.addEffectEnd(42, 0, impactX, impactY, 0, null);          // electric spark
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);          // electric arc
						LoadMap.timeVibrateScreen = 30;
						this.addSound(18);
						break;
					case 7: // Island Shaker Quake Tsunami
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(133, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(133, 1, impactX, impactY + 6, 0, null);
						GameScreen.addEffectEnd(134, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(134, 1, impactX - 40, impactY + 6, 0, null);
						GameScreen.addEffectEnd(134, 1, impactX + 40, impactY + 6, 0, null);
						GameScreen.addEffectEnd(110, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(50, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX - 40, impactY, 0, null);
						GameScreen.addEffectEnd(63, 0, impactX + 40, impactY, 0, null);
						GameScreen.addEffectEnd(175, 0, impactX, impactY - 20, 0, null);
						LoadMap.timeVibrateScreen = 35;
						this.addSound(51);
						this.addSound(14);
						break;
					case 8: // Set 8: Law Phẫu Thuật
						GameScreen.addEffectEnd(10, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(30, 0, impactX, impactY, 400, 0, null);
						this.addSound(18);
						break;
					case 9: // Damned Punk Railgun
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(175, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 27;
						break;
					case 10: // Venom Demon Hell's Judgment
						GameScreen.addHightDataeff(8, impactX, impactY);
						GameScreen.addEffectEnd(108, 8, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 26;
						break;
					case 11: // Perfume Femur Petrification
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(175, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 24;
						break;
					case 12: // Blue Phoenix Rebirth Blaze
						GameScreen.addHightDataeff(70, impactX, impactY - 30, false);
						GameScreen.addEffectEnd(63, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 25;
						break;
					case 13: // Golden Buddha Divine Shockwave
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(175, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 27;
						break;
					case 14: // Dragon Roar & Thunder Bagua
						GameScreen.addHightDataeff(4, impactX, impactY);
						GameScreen.addHightDataeff(33, impactX, impactY);
						GameScreen.addEffectEnd(92, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(50, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 27;
						break;
					case 15: // Sabo Dragon Flame Emperor
						GameScreen.addHightDataeff(70, impactX, impactY - 35, false);
						GameScreen.addEffectEnd(63, 0, impactX, impactY, 0, null);
						GameScreen.addEffectEnd(120, 0, impactX, impactY, 0, null);
						LoadMap.timeVibrateScreen = 26;
						break;
				}
				this.VecSubEff.removeElement(point_Focus);
				i--;
			}
		}
		}
		if ((f >= fRemove && this.VecSubEff.size() == 0) || f > fRemove + 15)
		{
			if (objFireMain != null) objFireMain.dy = 0;
			if (GameScreen.typePaintGameScreen == 1)
			{
				GameScreen.isPaintNormal();
			}
			this.removeEff();
		}
		// setId 2/3/5/6/7/8/12/13/16 dùng this.VecEff thay this.VecSubEff — this.removeEff khi hết fRemove
		int _setId2 = (typeEff >= 4001 && typeEff <= 4016) ? (typeEff - 4000) : (((typeEff - 4001) / 5) + 1);
		if (f > fRemove + 5 && (_setId2 == 2 || _setId2 == 3 || _setId2 == 5 || _setId2 == 6 || _setId2 == 7 || _setId2 == 8 || _setId2 == 12 || _setId2 == 13 || _setId2 == 16))
		{
			if (objFireMain != null) objFireMain.dy = 0;
			if (GameScreen.typePaintGameScreen == 1)
			{
				GameScreen.isPaintNormal();
			}
			this.removeEff();
		}
	}

	private void paintThanTrangSkill(mGraphics g)
	{
		if (g == null) return;
		try
		{
			int setId = (typeEffect == 4017 || typeEffect == 4010) ? 10 : ((typeEffect >= 4001 && typeEffect <= 4016) ? (typeEffect - 4000) : (((typeEffect - 4001) / 5) + 1));
			if (setId < 1 || setId > 16) setId = 1;
			ensureThanTrangFrames();
		// ═══════════════════════════════════════════════════════════════════
		// Paint Set 2: Đại Phún Hỏa Volcano _X (Akainu Magma Eruption)
		// Chuẩn hóa theo phong cách Hỏa Diễm & Nham Thạch nguyên bản HTTH
		// ═══════════════════════════════════════════════════════════════════
		if (setId == 2)
		{
			try
			{
				FrameImage[] p2 = (s_ttFrames != null && s_ttFrames.Length > 2) ? s_ttFrames[2] : null;
				if (p2 != null && p2.Length >= 10)
				{
					int casterX = (objFireMain != null) ? objFireMain.x : x;
					int casterY = (objFireMain != null) ? objFireMain.y : y;
					int impactX = toX;
					int impactY = toY;
					int facingSign = (Dir == 2) ? 1 : -1;
					int dirTrans = (Dir == 2) ? 0 : 2;

					// ─── TẦNG 1: TỤ LỰC DUNG NHAM TẠI CASTER (f: 0..12) ───
					if (f <= 12)
					{
						// Nền đất nứt lửa dưới chân Akainu (p2[7]: 246)
						if (p2[7] != null && p2[7].nFrame > 0)
						{
							int fissF = (f / 3) % p2[7].nFrame;
							p2[7].drawFrameNew(fissF, casterX, casterY + 4, 0, 3, g);
						}

						// Dòng dung nham cánh tay rực đỏ (p2[1]: 336)
						if (p2[1] != null && p2[1].nFrame > 0)
						{
							int armF = (f / 2) % p2[1].nFrame;
							p2[1].drawFrame(armF, casterX + facingSign * 12, casterY - 20, dirTrans, 3, g);
						}

						// Nắm đấm cự quyền tích tụ năng lượng (p2[0]: 271)
						if (p2[0] != null && p2[0].nFrame > 0)
						{
							int fistOffset = Math.Min(20, 8 + f);
							p2[0].drawFrameNew(0, casterX + facingSign * fistOffset, casterY - 20, dirTrans, 3, g);
						}
					}

					// ─── TẦNG 2: ĐẠI PHÚN HỎA CỰ QUYỀN LAO KÍCH (f: 12..24) ───
					if (f >= 12 && f <= 24)
					{
						int t = f - 12; // 0..12
						int fistStartX = casterX + facingSign * 24;
						int fistStartY = casterY - 20;
						int fistX = fistStartX + ((impactX - fistStartX) * t) / 12;
						int fistY = fistStartY + ((impactY - 15 - fistStartY) * t) / 12;

						// Bóng trượt mặt đất sém đen (p2[9]: 272)
						if (p2[9] != null && p2[9].nFrame > 0)
						{
							p2[9].drawFrame(0, fistX, impactY + 4, 0, 3, g);
						}

						// Luồng cánh tay dung nham nối dài kéo sau lưng Cự Quyền (p2[1]: 336)
						if (p2[1] != null && p2[1].nFrame > 0)
						{
							int armF = (f / 2) % p2[1].nFrame;
							p2[1].drawFrame(armF, fistX - facingSign * 35, fistY, dirTrans, 3, g);
						}

						// Phản lực hỏa diễm bộc phát (p2[2]: 254)
						if (p2[2] != null && p2[2].nFrame > 0)
						{
							p2[2].drawFrame(0, fistX - facingSign * 55, fistY, dirTrans, 3, g);
						}

						// ĐẠI PHÚN HỎA CỰ QUYỀN KHỔNG LỒ (p2[0]: 271, 130x80)
						if (p2[0] != null && p2[0].nFrame > 0)
						{
							int fistF = (f / 2) % p2[0].nFrame;
							p2[0].drawFrameNew(fistF, fistX, fistY, dirTrans, 3, g);
						}
					}

					// ─── TẦNG 3: VỤ NỔ DUNG NHAM ĐẠI BỘC PHÁ TẠI TÂM CHẤN (f: 24..38) ───
					if (f >= 24 && f <= 38 && p2[3] != null && p2[3].nFrame > 0)
					{
						int blastF = (f - 24) / 3;
						if (blastF < p2[3].nFrame)
						{
							p2[3].drawFrameNew(blastF, impactX, impactY - 18, (f % 2 == 0 ? 0 : 2), 3, g);
						}
					}

					// ─── TẦNG 4: HỒ DUNG NHAM, VẾT RẠN NỨT & CỘT NHAM THẠCH PHUN TRÀO (f: 24..62) ───
					if (f >= 24 && f <= 62)
					{
						// A. Vết Rạn Nứt Núi Lửa (p2[7]: 246)
						if (p2[7] != null && p2[7].nFrame > 0)
						{
							int fissF = ((f - 24) / 3) % p2[7].nFrame;
							p2[7].drawFrameNew(fissF, impactX, impactY + 5, 0, 3, g);
							p2[7].drawFrameNew((fissF + 1) % p2[7].nFrame, impactX - 50, impactY + 3, 2, 3, g);
							p2[7].drawFrameNew((fissF + 2) % p2[7].nFrame, impactX + 50, impactY + 3, 0, 3, g);
						}

						// B. Hồ Dung Nham Sôi Sùng Sục (p2[6]: 239)
						if (p2[6] != null && p2[6].nFrame > 0)
						{
							int poolTrans1 = (f % 8 < 4) ? 0 : 2;
							int poolTrans2 = (f % 8 < 4) ? 2 : 0;
							p2[6].drawFrame(0, impactX, impactY + 2, poolTrans1, 3, g);
							p2[6].drawFrame(0, impactX - 45, impactY + 2, poolTrans2, 3, g);
							p2[6].drawFrame(0, impactX + 45, impactY + 2, poolTrans1, 3, g);
						}

						// C. Cột Nham Thạch (p2[4]: 238) & Cột Lửa (p2[5]: 240) Phun Trào Dữ Dội
						if (f >= 25 && f <= 58)
						{
							// 1. Cột trung tâm chọc trời
							if (p2[4] != null && p2[4].nFrame > 0)
							{
								p2[4].drawFrame(0, impactX, impactY + 4, (f % 4 < 2 ? 0 : 2), 33, g);
							}
							if (p2[5] != null && p2[5].nFrame > 0)
							{
								p2[5].drawFrame(0, impactX, impactY - 60, (f % 4 < 2 ? 2 : 0), 33, g);
							}

							// 2. Hai cột phun trào hai bên sườn (bán kính 45px)
							if (p2[4] != null && p2[4].nFrame > 0)
							{
								p2[4].drawFrame(0, impactX - 45, impactY - 5, 2, 33, g);
								p2[4].drawFrame(0, impactX + 45, impactY - 5, 0, 33, g);
							}
							if (p2[5] != null && p2[5].nFrame > 0)
							{
								p2[5].drawFrame(0, impactX - 45, impactY - 65, 0, 33, g);
								p2[5].drawFrame(0, impactX + 45, impactY - 65, 2, 33, g);
							}

							// 3. Hai cột rìa ngoài bao quát vùng lan 140px (p2[4]: 238)
							if (p2[4] != null && p2[4].nFrame > 0)
							{
								p2[4].drawFrame(0, impactX - 85, impactY + 2, 0, 33, g);
								p2[4].drawFrame(0, impactX + 85, impactY + 2, 2, 33, g);
							}
						}
					}

					// ─── TẦNG 5: HẠT TÀN LỬA ĐỎ BẮN TUNG TÓE (f: 25..60) ───
					if (this.VecSubEff != null && f >= 25 && f <= 60 && p2[8] != null && p2[8].nFrame > 0)
					{
						for (int i = 0; i < this.VecSubEff.size(); i++)
						{
							Point p = (Point)this.VecSubEff.elementAt(i);
							if (p != null)
							{
								int sparkF = (p.f / 2) % p2[8].nFrame;
								p2[8].drawFrame(sparkF, p.x, p.y, p.color, 3, g);
							}
						}
					}

					// ─── TẦNG 6: HIỆU ỨNG THIÊU ĐỐT DƯỚI CHÂN MỤC TIÊU (f: 25..55) ───
					if (f >= 25 && f <= 55 && vecObjsBeFire != null)
					{
						for (int k = 0; k < vecObjsBeFire.size() && k < 5; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;

							int targetFlip = (k % 2 == 0) ? 0 : 2;

							// Vũng dung nham thiêu đốt dưới chân mục tiêu (p2[6]: 239)
							if (p2[6] != null && p2[6].nFrame > 0)
							{
								p2[6].drawFrame(0, tObj.x, tObj.y + 2, targetFlip, 3, g);
							}

							// Tàn lửa đỏ bùng cháy trên người mục tiêu (p2[8]: 78)
							if (p2[8] != null && p2[8].nFrame > 0 && (f + k) % 2 == 0)
							{
								int spF = (f / 2 + k) % p2[8].nFrame;
								p2[8].drawFrame(spF, tObj.x, tObj.y - tObj.hOne / 2, 0, 3, g);
							}
						}
					}
				}
			}
			catch (Exception)
			{
			}
		}

		// Paint Set 3: Kỷ Băng Hà Tuyết Đối X (Absolute Blizzard / Ice Age X)
		if (setId == 3)
		{
			try
			{
				FrameImage[] p3 = s_ttFrames[3];
				if (p3 != null && p3.Length >= 9)
				{
					int casterX = (objFireMain != null) ? objFireMain.x : x;
					int casterY = (objFireMain != null) ? objFireMain.y : y;
					int centerX = toX;
					int centerY = toY;
					int facingSign = (Dir == 2) ? 1 : -1;
					int dirTrans = (Dir == 2) ? 0 : 2;

					// ─── 1. CASTER SUB-ZERO AURA & 3D ORBITING DIAMOND ORBS (f: 0..22) ───
					if (f <= 22)
					{
						// Ground permafrost patch under caster feet (p3[1]: 40, 63x20)
						if (p3[1] != null && p3[1].nFrame > 0)
						{
							p3[1].drawFrame(0, casterX, casterY + 2, 0, 3, g);
						}

						// 3 Orbital Diamond Snow Spheres revolving around caster (p3[2]: 41, 40x40 & p3[7]: 104, 30x30)
						if (p3[2] != null && p3[2].nFrame > 0)
						{
							int baseAngle = (f * 24) % 360;
							for (int orb = 0; orb < 3; orb++)
							{
								int angle = (baseAngle + orb * 120) % 360;
								int ox = casterX + (CRes.getcos(angle) * 32 >> 10);
								int oy = casterY - 18 + (CRes.getsin(angle) * 12 >> 10);
								p3[2].drawFrame((f / 2 + orb) % p3[2].nFrame, ox, oy, 0, 3, g);
								if (p3[7] != null && p3[7].nFrame > 0 && f % 2 == 0)
								{
									p3[7].drawFrame((f / 2 + orb) % p3[7].nFrame, ox, oy - 2, 0, 3, g);
								}
							}
						}
					}

					// ─── 2. SURGING GLACIAL AVALANCHE WAVE (f: 5..20) ───
					if (f >= 5 && f <= 20)
					{
						int tProg = f - 5; // 0..15
						int waveX = casterX + ((centerX - casterX) * tProg) / 15;
						int waveY = casterY + ((centerY - casterY) * tProg) / 15;

						// Frozen Permafrost wake trailing behind the wave
						if (p3[1] != null && p3[1].nFrame > 0 && tProg >= 3)
						{
							int trailX = casterX + ((centerX - casterX) * (tProg - 3)) / 15;
							int trailY = casterY + ((centerY - casterY) * (tProg - 3)) / 15;
							p3[1].drawFrame(0, trailX, trailY + 2, dirTrans, 3, g);
						}

						// Colossal Ice Pheasant Crest Surge (p3[3]: 43, 84x110)
						if (p3[3] != null && p3[3].nFrame > 0)
						{
							int wFrame = Math.Min(p3[3].nFrame - 1, tProg / 3);
							p3[3].drawFrame(wFrame, waveX, waveY - 25, dirTrans, 3, g);
						}

						// Snow / Frost Mist Sparkles along path (p3[2]: 41)
						if (p3[2] != null && p3[2].nFrame > 0)
						{
							p3[2].drawFrame((f / 2) % p3[2].nFrame, waveX - facingSign * 20, waveY - 10, 0, 3, g);
						}
					}

					// ─── 3. PERMAFROST GROUND CARPET AT EPICENTER (f: 14..55) ───
					if (f >= 14 && f <= 55 && p3[1] != null && p3[1].nFrame > 0)
					{
						p3[1].drawFrame(0, centerX, centerY + 4, 0, 3, g);
						p3[1].drawFrame(0, centerX - 55, centerY + 2, 0, 3, g);
						p3[1].drawFrame(0, centerX + 55, centerY + 2, 2, 3, g);
						p3[1].drawFrame(0, centerX - 110, centerY + 3, 2, 3, g);
						p3[1].drawFrame(0, centerX + 110, centerY + 3, 0, 3, g);
					}

					// ─── 4. FOREST OF 7 COLOSSAL ICE SPIRES, JAGGED ICICLES & BLIZZARD VORTEX (f: 14..55) ───
					if (this.VecEff != null && f >= 14)
					{
						for (int i = 0; i < this.VecEff.size(); i++)
						{
							Point spire = (Point)this.VecEff.elementAt(i);
							if (spire == null) continue;

							if (spire.subType == 0)
							{
								int age = f - spire.fSmall;
								if (age < 0 || age >= 38) continue;

								// ─── Colossal Ice Spire (p3[0]: 37, 31x74) ───
								int targetH = spire.dis; // 65..140 px
								int curH = (age <= 4) ? (targetH * age / 4) : targetH;
								int shakeX = (age > 28 && (age % 2 == 0)) ? (spire.x + ((age % 4 == 0) ? 1 : -1)) : spire.x;
								int riseOffsetY = (targetH - curH);

								if (p3[0] != null && p3[0].nFrame > 0)
								{
									p3[0].drawFrame(0, shakeX, spire.y + 4 + riseOffsetY, spire.color, 33, g);
									if (curH > 74)
									{
										p3[0].drawFrame(0, shakeX, spire.y + 4 + riseOffsetY - 55, spire.color == 2 ? 0 : 2, 33, g);
									}
									if (curH > 120)
									{
										p3[0].drawFrame(0, shakeX, spire.y + 4 + riseOffsetY - 100, spire.color, 33, g);
									}
								}

								// Base Jagged Ice (p3[4]: 89, 28x44) at ground level
								if (p3[4] != null && p3[4].nFrame > 0 && age >= 2 && age <= 34)
								{
									int jF = Math.Min(p3[4].nFrame - 1, age / 4);
									p3[4].drawFrame(jF, shakeX, spire.y + 2, spire.color, 33, g);
								}

								// Spire Frost Sparkle Glint (p3[7]: 104) at tip
								if (p3[7] != null && p3[7].nFrame > 0 && age >= 4 && age <= 28 && ((age + spire.fSmall) % 3 == 0))
								{
									p3[7].drawFrame((age / 3) % p3[7].nFrame, shakeX, spire.y - curH + 4, 0, 3, g);
								}

								// Base sub-zero frost mist (p3[8]: 152)
								if (p3[8] != null && p3[8].nFrame > 0 && age <= 12)
								{
									p3[8].drawFrame(age / 3 % p3[8].nFrame, shakeX + (spire.color == 2 ? 8 : -8), spire.y + 2, spire.color, 33, g);
								}
							}
							else if (spire.subType == 1)
							{
								int age = f - spire.fSmall;
								if (age < 0 || age >= 38) continue;

								// ─── Intermediate Ground Icicles (p3[4]: 89, 28x44) ───
								if (p3[4] != null && p3[4].nFrame > 0 && age <= 32)
								{
									int jF = Math.Min(p3[4].nFrame - 1, age / 3);
									p3[4].drawFrame(jF, spire.x, spire.y + 2, spire.color, 33, g);
								}
							}
							else if (spire.subType == 2 && f >= 16 && f <= 54)
							{
								// ─── Blizzard Vortex & Diamond Dust Particles (subType = 2) ───
								int curAngle = (spire.frame + (f - 16) * 16) % 360;
								int radVariation = CRes.getsin(((f - 16) * 18 + spire.fSmall * 20) % 360) * 12 >> 10;
								int curR = Math.Max(12, spire.dis + radVariation);

								int px = centerX + (CRes.getcos(curAngle) * curR >> 10);
								int py = centerY - 28 + (CRes.getsin(curAngle) * (curR * 3 / 5) >> 10);

								if (spire.color == 1 && p3[7] != null && p3[7].nFrame > 0)
								{
									p3[7].drawFrame((f / 2 + spire.fSmall) % p3[7].nFrame, px, py, 0, 3, g);
								}
								else if (p3[2] != null && p3[2].nFrame > 0)
								{
									p3[2].drawFrame((f / 2 + spire.fSmall) % p3[2].nFrame, px, py, 0, 3, g);
								}
							}
						}
					}

					// ─── 5. FROST ENCASEMENT & DIAMOND GLINT OVER TARGETS (f: 18..50) ───
					if (f >= 18 && f <= 50 && vecObjsBeFire != null)
					{
						for (int k = 0; k < vecObjsBeFire.size() && k < 6; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;

							// Eternal permafrost block encasing target (p3[6]: 39, 53x28)
							if (p3[6] != null && p3[6].nFrame > 0)
							{
								p3[6].drawFrame(0, tObj.x, tObj.y - 6, 0, 3, g);
							}
							// Frozen head frost crown / diamond glint (p3[7]: 104)
							if (p3[7] != null && p3[7].nFrame > 0 && (f + k) % 3 == 0)
							{
								p3[7].drawFrame((f / 2 + k) % p3[7].nFrame, tObj.x, tObj.y - tObj.hOne - 4, 0, 3, g);
							}
							// Ground freeze base under target feet (p3[1]: 40)
							if (p3[1] != null && p3[1].nFrame > 0)
							{
								p3[1].drawFrame(0, tObj.x, tObj.y + 2, (k % 2 == 0 ? 0 : 2), 3, g);
							}
						}
					}

					// ─── 6. CATACLYSMIC ICE SHATTER SPARKS (f: 45..56) ───
					if (f >= 45 && f <= 56 && p3[7] != null && p3[7].nFrame > 0)
					{
						int sProg = f - 45;
						for (int sp = 0; sp < 8; sp++)
						{
							int spAng = sp * 45;
							int spDist = sProg * 10;
							int sx = centerX + (CRes.getcos(spAng) * spDist >> 10);
							int sy = centerY - 30 + (CRes.getsin(spAng) * (spDist * 3 / 4) >> 10);
							p3[7].drawFrame((sProg + sp) % p3[7].nFrame, sx, sy, 0, 3, g);
						}
					}
				}
			}
			catch (Exception)
			{
			}
		}

		// ═══════════════════════════════════════════════════════════════════
		// Paint Set 5: Hắc Ám Thôn Phệ Vô Tận _X (Marshall D. Teach Black Hole Abyss Singularity)
		// Hệ Thống 14 Tầng Hiệu Ứng Hắc Ám Hư Vô Tối Thượng (Authentic 480..485 + 272, 246, 285, 394, 104, 152, 92, 175)
		// ═══════════════════════════════════════════════════════════════════
		if (setId == 5)
		{
			try
			{
				FrameImage[] p5 = (s_ttFrames != null && s_ttFrames.Length > 5) ? s_ttFrames[5] : null;
				if (p5 != null && p5.Length >= 6)
				{
					int casterX = (objFireMain != null) ? objFireMain.x : x;
					int casterY = (objFireMain != null) ? objFireMain.y : y;
					int impactX = toX;
					int impactY = toY;
					int facingSign = (Dir == 2) ? 1 : -1;
					int dirTrans = (Dir == 2) ? 0 : 2;

					// ─── TẦNG 1: VỰC THẲM HẮC ÁM & HỒ BÓNG TỐI DƯỚI ĐẤT (f: 18..72) ───
					// Sử dụng p5[6] (272: Scorched Ground Shadow) xếp tầng tạo thành Hồ Hư Vô Khổng Lồ
					if (p5.Length > 6 && p5[6] != null && p5[6].nFrame > 0 && f >= 18 && f <= 72)
					{
						p5[6].drawFrame(0, impactX, impactY + 6, 0, 3, g);
						p5[6].drawFrame(0, impactX - 45, impactY + 4, 2, 3, g);
						p5[6].drawFrame(0, impactX + 45, impactY + 4, 0, 3, g);
						p5[6].drawFrame(0, impactX - 85, impactY + 3, 0, 3, g);
						p5[6].drawFrame(0, impactX + 85, impactY + 3, 2, 3, g);
					}

					// ─── TẦNG 2: VẾT RẠN NỨT ĐỊA CHẤN HƯ VÔ (f: 20..68) ───
					// Sử dụng p5[7] (246: Ground Fissure) rạn nứt mặt đất dưới sức hút hố đen
					if (p5.Length > 7 && p5[7] != null && p5[7].nFrame > 0 && this.VecEff != null && f >= 20 && f <= 68)
					{
						for (int r = 0; r < this.VecEff.size(); r++)
						{
							Point rp = (Point)this.VecEff.elementAt(r);
							if (rp != null && f >= rp.fSmall)
							{
								int fissF = ((f - rp.fSmall) / 3) % p5[7].nFrame;
								p5[7].drawFrameNew(fissF, rp.x, rp.y, rp.subType, 3, g);
							}
						}
					}

					// ─── TẦNG 3: VÀNH ĐAI SÓNG TRỌNG LỰC ĐỊA BÀN (f: 20..64) ───
					// Sử dụng p5[9] (394: 126x41 Ground Shockwave Ring) tỏa sóng hấp dẫn liên tục
					if (p5.Length > 9 && p5[9] != null && p5[9].nFrame > 0 && f >= 20 && f <= 64)
					{
						int ringF = ((f - 20) / 2) % p5[9].nFrame;
						p5[9].drawFrame(ringF, impactX, impactY + 4, 0, 3, g);
						if (f % 4 < 2)
						{
							p5[9].drawFrame((ringF + 1) % p5[9].nFrame, impactX, impactY + 2, 2, 3, g);
						}
					}

					// ─── TẦNG 4: KHÍ HẮC ÁM & CỔNG KHÔNG GIAN TẠI CASTER (f: 0..32) ───
					// Nền đen dưới chân Caster (p5[6]: 272)
					if (p5.Length > 6 && p5[6] != null && p5[6].nFrame > 0 && f <= 26)
					{
						p5[6].drawFrame(0, casterX, casterY + 4, 0, 3, g);
					}
					// Khói đen hư vô cuộn quanh Caster (p5[0]: 480)
					if (p5[0] != null && f <= 28)
					{
						int n = (p5[0].nFrame > 0) ? p5[0].nFrame : 8;
						int auraF = (f / 2) % n;
						p5[0].drawFrame(auraF, casterX, casterY + 2, dirTrans, 33, g);
					}
					// Cổng không gian mở trước Caster giải phóng hắc ám (p5[1]: 481)
					if (p5[1] != null && f <= 22)
					{
						int n = (p5[1].nFrame > 0) ? p5[1].nFrame : 8;
						int castF = Math.Min(n - 1, f / 2);
						p5[1].drawFrame(castF, casterX + facingSign * 35, casterY + 4, dirTrans, 33, g);
					}

					// ─── TẦNG 5: HẮC TINH CẦU LAO XÉ KHÔNG GIAN (f: 12..24) ───
					// p5[2]: 482 (Void Wave Projectile) phóng từ Caster tới tâm Hố Đen
					if (p5[2] != null && f >= 12 && f <= 24)
					{
						int n = (p5[2].nFrame > 0) ? p5[2].nFrame : 8;
						int pF = (f - 12) % n;
						int startX = casterX + facingSign * 35;
						int startY = casterY - 15;
						int endX = impactX;
						int endY = impactY - 20;
						int tProg = f - 12; // 0..12
						int curX = startX + (endX - startX) * tProg / 12;
						int curY = startY + (endY - startY) * tProg / 12;

						// Vòng nén trọng lực bay kèm (p5[13]: 175)
						if (p5.Length > 13 && p5[13] != null && p5[13].nFrame > 0)
						{
							p5[13].drawFrame(0, curX - facingSign * 18, curY, dirTrans, 3, g);
						}
						// Bóng hắc tinh cầu mặt đất (p5[6]: 272)
						if (p5.Length > 6 && p5[6] != null && p5[6].nFrame > 0)
						{
							p5[6].drawFrame(0, curX, impactY + 3, 0, 3, g);
						}
						// Hắc tinh cầu chính (p5[2]: 482)
						p5[2].drawFrame(pF, curX, curY, dirTrans, 3, g);
					}

					// ─── TẦNG 6: MA TRẬN XOÁY TRỌNG LỰC HƯ VÔ QUANH HỐ ĐEN (f: 20..54) ───
					// Vẽ các nhánh xoắn ốc trọng lực Logarithmic Spiral hút vào tâm (Procedural Cosmic Darkness)
					if (f >= 20 && f <= 54)
					{
						int rotBase = (f * 9) % 360;
						// 3 Nhánh xoắn ốc hắc ám (Triple Accretion Spiral Arms)
						for (int arm = 0; arm < 3; arm++)
						{
							int armAngle = (rotBase + arm * 120) % 360;
							int prevPx = 0;
							int prevPy = 0;
							// Đi từ rìa ngoài (120px) xoáy vào tâm (15px)
							for (int step = 6; step >= 1; step--)
							{
								int rDist = step * 20; // 120, 100, 80, 60, 40, 20
								int spiralAng = (armAngle + (6 - step) * 28) % 360;
								int px = impactX + (rDist * CRes.getcos(spiralAng) >> 10);
								int py = impactY - 20 + ((rDist * 3 / 5) * CRes.getsin(spiralAng) >> 10);

								if (step < 6)
								{
									// Lớp hào quang tím huyền ảo ngoài
									g.setColor(0x7B1FA2);
									g.drawLine(prevPx, prevPy, px, py);
									// Lớp lõi hắc ám obsidian bên trong
									g.setColor(0x1A0033);
									g.drawLine(prevPx + 1, prevPy, px + 1, py);
									g.setColor(0x0A0014);
									g.drawLine(prevPx, prevPy + 1, px, py + 1);
								}
								prevPx = px;
								prevPy = py;
							}
						}

						// Vành đai chân trời sự kiện (Concentric Event Horizon Rings)
						g.setColor(0x4A148C);
						int r1 = 35 + (f % 6) * 2;
						int r2 = 65 + ((f + 3) % 6) * 2;
						for (int ang = 0; ang < 12; ang++)
						{
							int a1 = (rotBase + ang * 30) % 360;
							int a2 = (rotBase + (ang + 1) * 30) % 360;
							int px1 = impactX + (r1 * CRes.getcos(a1) >> 10);
							int py1 = impactY - 20 + ((r1 * 3 / 5) * CRes.getsin(a1) >> 10);
							int px2 = impactX + (r1 * CRes.getcos(a2) >> 10);
							int py2 = impactY - 20 + ((r1 * 3 / 5) * CRes.getsin(a2) >> 10);
							g.drawLine(px1, py1, px2, py2);

							int qx1 = impactX + (r2 * CRes.getcos(a1) >> 10);
							int qy1 = impactY - 20 + ((r2 * 3 / 5) * CRes.getsin(a1) >> 10);
							int qx2 = impactX + (r2 * CRes.getcos(a2) >> 10);
							int qy2 = impactY - 20 + ((r2 * 3 / 5) * CRes.getsin(a2) >> 10);
							g.drawLine(qx1, qy1, qx2, qy2);
						}
					}

					// ─── TẦNG 7: 16 HẠT VẬT CHẤT TỐI XOAY HÚT VÀO HỐ ĐEN (this.VecSubEff) (f: 20..54) ───
					if (this.VecSubEff != null && f >= 20 && f <= 54)
					{
						for (int i = 0; i < this.VecSubEff.size(); i++)
						{
							Point bp = (Point)this.VecSubEff.elementAt(i);
							if (bp != null)
							{
								int px = impactX + (bp.dis * CRes.getcos(bp.frame) >> 10);
								int py = impactY - 20 + ((bp.dis * 3 / 5) * CRes.getsin(bp.frame) >> 10);

								// Đốm sao bụi hắc ám (p5[10]: 104)
								if (bp.subType == 1 && p5.Length > 10 && p5[10] != null && p5[10].nFrame > 0)
								{
									int sF = (f / 2 + bp.fSmall) % p5[10].nFrame;
									p5[10].drawFrame(sF, px, py, 0, 3, g);
								}
								else
								{
									// Điểm phát sáng vật chất tối tím/đen
									g.setColor(bp.color);
									g.fillRect(px - 1, py - 1, 3, 3);
									g.setColor(0xE1BEE7);
									g.fillRect(px, py, 1, 1);
								}
							}
						}
					}

					// ─── TẦNG 8: LỖ ĐEN THÔN PHỆ KHỔNG LỒ 240x240 (p5[3]: 483) (f: 20..52) ───
					if (p5[3] != null && f >= 20 && f <= 52)
					{
						int n = (p5[3].nFrame > 0) ? p5[3].nFrame : 10;
						int vF = ((f - 20) / 2) % n;
						p5[3].drawFrame(vF, impactX, impactY - 20, 0, 3, g);

						// Lớp lõi hố đen thứ hai xoay nghịch tạo chiều sâu không gian
						if (f >= 24 && f <= 48 && (f % 2 == 0))
						{
							p5[3].drawFrame((vF + 3) % n, impactX, impactY - 20, 2, 3, g);
						}
					}

					// ─── TẦNG 9: TIA SÉT HƯ VÔ KẾT NỐI TÂM HỐ ĐEN VỚI CÁC MỤC TIÊU (f: 24..50) ───
					if (f >= 24 && f <= 50 && vecObjsBeFire != null)
					{
						int tickSeed = GameCanvas.gameTick;
						for (int k = 0; k < vecObjsBeFire.size() && k < 6; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;

							int tx = tObj.x;
							int ty = tObj.y - tObj.hOne / 2;
							int jitter = ((tickSeed + k * 3) % 3 - 1) * 7;
							int midX1 = impactX + (tx - impactX) / 3 + jitter;
							int midY1 = (impactY - 20) + (ty - (impactY - 20)) / 3 - jitter / 2;
							int midX2 = impactX + (tx - impactX) * 2 / 3 - jitter;
							int midY2 = (impactY - 20) + (ty - (impactY - 20)) * 2 / 3 + jitter / 2;

							// Sét tím hư vô
							g.setColor(0x9C27B0);
							g.drawLine(impactX, impactY - 20, midX1, midY1);
							g.drawLine(midX1, midY1, midX2, midY2);
							g.drawLine(midX2, midY2, tx, ty);
							// Sợi quang học trắng-tím ở lõi
							g.setColor(0xE1BEE7);
							g.drawLine(impactX + 1, impactY - 20, midX1 + 1, midY1);
							g.drawLine(midX1 + 1, midY1, midX2 + 1, midY2);
							g.drawLine(midX2 + 1, midY2, tx + 1, ty);

							// Sét lớn hồ quang hư vô (p5[12]: 92)
							if (p5.Length > 12 && p5[12] != null && ((f + k) % 4 == 0))
							{
								p5[12].drawFrame(0, tx, ty - 15, (k % 2 == 0 ? 0 : 2), 3, g);
							}
						}
					}

					// ─── TẦNG 10: TRỤ HẮC ÁM & BỤI SAO TRÓI CHÂN MỤC TIÊU (f: 22..62) ───
					if (p5[5] != null && f >= 22 && f <= 62 && vecObjsBeFire != null)
					{
						int n = (p5[5].nFrame > 0) ? p5[5].nFrame : 8;
						for (int k = 0; k < vecObjsBeFire.size() && k < 6; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;

							int sF = ((f - 22) / 2 + k * 2) % n;
							// Bóng đen dưới chân mục tiêu (p5[6]: 272)
							if (p5.Length > 6 && p5[6] != null && p5[6].nFrame > 0)
							{
								p5[6].drawFrame(0, tObj.x, tObj.y + 3, (k % 2 == 0 ? 0 : 2), 3, g);
							}
							// Cột hắc khí xoắn ốc nuốt trọn mục tiêu (p5[5]: 485, 90x140)
							p5[5].drawFrame(sF, tObj.x, tObj.y + 2, (k % 2 == 0 ? 0 : 2), 33, g);

							// Điểm nổ sao đen trên người mục tiêu (p5[10]: 104)
							if (p5.Length > 10 && p5[10] != null && p5[10].nFrame > 0 && (f + k) % 3 == 0)
							{
								int spkF = (f / 2 + k) % p5[10].nFrame;
								p5[10].drawFrame(spkF, tObj.x, tObj.y - tObj.hOne / 2, 0, 3, g);
							}
						}
					}

					// ─── TẦNG 11: ĐẠI HƯ VÔ SỤP ĐỔ BỘC PHÁ CỰC ĐẠI (f: 48..74) ───
					// Vụ nổ hủy diệt 280x200 (p5[4]: 484)
					if (p5[4] != null && f >= 48 && f <= 74)
					{
						int n = (p5[4].nFrame > 0) ? p5[4].nFrame : 12;
						int colF = (f - 48) / 2;
						if (colF < n)
						{
							p5[4].drawFrame(colF, impactX, impactY - 25, (f % 2 == 0 ? 0 : 2), 3, g);
						}
					}

					// ─── TẦNG 12: SÓNG BIẾN DẠNG KHÔNG GIAN BÙNG NỔ (f: 48..66) ───
					// Sóng cầu méo mó không gian (p5[8]: 285, 111x90)
					if (p5.Length > 8 && p5[8] != null && p5[8].nFrame > 0 && f >= 48 && f <= 66)
					{
						int distF = Math.Min(p5[8].nFrame - 1, (f - 48) / 3);
						p5[8].drawFrame(distF, impactX, impactY - 25, 0, 3, g);
						p5[8].drawFrame(distF, impactX, impactY - 25, 2, 3, g);
					}

					// ─── TẦNG 13: VÀNH ĐAI XUNG KÍCH BỘC PHÁ BỐN PHƯƠNG (f: 49..68) ───
					// Vành đai nén trọng lực vỡ tung (p5[13]: 175) & Vành đai mặt đất (p5[9]: 394)
					if (f >= 49 && f <= 68)
					{
						int ageDet = f - 49;
						if (p5.Length > 13 && p5[13] != null && p5[13].nFrame > 0)
						{
							p5[13].drawFrame(0, impactX - ageDet * 6, impactY - 20, 0, 3, g);
							p5[13].drawFrame(0, impactX + ageDet * 6, impactY - 20, 2, 3, g);
							p5[13].drawFrame(0, impactX, impactY - 20 - ageDet * 4, 0, 3, g);
							p5[13].drawFrame(0, impactX, impactY - 20 + ageDet * 4, 2, 3, g);
						}
						if (p5.Length > 9 && p5[9] != null && p5[9].nFrame > 0 && ageDet <= 14)
						{
							p5[9].drawFrame(ageDet / 2 % p5[9].nFrame, impactX - 60, impactY + 4, 0, 3, g);
							p5[9].drawFrame(ageDet / 2 % p5[9].nFrame, impactX + 60, impactY + 4, 2, 3, g);
						}
					}

					// ─── TẦNG 14: MẢNH VẬT CHẤT TỐI BẮN RA 8 HƯỚNG & KHÓI BỤI SỤP ĐỔ (f: 50..70) ───
					if (f >= 50 && f <= 70)
					{
						int sProg = f - 50;
						// Bụi khói tiếp đất (p5[11]: 152)
						if (p5.Length > 11 && p5[11] != null && p5[11].nFrame > 0 && sProg <= 12)
						{
							int dF = Math.Min(p5[11].nFrame - 1, sProg / 2);
							p5[11].drawFrame(dF, impactX - 55, impactY + 4, 0, 3, g);
							p5[11].drawFrame(dF, impactX + 55, impactY + 4, 2, 3, g);
						}
						// 8 Tia mảnh vỡ hư vô bắn tỏa ra 8 góc (p5[10]: 104)
						if (p5.Length > 10 && p5[10] != null && p5[10].nFrame > 0)
						{
							for (int sp = 0; sp < 8; sp++)
							{
								int spAng = sp * 45;
								int spDist = sProg * 9;
								int sx = impactX + (spDist * CRes.getcos(spAng) >> 10);
								int sy = impactY - 25 + ((spDist * 3 / 4) * CRes.getsin(spAng) >> 10);
								p5[10].drawFrame((sProg + sp) % p5[10].nFrame, sx, sy, 0, 3, g);
							}
						}
					}
				}
			}
			catch (Exception)
			{
			}
		}

		// Paint Multi-Node Gradual Spatial Shatter & Full Screen Glass Break for Set 7 (Quake / Trấn Thiên)
		if (setId == 7 && this.VecEff != null)
		{
			int camX = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.xCam : 0;
			int camY = (MainScreen.cameraMain != null) ? MainScreen.cameraMain.yCam : 0;
			int screenW = GameCanvas.w > 0 ? GameCanvas.w : MotherCanvas.w;
			int screenH = GameCanvas.h > 0 ? GameCanvas.h : MotherCanvas.h;

			for (int i = 0; i < this.VecEff.size(); i++)
			{
				Point node = (Point)this.VecEff.elementAt(i);
				if (node != null)
				{
					int age = node.f - node.fSmall;
					if (age > 0 && age < 55)
					{
						FrameImage img = (node.color == 1 && fraImgSubEff != null) ? fraImgSubEff : fraImgEff;
						if (img != null && img.getImageFrame() != null)
						{
							int w = img.frameWidth;
							int h = img.frameHeight;
							if (w > 0 && h > 0)
							{
								int drawX = (node.subType == 2) ? (camX + screenW * node.x / 100) : node.x;
								int drawY = (node.subType == 2) ? (camY + screenH * node.y / 100) : node.y;

								// Subtle natural vibration on fracture lines
								if (age > 6 && age < 30 && (age % 3 == 0))
								{
									drawX += (age % 2 == 0 ? 1 : -1);
								}

								if (age < 5)
								{
									// Stage 1: Initial hairline fracture from crack center
									int clipW = Math.Max(1, w / 3);
									int clipH = Math.Max(1, h / 3);
									int srcX = (w - clipW) / 2;
									int srcY = (h - clipH) / 2;
									g.drawRegion(img.getImageFrame(), srcX, srcY, clipW, clipH, 0, drawX, drawY, 3);
								}
								else if (age < 14)
								{
									// Stage 2: Rapidly spiderwebbing cracks expanding outwards
									int clipW = Math.Max(1, w * 3 / 4);
									int clipH = Math.Max(1, h * 3 / 4);
									int srcX = (w - clipW) / 2;
									int srcY = (h - clipH) / 2;
									g.drawRegion(img.getImageFrame(), srcX, srcY, clipW, clipH, 0, drawX, drawY, 3);
								}
								else
								{
									// Stage 3: Full shattered glass pane & celestial spatial rupture
									g.drawRegion(img.getImageFrame(), 0, 0, w, h, 0, drawX, drawY, 3);
								}
							}
						}
					}
				}
			}
		}

		// Paint Set 6: Enel 200 Triệu Volt Thần Lôi — Pure Authentic Lightning Matrix, Aerial Teleport & 200M Volt El Thor
		if (setId == 6)
		{
			FrameImage[] p6 = s_ttFrames[6];
			if (p6 != null && p6.Length >= 8)
			{
				int casterX = (objFireMain != null) ? objFireMain.x : x;
				int casterY = (objFireMain != null) ? objFireMain.y : y;
				int casterDy = (objFireMain != null) ? objFireMain.dy : 0;
				int facingSign = (Dir == 2) ? 1 : -1;
				int cy = casterY + casterDy;

				int impactX = toX;
				int impactY = toY;
				if (impactX == 0 && impactY == 0)
				{
					impactX = casterX + facingSign * 140;
					impactY = casterY;
				}

				// ═══════════════════════════════════════════════════════════════════
				// LAYER 1: GROUND LIGHTNING MAGIC MATRIX (f: 4..68)
				// ═══════════════════════════════════════════════════════════════════
				if (f >= 4 && f <= 68)
				{
					// High-Voltage Ground Discharge Rings (p6[4]: 242, 49x28, 2f)
					if (p6[4] != null && p6[4].nFrame > 0)
					{
						int ringF = (f / 3) % p6[4].nFrame;
						// Epicenter ring
						p6[4].drawFrameNew(ringF, impactX, impactY + 4, 0, 3, g);
						// Perimeter discharge nodes (left, right, top, bottom, far-left, far-right)
						p6[4].drawFrameNew((ringF + 1) % p6[4].nFrame, impactX - 65, impactY + 4, 0, 3, g);
						p6[4].drawFrameNew((ringF + 2) % p6[4].nFrame, impactX + 65, impactY + 4, 2, 3, g);
						p6[4].drawFrameNew((ringF + 1) % p6[4].nFrame, impactX, impactY - 22, 0, 3, g);
						p6[4].drawFrameNew((ringF + 2) % p6[4].nFrame, impactX, impactY + 22, 2, 3, g);
						p6[4].drawFrameNew((ringF + 3) % p6[4].nFrame, impactX - 120, impactY + 4, 0, 3, g);
						p6[4].drawFrameNew((ringF + 3) % p6[4].nFrame, impactX + 120, impactY + 4, 2, 3, g);
					}

					// Ground Lightning Spiderweb Crackles (p6[3]: 241, 40x27, 2f)
					if (p6[3] != null && p6[3].maxNumFrame > 0)
					{
						int crackleF = (f % 2) * 2 + (GameCanvas.gameTick / 2 % 2);
						p6[3].drawFrameNew(crackleF, impactX, impactY + 8, 0, 3, g);
						p6[3].drawFrameNew((crackleF + 1) % 4, impactX - 70, impactY + 6, 0, 3, g);
						p6[3].drawFrameNew((crackleF + 2) % 4, impactX + 70, impactY + 6, 2, 3, g);
						p6[3].drawFrameNew((crackleF + 3) % 4, impactX - 130, impactY + 4, 0, 3, g);
						p6[3].drawFrameNew((crackleF) % 4, impactX + 130, impactY + 4, 2, 3, g);
					}

					// Locked target grounding rings (p6[4]: 242)
					if (p6[4] != null && p6[4].nFrame > 0 && vecObjsBeFire != null)
					{
						for (int k = 0; k < vecObjsBeFire.size() && k < 5; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
							if (tInfo != null)
							{
								MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
								if (tObj != null && !tObj.isDie && tObj.Hp > 0 && !tObj.isRemove)
								{
									int ringF = (f / 3 + k) % p6[4].nFrame;
									p6[4].drawFrameNew(ringF, tObj.x, tObj.y + 4, (k % 2 == 0 ? 0 : 2), 3, g);
								}
							}
						}
					}

					// Procedural Rotating Geometric Electric Matrix Polygons (Dual-layer Cyan & White Core)
					int rotAngle = (f * 5) % 360;
					g.setColor(0x00E5FF);
					for (int ang = 0; ang < 6; ang++)
					{
						int a1 = (rotAngle + ang * 60) % 360;
						int a2 = (rotAngle + (ang + 1) * 60) % 360;
						int px1 = impactX + (65 * CRes.getcos(a1) >> 10);
						int py1 = impactY + (39 * CRes.getsin(a1) >> 10);
						int px2 = impactX + (65 * CRes.getcos(a2) >> 10);
						int py2 = impactY + (39 * CRes.getsin(a2) >> 10);
						g.drawLine(px1, py1, px2, py2);

						int ox1 = impactX + (125 * CRes.getcos(a1) >> 10);
						int oy1 = impactY + (75 * CRes.getsin(a1) >> 10);
						int ox2 = impactX + (125 * CRes.getcos(a2) >> 10);
						int oy2 = impactY + (75 * CRes.getsin(a2) >> 10);
						g.drawLine(ox1, oy1, ox2, oy2);
						g.drawLine(px1, py1, ox1, oy1);
					}
					g.setColor(0xFFFFFF);
					for (int ang = 0; ang < 6; ang++)
					{
						int a1 = (rotAngle + ang * 60) % 360;
						int px1 = impactX + (65 * CRes.getcos(a1) >> 10);
						int py1 = impactY + (39 * CRes.getsin(a1) >> 10);
						g.fillRect(px1 - 1, py1 - 1, 3, 3);
					}
				}

				// ═══════════════════════════════════════════════════════════════════
				// LAYER 2: PROCEDURAL LIGHTNING MATRIX ARCS ("VẼ MA TRẬN SÉT") (f: 10..56)
				// ═══════════════════════════════════════════════════════════════════
				if (this.VecSubEff != null && f >= 10 && f <= 56)
				{
					int tickSeed = GameCanvas.gameTick;
					for (int e = 0; e < this.VecSubEff.size(); e++)
					{
						Point arc = (Point)this.VecSubEff.elementAt(e);
						if (arc != null && f >= arc.fSmall && f <= (arc.fSmall + arc.fRe))
						{
							int x1 = arc.x;
							int y1 = arc.y;
							int x2 = arc.x2;
							int y2 = arc.y2;

							// Compute 4-segment procedural electric zigzag displacement
							int jitter = ((tickSeed + arc.frame + e) % 3 - 1) * 6;
							int midX1 = x1 + (x2 - x1) / 4 + jitter;
							int midY1 = y1 + (y2 - y1) / 4 - (jitter / 2);
							int midX2 = x1 + (x2 - x1) / 2 - jitter;
							int midY2 = y1 + (y2 - y1) / 2 + (jitter / 2);
							int midX3 = x1 + (x2 - x1) * 3 / 4 + jitter;
							int midY3 = y1 + (y2 - y1) * 3 / 4 - (jitter / 2);

							// Outer High-Voltage Corona Line (Cyan / Electric Blue)
							g.setColor(arc.color);
							g.drawLine(x1, y1, midX1, midY1);
							g.drawLine(midX1, midY1, midX2, midY2);
							g.drawLine(midX2, midY2, midX3, midY3);
							g.drawLine(midX3, midY3, x2, y2);

							// Inner Searing White-Hot Core Line
							g.setColor(0xFFFFFF);
							g.drawLine(x1 + 1, y1, midX1 + 1, midY1);
							g.drawLine(midX1 + 1, midY1, midX2 + 1, midY2);
							g.drawLine(midX2 + 1, midY2, midX3 + 1, midY3);
							g.drawLine(midX3 + 1, midY3, x2 + 1, y2);

							// Starburst sparks at junction nodes
							if (p6[5] != null && p6[5].nFrame > 0 && ((f + e) % 3 == 0))
							{
								int sF = ((f / 2) + e) % p6[5].nFrame;
								p6[5].drawFrame(sF, midX2, midY2, 0, 3, g);
							}

							// High-voltage giant electric arc bursts (p6[7]: 92) at active nodes
							if (p6[7] != null && (f % 6 == 0) && e % 4 == 0)
							{
								p6[7].drawFrame(0, midX2, midY2 - 30, (e % 2 == 0 ? 0 : 2), 3, g);
							}
						}
					}

					// Swirling electric plasma sparks & mini-orbs drifting across matrix
					if (p6[5] != null && p6[5].nFrame > 0)
					{
						for (int em = 0; em < 6; em++)
						{
							int emAngle = (f * 14 + em * 60) % 360;
							int emDist = 45 + (em * 16);
							int emX = impactX + (emDist * CRes.getcos(emAngle) >> 10);
							int emY = impactY - 15 + ((emDist * 3 / 5) * CRes.getsin(emAngle) >> 10);
							int emF = (f / 2 + em) % p6[5].nFrame;
							p6[5].drawFrame(emF, emX, emY, (em % 2 == 0 ? 0 : 2), 3, g);
						}
					}
				}

				// ═══════════════════════════════════════════════════════════════════
				// LAYER 3: SEQUENTIAL SKY THUNDERBOLTS & CLIMAX 200M VOLT EL THOR
				// ═══════════════════════════════════════════════════════════════════
				if (this.VecEff != null)
				{
					for (int i = 0; i < this.VecEff.size(); i++)
					{
						Point strike = (Point)this.VecEff.elementAt(i);
						if (strike == null) continue;
						int age = f - strike.fSmall;

						// ─── TYPE 0: Diagonal Cascading Lightning Bolt ───
						if (strike.subType == 0)
						{
							// In-flight descending lightning fork (age -3..-1)
							if (age >= -3 && age < 0)
							{
								int startX = strike.x + (strike.color == 2 ? -80 : 80);
								int startY = strike.y - 240;
								int midX = (startX + strike.x) / 2 + (strike.color == 2 ? 15 : -15);
								int midY = (startY + strike.y) / 2;
								if (p6[1] != null && p6[1].maxNumFrame > 0)
								{
									int boltF1 = (f % 2) * 3 + ((age + 4) % 3);
									int boltF2 = ((f + 1) % 2) * 3 + ((age + 5) % 3);
									p6[1].drawFrameNew(boltF1, midX, midY, strike.color, 3, g);
									p6[1].drawFrameNew(boltF2, strike.x, strike.y - 20, strike.color == 2 ? 0 : 2, 3, g);
								}
							}
							// Ground Impact (age 0..8)
							else if (age >= 0 && age <= 8)
							{
								// Dust puff (p6[6]: 152)
								if (p6[6] != null && p6[6].nFrame > 0 && age <= 4)
								{
									int dustF = Math.Min(p6[6].nFrame - 1, age / 2);
									p6[6].drawFrame(dustF, strike.x, strike.y, strike.color, 3, g);
								}
								// Ground crackle (p6[3]: 241)
								if (p6[3] != null && p6[3].maxNumFrame > 0)
								{
									int crackleF = (strike.color == 2 ? 2 : 0) + (GameCanvas.gameTick / 2 % 2);
									p6[3].drawFrameNew(crackleF, strike.x, strike.y + 4, 0, 3, g);
								}
								// 3-Segment Vertical Lightning Column (p6[2]: 240)
								if (p6[2] != null && p6[2].maxNumFrame > 0 && age <= 6)
								{
									int colFlip = (age % 2 == 0) ? 0 : 2;
									for (int s = 0; s < 3; s++)
									{
										p6[2].drawFrameNew((f + s) % 2, strike.x, strike.y - s * 73, colFlip, 33, g);
									}
								}
								// Starburst Spark (p6[5]: 104)
								if (p6[5] != null && p6[5].nFrame > 0 && age <= 4)
								{
									p6[5].drawFrame(age / 2, strike.x, strike.y - 12, 0, 3, g);
								}
							}
						}

						// ─── TYPE 1: COLOSSAL 200 MILLION VOLT DIVINE BEAM (Climax at f = 36..60) ───
						else if (strike.subType == 1)
						{
							if (age >= 0 && age <= 24)
							{
								// Smooth beam expansion and taper
								int beamW;
								if (age <= 2)
								{
									beamW = 40 + (70 - 40) * age / 2;
								}
								else if (age <= 10)
								{
									beamW = 70 - 10 * (age - 2) / 8;
								}
								else if (age <= 18)
								{
									beamW = 60 - 30 * (age - 10) / 8;
								}
								else
								{
									beamW = Math.Max(8, 30 - 22 * (age - 18) / 6);
								}

								int beamJitter = (GameCanvas.gameTick % 2) << 1;
								int topY = strike.y - 420;

								// Layer 1: Deep Royal Plasma Blue Corona (0x0638E1)
								g.setColor(0x0638E1);
								g.fillRect(strike.x - beamW / 2, topY, beamW, 420);

								// Layer 2: Vivid Azure Cyan (0x007CEF)
								if (beamW > 10)
								{
									g.setColor(0x007CEF);
									g.fillRect(strike.x - (beamW - 10) / 2, topY, beamW - 10, 420);
								}

								// Layer 3: Electric Cyan (0x33B5E5)
								if (beamW > 20)
								{
									g.setColor(0x33B5E5);
									g.fillRect(strike.x - (beamW - 20) / 2, topY, beamW - 20, 420);
								}

								// Layer 4: Soft Celestial Glow (0xB1EDFC)
								if (beamW > 30)
								{
									g.setColor(0xB1EDFC);
									g.fillRect(strike.x - (beamW - 30) / 2 + beamJitter, topY, (beamW - 30) - (beamJitter << 1), 420);
								}

								// Layer 5: Ultra White-Cyan Glow (0xE0F7FA)
								if (beamW > 40)
								{
									g.setColor(0xE0F7FA);
									g.fillRect(strike.x - (beamW - 40) / 2 + beamJitter, topY, (beamW - 40) - (beamJitter << 1), 420);
								}

								// Layer 6: Searing White-Hot Core (0xFCFFFE)
								int coreW = Math.Max(6, beamW - 48);
								g.setColor(0xFCFFFE);
								g.fillRect(strike.x - coreW / 2 + beamJitter, topY, coreW, 420);

								// Layer 7: Pure White Center (0xFFFFFF)
								g.setColor(0xFFFFFF);
								g.fillRect(strike.x - 2 + (beamJitter / 2), topY, 4, 420);

								// Flanking Giant Lightning Columns (p6[2]: 240) stacked 5 segments high (365px tall)
								if (p6[2] != null && p6[2].maxNumFrame > 0 && age <= 18)
								{
									int flankFlip = (age % 2 == 0) ? 0 : 2;
									for (int s = 0; s < 5; s++)
									{
										p6[2].drawFrameNew((f + s) % 2, strike.x - 30, strike.y - s * 73, flankFlip, 33, g);
										p6[2].drawFrameNew((f + s + 1) % 2, strike.x + 30, strike.y - s * 73, flankFlip == 0 ? 2 : 0, 33, g);
									}
								}

								// Ground Mega Discharge Rings (p6[4]: 242)
								if (p6[4] != null && p6[4].nFrame > 0 && age <= 22)
								{
									int ringF = (GameCanvas.gameTick / 2) % p6[4].nFrame;
									p6[4].drawFrameNew(ringF, strike.x - 22, strike.y + 6, 0, 3, g);
									p6[4].drawFrameNew((ringF + 1) % p6[4].nFrame, strike.x + 22, strike.y + 6, 2, 3, g);
								}

								// Ground Spiderweb Lightning Crawl (p6[3]: 241)
								if (p6[3] != null && p6[3].maxNumFrame > 0 && age <= 22)
								{
									int cF = (GameCanvas.gameTick / 2) % 4;
									p6[3].drawFrameNew(cF, strike.x - 40, strike.y + 6, 0, 3, g);
									p6[3].drawFrameNew((cF + 1) % 4, strike.x + 40, strike.y + 6, 2, 3, g);
								}

								// Giant High-Voltage Electric Arc Spire (p6[7]: 92) surging at epicenter
								if (p6[7] != null && age >= 2 && age <= 18)
								{
									p6[7].drawFrame(0, strike.x, strike.y - 45, 0, 3, g);
									p6[7].drawFrame(0, strike.x - 25, strike.y - 35, 2, 3, g);
									p6[7].drawFrame(0, strike.x + 25, strike.y - 35, 0, 3, g);
								}

								// Exploding Starburst Sparks (p6[5]: 104)
								if (p6[5] != null && p6[5].nFrame > 0 && age <= 18)
								{
									int sparkF = (age / 2) % p6[5].nFrame;
									p6[5].drawFrame(sparkF, strike.x, strike.y - 18, 0, 3, g);
									p6[5].drawFrame((sparkF + 1) % p6[5].nFrame, strike.x - 28, strike.y - 35, 0, 3, g);
									p6[5].drawFrame((sparkF + 2) % p6[5].nFrame, strike.x + 28, strike.y - 35, 0, 3, g);
								}

								// Ground Dust Puffs (p6[6]: 152) surging outward
								if (p6[6] != null && p6[6].nFrame > 0 && age <= 14)
								{
									int dustF = Math.Min(p6[6].nFrame - 1, age / 3);
									p6[6].drawFrame(dustF, strike.x - 65, strike.y, 0, 3, g);
									p6[6].drawFrame(dustF, strike.x + 65, strike.y, 2, 3, g);
								}
							}
						}

						// ─── TYPE 2: Residual Aftershock Bolts ───
						else if (strike.subType == 2)
						{
							if (age >= 0 && age <= 8)
							{
								if (p6[3] != null && p6[3].maxNumFrame > 0)
								{
									p6[3].drawFrameNew((strike.color == 2 ? 2 : 0) + (GameCanvas.gameTick / 2 % 2), strike.x, strike.y + 4, strike.color, 3, g);
								}
								if (p6[2] != null && p6[2].maxNumFrame > 0 && age <= 5)
								{
									for (int s = 0; s < 2; s++)
									{
										p6[2].drawFrameNew((f + s) % 2, strike.x, strike.y - s * 73, strike.color, 33, g);
									}
								}
								if (p6[5] != null && p6[5].nFrame > 0 && age <= 4)
								{
									p6[5].drawFrame(age / 2, strike.x, strike.y - 10, 0, 3, g);
								}
							}
						}
					}
				}

				// ═══════════════════════════════════════════════════════════════════
				// LAYER 4: CASTER (ENEL) AERIAL RENDERING, CORONA & TELEPORTS (f: 0..68)
				// ═══════════════════════════════════════════════════════════════════
				// Ground discharge ring under caster when charging (f = 2..8)
				if (p6[4] != null && f >= 2 && f <= 8 && p6[4].nFrame > 0)
				{
					int ringF = (GameCanvas.gameTick / 2) % p6[4].nFrame;
					p6[4].drawFrameNew(ringF, casterX, casterY + 6, 0, 3, g);
				}

				// Ground lightning crackle directly beneath hovering Enel (f = 6..64)
				if (p6[3] != null && p6[3].maxNumFrame > 0 && f >= 6 && f <= 64)
				{
					int groundF = (f % 2) * 2 + (GameCanvas.gameTick / 2 % 2);
					p6[3].drawFrameNew(groundF, casterX, casterY + 6, 0, 3, g);
				}

				// Lightning Dash Flash Streak on Teleport frames (f = 6, 19, 32, 48, 68)
				if (f == 6 || f == 19 || f == 32 || f == 48 || f == 68)
				{
					g.setColor(0xFFFFFF);
					g.drawLine(casterX, cy, casterX, cy + 50);
					g.drawLine(casterX - 1, cy, casterX - 1, cy + 50);
					g.drawLine(casterX + 1, cy, casterX + 1, cy + 50);
					if (p6[5] != null && p6[5].nFrame > 0)
					{
						p6[5].drawFrame(0, casterX, cy, 0, 3, g);
					}
				}

				// Electric Corona Aura radiating around Enel while levitating
				if (f >= 4 && f <= 66)
				{
					int tick = GameCanvas.gameTick;
					g.setColor(0x00E5FF);
					for (int r = 0; r < 4; r++)
					{
						int rx = casterX + ((r % 2 == 0 ? -1 : 1) * (14 + (tick * 3 + r * 5) % 12));
						int ry = cy - 26 + ((r < 2 ? -1 : 1) * (8 + (tick * 2 + r * 7) % 14));
						g.drawLine(casterX, cy - 18, rx, ry);
					}
					g.setColor(0xFFFFFF);
					for (int r = 0; r < 4; r++)
					{
						int rx = casterX + ((r % 2 == 0 ? -1 : 1) * (14 + (tick * 3 + r * 5) % 12));
						int ry = cy - 26 + ((r < 2 ? -1 : 1) * (8 + (tick * 2 + r * 7) % 14));
						g.fillRect(rx - 1, ry - 1, 2, 2);
					}
				}

				// Zenith High-Voltage Arc Discharge summoning the El Thor (f = 28..44)
				if (p6[7] != null && f >= 28 && f <= 44)
				{
					p6[7].drawFrame(0, casterX, cy - 70, (f % 2 == 0 ? 0 : 2), 3, g);
				}

				// Triple Orbiting High-Voltage Lightning Spheres (p6[0]: 243)
				if (p6[0] != null && f >= 2 && f <= 66 && p6[0].nFrame > 0)
				{
					int baseAngle = (f * 18) % 360;
					for (int orbIdx = 0; orbIdx < 3; orbIdx++)
					{
						int orbAngle = (baseAngle + orbIdx * 120) % 360;
						int ox = casterX + (CRes.getcos(orbAngle) * 26 >> 10);
						int oy = cy - 16 + (CRes.getsin(orbAngle) * 10 >> 10);
						int orbF = ((f / 2) + orbIdx) % p6[0].nFrame;
						p6[0].drawFrame(orbF, ox, oy, 0, 3, g);
						if (p6[5] != null && p6[5].nFrame > 0)
						{
							p6[5].drawFrame(((f / 2) + orbIdx) % p6[5].nFrame, ox, oy - 2, 0, 3, g);
						}
					}
				}

				// Target Electrical Shock Sparks while floating / stunned (p6[5]: 104)
				if (p6[5] != null && f >= 36 && f <= 52 && p6[5].nFrame > 0 && vecObjsBeFire != null)
				{
					for (int k = 0; k < vecObjsBeFire.size(); k++)
					{
						Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
						if (tInfo == null) continue;
						MainObject t = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
						if (t == null || t.isDie || t.Hp <= 0 || t.isRemove) continue;
						int sparkF = (f / 2 + k) % p6[5].nFrame;
						p6[5].drawFrame(sparkF, t.x + (k % 2 == 0 ? -6 : 6), t.y + t.dy - 18, 0, 3, g);
					}
				}
			}
		}

		// Paint Set 8: Trafalgar Law - ROOM Gamma Knife (Phẫu thuật Ope Ope no Mi)
		// 393: ROOM sphere quanh nhân vật (1 frame 110x110)
		// 394: 3 frame dọc chia chuẩn xuất hiện ở chân nhân vật
		// 391: Hiệu ứng xuất hiện ở nửa trên mục tiêu dính
		// 392: 3 frame dọc tương tự 394 xuất hiện ở chân mục tiêu dính
		// Không vẽ bóng tròn ở mục tiêu; mục tiêu chỉ bay lên lơ lửng
		if (setId == 8)
		{
			int casterX = (objFireMain != null) ? objFireMain.x : x;
			int casterY = (objFireMain != null) ? objFireMain.y : y;
			int casterCenterY = casterY - (objFireMain != null ? (objFireMain.hOne / 2) : 22);

			// 1. Vòng sáng ma trận chân nhân vật tung chiêu (394: 3 frame dọc chia chuẩn)
			if (fraImgSubEff != null && fraImgSubEff.nFrame > 0 && f <= 65)
			{
				int f394 = (f / 3) % fraImgSubEff.nFrame;
				fraImgSubEff.drawFrame(f394, casterX, casterY, 0, 33, g);
			}

			// 2. Quả cầu ROOM bao quanh nhân vật tung chiêu (393: 110x110)
			if (fraImgEff != null && fraImgEff.nFrame > 0 && f >= 2 && f <= 65)
			{
				fraImgEff.drawFrame(0, casterX, casterCenterY, 0, 3, g);
			}

			// 3. Hiệu ứng trên mục tiêu dính đòn (vecObjsBeFire): 392 ở chân, 391 ở nửa trên, vết chém 358
			if (vecObjsBeFire != null && f >= 3 && f <= 68)
			{
				for (int k = 0; k < vecObjsBeFire.size(); k++)
				{
					Object_Effect_Skill targetInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (targetInfo == null) continue;
					MainObject target = MainObject.get_Object((int)targetInfo.ID, (sbyte)targetInfo.tem);
					if (target != null && !target.isDie && target.Hp > 0 && !target.isRemove)
					{
						int targetFeetY = target.y - target.dy;
						int targetH = (target.hOne > 0) ? target.hOne : 45;
						int targetUpperY = targetFeetY - (targetH * 3 / 4);

						// 392: 3 frame dọc chia chuẩn xuất hiện ở chân mục tiêu dính
						if (fraImgSub3Eff != null && fraImgSub3Eff.nFrame > 0)
						{
							int f392 = (f / 3) % fraImgSub3Eff.nFrame;
							fraImgSub3Eff.drawFrame(f392, target.x, targetFeetY, 0, 33, g);
						}

						// 391: Xuất hiện ở nửa trên mục tiêu dính
						if (fraImgSub2Eff != null && fraImgSub2Eff.nFrame > 0)
						{
							fraImgSub2Eff.drawFrame(0, target.x, targetUpperY, 0, 3, g);
						}

						// 358: Vết chém nhỏ ngẫu nhiên trên mục tiêu
						if (fraImgSub4Eff != null && fraImgSub4Eff.nFrame > 0 && f >= 4 && f <= 56)
						{
							int slashF = (f / 2) % fraImgSub4Eff.nFrame;
							int seed = (k * 13 + f * 17);
							int offX = (seed % 25) - 12;
							int offY = ((seed / 3) % 21) - 10;
							int trans = (seed % 2 == 0) ? 0 : 2;
							int targetCenterY = targetFeetY - (targetH / 2);
							fraImgSub4Eff.drawFrame(slashF, target.x + offX, targetCenterY + offY, trans, 3, g);
						}
					}
				}
			}
		}

		// ─── setId 12: Phượng Hoàng Bất Tử Bộc Phá _X (Marco Blue Phoenix Climax) — PAINT ───
		if (setId == 12)
		{
			FrameImage[] p12 = s_ttFrames[12];
			if (p12 != null && p12.Length >= 12)
			{
				int facingSign = (this.phoenixTargetX >= this.phoenixOrigX) ? 1 : -1;
				int pDir = (facingSign == 1) ? 2 : 0;

				// ─── LỚP HẠT PARTICLES DƯỚI (VecSubEff: lông vũ & tàn lửa) ───
				if (this.VecSubEff != null)
				{
					for (int i = 0; i < this.VecSubEff.size(); i++)
					{
						Point p = (Point)this.VecSubEff.elementAt(i);
						if (p != null && p.f < p.fRe)
						{
							if (p.subType == 0 && p12[9] != null && p12[9].nFrame > 0)
							{
								// Sacred Rebirth Feathers (224, 22x28, 5f)
								int featherF = (p.f / 3) % p12[9].nFrame;
								p12[9].drawFrame(featherF, p.x, p.y, p.color, 3, g);
							}
							else if (p12[8] != null && p12[8].nFrame > 0)
							{
								// Blue Ground Fire Sparks (241, 40x27, 2f)
								int sparkF = (p.f / 2) % p12[8].nFrame;
								p12[8].drawFrame(sparkF, p.x, p.y, p.color, 3, g);
							}
						}
					}
				}

				// ─── GIAI ĐOẠN 1: TỤ KHÍ & THỨC TỈNH (f = 4..16) ───
				if (f >= 4 && f < 18)
				{
					int castF = f - 4; // 0..13

					// Caster Aura (p12[0]: 474, 120x100, 8f, anchor 33)
					if (p12[0] != null && p12[0].nFrame > 0)
					{
						int auraF = (castF / 2) % p12[0].nFrame;
						p12[0].drawFrame(auraF, this.phoenixOrigX, this.phoenixOrigY, pDir, 33, g);
					}

					// Awakening Phoenix Silhouette behind Caster (p12[1]: 475, 240x180, 8f, anchor 33)
					if (p12[1] != null && p12[1].nFrame > 0 && castF >= 2)
					{
						int awkF = ((castF - 2) / 2) % p12[1].nFrame;
						p12[1].drawFrame(awkF, this.phoenixOrigX + (facingSign * 10), this.phoenixOrigY - 20, pDir, 33, g);
					}

					// Swirling Solar Blue Flame Core (p12[6]: 243, 36x39)
					if (p12[6] != null)
					{
						p12[6].drawFrame(0, this.phoenixOrigX, this.phoenixOrigY - 25, 0, 3, g);
					}
				}

				// ─── GIAI ĐOẠN 2: PHƯỢNG HOÀNG PHI THIÊN PROJECTILE (f = 16..34) ───
				if (f >= 16 && f <= 34)
				{
					int t = f - 16; // 0..18
					int totalT = 18;

					// Interpolation Parabol từ phoenixOrigX/Y tới phoenixTargetX/Y
					int curX = this.phoenixOrigX + (this.phoenixTargetX - this.phoenixOrigX) * t / totalT;
					int linearY = (this.phoenixOrigY - 25) + ((this.phoenixTargetY - 15) - (this.phoenixOrigY - 25)) * t / totalT;
					// Parabola arc: bay vút lên rồi cắm xuống: 4 * h * (t / totalT) * (1 - t / totalT)
					int arcH = 45;
					int curY = linearY - (4 * arcH * t * (totalT - t)) / (totalT * totalT);

					// Ground Shadow (p12[10]: 272, 50x24)
					if (p12[10] != null)
					{
						p12[10].drawFrame(0, curX, this.phoenixOrigY + 2, 0, 3, g);
					}

					// Flame Shockwave trail behind bird (p12[7]: 242, 49x28)
					if (p12[7] != null && p12[7].nFrame > 0 && t % 3 == 0)
					{
						p12[7].drawFrame((t / 3) % p12[7].nFrame, curX - facingSign * 35, curY + 10, pDir, 3, g);
					}

					// Phượng Hoàng Phi Thiên Projectile (p12[2]: 476, 180x80, 8f, anchor 3)
					if (p12[2] != null && p12[2].nFrame > 0)
					{
						int projF = (t / 2) % p12[2].nFrame;
						p12[2].drawFrame(projF, curX, curY, pDir, 3, g);
					}

					// Solar Blue Flame Core in projectile center (p12[6]: 243, 36x39)
					if (p12[6] != null)
					{
						p12[6].drawFrame(0, curX, curY, 0, 3, g);
					}
				}

				// ─── GIAI ĐOẠN 3: CLIMAX IMPACT _X (f = 32..48) ───
				if (f >= 32 && f <= 48)
				{
					int impAge = f - 32; // 0..16

					// Ground Shockwave Ring (p12[7]: 242, 49x28, 2f)
					if (p12[7] != null && p12[7].nFrame > 0 && impAge <= 12)
					{
						int ringF = (impAge / 2) % p12[7].nFrame;
						p12[7].drawFrame(ringF, this.phoenixTargetX - 35, this.phoenixTargetY + 5, 0, 3, g);
						p12[7].drawFrame((ringF + 1) % p12[7].nFrame, this.phoenixTargetX + 35, this.phoenixTargetY + 5, 2, 3, g);
					}

					// Ground Fire Radiating (p12[8]: 241, 40x27, 2f)
					if (p12[8] != null && p12[8].nFrame > 0 && impAge <= 14)
					{
						int fireF = (impAge / 2) % p12[8].nFrame;
						p12[8].drawFrame(fireF, this.phoenixTargetX, this.phoenixTargetY + 4, 0, 3, g);
						p12[8].drawFrame((fireF + 1) % p12[8].nFrame, this.phoenixTargetX - 60, this.phoenixTargetY + 4, 0, 3, g);
						p12[8].drawFrame((fireF + 1) % p12[8].nFrame, this.phoenixTargetX + 60, this.phoenixTargetY + 4, 2, 3, g);
					}

					// Lam Hỏa Đại Bộc Phá _X Impact (p12[3]: 477, 240x240, 10f, anchor 3)
					if (p12[3] != null && p12[3].nFrame > 0)
					{
						int impF = impAge;
						if (impF >= p12[3].nFrame) impF = p12[3].nFrame - 1;
						p12[3].drawFrame(impF, this.phoenixTargetX, this.phoenixTargetY - 40, pDir, 3, g);
					}

					// Starburst Sparks & Flash (p12[11]: 104, 30x30)
					if (p12[11] != null && impAge <= 8)
					{
						int sparkF = (impAge / 2) % 3;
						p12[11].drawFrame(sparkF, this.phoenixTargetX - 35, this.phoenixTargetY - 60, 0, 3, g);
						p12[11].drawFrame((sparkF + 1) % 3, this.phoenixTargetX + 35, this.phoenixTargetY - 60, 0, 3, g);
					}
				}

				// ─── GIAI ĐOẠN 4: TUNG CÁNH PHƯỢNG HOÀNG FINISHER (f = 40..64) ───
				if (f >= 40 && f <= 64)
				{
					int wingAge = f - 40; // 0..24

					// Finisher Wings Sweep (p12[4]: 478, 280x200, 12f, anchor 33)
					if (p12[4] != null && p12[4].nFrame > 0)
					{
						int wingF = wingAge / 2;
						if (wingF >= p12[4].nFrame) wingF = p12[4].nFrame - 1;
						p12[4].drawFrame(wingF, this.phoenixTargetX, this.phoenixTargetY, pDir, 33, g);
					}

					// Swirling Solar Core in chest of Wings (p12[6]: 243, 36x39)
					if (p12[6] != null && wingAge <= 16)
					{
						p12[6].drawFrame(0, this.phoenixTargetX, this.phoenixTargetY - 50, 0, 3, g);
					}
				}

				// ─── GIAI ĐOẠN 5: CỘT LAM HỎA THIÊU ĐỐT TRÊN MỤC TIÊU (f = 46..66) ───
				if (f >= 46 && f <= 66)
				{
					int geyAge = f - 46; // 0..20
					if (p12[5] != null && p12[5].nFrame > 0)
					{
						int geyF = (geyAge / 2);
						if (geyF >= p12[5].nFrame) geyF = p12[5].nFrame - 1;

						if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
						{
							for (int k = 0; k < vecObjsBeFire.size() && k < 5; k++)
							{
								Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
								if (tInfo == null) continue;
								MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
								if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
								if (Math.Abs(target.x - this.phoenixTargetX) <= 140)
								{
									p12[5].drawFrame(geyF, target.x, target.y, 0, 33, g);
								}
							}
						}
						else
						{
							p12[5].drawFrame(geyF, this.phoenixTargetX, this.phoenixTargetY, 0, 33, g);
						}
					}
				}
			}
		}

		// ─── Paint Set 9: Eustass Kid — Từ Trường Bộc Phá Đại Pháo _X (Straight Laser & Exploding Light Pillar) ───
		if (setId == 9)
		{
			FrameImage[] p9 = (s_ttFrames != null && s_ttFrames.Length > 9) ? s_ttFrames[9] : null;
			if (p9 != null && p9.Length >= 10)
			{
				int casterX = x;
				int casterY = y;
				int facingSign = (Dir == 2) ? 1 : -1;
				int impactX = (this.kidCannonImpactX != 0) ? this.kidCannonImpactX : toX;
				int impactY = (this.kidCannonImpactY != 0) ? this.kidCannonImpactY : toY;

				int cannonBaseX = casterX + facingSign * 35;
				int cannonBaseY = casterY - 26;

				// 1. Vòng định vị từ tính dưới chân mục tiêu (f < 18)
				if (f < 18 && p9[9] != null && p9[9].nFrame > 0)
				{
					p9[9].drawFrame(0, impactX, impactY + 4, 0, 3, g);
				}

				// 2. Cấu trúc nòng pháo kim loại (f < 32)
				if (f < 32)
				{
					// Layer 0: Các vũ khí lớp sau (color == 0)
					if (this.VecEff != null)
					{
						for (int i = 0; i < this.VecEff.size(); i++)
						{
							Point p = (Point)this.VecEff.elementAt(i);
							if (p != null && p.color == 0)
							{
								paintKidWeapon(g, p, p9);
							}
						}
					}

					// Lõi plasma từ trường quay cuồng ở khóa nòng (p9[0]: 243)
					if (f >= 8 && p9[0] != null && p9[0].nFrame > 0)
					{
						int coreF = (f / 2) % p9[0].nFrame;
						p9[0].drawFrame(coreF, cannonBaseX - facingSign * 10, cannonBaseY, 0, 3, g);
					}

					// Tia sét hồ quang điện từ giữa các thanh ray (p9[1]: 92)
					if (f >= 8 && p9[1] != null && p9[1].nFrame > 0)
					{
						for (int k = 0; k < 3; k++)
						{
							int arcX = cannonBaseX + facingSign * (k * 14);
							int arcY = cannonBaseY + ((k % 2 == 0) ? -12 : 12);
							int arcF = (f / 2 + k) % p9[1].nFrame;
							p9[1].drawFrame(arcF, arcX, arcY, (k % 2 == 0 ? 0 : 2), 3, g);
						}
					}

					// Layer 1: Các vũ khí lớp trước (color != 0)
					if (this.VecEff != null)
					{
						for (int i = 0; i < this.VecEff.size(); i++)
						{
							Point p = (Point)this.VecEff.elementAt(i);
							if (p != null && p.color != 0)
							{
								paintKidWeapon(g, p, p9);
							}
						}
					}

					// Vòng sóng nén họng pháo khi bắn (f: 14..32)
					int muzzleX = cannonBaseX + facingSign * 45;
					int muzzleY = cannonBaseY;
					if (f >= 14 && f <= 32)
					{
						if (p9[3] != null && p9[3].nFrame > 0)
						{
							int ringF = ((f - 14) / 2) % p9[3].nFrame;
							p9[3].drawFrame(ringF, muzzleX, muzzleY, 0, 3, g);
						}
					}
				}
				else
				{
					// Phase 5: Tán xạ các mảnh kim loại ra xa (f >= 32)
					if (this.VecEff != null)
					{
						for (int i = 0; i < this.VecEff.size(); i++)
						{
							Point p = (Point)this.VecEff.elementAt(i);
							if (p != null)
							{
								paintKidWeapon(g, p, p9);
								if (f % 2 == 0 && p9[2] != null && p9[2].nFrame > 0)
								{
									p9[2].drawFrame(0, p.x, p.y - 4, 0, 3, g);
								}
							}
						}
					}
				}

				// 3. TIA LAZE BẮN THEO HƯỚNG MỤC TIÊU THẲNG TẮP (f: 14..32)
				if (f >= 14 && f < 32)
				{
					int muzzleX = cannonBaseX + facingSign * 45;
					int muzzleY = cannonBaseY;
					int endX = impactX + facingSign * 35;
					int endY = impactY;

					int dx = endX - muzzleX;
					int dy = endY - muzzleY;
					int beamDist = (int)System.Math.Sqrt(dx * dx + dy * dy);
					if (beamDist <= 0) beamDist = 1;

					// Vector pháp tuyến đơn vị tỉ lệ 1000
					int perpX = (-dy * 1000) / beamDist;
					int perpY = (dx * 1000) / beamDist;

					// Độ dày chùm tia nở rộng rồi thu gọn
					int beamW;
					if (f < 18)
					{
						beamW = 8 + (f - 14) * 4;
					}
					else if (f < 26)
					{
						beamW = 24 + ((f % 2) * 4);
					}
					else
					{
						beamW = Math.Max(2, 24 - (f - 26) * 4);
					}

					// Tầng 1: Hào quang điện từ xanh ngoại vi (0x007CEF)
					g.setColor(0x007CEF);
					for (int w = -beamW / 2; w <= beamW / 2; w += 2)
					{
						int ox = (w * perpX) / 1000;
						int oy = (w * perpY) / 1000;
						g.drawLine(muzzleX + ox, muzzleY + oy, endX + ox, endY + oy);
					}

					// Tầng 2: Chùm tia Neon Cyan từ tính (0x00E5FF)
					int innerW = (beamW * 3) / 5;
					if (innerW > 4)
					{
						g.setColor(0x00E5FF);
						for (int w = -innerW / 2; w <= innerW / 2; w += 2)
						{
							int ox = (w * perpX) / 1000;
							int oy = (w * perpY) / 1000;
							g.drawLine(muzzleX + ox, muzzleY + oy, endX + ox, endY + oy);
						}
					}

					// Tầng 3: Lõi năng lượng trắng sáng chói lòa (0xFFFFFF)
					int coreW = Math.Max(2, beamW / 3);
					g.setColor(0xFFFFFF);
					for (int w = -coreW / 2; w <= coreW / 2; w++)
					{
						int ox = (w * perpX) / 1000;
						int oy = (w * perpY) / 1000;
						g.drawLine(muzzleX + ox, muzzleY + oy, endX + ox, endY + oy);
					}

					// Vòng sóng nén lao dọc theo tia laze
					if (p9[3] != null && p9[3].nFrame > 0)
					{
						for (int s = 0; s < 3; s++)
						{
							int progress = ((f * 50 + s * 333) % 1000);
							int sx = muzzleX + (dx * progress) / 1000;
							int sy = muzzleY + (dy * progress) / 1000;
							p9[3].drawFrame((f + s) % p9[3].nFrame, sx, sy, 0, 3, g);
						}
					}
				}

				// 4. CỘT SÁNG NỔ ĐI TẠI MỤC TIÊU (f: 18..44)
				if (f >= 18 && f < 44)
				{
					int pillarAge = f - 18;
					int topY = impactY - 400;

					int pillW;
					if (pillarAge <= 3)
					{
						pillW = 28 + pillarAge * 14;
					}
					else if (pillarAge <= 11)
					{
						pillW = 70 - (pillarAge - 4) * 2;
					}
					else
					{
						pillW = Math.Max(4, 56 - (pillarAge - 12) * 5);
					}

					int jitter = (GameCanvas.gameTick % 2) << 1;

					// Tầng 1: Hào quang tím xanh đậm (0x0638E1)
					g.setColor(0x0638E1);
					g.fillRect(impactX - pillW / 2, topY, pillW, 400);

					// Tầng 2: Xanh lam từ trường (0x007CEF)
					if (pillW > 10)
					{
						g.setColor(0x007CEF);
						g.fillRect(impactX - (pillW - 10) / 2, topY, pillW - 10, 400);
					}

					// Tầng 3: Laser Neon Cyan rực rỡ (0x00E5FF)
					if (pillW > 20)
					{
						g.setColor(0x00E5FF);
						g.fillRect(impactX - (pillW - 20) / 2, topY, pillW - 20, 400);
					}

					// Tầng 4: Ánh sáng trắng-cyan mềm mại (0xB1EDFC)
					if (pillW > 32)
					{
						g.setColor(0xB1EDFC);
						g.fillRect(impactX - (pillW - 32) / 2 + jitter, topY, (pillW - 32) - (jitter << 1), 400);
					}

					// Tầng 5: Lõi nhiệt hạch trắng tinh khiết (0xFCFFFE)
					int coreW = Math.Max(4, pillW - 46);
					g.setColor(0xFCFFFE);
					g.fillRect(impactX - coreW / 2 + jitter, topY, coreW, 400);

					// Trụ cột quang năng bên trong cột sáng (p9[5]: 240, 30x73)
					if (p9[5] != null && p9[5].nFrame > 0)
					{
						int flip = (pillarAge % 2 == 0) ? 0 : 2;
						for (int seg = 0; seg < 5; seg++)
						{
							p9[5].drawFrame(0, impactX, impactY - seg * 73, flip, 33, g);
						}
					}

					// Tia lửa ma sát mặt đất tại tâm nổ (p9[2]: 104)
					if (pillarAge <= 8 && p9[2] != null && p9[2].nFrame > 0)
					{
						p9[2].drawFrame(pillarAge / 2, impactX, impactY - 12, 0, 3, g);
					}
				}
			}
		}

		// ─── setId 10: Cổ Độc Phán Quyết Venom _X (Magellan Hell's Judgment) — CINEMATIC PAINT ───
		if (setId == 10)
		{
			FrameImage[] p10 = (s_ttFrames != null && s_ttFrames.Length > 10) ? s_ttFrames[10] : null;
			if (p10 != null && p10.Length >= 7)
			{
				int casterX = (objFireMain != null) ? objFireMain.x : x;
				int casterY = (objFireMain != null) ? objFireMain.y : y;
				int casterDir = Dir;

				// Vị trí mục tiêu cố định 1 vị trí đã chốt từ lúc thi triển skill (không trôi theo quái)
				int targetX = (this.VecEff != null && this.VecEff.size() > 0) ? ((Point)this.VecEff.elementAt(0)).x : toX;
				int targetY = (this.VecEff != null && this.VecEff.size() > 0) ? ((Point)this.VecEff.elementAt(0)).y : toY;
				if (targetX == 0 && targetY == 0)
				{
					targetX = casterX + (casterDir == 2 ? 140 : -140);
					targetY = casterY;
				}

				// GROUND POOL EFFECT (subType == 1004): Vũng axit độc cố định 1 vị trí tại x, y mặt đất
				if (this.subType == 1004)
				{
					if (f >= 35 && f <= 70 && p10[4] != null && p10[4].nFrame > 0)
					{
						int poolFrame = Math.Min(p10[4].nFrame - 1, (f - 35) * p10[4].nFrame / 35);
						p10[4].drawFrame(poolFrame, x, y, 0, 3, g);
					}
					return;
				}

				// 2. PHASE 1: CASTER AURA (f = 5..25) — Toxic aura around caster feet
				if (f >= 5 && f <= 25 && p10[0] != null && p10[0].nFrame > 0)
				{
					int auraFrame = (f - 5) * p10[0].nFrame / 20;
					if (auraFrame >= p10[0].nFrame) auraFrame = p10[0].nFrame - 1;
					p10[0].drawFrame(auraFrame, casterX, casterY, 0, 33, g);
				}

				// 3. PHASE 2 & 3: HYDRA SUMMON (f = 10..30) — Colossal 3-headed poison dragon
				if (f >= 10 && f <= 30 && p10[1] != null && p10[1].nFrame > 0)
				{
					int hydraFrame = Math.Min(p10[1].nFrame - 1, (f - 10) * p10[1].nFrame / 20);
					int hTrans = (casterDir == 2) ? 0 : 2;
					p10[1].drawFrame(hydraFrame, casterX, casterY, hTrans, 33, g);
				}

				// LỚP 4: ẢO ẢNH PHANTOM MAGELLAN (f = 10..26) — Avatar chỉ huy đứng sát lưng Caster
				if (f >= 10 && f <= 26 && p10[6] != null && p10[6].nFrame > 0)
				{
					int mPose = (f <= 17) ? 1 : 2; // Pose 1: Gồng trượng, Pose 2: Đâm trượng chỉ định
					p10[6].drawFrame(mPose, casterX, casterY, (casterDir == 2 ? 0 : 2), 33, g);
				}

				// 5. PHASE 4: VENOM PROJECTILE TRAVEL (f = 24..35) — Flying poison dragon head
				if (f >= 24 && f <= 35 && p10[2] != null && p10[2].nFrame > 0)
				{
					int pProg = f - 24; // 0..11
					int startX = casterX;
					int startY = casterY - 30;
					int endY = targetY - 25;
					int curX = startX + (targetX - startX) * pProg / 11;
					int arc = CRes.getsin(pProg * 180 / 11) * 35 >> 10;
					int curY = startY + (endY - startY) * pProg / 11 - arc;
					int projFrame = (pProg * p10[2].nFrame / 12) % p10[2].nFrame;
					int pTrans = (targetX >= startX) ? 0 : 2;
					p10[2].drawFrame(projFrame, curX, curY, pTrans, 3, g);
				}

				// 6. PHASE 5: VENOM IMPACT X CATACLYSM (f = 35..48) — Giant purple/magenta X explosion
				if (f >= 35 && f <= 48 && p10[3] != null && p10[3].nFrame > 0)
				{
					int impFrame = Math.Min(p10[3].nFrame - 1, (f - 35) * p10[3].nFrame / 13);
					p10[3].drawFrame(impFrame, targetX, targetY - 20, 0, 33, g);
				}

				// 7. PHASE 7: POISON BURST GEYSERS (f = 46..68) — Erupting skull geysers at each target
				if (f >= 46 && f <= 68 && p10[5] != null && p10[5].nFrame > 0)
				{
					int burstFrame = Math.Min(p10[5].nFrame - 1, (f - 46) * p10[5].nFrame / 22);
					if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
					{
						for (int k = 0; k < vecObjsBeFire.size() && k < 6; k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
							if (tInfo == null) continue;
							MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
							if (tObj == null || tObj.isDie || tObj.Hp <= 0 || tObj.isRemove) continue;
							int tBurstF = Math.Min(p10[5].nFrame - 1, Math.Max(0, burstFrame - (k % 2)));
							p10[5].drawFrame(tBurstF, tObj.x, tObj.y, 0, 3, g);
						}
					}
					else
					{
						p10[5].drawFrame(burstFrame, targetX, targetY, 0, 3, g);
					}
				}
			}
		}

		// Paint Set 13: Đại Phật Sóng Xung Kích _X - Sengoku Daibutsu Authentic Golden Shockwave
		if (setId == 13)
		{
			FrameImage[] parts = (s_ttFrames != null && s_ttFrames.Length > 13) ? s_ttFrames[13] : null;
			if (parts != null && parts.Length >= 15)
			{
				int casterX = (this.daibutsuCasterX != 0) ? this.daibutsuCasterX : ((objFireMain != null) ? objFireMain.x : x);
				int casterY = (this.daibutsuCasterY != 0) ? this.daibutsuCasterY : ((objFireMain != null) ? objFireMain.y : y);
				int impactX = (this.daibutsuImpactX != 0) ? this.daibutsuImpactX : toX;
				int impactY = (this.daibutsuImpactY != 0) ? this.daibutsuImpactY : toY;
				int dirTrans = (Dir == 2) ? 0 : 2;
				int facingSign = (Dir == 2) ? 1 : -1;

				if (impactX == 0 && impactY == 0)
				{
					impactX = casterX + facingSign * 180;
					impactY = casterY - 15;
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 1: Ground Seismic Crevasses & Earth Dust Upheaval
				// ─────────────────────────────────────────────────────────────
				// Ground crevasses under caster feet during windup (f <= 22, parts[9]: 246, 49x21)
				if (f <= 22 && parts[9] != null)
				{
					parts[9].drawFrameNew((f / 2) % 4, casterX, casterY + 6, 0, 3, g);
					parts[9].drawFrameNew(((f / 2) + 1) % 4, casterX + facingSign * 35, casterY + 6, dirTrans, 3, g);
				}
				// Ground crevasses under impact epicenter (f 20..44, parts[9]: 246, 49x21)
				if (f >= 20 && f <= 44 && parts[9] != null)
				{
					int fissF = ((f - 20) / 2) % 4;
					parts[9].drawFrameNew(fissF, impactX, impactY + 12, 0, 3, g);
					parts[9].drawFrameNew((fissF + 1) % 4, impactX - 45, impactY + 10, 0, 3, g);
					parts[9].drawFrameNew((fissF + 2) % 4, impactX + 45, impactY + 10, 2, 3, g);
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 2: Caster Daibutsu Manifestation: Shimmering Aura, Dharma Nimbus & Core
				// ─────────────────────────────────────────────────────────────
				// Golden Shimmering Transformation Aura (f <= 25, parts[13]: 267, 47x53)
				if (f <= 25 && parts[13] != null)
				{
					int aFlip = (f % 2 == 0) ? dirTrans : (dirTrans == 0 ? 2 : 0);
					int aJitterX = (f % 2 == 0) ? 1 : -1;
					parts[13].drawFrame(0, casterX + aJitterX, casterY - 20, aFlip, 3, g);
					parts[13].drawFrame(0, casterX, casterY - 20, dirTrans, 3, g);
				}
				// Sacred Dharma Wheel Nimbus (Pháp Luân Kim Quang) spinning smoothly behind caster (f <= 26, parts[4]: 357, 100x100)
				if (f <= 26 && parts[4] != null)
				{
					int haloF = (f / 2) % 4;
					int haloBobY = (CRes.getsin((f * 36) % 360) * 3) >> 10;
					parts[4].drawFrameNew(haloF, casterX - facingSign * 10, casterY - 38 + haloBobY, 0, 3, g);
				}
				// Condensed Golden Solar Core in palm during windup (f <= 11, parts[6]: 174, 40x40)
				if (f <= 11 && parts[6] != null)
				{
					int coreF = f % 8; // High-speed plasma spin (1 tick per frame)
					int coreX = casterX + facingSign * (18 + (f * 6 / 11));
					int coreY = casterY - 20;
					parts[6].drawFrameNew(coreF, coreX, coreY, 0, 3, g);
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 3: Kim Cương Phật Chưởng Thrust & Shockwave Burst Snap (f 10..22)
				// ─────────────────────────────────────────────────────────────
				if (f >= 10 && f <= 22 && parts[0] != null)
				{
					int palmProgress = f - 10;
					int palmOffset;
					if (palmProgress <= 0) palmOffset = 0;
					else if (palmProgress == 1) palmOffset = 24;
					else if (palmProgress == 2) palmOffset = 50;
					else if (palmProgress == 3) palmOffset = 68;
					else palmOffset = Math.Min(78, 68 + (palmProgress - 3) * 2);

					int palmX = casterX + facingSign * (26 + palmOffset);
					int palmY = casterY - 20;
					int palmF = Math.Min(3, palmProgress / 2);

					// Dynamic Kinetic Ghost Trails (After-Images)
					if (palmProgress >= 1 && palmProgress <= 6)
					{
						parts[0].drawFrame(Math.Max(0, palmF - 1), palmX - facingSign * 16, palmY, dirTrans, 3, g);
						parts[0].drawFrame(Math.Max(0, palmF - 2), palmX - facingSign * 32, palmY, dirTrans, 3, g);
					}
					// Main Golden Palm Thrust Wave (parts[0]: 416, 78x40)
					parts[0].drawFrame(palmF, palmX, palmY, dirTrans, 3, g);

					// Palm Blast Burst Cone pulsing with rapid alternation (parts[7]: 335, 80x80)
					if (parts[7] != null && f >= 11 && f <= 20)
					{
						int blastF = (f - 11) % 4;
						parts[7].drawFrameNew(blastF, palmX + facingSign * 32, palmY, dirTrans, 3, g);
					}
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 4: Progressive Traveling Kinetic Shockwave with Cubic Easing Out (f 12..21)
				// ─────────────────────────────────────────────────────────────
				if (f >= 12 && f <= 21 && parts[1] != null)
				{
					int startX = casterX + facingSign * 48;
					int t = f - 12; // 0..9
					// Cubic Ease-Out acceleration: 0%, 34%, 58%, 75%, 88%, 95%, 98%, 100%
					int easeRatio;
					if (t <= 0) easeRatio = 0;
					else if (t == 1) easeRatio = 87;
					else if (t == 2) easeRatio = 148;
					else if (t == 3) easeRatio = 193;
					else if (t == 4) easeRatio = 224;
					else if (t == 5) easeRatio = 243;
					else if (t == 6) easeRatio = 252;
					else if (t == 7) easeRatio = 255;
					else easeRatio = 256;

					int distX = impactX - startX;
					int distY = impactY - (casterY - 20);
					int ringX = startX + (distX * easeRatio) / 256;
					int ringY = (casterY - 20) + (distY * easeRatio) / 256;
					int waveJitter = (t % 2 == 0) ? 1 : -1;
					int ringF = Math.Min(3, t / 2);

					// 1. Leading Compression Arc Wave (parts[0]: 416)
					if (parts[0] != null)
					{
						parts[0].drawFrame(ringF, ringX + facingSign * 24, ringY, dirTrans, 3, g);
					}

					// 2. Primary Golden Shockwave Ring (parts[1]: 171, 153x84) with vibration
					parts[1].drawFrame(ringF, ringX, ringY + waveJitter, dirTrans, 3, g);

					// 3. Secondary Concentric Echo Ring trailing 24px behind
					if (t >= 1)
					{
						int echoX = ringX - facingSign * 24;
						parts[1].drawFrame(Math.Max(0, ringF - 1), echoX, ringY, dirTrans, 3, g);
					}

					// 4. Ground Elliptical Ring sweeping along floor (parts[3]: 394, 126x41)
					if (parts[3] != null)
					{
						parts[3].drawFrame(t % 3, ringX, casterY + 2, 0, 3, g);
					}
					// 5. Space Distortion Wave vibrating atmosphere (parts[10]: 285, 111x90)
					if (parts[10] != null)
					{
						int dFlip = (t % 2 == 0) ? dirTrans : (dirTrans == 0 ? 2 : 0);
						parts[10].drawFrame((t * 2) % 3, ringX - facingSign * 10, ringY, dFlip, 3, g);
					}
					// 6. Ground Seismic Dust Upheaval Cascade sweeping directly under wave (parts[8]: 300, 80x25)
					if (parts[8] != null)
					{
						parts[8].drawFrameNew((t * 2) % 3, ringX, casterY + 4, dirTrans, 33, g);
					}
					// 7. Starburst Sparks on Wave Crest (parts[12]: 104, 30x30)
					if (parts[12] != null)
					{
						parts[12].drawFrame(t % 3, ringX + facingSign * 42, ringY - 18, 0, 3, g);
						parts[12].drawFrame((t + 1) % 3, ringX + facingSign * 42, ringY + 18, 2, 3, g);
					}
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 5: Epicenter Cataclysm & Detonation at (impactX, impactY) (f 21..36)
				// ─────────────────────────────────────────────────────────────
				// Ground Elliptical Shockwave Ring (parts[3]: 394, 126x41)
				if (f >= 20 && f <= 36 && parts[3] != null)
				{
					int gF = ((f - 20) / 2) % 3;
					parts[3].drawFrame(gF, impactX, impactY + 12, 0, 3, g);
				}

				// Colossal Mega Shockwave Explosion (parts[2]: 453, 169x126) (f 21..34)
				if (f >= 21 && f <= 34 && parts[2] != null)
				{
					int blastAge = f - 21;
					int megaF = (blastAge < 3) ? 0 : ((blastAge < 7) ? 1 : 2);
					int megaJitter = (f % 2 == 0) ? 1 : -1;
					parts[2].drawFrame(megaF, impactX + megaJitter, impactY - 10, 0, 3, g);
				}

				// Radial Space Distortion Wave with Alternating Flip (parts[10]: 285, 111x90) (f 21..32)
				if (f >= 21 && f <= 32 && parts[10] != null)
				{
					int distF = ((f - 21) / 2) % 3;
					int distFlip = ((f - 21) % 2 == 0) ? 0 : 2;
					parts[10].drawFrame(distF, impactX, impactY - 15, distFlip, 3, g);
				}

				// Blinding White-Gold Core Flash (parts[11]: 66, 75x55) (f 21..24)
				if (f >= 21 && f <= 24 && parts[11] != null)
				{
					parts[11].drawFrame(0, impactX, impactY - 15, 0, 3, g);
					if (vecObjsBeFire != null)
					{
						for (int k = 1; k < vecObjsBeFire.size(); k++)
						{
							Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
							if (tInfo != null)
							{
								MainObject tObj = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
								if (tObj != null && !tObj.isDie && tObj.Hp > 0 && !tObj.isRemove)
								{
									parts[11].drawFrame(0, tObj.x, tObj.y - tObj.hOne / 2, 0, 3, g);
								}
							}
						}
					}
				}

				// Diamond Starburst Sparks 4-Way Detonation Expanding with Velocity (parts[12]: 104, 30x30) (f 22..35)
				if (f >= 22 && f <= 35 && parts[12] != null)
				{
					int spAge = f - 22;
					int spDist = spAge * 5;
					int spF = (spAge / 2) % 3;
					parts[12].drawFrame(spF, impactX - 30 - spDist, impactY - 20 - spDist, 0, 3, g);
					parts[12].drawFrame((spF + 1) % 3, impactX + 30 + spDist, impactY - 20 - spDist, 2, 3, g);
					parts[12].drawFrame((spF + 2) % 3, impactX - 20 - spDist, impactY + 10 + spDist / 2, 0, 3, g);
					parts[12].drawFrame(spF, impactX + 20 + spDist, impactY + 10 + spDist / 2, 2, 3, g);
				}

				// ─────────────────────────────────────────────────────────────
				// LAYER 6: Divine Lotus Bloom & Sacred Dharma Wheel Resonance (f 21..44)
				// ─────────────────────────────────────────────────────────────
				// Sacred Dharma Wheel Mandala Nimbus at Epicenter (parts[4]: 357, 100x100) (f 21..38)
				if (f >= 21 && f <= 38 && parts[4] != null)
				{
					int wheelF = ((f - 21) / 2) % 4;
					parts[4].drawFrameNew(wheelF, impactX, impactY - 25, 0, 3, g);
				}

				// Divine Lotus Bloom Khai Hoa Kim Liên (parts[5]: 315, 77x54) (f 21..44)
				// Full biological blooming stages 0..5 (bud -> opening -> bloom -> full radiance)
				if (f >= 21 && f <= 44 && parts[5] != null)
				{
					int lotusF = Math.Min(5, (f - 21) / 4);
					parts[5].drawFrameNew(lotusF, impactX, impactY + 6, 0, 3, g);
				}

				// 6 Sacred Golden Nirvana Embers with Multi-Harmonic Sinusoidal Sway (parts[14]: 224, 22x28) (f 24..45)
				if (f >= 24 && f <= 45 && parts[14] != null)
				{
					for (int e = 0; e < 6; e++)
					{
						int eAge = f - 24 + e * 3;
						int driftUp = eAge * 3;
						int angle = (eAge * 28 + e * 60) % 360;
						int swayX = (CRes.getsin(angle) * (14 + (e % 3) * 4)) >> 10;
						int embF = (eAge / 2) % 5;
						int eBaseX = impactX - 50 + e * 20;
						parts[14].drawFrameNew(embF, eBaseX + swayX, impactY - 15 - driftUp, (e % 2 == 0 ? 0 : 2), 3, g);
					}
				}
			}
		}

		// Paint caster charging aura during initial casting frames
		if (objFireMain != null && f < 15 && setId != 3 && setId != 5 && setId != 6 && setId != 7 && setId != 8 && setId != 9 && setId != 10 && setId != 12 && setId != 13)
		{
			if (setId == 1 && fraImgSub2Eff != null && fraImgSub2Eff.nFrame > 0)
			{
				fraImgSub2Eff.drawFrame(0, objFireMain.x, objFireMain.y + 4, 0, 3, g);
			}
			if (fraImgEff != null && fraImgEff.nFrame > 0)
			{
				int frameIdx = (f / 2) % fraImgEff.nFrame;
				fraImgEff.drawFrame(frameIdx, objFireMain.x + (Dir == 2 ? 16 : -16), objFireMain.y - 15, Dir == 2 ? 0 : 2, 3, g);
			}
		}

		// Paint active flying projectiles with directional orientation & glowing dual layer
		if (this.VecSubEff != null && setId != 2 && setId != 3 && setId != 5 && setId != 6 && setId != 7 && setId != 8 && setId != 9 && setId != 10 && setId != 12 && setId != 13 && setId != 16)
		{
			for (int i = 0; i < this.VecSubEff.size(); i++)
			{
				Point_Focus projectile = (Point_Focus)this.VecSubEff.elementAt(i);
				if (projectile != null)
				{
					if (fraImgSubEff != null && fraImgSubEff.nFrame > 0)
					{
						int frameIdx = (projectile.f / 2) % fraImgSubEff.nFrame;
						fraImgSubEff.drawFrame(frameIdx, projectile.x, projectile.y, projectile.Dir == 2 ? 0 : 2, 3, g);
					}
					if (setId == 1 && fraImgSub3Eff != null && fraImgSub3Eff.nFrame > 0)
					{
						int frameIdx = (projectile.f / 2) % fraImgSub3Eff.nFrame;
						fraImgSub3Eff.drawFrame(frameIdx, projectile.x + (projectile.Dir == 2 ? 10 : -10), projectile.y, projectile.Dir == 2 ? 0 : 2, 3, g);
					}
					if (fraImgEff != null && fraImgEff.nFrame > 0)
					{
						int frameIdx = ((projectile.f / 2) + 1) % fraImgEff.nFrame;
						fraImgEff.drawFrame(frameIdx, projectile.x, projectile.y, projectile.Dir == 2 ? 0 : 2, 3, g);
					}
				}
			}
		}
		}
		catch (Exception)
		{
		}
	}

	private void paintKidWeapon(mGraphics g, Point p, FrameImage[] p9)
	{
		if (p == null || g == null) return;
		bool drawn = false;
		try
		{
			MainImage m = ObjectData.getImageAll((short)p.subType, ObjectData.hashImageItem, (short)3000);
			if (m != null && m.img != null && m.img.image != null)
			{
				if (m.w <= 0 || m.h <= 0)
				{
					m.set_W_H();
				}
				if (m.w > 0 && m.h > 0)
				{
					g.drawRegion(m.img, 0, 0, m.w, m.h, p.frame % 8, p.x, p.y, 3);
					drawn = true;
				}
			}
		}
		catch (Exception)
		{
		}

		// Fallback to authentic metallic blade glint (p9[8]: 358, 51x22)
		if (!drawn && p9 != null && p9.Length > 8 && p9[8] != null && p9[8].nFrame > 0)
		{
			p9[8].drawFrame(0, p.x, p.y, p.frame % 4, 3, g);
		}
	}

	// =========================================================================
	

		// =========================================================================
	// DOKU DOKU NO MI DEVIL FRUIT SYSTEM (SKILL 4017, 4018, 4019)
	// ID 4017: Cự Độc Phán Quyết (Y hệt 4010 Thần Trang Magellan)
	// ID 4018: Bách Độc Vũ Mưa Độc Tách Tầng Chuẩn Xịn (Decoupled Toxic Rain)
	// ID 4019: Độc Long Thức Tỉnh (Thuần Gồng Buff Siêu Tốc 0.2s Frame 6)
	// =========================================================================

	public static FrameImage s_imgRainDrop;      // ID 474 (36x48, 6f) - Mưa Độc Long giáng xuống (tilted -52 deg)
	public static FrameImage s_imgRainSplash;    // ID 475 (64x36, 6f) - Bộc phá tiếp đất
	public static FrameImage s_imgPoisonBubbles; // ID 476 (30x30, 8f) - Khí độc dập dềnh
	public static FrameImage s_imgAuraDragon;    // ID 477 (100x120, 8f) - Song Long Hỏa Trụ sau lưng Caster
	public static FrameImage s_imgAcidPool4018;  // ID 478 (110x50, 6f) - Vũng axit mặt đất dưới chân mục tiêu

	public static void ensureVenomSkillFrames(bool forceLoad = false)
	{
		if (forceLoad || s_imgRainDrop == null || s_imgRainDrop.nFrame != 6)
		{
			s_imgRainDrop = new FrameImage(474, 6);
		}
		if (forceLoad || s_imgRainSplash == null || s_imgRainSplash.nFrame != 6)
		{
			s_imgRainSplash = new FrameImage(475, 6);
		}
		if (forceLoad || s_imgPoisonBubbles == null || s_imgPoisonBubbles.nFrame != 8)
		{
			s_imgPoisonBubbles = new FrameImage(476, 8);
		}
		if (forceLoad || s_imgAuraDragon == null || s_imgAuraDragon.nFrame != 8)
		{
			s_imgAuraDragon = new FrameImage(477, 8);
		}
		if (forceLoad || s_imgAcidPool4018 == null || s_imgAcidPool4018.nFrame != 6)
		{
			s_imgAcidPool4018 = new FrameImage(478, 6);
		}
	}

	private static readonly int[] s_bXOffsets = new int[] { -82, -68, -52, -38, -25, -12, 0, 14, 26, 38, 52, 66, 80, -45, 18, 58 };
	private static readonly int[] s_bYOffsets = new int[] { 3, 9, -7, 6, -9, 8, 2, -6, 9, 3, -8, 7, 2, 11, -5, 5 };
	private static readonly int[][] s_poolConfigs = new int[][]
	{
		new int[] { 0, 0, 0, 0, 0 },
		new int[] { -32, 5, 2, 2, 1 },
		new int[] { 35, -3, 0, 4, 2 },
		new int[] { 10, 8, 2, 1, 3 },
		new int[] { -12, -7, 0, 3, 4 },
		new int[] { -60, -4, 2, 5, 5 },
		new int[] { 62, 6, 0, 0, 6 },
		new int[] { -86, 5, 2, 3, 8 },
		new int[] { 88, -4, 0, 1, 9 },
		new int[] { -46, 11, 0, 4, 10 },
		new int[] { 44, -10, 2, 2, 11 },
		new int[] { -15, 13, 2, 5, 12 }
	};

	// ─────────────────────────────────────────────────────────────────────────
	// SKILL 4018: BÁCH ĐỘC VŨ (VENOM DRAGON RAIN & ACID POOL UNDER TARGETS)
	// ─────────────────────────────────────────────────────────────────────────

	private void createVenomRain4018()
	{
		VecEff = new mVector();
		VecSubEff = new mVector();
		isaddEff = false; // Flag: cụm vũng độc ngầm chưa tạo
		ensureVenomSkillFrames(true);
		fRemove = 65;
		levelPaint = 0; // TẦNG TRÊN (0): GIỌT MƯA VÀ IMPACT TIẾP ĐẤT RƠI TRÊN MẶT ĐẤT & TRÊN MỤC TIÊU

		// 1. Ưu tiên tuyệt đối ghim chuẩn giữa mục tiêu đang chọn (GameScreen.objFocus)
		MainObject target = null;
		if (GameScreen.objFocus != null && !GameScreen.objFocus.isDie && !GameScreen.objFocus.isRemove && GameScreen.objFocus != objFireMain)
		{
			target = GameScreen.objFocus;
		}
		else if (objBeFireMain != null && objBeFireMain != objFireMain && !objBeFireMain.isDie && !objBeFireMain.isRemove)
		{
			target = objBeFireMain;
		}
		else if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill o = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (o != null)
				{
					MainObject mob = MainObject.get_Object((int)o.ID, (sbyte)o.tem);
					if (mob != null && !mob.isDie && !mob.isRemove && mob != objFireMain)
					{
						target = mob;
						break;
					}
				}
			}
		}

		if (target != null)
		{
			objBeFireMain = target;
			toX = target.x;
			toY = target.y;
			if (objFireMain != null)
			{
				Dir = (sbyte)((target.x >= objFireMain.x) ? 2 : 0);
				objFireMain.type_left_right = Dir;
				objFireMain.Dir = Dir;
			}
		}

		// Xác định vị trí mặt đất chuẩn dưới chân mục tiêu (bỏ dyShadow âm làm trôi cao)
		int groundY = (target != null) ? target.y : ((toY != 0) ? toY : y);
		int mainTargetX = (target != null) ? target.x : ((toX != 0) ? toX : (x + ((Dir == 2) ? 140 : -140)));
		toX = mainTargetX;
		toY = groundY;

		// 1. Tạo bão Mưa Độc Long dày đặc (474: 36x48) rơi liên tục từ f=3 đến f=50 (70 giọt phân bố ngẫu nhiên cách đều, sinh động)
		int totalDrops = 70;
		for (int i = 0; i < totalDrops; i++)
		{
			Point drop = new Point();
			// Khởi hành rải đều liên tục từ f=3 đến f=48 với độ lệch ngẫu nhiên
			int fStart = 3 + (i * 45 / totalDrops) + ((i * 3) % 4 - 1);
			if (fStart < 3) fStart = 3;
			int fallDur = 6 + (i % 6); // 6..11 frames rơi vũ bão, tốc độ đa dạng sinh động
			drop.fSmall = fStart;
			drop.fRe = fStart + fallDur;

			// Tọa độ xuất phát trên cao theo góc chéo tự nhiên ~48..56 độ:
			int spawnDistY = 175 + ((i * 19) % 55); // 175..229 px
			int spawnAngleRatio = 125 + ((i * 7) % 30); // 125..154
			int spawnDistX = spawnDistY * spawnAngleRatio / 180;
			drop.dis = (Dir == 2) ? -spawnDistX : spawnDistX;
			drop.vy = spawnDistY;

			// Điểm tiếp đất trải rộng ngẫu nhiên khắp khu vực đầm lầy độc (-115..+115, -16..+18)
			int landOffsetX = -115 + ((i * 53 + 17) % 231) + ((i * 5) % 9 - 4);
			int landOffsetY = -16 + ((i * 23 + 11) % 35) + ((i * 3) % 7 - 3);
			drop.color = landOffsetX;
			drop.frame = landOffsetY;
			drop.x2 = mainTargetX + drop.color;
			drop.y2 = groundY + drop.frame;
			drop.obj = null; // CỐ ĐỊNH 1 VỊ TRÍ TRÊN MẶT ĐẤT, KHÔNG TRÔI THEO QUÁI
			drop.f = 0; // Đếm frame hiệu ứng bắn tóe tiếp đất (rain_splash)
			drop.isRemove = false; // false: đang rơi, true: tiếp đất bộc phá
			VecSubEff.addElement(drop);
		}

		// 2. Bong bóng độc (476: 30x30) dập dềnh trên khắp mặt hồ vũng độc (16 bóng trải rộng)
		for (int b = 0; b < 16; b++)
		{
			Point bubble = new Point();
			int bxOff = s_bXOffsets[b] + ((b * 5) % 9 - 4);
			int byOff = s_bYOffsets[b] + ((b * 3) % 7 - 3);
			bubble.x = mainTargetX + bxOff;
			bubble.y = groundY + byOff;
			bubble.x2 = bubble.x;
			bubble.y2 = bubble.y;
			bubble.color = bxOff;
			bubble.subType = byOff;
			bubble.obj = null; // CỐ ĐỊNH 1 VỊ TRÍ TRÊN MẶT ĐẤT, KHÔNG TRÔI THEO QUÁI
			bubble.fSmall = 10 + (b * 2); // Nổi lên dập dềnh so le
			bubble.fRe = bubble.fSmall + 46;
			bubble.f = 0;
			bubble.frame = (b * 3) % 8;
			VecEff.addElement(bubble);
		}

		addSound(10);
	}

	private void updateVenomRain4018()
	{
		// Xử lý Companion Sub-effect: CỤM 12 VŨNG ĐỘC DƯỚI CHÂN MỤC TIÊU (subType == 1008, levelPaint == -1)
		// CỐ ĐỊNH 100% VỊ TRÍ MẶT ĐẤT ĐÃ TẠO, KHÔNG DI CHUYỂN THEO QUÁI
		if (subType == 1008)
		{
			if (f >= fRemove)
			{
				removeEff();
			}
			return;
		}

		// Âm thanh và rung chấn ở từng đợt giáng thế của mưa độc long
		if (f == 8)
		{
			addSound(10);
		}
		else if (f == 12 || f == 18 || f == 24 || f == 30 || f == 36 || f == 42 || f == 48)
		{
			addSound(51);
			LoadMap.timeVibrateScreen = 4;
			if (vecObjsBeFire != null)
			{
				for (int k = 0; k < vecObjsBeFire.size() && k < 4; k++)
				{
					Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
					if (tInfo == null) continue;
					MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
					if (target == null || target.isDie || target.Hp <= 0 || target.isRemove) continue;
					setAva(2, target);
					target.dy = -3;
				}
			}
		}

		// TẠO CỤM NHIỀU VŨNG ĐỘC NGẪU NHIÊN CÁCH NHAU ĐẸP MẮT ĐAN XEN DƯỚI CHÂN MỤC TIÊU KHI GIỌT ĐẦU TIẾP ĐẤT (f >= 10)
		// ĐẶC BIỆT: levelPaint = -1 (LỚP TILE MAP, DƯỚI TẤT CẢ CHAR VÀ QUÁI - CHAR VÀ QUÁI Ở TRÊN)
		if (f >= 10 && !isaddEff)
		{
			isaddEff = true;
			int poolX = toX;
			int poolY = toY + 4; // Hạ thấp 4px sát chân/bóng đất (cố định 1 vị trí theo toX, toY)
			Effect_Skill groundPool = new Effect_Skill();
			groundPool.typeEffect = 4018;
			groundPool.subType = 1008; // 1008 = Ground acid pool cluster companion
			groundPool.levelPaint = -1; // VẼ TRÊN LỚP TILE MAP, DƯỚI TẤT CẢ MỤC TIÊU & NHÂN VẬT!
			groundPool.x = poolX;
			groundPool.y = poolY;
			groundPool.toX = poolX;
			groundPool.toY = poolY;
			groundPool.Dir = this.Dir;
			groundPool.objFireMain = (this.objFireMain != null) ? this.objFireMain : GameScreen.player;
			groundPool.objBeFireMain = this.objBeFireMain;
			groundPool.VecEff = this.VecEff;
			groundPool.VecSubEff = new mVector();

			// Cụm 12 vũng độc ngẫu nhiên, mở rộng thành đầm lầy độc (tái sử dụng s_poolConfigs):
			for (int pIdx = 0; pIdx < s_poolConfigs.Length; pIdx++)
			{
				Point p = new Point();
				p.color = s_poolConfigs[pIdx][0] + CRes.random_Am_0(6);   // offsetX ngẫu nhiên
				p.subType = s_poolConfigs[pIdx][1] + CRes.random_Am_0(3); // offsetY ngẫu nhiên
				p.dis = s_poolConfigs[pIdx][2];                           // trans (0=bình thường, 2=lật ngang)
				p.frame = s_poolConfigs[pIdx][3];                         // phase offset (bọt khí không trùng nhịp)
				p.fSmall = s_poolConfigs[pIdx][4];                        // delay frame xuất hiện lan tỏa
				groundPool.VecSubEff.addElement(p);
			}

			groundPool.f = 0;
			groundPool.fRemove = 55; // 10 + 55 = 65 (kết thúc cùng lúc với skill)
			GameScreen.VecEffect.addElement(groundPool);
		}

		// Cập nhật giọt mưa độc rơi và bộc phá tiếp đất
		if (VecSubEff != null)
		{
			for (int i = 0; i < VecSubEff.size(); i++)
			{
				Point drop = (Point)VecSubEff.elementAt(i);
				if (drop == null || f < drop.fSmall) continue;

				if (f >= drop.fRe)
				{
					drop.isRemove = true;
					drop.f++; // Đếm frame hoạt ảnh bắn tóe (rain_splash)
				}
			}
		}

		// Cập nhật bong bóng độc dập dềnh trên mặt vũng độc (không bay vọt lên trời)
		if (VecEff != null)
		{
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point bubble = (Point)VecEff.elementAt(i);
				if (bubble == null || f < bubble.fSmall || f >= bubble.fRe) continue;
				bubble.f++;
				int sine = CRes.getsin(((bubble.f + bubble.frame * 8) * 24) % 360) * 2 >> 10;
				bubble.y = bubble.y2 + sine;
			}
		}

		if (f >= 55 && vecObjsBeFire != null)
		{
			for (int k = 0; k < vecObjsBeFire.size(); k++)
			{
				Object_Effect_Skill tInfo = (Object_Effect_Skill)vecObjsBeFire.elementAt(k);
				if (tInfo != null)
				{
					MainObject target = MainObject.get_Object((int)tInfo.ID, (sbyte)tInfo.tem);
					if (target != null) target.dy = 0;
				}
			}
		}

		if (f >= fRemove)
		{
			removeEff();
		}
	}

	private void paintVenomRain4018(mGraphics g)
	{
		if (g == null) return;
		ensureVenomSkillFrames();

		// 1. NẾU LÀ COMPANION SUB-EFFECT (subType == 1008, levelPaint == -1):
		// VẼ CỤM NHIỀU VŨNG AXIT NGẪU NHIÊN GẦN NHAU DƯỚI CHÂN MỤC TIÊU CHÍNH (ANCHOR 3, LEVELPAINT = -1, DƯỚI TẤT CẢ CHAR VÀ QUÁI)
		if (subType == 1008)
		{
			if (s_imgAcidPool4018 != null && s_imgAcidPool4018.nFrame > 0)
			{
				if (VecSubEff != null && VecSubEff.size() > 0)
				{
					for (int pIdx = 0; pIdx < VecSubEff.size(); pIdx++)
					{
						Point pool = (Point)VecSubEff.elementAt(pIdx);
					if (pool == null || f < pool.fSmall) continue;
					int poolAge = f - pool.fSmall;
					int poolF = (poolAge < 8) ? (poolAge * s_imgAcidPool4018.nFrame / 8) : (((poolAge / 2) + pool.frame) % s_imgAcidPool4018.nFrame);
					int px = x + pool.color;
					int py = y + pool.subType;
					s_imgAcidPool4018.drawFrame(poolF % s_imgAcidPool4018.nFrame, px, py, pool.dis, 3, g);
					}
				}
				else
				{
					int poolF = (f < 8) ? (f * s_imgAcidPool4018.nFrame / 8) : ((f / 2) % s_imgAcidPool4018.nFrame);
					s_imgAcidPool4018.drawFrame(poolF % s_imgAcidPool4018.nFrame, x, y, 0, 3, g);
				}
			}
			// Bong bóng độc mặt đất cũng vẽ ở tầng ngầm này để nằm dưới chân nhân vật và quái
			if (s_imgPoisonBubbles != null && s_imgPoisonBubbles.nFrame > 0 && VecEff != null)
			{
				for (int i = 0; i < VecEff.size(); i++)
				{
					Point bubble = (Point)VecEff.elementAt(i);
					if (bubble == null || f < bubble.fSmall || f >= bubble.fRe) continue;
					int bFrame = ((bubble.f / 2) + bubble.frame) % s_imgPoisonBubbles.nFrame;
					s_imgPoisonBubbles.drawFrame(bFrame, bubble.x, bubble.y, 0, 3, g);
				}
			}
			return;
		}

		// 2. VƯƠNG MIỆN BỘC PHÁ TIẾP ĐẤT (rain_splash: 475, 64x36, ANCHOR 3 TẠI TIẾP ĐẤT SÁT MẶT ĐẤT)
		if (s_imgRainSplash != null && s_imgRainSplash.nFrame > 0 && VecSubEff != null)
		{
			for (int i = 0; i < VecSubEff.size(); i++)
			{
				Point drop = (Point)VecSubEff.elementAt(i);
				if (drop == null || !drop.isRemove) continue;
				if (drop.f >= 0 && drop.f < s_imgRainSplash.nFrame)
				{
					s_imgRainSplash.drawFrame(drop.f, drop.x2, drop.y2, 0, 3, g);
				}
			}
		}

		// 3. GIỌT MƯA ĐỘC LONG BAY CHÉO XUỐNG ĐẤT (rain_drop: 474, 36x48, ANCHOR 3)
		// Quỹ đạo rơi chéo từ (lx + drop.dis, ly - drop.vy) tiếp đất tại (lx, ly)
		if (s_imgRainDrop != null && s_imgRainDrop.nFrame > 0 && VecSubEff != null)
		{
			for (int i = 0; i < VecSubEff.size(); i++)
			{
				Point drop = (Point)VecSubEff.elementAt(i);
				if (drop == null || drop.isRemove || f < drop.fSmall || f >= drop.fRe) continue;

				int lx = drop.x2;
				int ly = drop.y2;

				int p = f - drop.fSmall;
				int dur = drop.fRe - drop.fSmall;
				if (dur < 1) dur = 1;
				int sx = lx + drop.dis;
				int sy = ly - drop.vy;

				int curX = sx + (lx - sx) * p / dur;
				int curY = sy + (ly - sy) * p / dur;

				int dropF = (p * s_imgRainDrop.nFrame / dur) % s_imgRainDrop.nFrame;
				int trans = (Dir == 2) ? 0 : 2;
				int headOffX = (Dir == 2) ? 6 : -6;
				s_imgRainDrop.drawFrame(dropF, curX - headOffX, curY, trans, 3, g);
			}
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// SKILL 4019: ĐỘC LONG THỨC TỈNH (BUFF SAU LƯNG NHÂN VẬT, 0.2s KÍCH HOẠT)
	// ─────────────────────────────────────────────────────────────────────────

	private void createVenomBuff4019()
	{
		ensureVenomSkillFrames(true);
		fRemove = 999999;
		levelPaint = -1; // VẼ SAU LƯNG NHÂN VẬT THEO YÊU CẦU NGƯỜI DÙNG!
		timeBegin = GameCanvas.timeNow;

		// Tính toán thời gian tác dụng buff chuẩn từ tham số kỹ năng
		int buffMs = 0;
		if (timeEnd > 0)
		{
			buffMs = (timeEnd < 1000) ? (timeEnd * 100) : (int)timeEnd;
		}
		if (buffMs <= 0 && this.skill != null)
		{
			if (this.skill.timebuff > 0)
			{
				buffMs = (this.skill.timebuff < 1000) ? (this.skill.timebuff * 100) : (int)this.skill.timebuff;
			}
			Skill_Info sk = Skill_Info.getSkillFromID(this.skill.ID);
			if (sk != null && sk.vecAtt != null)
			{
				for (int i = 0; i < sk.vecAtt.size(); i++)
				{
					MainInfoItem item = (MainInfoItem)sk.vecAtt.elementAt(i);
					if (item != null && item.id == 32 && item.value > 0)
					{
						int val = (item.value < 1000) ? (item.value * 100) : item.value;
						if (val > buffMs) buffMs = val;
						break;
					}
				}
				if (buffMs <= 0 && sk.timeEffSpec > 0)
				{
					buffMs = (sk.timeEffSpec < 1000) ? (sk.timeEffSpec * 100) : (int)sk.timeEffSpec;
				}
			}
		}
		if (buffMs <= 0)
		{
			// Mặc định chuẩn cấu hình server: sk4019 Option 32 = 180 (18.0s = 18000ms)
			buffMs = 18000;
		}
		timeEnd = (short)Math.Min(32000, buffMs);
		addSound(10);
	}

	private void updateVenomBuff4019()
	{
		// KÍCH HOẠT THẦN TỐC TẠI FRAME 6 (~0.2s)!
		if (f == 6)
		{
			addSound(14);
			addSound(51);
			LoadMap.timeVibrateScreen = 8;

			if (objFireMain != null)
			{
				objFireMain.addDataEff(108, timeEnd, 0, 0);
			}
		}

		// Hết thời gian tác dụng buff theo param của skill -> tự xóa
		if (timeEnd > 0 && GameCanvas.timeNow - timeBegin >= timeEnd)
		{
			removeEff();
			return;
		}

		// Nếu đối tượng thi triển tử vong hoặc bị xóa -> tự xóa
		if (objFireMain != null && (objFireMain.isDie || objFireMain.Hp <= 0 || objFireMain.isRemove))
		{
			removeEff();
			return;
		}
	}

	private void paintVenomBuff4019(mGraphics g)
	{
		if (g == null) return;
		ensureVenomSkillFrames();
		levelPaint = -1; // Đảm bảo luôn vẽ sau lưng / dưới nhân vật

		int casterX = (objFireMain != null) ? objFireMain.x : x;
		int casterY = (objFireMain != null) ? objFireMain.y : y;

		// Song Long Hỏa Trụ (aura: 477, 100x120) - SAU LƯNG NHÂN VẬT (chỉ vẽ trên nhân vật, đã bỏ vũng độc)
		if (s_imgAuraDragon != null && s_imgAuraDragon.nFrame > 0)
		{
			int dragonF = ((f < 4 ? f : (f - 4)) / 2) % s_imgAuraDragon.nFrame;
			s_imgAuraDragon.drawFrame(dragonF, casterX, casterY, 0, 33, g);
		}
	}

	// =========================================================================
	// HE THONG SKILL TRAI AC QUY THANH LONG KAIDO (SEIRYU)
	// Skill 1 (4021): Co Long Hoang Kim (Ma tran chan 4021 -> Long Chau 65 -> Than Long Ngoam 4025)
	// Skill 2 (4022): Than Long Giang Loi (Than Long 4022 -> Loi Cau 56 tu mieng rong dx=+48, dy=-75 -> Boc pha set 57)
	// Skill 3 (4023): Long Than Ho The (Hao quang kim than 4023 bao quanh Caster 18s)
	// =========================================================================

	private DataSkillEff effThanhLongBullet;
	private DataSkillEff effThanhLongThunderBullet;

	// --- SKILL 1 (4021): CO LONG HOANG KIM ---
	private void createThanhLongActive4021()
	{
		VecEff = new mVector();
		fRemove = 55;
		levelPaint = 0;

		MainObject target = null;
		if (GameScreen.objFocus != null && !GameScreen.objFocus.isDie && !GameScreen.objFocus.isRemove && GameScreen.objFocus != objFireMain)
		{
			target = GameScreen.objFocus;
		}
		else if (objBeFireMain != null && objBeFireMain != objFireMain && !objBeFireMain.isDie && !objBeFireMain.isRemove)
		{
			target = objBeFireMain;
		}
		else if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill o = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (o != null)
				{
					MainObject mob = MainObject.get_Object((int)o.ID, (sbyte)o.tem);
					if (mob != null && !mob.isDie && !mob.isRemove && mob != objFireMain)
					{
						target = mob;
						break;
					}
				}
			}
		}

		int casterX = (objFireMain != null) ? objFireMain.x : x;
		int casterY = (objFireMain != null) ? objFireMain.y : y;
		int targetX = (target != null) ? target.x : ((toX != 0) ? toX : (casterX + ((Dir == 2) ? 140 : -140)));
		int targetY = (target != null) ? target.y : ((toY != 0) ? toY : casterY);

		if (objFireMain != null)
		{
			Dir = (sbyte)((targetX >= casterX) ? 2 : 0);
			objFireMain.type_left_right = Dir;
			objFireMain.Dir = Dir;
		}
		toX = targetX;
		toY = targetY;

		// 1. Ma tran chan Co Long 4021 tai vi tri Caster
		GameScreen.addHightDataeff((short)4021, casterX, casterY, Dir == 0);
		addSound((sbyte)10);

		// 2. Tao dan Long Chau Hoang Kim 65 lao toi muc tieu
		Point bullet = new Point();
		bullet.x = casterX;
		bullet.y = casterY - 20;
		bullet.x2 = targetX;
		bullet.y2 = targetY;
		bullet.fSmall = 4;
		bullet.fRe = 14;
		bullet.f = 0;
		bullet.isRemove = false;
		bullet.obj = target;
		VecEff.addElement(bullet);

		effThanhLongBullet = new DataSkillEff((short)65, -1, Dir == 0);
	}

	private void updateThanhLongActive4021()
	{
		if (f >= fRemove)
		{
			removeEff();
			return;
		}

		if (effThanhLongBullet != null)
		{
			effThanhLongBullet.update();
		}

		if (VecEff != null && VecEff.size() > 0)
		{
			Point bullet = (Point)VecEff.elementAt(0);
			if (bullet != null)
			{
				bullet.f++;
				if (bullet.obj != null && !bullet.obj.isDie && !bullet.obj.isRemove)
				{
					bullet.x2 = bullet.obj.x;
					bullet.y2 = bullet.obj.y;
				}

				int startX = (objFireMain != null) ? objFireMain.x : x;
				int startY = ((objFireMain != null) ? objFireMain.y : y) - 20;
				int progress = bullet.f - bullet.fSmall;
				int totalDur = bullet.fRe - bullet.fSmall;

				if (progress > 0 && progress < totalDur)
				{
					bullet.x = startX + (bullet.x2 - startX) * progress / totalDur;
					bullet.y = startY + (bullet.y2 - startY) * progress / totalDur;
				}
				else if (progress >= totalDur && !bullet.isRemove)
				{
					bullet.isRemove = true;
					bullet.x = bullet.x2;
					bullet.y = bullet.y2;

					// Kich hoat Than Long ngoam nat muc tieu (4025)
					GameScreen.addHightDataeff((short)4025, bullet.x2, bullet.y2, Dir == 0);
					addSound((sbyte)51);
					LoadMap.timeVibrateScreen = 8;
				}
			}
		}
	}

	private void paintThanhLongActive4021(mGraphics g)
	{
		if (VecEff != null && VecEff.size() > 0)
		{
			Point bullet = (Point)VecEff.elementAt(0);
			if (bullet != null && bullet.f >= bullet.fSmall && !bullet.isRemove)
			{
				if (effThanhLongBullet != null)
				{
					effThanhLongBullet.x = bullet.x;
					effThanhLongBullet.y = bullet.y;
					effThanhLongBullet.paint(g);
				}
			}
		}
	}

	// --- SKILL 2 (4022): THAN LONG GIANG LOI ---
	private void createThanhLongActive4022()
	{
		VecEff = new mVector();
		fRemove = 55;
		levelPaint = 0;

		MainObject target = null;
		if (GameScreen.objFocus != null && !GameScreen.objFocus.isDie && !GameScreen.objFocus.isRemove && GameScreen.objFocus != objFireMain)
		{
			target = GameScreen.objFocus;
		}
		else if (objBeFireMain != null && objBeFireMain != objFireMain && !objBeFireMain.isDie && !objBeFireMain.isRemove)
		{
			target = objBeFireMain;
		}
		else if (vecObjsBeFire != null && vecObjsBeFire.size() > 0)
		{
			for (int i = 0; i < vecObjsBeFire.size(); i++)
			{
				Object_Effect_Skill o = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (o != null)
				{
					MainObject mob = MainObject.get_Object((int)o.ID, (sbyte)o.tem);
					if (mob != null && !mob.isDie && !mob.isRemove && mob != objFireMain)
					{
						target = mob;
						break;
					}
				}
			}
		}

		int casterX = (objFireMain != null) ? objFireMain.x : x;
		int casterY = (objFireMain != null) ? objFireMain.y : y;
		int targetX = (target != null) ? target.x : ((toX != 0) ? toX : (casterX + ((Dir == 2) ? 140 : -140)));
		int targetY = (target != null) ? target.y : ((toY != 0) ? toY : casterY);

		if (objFireMain != null)
		{
			Dir = (sbyte)((targetX >= casterX) ? 2 : 0);
			objFireMain.type_left_right = Dir;
			objFireMain.Dir = Dir;
		}
		toX = targetX;
		toY = targetY;

		// 1. Than Long xuat hien tai Caster (4022)
		GameScreen.addHightDataeff((short)4022, casterX, casterY, Dir == 0);
		addSound((sbyte)10);

		// 2. Set offset mieng rong: dx = (Dir == 2 ? 48 : -48), dy = -75
		int mouthX = casterX + ((Dir == 2) ? 48 : -48);
		int mouthY = casterY - 75;

		// 3. Ban 3 Loi Cau 56 tu mieng rong toi muc tieu
		int[] fStarts = new int[] { 8, 14, 20 };
		for (int i = 0; i < 3; i++)
		{
			Point p = new Point();
			p.x = mouthX;
			p.y = mouthY;
			MainObject tObj = target;
			if (vecObjsBeFire != null && vecObjsBeFire.size() > i)
			{
				Object_Effect_Skill o = (Object_Effect_Skill)vecObjsBeFire.elementAt(i);
				if (o != null)
				{
					MainObject mob = MainObject.get_Object((int)o.ID, (sbyte)o.tem);
					if (mob != null && !mob.isDie && !mob.isRemove) tObj = mob;
				}
			}
			p.obj = tObj;
			p.x2 = (tObj != null) ? tObj.x : targetX;
			p.y2 = (tObj != null) ? tObj.y : targetY;
			p.fSmall = fStarts[i];
			p.fRe = fStarts[i] + 8;
			p.f = 0;
			p.isRemove = false;
			p.color = i;
			VecEff.addElement(p);
		}

		effThanhLongThunderBullet = new DataSkillEff((short)56, -1, Dir == 0);
	}

	private void updateThanhLongActive4022()
	{
		if (f >= fRemove)
		{
			removeEff();
			return;
		}

		int casterX = (objFireMain != null) ? objFireMain.x : x;
		int casterY = (objFireMain != null) ? objFireMain.y : y;
		int mouthX = casterX + ((Dir == 2) ? 48 : -48);
		int mouthY = casterY - 75;

		if (effThanhLongThunderBullet != null)
		{
			effThanhLongThunderBullet.update();
		}

		if (VecEff != null)
		{
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point bullet = (Point)VecEff.elementAt(i);
				if (bullet == null) continue;

				bullet.f++;
				if (bullet.obj != null && !bullet.obj.isDie && !bullet.obj.isRemove)
				{
					bullet.x2 = bullet.obj.x;
					bullet.y2 = bullet.obj.y;
				}

				int progress = bullet.f - bullet.fSmall;
				int totalDur = bullet.fRe - bullet.fSmall;

				if (progress > 0 && progress < totalDur)
				{
					bullet.x = mouthX + (bullet.x2 - mouthX) * progress / totalDur;
					bullet.y = mouthY + (bullet.y2 - mouthY) * progress / totalDur;
				}
				else if (progress >= totalDur && !bullet.isRemove)
				{
					bullet.isRemove = true;
					bullet.x = bullet.x2;
					bullet.y = bullet.y2;

					// IMPACT: Boc pha loi dien sam set 57 tai muc tieu
					GameScreen.addHightDataeff((short)57, bullet.x2, bullet.y2, Dir == 0);
					addSound((sbyte)51);
					LoadMap.timeVibrateScreen = (bullet.color == 2) ? 10 : 6;
				}
			}
		}
	}

	private void paintThanhLongActive4022(mGraphics g)
	{
		if (VecEff != null)
		{
			for (int i = 0; i < VecEff.size(); i++)
			{
				Point bullet = (Point)VecEff.elementAt(i);
				if (bullet != null && bullet.f >= bullet.fSmall && !bullet.isRemove)
				{
					if (effThanhLongThunderBullet != null)
					{
						effThanhLongThunderBullet.x = bullet.x;
						effThanhLongThunderBullet.y = bullet.y;
						effThanhLongThunderBullet.paint(g);
					}
				}
			}
		}
	}

	// --- SKILL 3 (4023): LONG THAN HO THE (BUFF) ---
	private void createThanhLongBuff4023()
	{
		fRemove = 1200;
		int buffMs = 18000;
		timeEnd = (short)Math.Min(32000, buffMs);
		addSound((sbyte)10);
	}

	private void updateThanhLongBuff4023()
	{
		// Kich hoat tai frame 6 (~0.2s)
		if (f == 6)
		{
			addSound((sbyte)14);
			addSound((sbyte)51);
			LoadMap.timeVibrateScreen = 6;

			if (objFireMain != null)
			{
				objFireMain.addDataEff((short)4023, (int)timeEnd, (sbyte)0, (sbyte)0);
			}
		}

		if (timeEnd > 0 && GameCanvas.timeNow - timeBegin >= (long)timeEnd)
		{
			removeEff();
			return;
		}

		if (objFireMain != null && (objFireMain.isDie || objFireMain.Hp <= 0 || objFireMain.isRemove))
		{
			removeEff();
			return;
		}
	}

	private void paintThanhLongBuff4023(mGraphics g)
	{
		// DataEffect 4023 duoc MainObject tu dong ve quanh nhan vat
	}

}
