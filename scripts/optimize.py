from google import genai
import os
import sys

client = genai.Client(api_key=os.environ.get("GEMINI_API_KEY", ""))

def optimize(file_path):
    print("⚡ 최적화 분석 중...")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        code = f.read()
    
    response = client.models.generate_content(
        model='models/gemini-2.5-flash',
        contents=f"""
    다음 알고리즘 코드를 최적화해줘:
```python
    {code}
```
    
    다음 내용을 포함해서:
    1. 시간복잡도 개선 방법
    2. 공간복잡도 개선 방법
    3. 가독성 개선
    4. 최종 최적화된 코드
    5. 왜 이 방법이 최선인지 설명
    
    구체적인 코드 변경 예시 포함.
    """)
    
    result = response.text
    
    full_result = f"""# 최적화 분석

{result}
"""
    
    print(full_result)
    
    # 입력 파일과 같은 디렉토리에 저장
    import os
    file_dir = os.path.dirname(file_path) if os.path.dirname(file_path) else "."
    output_path = os.path.join(file_dir, "_optimized.md")
    
    with open(output_path, "w", encoding="utf-8") as f:
        f.write(full_result)
    
    print(f"\n✅ {output_path} 저장 완료")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        optimize(sys.argv[1])
    else:
        file = input("코드 파일 경로: ")
        optimize(file)
