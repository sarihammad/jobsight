FROM python:3.13-alpine

WORKDIR /app

COPY ml-service/requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY ml-service .

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "5001"]