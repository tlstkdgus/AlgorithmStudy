from google import genai
import os
import sys

client = genai.Client(api_key=os.environ.get("GEMINI_API_KEY", ""))

def review(file_path):
    print("🔬 코드 리뷰 중...")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        code = f.read()
    
    response = client.models.generate_content(
        model='models/gemini-2.5-flash',
        contents=f"""
    내 알고리즘 풀이를 리뷰해줘:
```python
    {code}
```
    
    다음을 분석해줘:
    1. 시간/공간 복잡도
    2. 논리 오류 있는지
    3. 더 효율적인 방법
    4. 놓친 엣지 케이스
    5. 코드 개선점
    
    비판적이되 건설적으로!
    """)
    
    result = response.text
    print(result)
    
    # 입력 파일과 같은 디렉토리에 저장
    import os
    file_dir = os.path.dirname(file_path) if os.path.dirname(file_path) else "."
    output_path = os.path.join(file_dir, "_review.md")
    
    with open(output_path, "w", encoding="utf-8") as f:
        f.write(f"# 코드 리뷰\n\n**파일**: {file_path}\n\n{result}")
    
    print(f"\n✅ {output_path} 저장 완료")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        review(sys.argv[1])
    else:
        file = input("코드 파일 경로: ")
        review(file)