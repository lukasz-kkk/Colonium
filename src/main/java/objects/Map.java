package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;
import core.Boot;
import core.GameScreen;
import core.UserInput;

import java.awt.*;

public class Map {
    private final Texture texture;
    GameScreen gameScreen;
    SpriteBatch batch;
    PolygonSprite[] polySprite;
    PolygonSpriteBatch polyBatch;
    private final Texture backgroundTexture;
    private Texture pixTexture;
    public static int numberOfProvinces;
    private Province[] provinces;
    int[][] coordinates = {{330, 302}, {443, 355}, {564, 381}, {753, 355}, {954, 438}, {987, 328}, {1122, 336}, {1210, 258}, {1397, 337}, {395, 578}, {551, 559}, {721, 479}, {871, 502}, {1082, 425}, {1222, 417}, {1347, 443}, {414, 671}, {529, 785}, {671, 684}, {803, 652}, {947, 616}, {1077, 556}, {1170, 664}, {1285, 586}, {788, 797}, {964, 840}, {1023, 746}};

    public Map(int numberOfProvinces, GameScreen gameScreen, SpriteBatch batch) {
        this.numberOfProvinces = numberOfProvinces;
        this.gameScreen = gameScreen;
        this.batch = batch;
        this.texture = new Texture("white.png");
        this.backgroundTexture = new Texture("prov1_borders.png");

        provincesInit();
        polygonRendererInit();
        testInitValues();
    }

