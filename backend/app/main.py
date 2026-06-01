from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from . import crud, models, schemas
from .database import SessionLocal, engine, get_db

import time
from sqlalchemy.exc import OperationalError

app = FastAPI(title="Code Slasher API")

# Reintentar conexión a la DB hasta que esté lista
def init_db():
    retries = 5
    while retries > 0:
        try:
            models.Base.metadata.create_all(bind=engine)
            print("Database initialized successfully!")
            break
        except OperationalError:
            retries -= 1
            print(f"Database not ready. Retrying in 5 seconds... ({retries} retries left)")
            time.sleep(5)
    if retries == 0:
        print("Could not connect to the database. Starting API anyway, but DB operations might fail.")

init_db()

@app.get("/")
def read_root():
    return {"message": "Welcome to Code Slasher API"}

@app.post("/scores/", response_model=schemas.ScoreResponse)
def create_score(score: schemas.ScoreCreate, db: Session = Depends(get_db)):
    return crud.create_score(db=db, score=score)

@app.get("/rankings/", response_model=List[schemas.RankingEntry])
def read_rankings(limit: int = 10, db: Session = Depends(get_db)):
    return crud.get_rankings(db, limit=limit)
