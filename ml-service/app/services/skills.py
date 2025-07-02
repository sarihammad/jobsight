from transformers import AutoTokenizer, AutoModelForTokenClassification, pipeline
from transformers.pipelines.token_classification import AggregationStrategy

model_name = "algiraldohe/lm-ner-linkedin-skills-recognition"
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForTokenClassification.from_pretrained(model_name)
skill_ner = pipeline("ner", model=model, tokenizer=tokenizer, aggregation_strategy=AggregationStrategy.SIMPLE)

def extract_skills(text: str) -> set[str]:
    results = skill_ner(text)
    print("Results:", results)
    return {
        ent["word"].lower()
        for ent in results
        if ent["entity_group"] in ("TECHNOLOGY", "SOFT", "HARD")
    }