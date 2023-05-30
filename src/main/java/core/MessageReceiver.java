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
                //System.out.println("kurwa" +type+'\n');
                if (type.equals("attack"))
                {
                    String srcs = Client.jsonResponse.get("src").toString();
                    int src = Integer.parseInt(srcs);
                    //System.out.println(srcs+'\n');
                    srcs = Client.jsonResponse.get("dst").toString();
                    int dsc = Integer.parseInt(srcs);
                    //System.out.println(srcs+'\n');
                    provinces[dsc].setValue(provinces[dsc].getValue() + provinces[src].getValue());
                    provinces[src].setValue(0);
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
