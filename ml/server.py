import os
import sys
from flask import Flask, render_template, request, abort
from werkzeug.utils import secure_filename
from flask_cors import CORS
import fun
import requests

ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg'}

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = os.getcwd()
CORS(app)

@app.route('/uploader', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        f = request.json
        # print (f['url'], file=sys.stderr)
        response = requests.get(f['url'])
        # print(response.headers['content-type'], file= sys.stderr)
        s=response.headers['content-type']
        rex=s.split('/')[1]
        file = open("sample_image."+'jpg', "wb")
        file.write(response.content)
        file.close()
        filename="sample_image."+'jpg'
        print(filename, file = sys.stderr)
        # print(fun.predict(filename), file = sys.stderr)
        rv = fun.predict(filename)
        # userData = faces_image.fun(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        os.remove(filename)
        return rv

if __name__ == '__main__':
    app.run(debug=True)
