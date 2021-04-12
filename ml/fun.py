from sklearn import neighbors
import os
import os.path
import pickle
import face_recognition as fr
from sklearn.neighbors import KNeighborsClassifier
import cv2
# from imutils import paths   

def train(train_dir,model_save_path='trained_knn_model.clf',n_neighbors=3,knn_algo='ball_tree'):

    x=[]
    y=[]

    for class_dir in os.listdir(train_dir):
        if not os.path.isdir(os.path.join(train_dir,class_dir)):
            continue

        folderPath = os.path.join(train_dir,class_dir)
        for image_name in os.listdir(folderPath):
            image_path = os.path.join(folderPath,image_name)
            image = cv2.imread(image_path)
            rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            boxes = fr.face_locations(rgb,model="hog")

            encodings = fr.face_encodings(rgb, boxes)
            # loop over the encodings
            for encoding in encodings:
                x.append(encoding)
                y.append(class_dir)

    print(x,y)
    knn_clf=KNeighborsClassifier(n_neighbors=n_neighbors)
    knn_clf.fit(x,y)
 
    if model_save_path is not None:
        with open(model_save_path,'wb') as f:
            pickle.dump(knn_clf,f)
    return knn_clf


def predict(x_img_path,knn_clf=None,model_path='trained_knn_model.clf',distance_threshold=0.35):

    if knn_clf is None:
        with open(model_path,'rb') as f: 
            knn_clf=pickle.load(f)
    
    x_img = fr.load_image_file(x_img_path)

    x_face_location=fr.face_locations(x_img)

    encodings=fr.face_encodings(x_img)

    closest_distace=knn_clf.kneighbors(encodings,n_neighbors=3)
    print(closest_distace)
 
    are_matches=[closest_distace[0][i][0]<=distance_threshold for i in range(len(x_face_location))] #anonymous function, are_matches
    
    if are_matches[0] == False:
        return {"found" : False, "id" : 13}

    return {"found" : True, "id" : knn_clf.predict(encodings)[0]}
    print(are_matches)

    # print(knn_clf.predict(encodings))
    # print(list(x_face_location))
    # print(list(are_matches))
    # print(list(zip(knn_clf.predict(encodings),x_face_location,are_matches)))
    # return are_matches



if __name__ == '__main__':
    train(os.getcwd() + '\MLapp\dataset')
    # predict("8.jpg")


