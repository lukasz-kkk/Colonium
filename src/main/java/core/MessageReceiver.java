package core;

import objects.Province;

import static objects.Map.provinces;

public class MessageReceiver extends Thread {
    @Override
    public void run()
    {
        while(true)
        {
            //System.out.println(Client.jsonResponse);
            if (Client.jsonResponse != null)
            {
                String type = Client.jsonResponse.get("type").toString();
                if (type.equals("attack"))
                {
                    String srcs = Client.jsonResponse.get("src").toString();
                    int src = Integer.parseInt(srcs);
                    //System.out.println(srcs+'\n');
                    srcs = Client.jsonResponse.get("dst").toString();
                    int dst = Integer.parseInt(srcs);
                    //System.out.println(srcs+'\n');
//                    provinces[dst].setValue(provinces[dst].getValue() + provinces[src].getValue());
//                    provinces[src].setValue(0);
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
