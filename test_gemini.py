from google import genai
import os

client = genai.Client(api_key=os.environ.get("GEMINI_API_KEY", ""))

# 사용 가능한 모델 리스트 확인
print("사용 가능한 모델:")
for model in client.models.list():
    print(f"  - {model.name}")


