package core;

import Screens.GameScreen;

public class MessageReceiver extends Thread {
    @Override
    public void run()
    {
        while(true)
        {
            if (Client.jsonResponse != null)
            {
                String type = Client.jsonResponse.get("type").toString();
                if (type.equals("attack"))
                {
                    String srcs = Client.jsonResponse.get("src").toString();
                    int src = Integer.parseInt(srcs);
                    srcs = Client.jsonResponse.get("dst").toString();
                    int dst = Integer.parseInt(srcs);
                    if(GameScreen.unitHandler != null){
                        GameScreen.unitHandler.sendUnits(src, dst);
                    }
                    Client.jsonResponse = null;
                }
                else if(type.equals("map")){
                    System.out.println(Client.jsonResponse);
                    MessageUtility.jsonMapMessageDecoder(Client.jsonResponse);
                    Client.jsonResponse = null;
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
