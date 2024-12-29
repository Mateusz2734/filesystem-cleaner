import os
import signal
import threading
import time

from fastapi import FastAPI
from fastapi.responses import Response
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer

model_path = "./models/snowflake-arctic-embed-m"
model_name = "Snowflake/snowflake-arctic-embed-m"


class TextRequest(BaseModel):
    text: str


if not os.path.exists(model_path):
    os.makedirs(model_path)

    model = SentenceTransformer(model_name)
    model.save(model_path)
else:
    model = SentenceTransformer(model_path)

app = FastAPI()


@app.post("/")
def embed_text(request: TextRequest):
    embeddings = model.encode(request.text)
    return embeddings.tolist()


@app.get("/ping")
def ping():
    return Response(status_code=200)


@app.get("/shutdown")
def shutdown():
    def delayed_shutdown():
        time.sleep(0.1)
        os.kill(os.getpid(), signal.SIGTERM)

    threading.Thread(target=delayed_shutdown).start()
    return Response(status_code=200)
