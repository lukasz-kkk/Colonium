import socket
import threading
import os
from utils import *
import time
from player import Player
from map import *
from signal import signal, SIGPIPE, SIG_DFL  
import sys
signal(SIGPIPE,SIG_DFL) 
PLAYERS=[]
LOBBIES=[]
PORT = 2137
SERVER="20.117.176.229"
SERVER = socket.gethostbyname(socket.gethostname())
server = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1)
server.bind((SERVER,PORT))
class Lobby():
    def __init__(self,name) -> None:
        self.players=[]
        self.name=name
        self.game=None 
        self.started = 0
        self.ended=0
        self.winner=None
        f=open('map_1.json','r')
        x=json.loads(f.read())
        f.close()
        self.starting_positions=x["starting_positions"]
        
        #create_map_thread("map_1.json")
        #while self.game == None:
        #    self.game=get_map()
        self.tickrate=60
        thread = threading.Thread(target=self.creator)
        thread.start()
    def creator(self):
        self.game=Map('map_1.json') ##TODO starting positions
        while True:
            if self.started == 1:
                self.game.updateMap()
                #print(time.time()-st)
                time.sleep(1/self.game.tickrate)
    def start(self):
        if self.started==1:
            return
        count=0
        for pl in self.players:
            self.game.provinces[self.starting_positions[count]].owner=pl.name
            count+=1
        self.started=1
    def reset_map(self):
        t=self.game.temp
        while t==self.game.temp:
            #time.sleep(2)
            self.game.reset("map_1.json")
    def show_lobby(self):
        out=''
        for a in self.game.provinces:
            out+=f"[ID]{a.id}|[ARMY]{a.army}\n"
        log(out,5)
    def select_map(self,name):
        self.map_name=name

class Player():
    def __init__(self,conn,addr) -> None:
        self.connection=conn
        self.address=addr
        self.connected=True
        self.name="None"
        self.gold=0
        global z
    @staticmethod
    def create_lobby(self,lobbyname):
        global LOBBIES
        for lbb in LOBBIES:
            if lbb.name==lobbyname:
                return False
        new_lobby=Lobby(lobbyname)
        new_lobby.players.append(self)
        #log(f"[LOBBY CREATED {lobbyname}]",3)
        LOBBIES.append(new_lobby)
        return new_lobby in LOBBIES

    def join_lobby(self,lobbyname):
        global LOBBIES
        for lobb in LOBBIES:
            if lobb.name==lobbyname:
                lobb.players.append(self)
                return True
        return False
    def send_lobbies(self):
        global LOBBIES
        
        x={}
        x.update({"type":"lobbies"})
        x.update({"lobbies":[]})
        for lbb in LOBBIES:
            plyrs=[]
            for pl in lbb.players:
                plyrs.append(pl.name)
            xd={}
            xd.update({"name":f"{lbb.name}"})
            xd.update({"players":plyrs})
            x['lobbies'].append(xd)
        print(str(x).replace("'",'"').replace("None",'null'))
        self.respond(str(x).replace("'",'"').replace("None",'null')+"\n")
    
    def send_ok(self):
        out=str({"type":"success"}).replace("'",'"')
        self.respond(out+"\n")
        
    def send_error(self,error):
        out=str({"type":"failed","message":"errorcode"}).replace("errorcode",str(error)).replace("'",'"')
        self.respond(out+"\n")

    def handle_order(self,order):
        try:
                error=None
                
                if order["type"]=="set_name": ##SETNAME
                    
                    for lbb in LOBBIES:
                        for pl in lbb.players:
                            if pl.name==order["value"]:
                                self.send_error("Nickname already exists")
                                return
                    self.name=order["value"]
                    log("NAME CHANGED TO : "+str(order["value"]),4)
                    self.send_ok()
                    return
                
                if order["type"]=="attack": ##ATTACK
                    log(f"[{self.name}:ATTACK ORDER]",4)
                    for lbb in LOBBIES:
                        if self in lbb.players:
                            lbb.game.attack(order["src"],order["dst"])
                    self.send_response_to_lobby(self.msg+'\n')
                    return
                if order["type"]=="create_lobby": ##ATTACK
                    ok=False
                    ok=Player.create_lobby(self,order["lobby_name"])
                    if ok:
                        log(f"[{self.name} CREATED LOBBY {order['lobby_name']}]",4)
                        self.send_ok()
                    else:
                        log(f"[{self.name} FAILED TO CREATE LOBBY {order['lobby_name']}]",4)
                        self.send_error("failed to create lobby")
                    return
                if order["type"]=="join_lobby": ##JOINLOBBY
                    ok=False
                    ok=self.join_lobby(order["lobby_name"])
                    if ok:
                        log(f"[{self.name} JOINED LOBBY {order['lobby_name']}]",4)
                        self.send_ok()
                    else:
                        self.send_error("failed to join lobby")
                    return
                if order["type"]=="show_lobby": ##SHOW LOBBY
                    for lbb in LOBBIES:
                        log(lbb.name,1)
                        for pl in lbb.players:
                            print(pl.name)
                    return
                if order["type"]=="reset_map":
                    for lbb in LOBBIES:
                        if order["lobby_name"] == lbb.name:
                            lbb.reset_map()
                    log(f"[{self.name} MAP RESETED {order['lobby_name']}]",4)
                    self.send_ok()
                    print("map reseted")
                    return
                if order["type"]=="start_map":
                    try:
                        for lbb in LOBBIES:
                            if order["lobby_name"] == lbb.name:
                                lbb.start()
                        for lbb in LOBBIES:
                            if order["lobby_name"] == lbb.name:
                                for pl in lbb.players:
                                    pl.respond(str(order).replace("'",'"').replace("None",'null')+"\n")
                                    #print("FAILED TO START LOBBY")
                        log(f"[{self.name} MAP STARTED {order['lobby_name']}]",4)
                        self.send_ok()
                        print("map started")
                        return
                    except Exception as p:
                        print(p)
                if order["type"]=="list_lobbies":
                    try:
                        self.send_lobbies()
                    except:
                        print("FAILED LOBBY")
                    return
                if order["type"]=="leave_lobby":
                    for lbb in LOBBIES:
                        for pl in lbb.players:
                            if pl.name==self.name:
                                self.send_response_to_lobby('{"type":"player_left","nickname":'+self.name+'}')
                                lbb.players.remove(pl)
                                if len(lbb.players)==0:
                                    LOBBIES.remove(lbb)
                    
                    log(f"[{self.name} LEFT LOBBY {order['lobby_name']}]",4) ##TODO optimalize
                    self.send_ok()
                    return
                if order["type"]=="upgrade":
                    for lbb in LOBBIES:
                        if order["lobby_name"] == lbb.name:
                            pass
                    return
                if order["type"]=="disconnect":
                    for lbb in LOBBIES:
                        for pl in lbb.players:
                            if pl.name==self.name:
                                self.send_response_to_lobby('{"type":"player_left","nickname":'+self.name+'}')
                                lbb.players.remove(pl)
                                if len(lbb.players)==0:
                                    LOBBIES.remove(lbb)
                    print(f"{self.name} DISCONNECTED")
                    
                    #self.connected=False
                    #self.connection.close()
                    del self
                    
                    for lbb in LOBBIES:
                        print(lbb.players)
                    return


                else:
                    log(f"[RECIVED MESSAGE]",3)
                    self.send_error("no such endpoint"+'\n')
                    return
                

                
        except Exception as p:
                print("ERROR")
                print(self.msg)
                
                self.send_error(p)
                
    def run(self):
        global LOBBIES
        while self.connected:
            self.msg = self.connection.recv(4096)
            self.msg = self.msg.decode('utf-8')
            try:
                error=None
                order=json.loads(self.msg)
                self.handle_order(order)
            except Exception as p:
                try:
                    error=None
                    queue=[]
                    for ord in self.msg.split("}")[:-1]:
                        queue.append(ord+"}")
                    for ord in queue:
                        order=json.loads(ord)
                        self.handle_order(order)
                except Exception as p:
                    print(self.msg)
                    print(p)
                    self.send_error(p)
                    continue
                continue
    

        
    def respond(self,msg):
        try:
            self.connection.sendall(msg.encode('utf-8'))
        except BrokenPipeError: ##TODO
            pass
        except BrokenPipeError: ##TODO
            pass
    def send_response_to_lobby(self, msg):
        global LOBBIES
        for lbb in LOBBIES:
            if self in lbb.players:
                for receiver in lbb.players:
                    #log(f"[MESSAGE SENT TO {receiver.address}]",3)
                    if receiver.name.count("_obs")==0:
                        receiver.connection.sendall(msg.encode('utf-8'))