    private void provincesInit() {
        provinces = new Province[numberOfProvinces];
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i] = new Province(coordinates[i][0] + 50, Gdx.graphics.getHeight() - coordinates[i][1] + 50, i, gameScreen);
            System.out.println(coordinates[i][0] + " " + coordinates[i][1]);
        }
    }

    UserInput user = new UserInput();
    public void provincesRender() {
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i].render(batch);
        }
    }

    public void update() {
        for (int i = 0; i < numberOfProvinces; i++) {
            provinces[i].update();
        }
       user.sneding_troops(provinces);
    }


    public void render() {
        polyBatch.begin();
        for (int i = 0; i < numberOfProvinces; i++) {
            polygonRender(i);
        }
        polyBatch.end();

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Boot.INSTANCE.getScreenWidth(), Boot.INSTANCE.getScreenHeight());
        batch.end();
    }

    public void polygonRender(int polygonID) {
        if (provinces[polygonID].owner == 0)
            polySprite[polygonID].setColor(new Color(0x8e8e8eff)); // GRAY
        if (provinces[polygonID].owner == 1)
            polySprite[polygonID].setColor(new Color(Color.SKY)); // BLUE
        if (provinces[polygonID].owner == 2)
            polySprite[polygonID].setColor(new Color(Color.SALMON)); // RED
        if (provinces[polygonID].owner == 3)
            polySprite[polygonID].setColor(new Color(Color.OLIVE)); // GREEN
        if (provinces[polygonID].owner == 4)
            polySprite[polygonID].setColor(new Color(Color.GOLD)); // YELLOW

        polySprite[polygonID].draw(polyBatch);
    }

    public void testInitValues() {
        provinces[0].owner = 1;
        provinces[1].owner = 1;
        provinces[2].owner = 1;

        provinces[8].owner = 2;
        provinces[7].owner = 2;
        provinces[15].owner = 2;

        provinces[17].owner = 3;

        provinces[22].owner = 4;
    }


    public void polygonRendererInit() {
        polyBatch = new PolygonSpriteBatch();
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0, 0, 1, 1);
        pix.fill();
        pixTexture = new Texture(pix);
        TextureRegion textureRegion = new TextureRegion(texture);
        EarClippingTriangulator triangulator = new EarClippingTriangulator();

        ShortArray[] trianfleIndicies = new ShortArray[numberOfProvinces];
        PolygonRegion[] polyReg = new PolygonRegion[numberOfProvinces];
        polySprite = new PolygonSprite[numberOfProvinces];

        for (int i = 0; i < numberOfProvinces; i++) {
            trianfleIndicies[i] = triangulator.computeTriangles(vertices[i]);
            polyReg[i] = new PolygonRegion(textureRegion, vertices[i], trianfleIndicies[i].toArray());
            polySprite[i] = new PolygonSprite(polyReg[i]);
        }
    }

    float[][] vertices = {{396, 675, 387, 676, 374, 686, 360, 692, 354, 696, 348, 715, 343, 730, 337, 739, 324, 752, 317, 762, 311, 773, 315, 813, 322, 818, 354, 835, 354, 860, 371, 871, 378, 868, 389, 862, 403, 856, 411, 850, 426, 846, 437, 843, 439, 837, 441, 828, 438, 824, 417, 816, 414, 805, 413, 793, 418, 788, 425, 782, 433, 781, 451, 780, 451, 772, 450, 762, 447, 749, 443, 741, 440, 732, 437, 725, 443, 708, 439, 706, 415, 699, 410, 697},
            {399, 675, 411, 700, 443, 709, 435, 724, 450, 759, 451, 781, 474, 788, 504, 793, 533, 792, 543, 793, 567, 781, 572, 753, 560, 721, 547, 707, 535, 692, 537, 674, 524, 650, 535, 646, 550, 637, 540, 622, 534, 616, 510, 614, 488, 613, 474, 620, 454, 630, 439, 640, 419, 652, 411, 661},
            {550, 639, 524, 652, 537, 673, 536, 693, 559, 720, 571, 750, 568, 781, 604, 778, 638, 771, 672, 764, 715, 762, 709, 742, 704, 738, 676, 724, 704, 687, 704, 665, 658, 656, 659, 649, 668, 634, 664, 616, 661, 598, 601, 597, 587, 593, 577, 586, 572, 589, 543, 621},
            {666, 634, 655, 655, 705, 666, 705, 688, 676, 722, 707, 740, 712, 762, 747, 773, 776, 766, 800, 759, 843, 762, 907, 744, 892, 725, 901, 683, 908, 661, 888, 649, 881, 648, 861, 658, 811, 658, 781, 634, 763, 635, 748, 640, 739, 649, 710, 639, 702, 637, 690, 643, 674, 635},
            {884, 648, 908, 663, 896, 687, 889, 725, 909, 746, 926, 736, 950, 739, 948, 714, 968, 697, 981, 682, 1009, 673, 1051, 674, 1070, 658, 1088, 635, 1082, 608, 1111, 571, 1091, 540, 1072, 532, 1032, 532, 1003, 531, 999, 559, 983, 571, 982, 592, 929, 599, 924, 611, 897, 611, 879, 627, 880, 643},
            {950, 741, 998, 753, 1075, 823, 1111, 831, 1125, 808, 1100, 781, 1121, 764, 1113, 738, 1089, 740, 1096, 709, 1067, 703, 1052, 683, 1052, 671, 1013, 674, 984, 681, 974, 688, 958, 704, 949, 716},
            {1093, 711, 1091, 739, 1109, 737, 1122, 766, 1100, 782, 1126, 809, 1158, 807, 1175, 789, 1224, 774, 1253, 747, 1207, 699, 1208, 680, 1187, 665, 1161, 668, 1157, 684, 1135, 688, 1121, 707},
            {1126, 807, 1111, 832, 1178, 842, 1245, 861, 1296, 848, 1323, 823, 1378, 826, 1384, 814, 1358, 783, 1326, 779, 1307, 770, 1299, 761, 1297, 750, 1297, 740, 1268, 753, 1261, 751, 1254, 745, 1225, 773, 1208, 780, 1173, 788, 1165, 796, 1153, 806},
            {1298, 741, 1296, 761, 1312, 773, 1356, 784, 1381, 816, 1393, 808, 1418, 815, 1437, 811, 1442, 796, 1448, 785, 1471, 775, 1503, 762, 1535, 759, 1567, 757, 1575, 758, 1584, 715, 1583, 697, 1573, 686, 1559, 674, 1531, 685, 1502, 682, 1494, 679, 1486, 666, 1461, 677, 1441, 671, 1384, 711, 1365, 702, 1342, 719, 1317, 727},
            {362, 499, 367, 523, 354, 542, 376, 552, 416, 560, 428, 546, 463, 527, 504, 506, 512, 486, 533, 474, 527, 436, 482, 442, 452, 432, 434, 446, 414, 468, 386, 497},
            {414, 561, 429, 579, 449, 602, 454, 629, 487, 612, 534, 616, 541, 622, 577, 587, 593, 597, 662, 598, 699, 565, 707, 534, 756, 525, 752, 506, 760, 483, 728, 496, 665, 469, 677, 442, 669, 415, 644, 388, 613, 383, 589, 372, 564, 412, 542, 437, 524, 438, 532, 475, 507, 489, 502, 505, 461, 528, 425, 547},
            {662, 597, 668, 632, 686, 645, 711, 637, 739, 651, 752, 638, 783, 633, 811, 658, 857, 659, 886, 651, 879, 626, 864, 630, 845, 582, 854, 558, 871, 551, 881, 525, 864, 516, 818, 517, 795, 531, 771, 535, 756, 525, 705, 533, 700, 565, 674, 588},
            {880, 525, 869, 553, 851, 560, 842, 582, 863, 629, 880, 628, 897, 610, 925, 610, 932, 597, 982, 593, 984, 572, 1001, 559, 1003, 530, 959, 520, 921, 530, 881, 525},
            {1111, 574, 1080, 608, 1087, 638, 1050, 682, 1068, 706, 1093, 710, 1126, 706, 1138, 686, 1161, 682, 1162, 667, 1184, 665, 1207, 682, 1222, 671, 1222, 641, 1204, 615, 1202, 586, 1189, 581, 1141, 596, 1125, 583},
            {1193, 563, 1190, 582, 1202, 586, 1202, 614, 1221, 642, 1221, 673, 1206, 683, 1205, 698, 1266, 756, 1294, 745, 1330, 719, 1320, 649, 1320, 640, 1306, 613, 1318, 595, 1305, 573, 1283, 566, 1238, 571, 1218, 558},
            {1309, 579, 1317, 596, 1306, 614, 1320, 645, 1329, 717, 1344, 721, 1366, 703, 1383, 712, 1442, 671, 1460, 678, 1487, 668, 1464, 624, 1456, 590, 1463, 546, 1446, 543, 1423, 553, 1407, 564, 1382, 555, 1351, 564, 1326, 581},
            {350, 423, 365, 425, 380, 443, 379, 470, 359, 483, 361, 500, 389, 498, 426, 453, 450, 434, 480, 442, 542, 437, 545, 433, 566, 410, 589, 372, 571, 363, 530, 370, 504, 359, 490, 364, 475, 368, 459, 351, 422, 365, 388, 381, 364, 407},
            {491, 191, 488, 263, 497, 273, 469, 318, 460, 350, 476, 369, 501, 360, 528, 370, 570, 364, 607, 384, 640, 392, 651, 380, 651, 368, 649, 335, 664, 306, 719, 276, 724, 260, 746, 241, 719, 235, 719, 223, 692, 223, 660, 207, 634, 201, 565, 207, 531, 186},
            {717, 275, 663, 306, 648, 334, 653, 382, 644, 392, 667, 416, 676, 442, 665, 469, 724, 497, 763, 482, 762, 458, 788, 407, 783, 365, 788, 332, 780, 318, 745, 286, 731, 298},
            {788, 335, 783, 365, 788, 408, 762, 457, 763, 482, 752, 507, 756, 528, 776, 536, 804, 529, 819, 516, 863, 516, 878, 526, 890, 476, 921, 464, 908, 410, 913, 393, 945, 391, 964, 377, 954, 369, 963, 346, 918, 299, 875, 323, 861, 333, 830, 331, 813, 322},
            {913, 391, 907, 413, 919, 465, 887, 477, 880, 526, 921, 533, 958, 520, 1043, 533, 1063, 485, 1064, 456, 1090, 416, 1104, 385, 1126, 385, 1132, 358, 1115, 352, 1092, 373, 1074, 378, 1051, 362, 1014, 393, 990, 388, 987, 371, 959, 380, 943, 391},
            {1103, 384, 1080, 433, 1062, 456, 1061, 486, 1043, 534, 1072, 531, 1094, 542, 1110, 573, 1141, 595, 1191, 581, 1205, 509, 1218, 513, 1218, 496, 1196, 483, 1165, 479, 1159, 457, 1127, 446, 1139, 434, 1125, 385},
            {1132, 358, 1124, 387, 1138, 434, 1126, 445, 1159, 456, 1166, 479, 1196, 483, 1219, 499, 1219, 513, 1231, 513, 1267, 458, 1317, 427, 1306, 392, 1316, 367, 1296, 348, 1275, 342, 1234, 343, 1218, 319, 1199, 338, 1157, 351},
            {1205, 508, 1195, 567, 1217, 558, 1238, 572, 1283, 567, 1326, 582, 1353, 564, 1382, 555, 1407, 563, 1447, 543, 1461, 549, 1466, 516, 1437, 518, 1418, 499, 1418, 480, 1395, 479, 1393, 458, 1437, 450, 1452, 415, 1481, 385, 1507, 365, 1514, 343, 1500, 343, 1474, 347, 1434, 338, 1419, 326, 1362, 370, 1340, 363, 1314, 368, 1305, 392, 1315, 430, 1267, 458, 1241, 494, 1228, 511, 1217, 511},
            {765, 234, 726, 257, 720, 265, 717, 278, 733, 299, 745, 288, 782, 320, 789, 337, 813, 321, 832, 331, 863, 334, 890, 314, 918, 300, 929, 257, 908, 254, 925, 229, 944, 236, 948, 185, 913, 200, 891, 207, 856, 206, 831, 214, 810, 246, 785, 239},
            {949, 187, 941, 236, 922, 230, 910, 254, 930, 258, 919, 300, 947, 326, 955, 306, 984, 312, 1003, 293, 1016, 300, 1058, 259, 1071, 269, 1098, 246, 1124, 264, 1137, 260, 1155, 218, 1147, 196, 1100, 184, 1086, 194, 1061, 182, 1031, 188, 979, 168},
            {956, 304, 947, 326, 964, 345, 953, 369, 962, 380, 989, 372, 993, 390, 1016, 395, 1052, 363, 1075, 379, 1092, 373, 1115, 352, 1131, 361, 1203, 335, 1220, 322, 1216, 306, 1187, 305, 1180, 280, 1158, 290, 1133, 284, 1127, 263, 1100, 246, 1071, 269, 1055, 259, 1014, 299, 1002, 294, 982, 311}};
}

/*
POLYGON INFO COLLECTOR

if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            System.out.print(Gdx.input.getX() + "," + (1080 - Gdx.input.getY() - 64) + ",");
        }

PROVINCES CORDS COLLECTOR
if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            System.out.print("{" + (Gdx.input.getX() - 50 ) + "," + (Gdx.input.getY() + 50) + "},");
        }
 */
