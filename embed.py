import json
import sys
import logging

from sentence_transformers import SentenceTransformer


def main():
    logger = logging.getLogger(__name__)
    logging.basicConfig(filename='example.log', encoding='utf-8', level=logging.INFO)
    
    logger.info("Starting server.")
    model = SentenceTransformer("Snowflake/snowflake-arctic-embed-m")
    # print("Model loaded.")
    # sys.stdout.flush()


    for line in sys.stdin:
        input_string = line.strip()
        if input_string.lower() == "exit":
            print("Shutting down.")
            sys.stdout.flush()
            break

        embedding = model.encode(input_string).tolist()
        print(json.dumps(embedding))
        sys.stdout.flush()

if __name__ == "__main__":
    main()