def send_map():
    while True:
        global LOBBIES
        for lbb in LOBBIES:
            if lbb.started==0:
                continue
            gold={}
            goldarr=[]
            for pl in lbb.players:
                goldarr.append({pl.name:pl.gold})
                gold.update({"playersgold":goldarr})
            for pl in lbb.players:
                temp=extract_dict(lbb.game)
                #print(temp)
                out=str(temp).replace("'",'"').replace("None",'null')
                pl.respond(out+"\n") ##TODO catch broken pipe
                #print(f"sent map to {pl.name} {out[:200]}")
        time.sleep(0.2)

def handle_client(conn,addr):
    global player_count
    player = Player(conn,addr)
    log(f"[PLAYER CONNECTED]",3)
    global LOBBIES
    try:
        player.run()
    except ConnectionResetError:
        name=player.name
        conn.close()
        player_count-=1
        for lbb in LOBBIES:
            if player in lbb.players:
                lbb.players.remove(player)
                if len(lbb.players)==0:
                    LOBBIES.remove(lbb)
        log(f"[PLAYER {name} DISCONNECTED]",5)
    except BrokenPipeError:
        name=player.name
        conn.close()
        player_count-=1
        for lbb in LOBBIES:
            if player in lbb.players:
                lbb.players.remove(player)
        log(f"[PLAYER {name} DISCONNECTED]",5)
def start():
    global player_count
    server.listen()
    thread = threading.Thread(target=send_map)
    thread.start()
    while True:
        conn,addr = server.accept()
        thread = threading.Thread(target=handle_client,args=(conn,addr))
        player_count+=1
        log(f"[ACTIVE PLAYERS:{player_count}]",3)
        thread.start()
        
player_count=0
log("STARTING...",0)
log("STARTED",0)
try:
    start()
except KeyboardInterrupt:
    log("SERVER KILLED",5)
    os._exit(os.EX_OK)


##TODO winning condition
##time for attack
##gold/units better generation
##delete empty lobbies
##check player name for repetition DONE
##select map custom json
