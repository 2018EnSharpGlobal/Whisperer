package com.ensharp.kimyejin.voicerecognitiontest;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ensharp.kimyejin.voicerecognitiontest.MainActivity;
import com.ensharp.kimyejin.voicerecognitiontest.R;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

public class Sound implements TextToSpeechListener, SpeechRecognizeListener {
    // for kakao
    private SpeechRecognizerClient client;
    private TextToSpeechClient ttsClient;
    private boolean isSpeechMode;
    private String serviceType = SpeechRecognizerClient.SERVICE_TYPE_DICTATION;
    private String speechMode = TextToSpeechClient.NEWTONE_TALK_2;
    private String voiceType = TextToSpeechClient.VOICE_MAN_DIALOG_BRIGHT;
    private double speechSpeed = 4.0D;

    public Sound() {
        isSpeechMode = false;
    }

    public void playSound(String output) {
        if (ttsClient != null && ttsClient.isPlaying()) {
            ttsClient.stop();
            return;
        }

        ttsClient = new TextToSpeechClient.Builder()
                .setSpeechMode(speechMode)
                .setSpeechSpeed(speechSpeed)
                .setSpeechVoice(voiceType)
                .setListener(this)
                .build();

        ttsClient.play(output);
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    @Override
    public void onResults(Bundle results) {
        // result of user input
        String result = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS).get(0);

        client = null;
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {
        if (isSpeechMode)
            isSpeechMode = false;
        else
            ttsClient = null;
    }

    @Override
    public void onError(int code, String message) {
        if (isSpeechMode) {
            client = null;
            isSpeechMode = false;
        }
        else {
            handleError(code);
            ttsClient = null;
        }
    }

    private void handleError(int errorCode) {
        String errorText;
        switch (errorCode) {
            case TextToSpeechClient.ERROR_NETWORK:
                errorText = "네트워크 오류";
                break;
            case TextToSpeechClient.ERROR_NETWORK_TIMEOUT:
                errorText = "네트워크 지연";
                break;
            case TextToSpeechClient.ERROR_CLIENT_INETRNAL:
                errorText = "음성합성 클라이언트 내부 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_INTERNAL:
                errorText = "음성합성 서버 내부 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_TIMEOUT:
                errorText = "음성합성 서버 최대 접속시간 초과";
                break;
            case TextToSpeechClient.ERROR_SERVER_AUTHENTICATION:
                errorText = "음성합성 인증 실패";
                break;
            case TextToSpeechClient.ERROR_SERVER_SPEECH_TEXT_BAD:
                errorText = "음성합성 텍스트 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_SPEECH_TEXT_EXCESS:
                errorText = "음성합성 텍스트 허용 길이 초과";
                break;
            case TextToSpeechClient.ERROR_SERVER_UNSUPPORTED_SERVICE:
                errorText = "음성합성 서비스 모드 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_ALLOWED_REQUESTS_EXCESS:
                errorText = "허용 횟수 초과";
                break;
            default:
                errorText = "정의하지 않은 오류";
                break;
        }

        final String statusMessage = errorText + " (" + errorCode + ")";

        Log.e("LISTEN_MODE", "ERROR:" + statusMessage);
    }
}
