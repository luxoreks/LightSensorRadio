package com.example.LightSensitiveRadio.startScreen;

public class TextProvider {

    private static final int howManySongs = 5;
    private static final int howManyAds = 5;

    private static final String message = String.format("Ta aplikacja nie jest idealna dlatego wymaga początkowych ustawień " +
            "od użytkownika. Są one NIEZBĘDNE do poprawnego działania aplikacji. "+
            "Ale spokojnie, to nie jest nic trudnego. \n"+
            "Oto niezbędne kroki jakie należy wykonać PRZED wciśnięciem przycisku \"Dalej\":\n"+
            "1. W pamięci głównej urządzenia należy utworzyć folder o nazwie \"sfx\"\n"+
            "2. Na początku w folderze \"sfx\" należy umieścić minimum %s plików audio, inaczej aplikacja nie będzie działać \n"+
            "3. W folderze\"sfx\" należy utworzyć dwa kolejne foldery: \"pzg\" oraz \"ads\"\n"+
            "4. W folderze \"pzg\" należy umieścić 2 pliki audio: \"zgrywus.mp3\" oraz \"silence.mp3\" \n"+
            "5. w folderze \"ads\" należy umieścić conajmniej %s plików audio"+
            "\n\n"+
            "Uwagi: \n"+
            "a) Brak uwag", howManySongs, howManyAds);

    public static String getStaringMessage(){
        return message;
    }
}