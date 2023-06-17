
from utils import log


class Player():
    def __init__(self,conn,addr) -> None:
        self.connection=conn
        self.address=addr
        self.connected=True
    def run(self):
        while self.connected:
            msg = self.connection.recv(1024)
            msg = msg.decode('utf-8')
            log(f"[RECIVED MESSAGE]",3)
            log(f"{self.address}: {msg}",2)
            if msg=="disconunu":
                con=False
                break
            out=msg.encode("utf-8")
            self.send_response(msg+'\n')
    def send_response(self, msg):
        for receiver in PLAYERS:
            log(f"[MESSAGE SENT TO {receiver.address}]",3)
            receiver.connection.sendall(msg.encode('utf-8'))

    