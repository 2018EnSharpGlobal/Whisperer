package com.ensharp.kimyejin.voicerecognitiontest.InformationExtractor;

import android.util.Log;

import com.ensharp.kimyejin.voicerecognitiontest.InformationVO;
import com.ensharp.kimyejin.voicerecognitiontest.LocationVO;
import com.ensharp.kimyejin.voicerecognitiontest.MapManager.Node;
import com.ensharp.kimyejin.voicerecognitiontest.MapManager.ProcedureManager;
import com.ensharp.kimyejin.voicerecognitiontest.Navigation;
import com.ensharp.kimyejin.voicerecognitiontest.Sound;

public class InformationCollector {
    private InformationVO information;
    private boolean isMappedPlace = true;
    private Navigation navigation;
    private ProcedureManager procedureManager;
    private Node destinationNode;
    private LocationVO location;


    public InformationCollector() {
        information = InformationVO.getInstance();
        navigation = new Navigation();
        procedureManager = new ProcedureManager();
        location = LocationVO.getInstance();
    }

    public void run() {
        if (!checkExistenceOfinformations())
            Log.e("ERROR_MODE", "명령어 이해 불가");
    }

    public boolean checkExistenceOfinformations() {
        if (!checkIntention()) return false;

        return true;
    }

    public boolean checkDeparture() {
        if (information.getDeparture().isEmpty() || information.getDeparture().equals("null")) {
            // 현재 위치가 매핑된 곳인지 확인
            //  1. 매핑된 곳이라면 현재 위치를 출발점으로
            // if (isMappedPlace) information.setDeparture(현재위치);
            //  2. 매핑된 곳이 아니라면
            // if (information.getIntend().equals("가다")) 지금 계신 위치에서 안내할 수 없다는 오류 메시지; return false;
            // else 출발지를 다시 묻는 메시지(프로토타입의 경우 필요 없음); // 충족되면 true, 아니면 false;
        }

        return true;
    }

    public String getCurrentLocation() {
        return "어린이대공원";
    }

    public boolean isMatchDeparture() {
        if (getCurrentLocation().matches(information.getDestination())) return true;
        else return false;
    }

    public boolean checkIntention() {
        switch (information.getIntend()) {
            case "":
                // 동사가 입력되지 않은 경우
                break;
            case "null":
                // 동사라고 생각한 단어가 사전에 등록되어있지 않은 경우
                break;
            case "가다":
                checkInformationForGo();
                break;
            case "알려주다":

                break;
            case "전화하다":
                break;
        }

        return true;
    }

    public void checkInformationForGo() {
        if (checkDestination()) {
            procedureManager.real_get_Path(new Node(5, 96), destinationNode);
            procedureManager.Navi_Path();
            procedureManager.outPut_Navi();

        }

    }

    public boolean checkDestination() {
        switch (information.getDestination()) {
            case "": // 입력이 없거나 도착지가 잘못된 경우()

                break;
            case "null": // 도착지가 잘못된 경우

                break;
            default:
                // isMatchDeparture();
                destinationNode = navigation.callNavigation();
                break;
        }
        return true;
    }
}
