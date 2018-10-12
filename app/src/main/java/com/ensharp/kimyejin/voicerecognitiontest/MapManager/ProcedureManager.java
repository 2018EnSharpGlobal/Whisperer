package com.ensharp.kimyejin.voicerecognitiontest.MapManager;

import android.os.Bundle;
import android.util.Log;

import com.ensharp.kimyejin.voicerecognitiontest.InformationVO;
import com.ensharp.kimyejin.voicerecognitiontest.LocationVO;
import com.ensharp.kimyejin.voicerecognitiontest.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcedureManager {
    Sound sound;

    //위도 경도
    double user_latitude;
    double user_longitude;

    //사용자 층 수
    int user_floor;

    boolean use_elevator;
    boolean use_stair;
//
//    Node initialNode;
//    Node finalNode;

    List<Node> path;
    List<Node> floor;
    List<Node> other_floor;

    public ArrayList navi_text_List;
    private boolean isAnnouncementReleased = false;
    private InformationVO information;

    public ProcedureManager() {

        use_elevator = false;
        use_stair = false;

//        initialNode = new Node(5, 96);
//        initialNode.setFloor(-1);
//        finalNode = new Node(9, 53);
//        finalNode.setFloor(-2);

        path = null;

        floor = null;
        other_floor = null;

        navi_text_List = new ArrayList();

        sound = new Sound();
        information = InformationVO.getInstance();
    }

    public List<Node> real_get_Path(Node initialNode, Node finalNode){

        Navigation underGround_1 = new Navigation(MapInfo.map_rows,MapInfo.map_cols,MapInfo.B1);
        underGround_1.set_Initail_Final_Node(initialNode,finalNode);
        underGround_1.setNodes();
        underGround_1.setInformations(MapInfo.underGround_1_info);
        Navigation underGround_2 = new Navigation(MapInfo.map_rows, MapInfo.map_cols, MapInfo.B2);
        underGround_2.set_Initail_Final_Node(initialNode,finalNode);
        underGround_2.setNodes();
        underGround_2.setInformations(MapInfo.underGround_2_info);
        Navigation underGround_3 = new Navigation(MapInfo.map_rows, MapInfo.map_cols, MapInfo.B3);


        //층 수 가 다를 경우
        if (initialNode.getFloor() != finalNode.getFloor()) {

            int[][] initial_Info=null;
            int[][] final_Info=null;

            int initial_floor = 0;
            int final_floor = 0;

            switch (initialNode.getFloor()) {
                case MapInfo.B1:
                    initial_Info = MapInfo.underGround_1_info;
                    initial_floor = MapInfo.B1;
                    break;
                case MapInfo.B2:
                    initial_Info = MapInfo.underGround_2_info;
                    initial_floor = MapInfo.B2;
                    break;
                case MapInfo.B3:
                    initial_Info = MapInfo.underGround_3_info;
                    initial_floor = MapInfo.B3;
                    break;
            }

            switch (finalNode.getFloor()) {
                case MapInfo.B1:
                    final_Info = MapInfo.underGround_1_info;
                    final_floor = MapInfo.B1;
                    break;
                case MapInfo.B2:
                    final_Info = MapInfo.underGround_2_info;
                    final_floor = MapInfo.B2;
                    break;
                case MapInfo.B3:
                    final_Info = MapInfo.underGround_3_info;
                    final_floor = MapInfo.B3;
                    break;
            }

            //임시 테스트 플래그
            use_stair = true;

            //엘레베이터 선호 할 때
            if (use_elevator) {

                Node better_elevator = null;

                int[] check_Elevator = new int[3];
                for (int i = 0; i < check_Elevator.length; i++) {
                    check_Elevator[i] = 0;
                }

                switch (initialNode.getFloor()) {
                    case MapInfo.B1:
                        check_Elevator[0] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[22][93]);
                        check_Elevator[1] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[7][67]);
                        check_Elevator[2] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[15][67]);
//                        check_Elevator[3] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[5][4]);
                        break;
                    case MapInfo.B2:
                        check_Elevator[0] = underGround_1.calculate_F(initialNode, underGround_2.getSearchArea()[22][93]);
                        check_Elevator[1] = underGround_1.calculate_F(initialNode, underGround_2.getSearchArea()[7][67]);
                        check_Elevator[2] = underGround_1.calculate_F(initialNode, underGround_2.getSearchArea()[15][67]);
//                        check_Elevator[3] = underGround_2.calculate_F(initialNode, underGround_2.getSearchArea()[5][4]);
                        break;
                    case MapInfo.B3:
                        check_Elevator[0] = underGround_1.calculate_F(initialNode, underGround_3.getSearchArea()[22][93]);
                        check_Elevator[1] = underGround_1.calculate_F(initialNode, underGround_3.getSearchArea()[7][67]);
                        check_Elevator[2] = underGround_1.calculate_F(initialNode, underGround_3.getSearchArea()[15][67]);
//                        check_Elevator[3] = underGround_3.calculate_F(initialNode, underGround_3.getSearchArea()[5][4]);
                        break;
                    default:
                        break;
                }
                switch (finalNode.getFloor()) {
                    case MapInfo.B1:
                        check_Elevator[0] += underGround_1.calculate_F(underGround_1.getSearchArea()[22][93], finalNode);
                        check_Elevator[1] += underGround_1.calculate_F(underGround_1.getSearchArea()[7][67], finalNode);
                        check_Elevator[2] += underGround_1.calculate_F(underGround_1.getSearchArea()[15][67], finalNode);
//                        check_Elevator[3] = underGround_1.calculate_F(underGround_1.getSearchArea()[5][4], finalNode);
                        break;
                    case MapInfo.B2:
                        check_Elevator[0] += underGround_2.calculate_F(underGround_2.getSearchArea()[22][93], finalNode);
                        check_Elevator[1] += underGround_2.calculate_F(underGround_2.getSearchArea()[7][67], finalNode);
                        check_Elevator[2] += underGround_2.calculate_F(underGround_2.getSearchArea()[15][67], finalNode);
//                        check_Elevator[3] = underGround_2.calculate_F(underGround_2.getSearchArea()[5][4], finalNode);
                        break;
                    case MapInfo.B3:
                        check_Elevator[0] += underGround_3.calculate_F(underGround_3.getSearchArea()[22][93], finalNode);
                        check_Elevator[1] += underGround_3.calculate_F(underGround_3.getSearchArea()[7][67], finalNode);
                        check_Elevator[2] += underGround_3.calculate_F(underGround_3.getSearchArea()[15][67], finalNode);
//                        check_Elevator[3] = underGround_3.calculate_F(underGround_3.getSearchArea()[5][4], finalNode);
                        break;
                    default:
                        break;
                }

                int find_component = compare_Minimum(check_Elevator);

                switch (find_component + 7) {  //여기서 더하는 7 값은 보정값
                    case MapInfo.elevator_1:
                        better_elevator = new Node(22,93);
//                                underGround_1.getSearchArea()[22][93];
                        break;
                    case MapInfo.elevator_2:
                        better_elevator = new Node(7,67);
//                                underGround_1.getSearchArea()[7][67];
//                        better_elevator = underGround_1.getSearchArea()[7][67];
                        break;
                    case MapInfo.elevator_3:
                        better_elevator = new Node(15,67);
//                                underGround_1.getSearchArea()[15][67];
                        break;
//                    case MapInfo.elevator_4:
//                        better_elevator = underGround_1.getSearchArea()[5][4];
//                        break;
                    default:
                        break;
                }

                Navigation initial_Navi = new Navigation(MapInfo.map_rows,MapInfo.map_cols,initial_floor);
                Navigation final_Navi = new Navigation(MapInfo.map_rows,MapInfo.map_cols,final_floor);

                initial_Navi.set_Initail_Final_Node(initialNode,better_elevator);
                initial_Navi.setNodes();
                initial_Navi.setInformations(initial_Info);

                final_Navi.set_Initail_Final_Node(better_elevator,finalNode);
                final_Navi.setNodes();
                final_Navi.setInformations(final_Info);

                floor = initial_Navi.findPath();
                other_floor = final_Navi.findPath();

                path = floor;
                path.addAll(other_floor);

            }

            else if (use_stair) {
                Node floor_stair = null;
                Node other_floor_stair = null;

                int[] check_Stair = new int[6];
                for (int i = 0; i < check_Stair.length; i++) {
                    check_Stair[i] = 0;
                }

                int check = 0;

                switch (initialNode.getFloor()) {
                    case MapInfo.B1:
                        check = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[15][78]);
//                        check_Stair[1] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[5][2]);
//                        check_Stair[2] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[5][3]);
//                        check_Stair[3] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[5][4]);
//                        check_Stair[4] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[5][5]);
//                        check_Stair[5] = underGround_1.calculate_F(initialNode, underGround_1.getSearchArea()[5][6]);
                        break;
                    case MapInfo.B2:
                        check = underGround_2.calculate_F(initialNode, underGround_2.getSearchArea()[12][69]);
//                             check_Stair[1] = underGround_2.calculate_F(initialNode, underGround_2.getSearchArea()[5][2]);
//                        check_Stair[2] = underGround_2.calculate_F(initialNode, underGround_2.getSearchArea()[5][3]);
//                        check_Stair[3] = underGround_2.calculate_F(initialNode, underGround_2.getSearchArea()[5][4]);
//                        check_Stair[4] = underGround_2.calculate_F(initialNode, underGround_2.getSearchArea()[5][5]);
//                        check_Stair[5] = underGround_2.calculate_F(initialNode, underGround_2.getSearchArea()[5][6]);
                        break;
                    case MapInfo.B3:
//                        check = underGround_3.calculate_F(initialNode, underGround_3.getSearchArea()[15][78]);
//                        check_Stair[1] = underGround_3.calculate_F(initialNode, underGround_3.getSearchArea()[5][2]);
//                        check_Stair[2] = underGround_3.calculate_F(initialNode, underGround_3.getSearchArea()[5][3]);
//                        check_Stair[3] = underGround_3.calculate_F(initialNode, underGround_3.getSearchArea()[5][4]);
//                        check_Stair[4] = underGround_3.calculate_F(initialNode, underGround_3.getSearchArea()[5][5]);
//                        check_Stair[5] = underGround_3.calculate_F(initialNode, underGround_3.getSearchArea()[5][6]);
                        break;
                    default:
                        break;
                }

                switch (finalNode.getFloor()) {
                    case MapInfo.B1:
                        check += underGround_1.calculate_F( underGround_1.getSearchArea()[15][78],finalNode);
//                        check_Stair[1] = underGround_1.calculate_F(underGround_1.getSearchArea()[5][2], finalNode);
//                        check_Stair[2] = underGround_1.calculate_F(underGround_1.getSearchArea()[5][3], finalNode);
//                        check_Stair[3] = underGround_1.calculate_F(underGround_1.getSearchArea()[5][4], finalNode);
//                        check_Stair[4] = underGround_1.calculate_F(underGround_1.getSearchArea()[5][5], finalNode);
//                        check_Stair[5] = underGround_1.calculate_F(underGround_1.getSearchArea()[5][6], finalNode);
                        break;
                    case MapInfo.B2:
                        check += underGround_2.calculate_F(underGround_2.getSearchArea()[12][69], finalNode);
//                        check_Stair[1] = underGround_2.calculate_F(underGround_2.getSearchArea()[5][2], finalNode);
//                        check_Stair[2] = underGround_2.calculate_F(underGround_2.getSearchArea()[5][3], finalNode);
//                        check_Stair[3] = underGround_2.calculate_F(underGround_2.getSearchArea()[5][4], finalNode);
//                        check_Stair[4] = underGround_2.calculate_F(underGround_2.getSearchArea()[5][5], finalNode);
//                        check_Stair[5] = underGround_2.calculate_F(underGround_2.getSearchArea()[5][6], finalNode);
                        break;
                    case MapInfo.B3:
//                        check += underGround_3.calculate_F( underGround_3.getSearchArea()[15][78],finalNode);
//                        check_Stair[1] = underGround_3.calculate_F(underGround_3.getSearchArea()[5][2], finalNode);
//                        check_Stair[2] = underGround_3.calculate_F(underGround_3.getSearchArea()[5][3], finalNode);
//                        check_Stair[3] = underGround_3.calculate_F(underGround_3.getSearchArea()[5][4], finalNode);
//                        check_Stair[4] = underGround_3.calculate_F(underGround_3.getSearchArea()[5][5], finalNode);
//                        check_Stair[5] = underGround_3.calculate_F(underGround_3.getSearchArea()[5][6], finalNode);
                        break;
                    default:
                        break;
                }

                //int find_component = compare_Minimum(check[0]);

                switch (MapInfo.stair_1) {  //여기서 더하는 11 값은 보정값
                    case MapInfo.stair_1:
                        floor_stair = underGround_1.getSearchArea()[15][78];
                        other_floor_stair = underGround_2.getSearchArea()[12][69];
                        break;
//                    case MapInfo.stair_2:
//                        floor_stair = underGround_1.getSearchArea()[5][2];
//                        other_floor_stair = underGround_2.getSearchArea()[6][2];
//                        break;
//                    case MapInfo.stair_3:
//                        floor_stair = underGround_2.getSearchArea()[5][3];
//                        other_floor_stair = underGround_3.getSearchArea()[6][3];
//                        break;
//                    case MapInfo.stair_4:
//                        floor_stair = underGround_2.getSearchArea()[5][4];
//                        other_floor_stair = underGround_3.getSearchArea()[6][4];
//                        break;
//                    case MapInfo.stair_5:
//                        floor_stair = underGround_2.getSearchArea()[5][5];
//                        other_floor_stair = underGround_3.getSearchArea()[6][5];
//                        break;
//                    case MapInfo.stair_6:
//                        floor_stair = underGround_2.getSearchArea()[5][6];
//                        other_floor_stair = underGround_3.getSearchArea()[6][6];
//                        break;
                    default:
                        break;
                }

                Navigation stair_path = new Navigation(MapInfo.map_rows, MapInfo.map_cols,MapInfo.B1);
                stair_path.set_Initail_Final_Node(initialNode,floor_stair);
                stair_path.setNodes();
                stair_path.setInformations(MapInfo.underGround_1_info);
                floor = stair_path.findPath();

                stair_path.set_Initail_Final_Node(other_floor_stair,finalNode);
                stair_path.setInformations(MapInfo.underGround_2_info);
                other_floor = stair_path.findPath();


                path = floor;
                path.addAll(other_floor);

            } else if (!use_elevator && !use_stair) {

            }
        } else {

            Navigation temp = null;

            switch (initialNode.getFloor()) {
                case MapInfo.B1:
                    temp = underGround_1;
                    break;
                case MapInfo.B2:
                    temp = underGround_2;
                    break;
                case MapInfo.B3:
                    temp = underGround_3;
                    break;
            }

            temp.set_Initail_Final_Node(initialNode, finalNode);

            path = temp.findPath();

        }

