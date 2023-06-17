import json
import threading
import time
import random
from utils import *

class Map:
    def __init__(self,filename):
        self.type='map'
        self.temp=random.randint(1,100000)
        f=open(filename,'r')
        x=json.loads(f.read())
        self.provinces=[]
        self.running=1
        self.tickrate=60
        for a in x["provinces"]:
            self.provinces.append(Province(a))
        f.close()
        while False:
            st=time.time()
            self.updateMap()
            #print(time.time()-st)
            time.sleep(1/self.tickrate)
            #self.provincesStats()
        
     
    def start_game(self):
        self.running=0
    def reset(self,filename):
        self.type='map'
        self.temp=random.randint(1,100000)
        f=open(filename,'r')
        x=json.loads(f.read())
        self.provinces=[]
        for a in x["provinces"]:
            self.provinces.append(Province(a))
        f.close()
        global mapa
        mapa=self
        self.tickrate=60
    def provincesStats(self):
        t=random.randint(0,4)
        for province in self.provinces:
            log(f"id={province.id} income={province.income} army={province.army} owner={round(province.progress,3)}",t)
    def updateMap(self):
        if self.running == 1:
            for prov in self.provinces:
                prov.update()
    def attack(self,src,dst):
        
        for prov in self.provinces:
            if prov.id==src:
                src=prov
            if prov.id==dst:
                dst=prov
        amount=src.army
        src.army=0
        if dst.owner==src.owner:
            dst.army+=amount
        else:
            if dst.army-amount>=0:
                dst.army-=amount
            else:
                dst.army=abs(dst.army-amount)
                dst.owner=src.owner

class Province:
    def __init__(self,data):
        
        self.id=data["id"]
        self.owner = "unowned"
        #if self.id in [1,2]:
        #    self.owner = 1
        #if self.id in [10,11]:
        #    self.owner = 2
        #if self.id in [15,16]:
        #    self.owner = 3
        #if self.id in [6,7]:
        #    self.owner = 4
        self.income=data["income"]
        self.manpower=data["manpower"] ##TODO change to capacity
        self.production = 0
        self.blob = data["blobCoords"]
        self.progress=0
        self.army=10
    def update(self):
        if self.owner==1:
            self.progress+=(random.random()*random.randint(3,10))/100
        else:
            self.progress+=(random.random()*random.randint(1,5))/100
        if(self.progress>=1):
            #self.army=self.army**2

            self.progress=0
            if self.owner=="unowned":
                if self.army<10:
                    self.army+=1
            else:
                self.army+=1
    
#x=Map("map_1.json")
#x.provincesStats()


mapa = None
def extract_dict(obj):
    if isinstance(obj, list):
        return [extract_dict(item) for item in obj]
    elif isinstance(obj, dict):
        return {key: extract_dict(value) for key, value in obj.items()}
    elif hasattr(obj, "__dict__"):
        return extract_dict(obj.__dict__)
    else:
        return obj
def create_map(filename):
    map_instance = Map(filename)
    return map_instance

def create_map_thread(filename):
    thread = threading.Thread(target=create_map, args=(filename,))
    thread.start()
    return thread
def get_map():
    global mapa
    return mapa

    #time.sleep()

    
    
    
        
    
#create_map_thread("map_1.json")
