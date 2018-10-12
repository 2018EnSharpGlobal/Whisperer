package com.ensharp.kimyejin.voicerecognitiontest;

import android.util.Log;

import com.ensharp.kimyejin.voicerecognitiontest.InformationExtractor.JSONTask;
import com.ensharp.kimyejin.voicerecognitiontest.MapManager.Node;

public class Navigation {

    InformationVO information;
    Sound sound;

    public Navigation() {
        information = InformationVO.getInstance();
        sound = new Sound();
    }

    public Node callNavigation() {
        Node destination_Node = null;
        switch (information.getTag()) {
            case "station":
                if(Integer.parseInt(information.getDeparture().split(",")[2]) < information.getCode()) {
                    Log.e("test", "상행");
                    destination_Node = new Node(9,53);
                    destination_Node.setFloor(-2);
                }
                else {
                    Log.e("test", "하행");
                    destination_Node = new Node(15,53);
                    destination_Node.setFloor(-2);
                }
                break;
            case "lavatory":
                Log.e("test", "똥");
                destination_Node = new Node(17, 62);
                destination_Node.setFloor(-2);
                break;
            case "exit":
                break;
            default:
                break;
        }
        return destination_Node;
    }
}
