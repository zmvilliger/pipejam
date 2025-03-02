import json
import cv2
import time
import socket
from ultralytics import YOLO

# Initialize YOLO model - This must point to the path of the model weights for jam detection
model = YOLO(r"C:\Users\villi\vscode-projects\yoloproj\runs\obb\train12\weights\best.pt")

host = 'localhost'
port = 7474
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    

#connect to java server that will process collisions, and allow other functionality
def connectSocket(): 
    
    sock.connect(('localhost', 7474))
    print("connected to java server")

def sendResult(results):

    boxes = []

    for result in results:
        time.sleep(.2)
        if result.obb:

            for result_box in result.obb.xyxyxyxy:
                coords = result.obb.xyxyxyxy.tolist()
                boxes.append(coords)

            json_boxes = json.dumps(boxes[-1])  
                    
            sock.sendall((json_boxes + '\n').encode('utf-8'))  

            boxes.clear()  

            print("======== Data sent to java server ==========")



def run_inference():

    connectSocket()

    cap = cv2.VideoCapture(0)

    if not cap.isOpened():
        print("Error: Could not open webcam.")
        exit()

    while True:
        ret, frame = cap.read()
        
        if not ret:
            print("Error: Could not read frame.")
            break

        try:
            results = model.predict(source=0, conf = .6, imgsz = 1280, device=0, show = True, stream=True) 
            sendResult(results)

        except Exception as e:
            print(f"Error: {e}")


    cap.release()
    cv2.destroyAllWindows()        

run_inference()