//        //경로 로그에 출력하기
//        for (Node node : path) {
//            Log.e("Path", node.toString());
//        }

        return path;
    }

    //노드가 가는 경로가 잘 가고 있는지 체크하기
    public boolean user_CheckPosition(List<Node> path,int user_row,int user_col){
        boolean check_Position = false;
        for(Node node : path) {
            if (node.getRow() - 1 >= 0 &&
                    node.getCol() - 1 >= 0 &&
                    node.getRow() + 1 <= MapInfo.map_rows &&
                    node.getCol() + 1 <= MapInfo.map_cols) {
                if (node.getRow() - 1 <= user_row ||
                        user_row <= node.getRow() + 1 ||
                        node.getCol() - 1 <= user_col ||
                        user_col <= node.getCol() + 1) {
                    check_Position = true;
                }
            }
        }
        if(check_Position){
            return true;
        }
        else{
            return false;
        }
    }

    //최소의 F값 찾기
    public  int compare_Minimum(int[] means_transportation){
        int find_Minimum = means_transportation[0];
        for(int i=1;i<means_transportation.length;i++){
            if(find_Minimum >= means_transportation[i]){
                find_Minimum = means_transportation[i];
            }
        }

        int find_component;

        for(find_component=0;find_component<means_transportation.length;find_component++){
            if(find_Minimum == means_transportation[find_component])
                break;
        }

        return find_component;
    }

    public Node get_currentNode(double user_latitude,double user_longitude){
        Node current_Node = null;
        user_latitude -= 37.54715706;
        user_longitude -= 127.07383858;

        double b = 93806;
        double a = 158983;
        double c = 184595;

        user_latitude = a / c * user_latitude + b / c * user_longitude;
        user_longitude = -b / c * user_latitude + a / c * user_longitude;

        int count_row = Integer.parseInt (String.valueOf(user_latitude / 0.00001999));
        int count_col = Integer.parseInt(String.valueOf(user_longitude / 0.00002148));


        current_Node = new Node(count_row,count_col);

        if(count_row < 0 || count_col <0){
            return null;
        }
        else{
            return current_Node;
        }
    }
    //목적지에 대한 노드 얻기

    public String get_Device_bearing(double bearing){
        if(bearing >= MapInfo.up_bearing -45 && bearing <= MapInfo.up_bearing + 45){
            return "앞";
        }
        else if(bearing >= MapInfo.left_bearing -45 && bearing <= MapInfo.left_bearing +45){
            return "왼쪽";
        }
        else if(bearing >= MapInfo.down_bearing -45 && bearing <= MapInfo.down_bearing +45){
            return "아래쪽";
        }
        else{
            return "오른쪽";
        }
    }

    public void Navi_Path(){
        int i=0;


        for(i=0;i<path.size()-1; i++){
            int change_row = 0;
            int change_col = 0;

            String navi_text = "";

            change_row = path.get(i+1).getRow() - path.get(i).getRow();
            change_col = path.get(i+1).getCol() - path.get(i).getCol();

            if(change_row == 1 && change_col ==0){
                navi_text= "왼쪽";
            }
            else if(change_row == -1 && change_col ==0){
                navi_text= "오른쪽";
            }
            else if(change_row == 0 && change_col == 1){
                navi_text= "아래쪽";
            }
            else if(change_row == 0 && change_col == -1){
                navi_text= "위쪽";
            }
            else if(change_row == 1 && change_col ==1){
                navi_text= "왼쪽아래";
            }
            else if(change_row == 1 && change_col ==-1){
                navi_text= "왼쪽위";
            }
            else if(change_row ==-1 && change_col ==1){
                navi_text= "오른쪽아래";
            }
            else if(change_row ==-1 && change_col ==-1){
                navi_text= "오른쪽위";
            }
            else{
                navi_text = "이동수단";
            }

            navi_text_List.add(navi_text);

        }

    }

    public void outPut_Navi(){
        String current_text =(String)navi_text_List.get(0);

        if(current_text.equals("이동수단")){
            Log.e("이동수단","2000 계단 남음");
            navi_text_List.remove(0);
            return ;
        }

        int count = 1 ;

        int i=1;
        while((String)navi_text_List.get(i)== current_text){
            i++;
            count++;
        }

        Log.e("i의 값",String.valueOf(i));

//        for(Object text : navi_text_List){
//            Log.e("navi",(String)text);
//        }

        for(int j=0;j<i;j++){
            navi_text_List.remove(0);
        }

        Log.e(" 방향",current_text);
        Log.e("거리",String.valueOf(count*MapInfo.node_space)+"m");

        String announcement;

        if (!isAnnouncementReleased) {
            announcement = String.format("%s역으로 안내를 시작합니다.%s 으로 %s 미터 가십시오", information.getDestination(), current_text, String.valueOf(count*MapInfo.node_space));
            isAnnouncementReleased = true;
        }
        else {
            announcement = String.format("%s 으로 %s 미터 가십시오", current_text, String.valueOf(count*MapInfo.node_space));
        }


        sound.playSound(announcement);
//        sound.playSound("이다인                      메롱       약오르지 까꿍");
//        sound.playSound(current_text + "으로" + String.valueOf(count*MapInfo.node_space)+"미터");
        Log.e("sound", "Aaa");
    }
}
