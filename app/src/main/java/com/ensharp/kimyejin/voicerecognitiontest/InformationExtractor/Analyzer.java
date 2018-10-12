package com.ensharp.kimyejin.voicerecognitiontest.InformationExtractor;

import android.nfc.Tag;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.ensharp.kimyejin.voicerecognitiontest.Constant;
import com.ensharp.kimyejin.voicerecognitiontest.InformationVO;
import com.ensharp.kimyejin.voicerecognitiontest.MainActivity;
import com.ensharp.kimyejin.voicerecognitiontest.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Analyzer {

	private MainActivity main;
	private JSONTask serverManager;
	private String serverAddress;
	private boolean isEnd;
	private String word;

	private String line;
	private List<String> words = new ArrayList<String>();
	private String departuralJosa = "에서";
	private String[] josas = {"에", "으로", "로", "을", "를"};

	private InformationVO information;
	private String departure;
	private String destination;
	private String destinationTag;
	private int destinationCode;
	private String intend;

	private InformationCollector informationCollector;

	public Analyzer(MainActivity main, String address) {
		this.main = main;
		serverAddress = String.format("http://%s.ngrok.io/", address);

		information = InformationVO.getInstance();
		informationCollector = new InformationCollector();
	}

	public void analyzeLine() {
		clearAllInformation();
		line = ((TextView)main.findViewById(R.id.inputText)).getText().toString();

		analyzeWords();
		isEnd = true;
	}
	
	public void clearAllInformation() {
		line = "";
		departure = "";
		destination = "";
		destinationTag= "";
		destinationCode = -1;
		intend = "";
	}
	
	public void analyzeWords() {
		isEnd = false;
		words = Arrays.asList(line.split(" "));

		for (int i = 0; i < words.size(); i++) {
			separateIntoMorpheme(words.get(i));
		}
	}
	
	public void separateIntoMorpheme(String word) {
		String temp;
		this.word = word;

		for (int i = 0; i < josas.length; i++) {
			if (word.contains(josas[i])) {
				if (word.indexOf(josas[i]) == word.length() - josas[i].length())
				{
					temp = word.substring(0, word.indexOf(josas[i]));
					executeServer("noun","name", temp, Constant.DESTINATION);
					return;
				}	
			}
		}

		if (word.contains(departuralJosa)) {
			if (word.indexOf(departuralJosa) == word.length() - departuralJosa.length())
			{
				temp = word.substring(0, word.indexOf(departuralJosa));
				executeServer("noun", "name", temp, Constant.DEPARTURE);
				return;
			}
		}

        if (word.equals("싶다") || word.equals("싶어")) return;
        Log.e("동사", "명사검사."+word);
		executeServer("noun", "name", word, Constant.NOUN);
	}

	public void anaylizeVerb(String word) {
        Log.e("동사", "검사한다."+word);




		executeServer("verb", "name", word, Constant.INTEND);
	}
	
	public void printWords() {
		((EditText)main.findViewById(R.id.detailInformation)).setText(getResult());
	}

	public void setInformation() {
	    information.setInformations(departure, destination, intend);
	    information.setDetail(destinationTag, destinationCode);
    }

	public void executeServer(String table, String column, String value, int purpose) {
		String address = String.format("%s%s", serverAddress, table);

		serverManager = new JSONTask(column, value, purpose);
		serverManager.setAnalyzer(this);
		serverManager.execute(address);
	}

	public void setDestination(String result) {
		this.destination = result.split(",")[0];
		executeServer("noun", "name", result.split(",")[0], Constant.TAG);
	}

	public void setDestinationDetail(String result) {
		destinationTag = result.split(",")[1];
		destinationCode = Integer.parseInt(result.split(",")[2]);
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public void setIntend(String intend) {
		this.intend = intend;
	}

	public String getResult() {
		StringBuilder result = new StringBuilder();

		result.append(String.format("출발지:%s\n", departure));
		result.append(String.format("도착지:%s\n", destination));
		result.append(String.format("의도:%s\n", intend));
        result.append(String.format("part:%s\n", destinationTag));
        result.append(String.format("code:%d", destinationCode));

		return result.toString();
	}

	public boolean getIsEnd() {return isEnd;}

	public void runInformationCollector() {informationCollector.run();}
}
