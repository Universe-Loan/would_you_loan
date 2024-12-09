// // API 키를 저장할 변수
// let API_KEY = '';
//
// // 백엔드에서 API 키를 가져오는 함수
// async function fetchApiKey() {
//     try {
//         const response = await fetch('/api/key');
//         if (!response.ok) {
//             throw new Error('API 키를 가져오는데 실패했습니다.');
//         }
//
//         // response.text()의 결과를 기다린 후 trim 호출
//         const apiKeyText = await response.text(); // Promise 해결
//         API_KEY = apiKeyText.trim(); // 문자열로 변환 후 공백 제거
//         console.log('API 키를 성공적으로 가져왔습니다:');
//     } catch (error) {
//         console.error('API 키를 가져오는 중 오류 발생:', error);
//     }
// }
//
//
// fetch('/api/key')
//     .then(async response => {
//         if (!response.ok) {
//             console.error('서버 응답 오류:', await response.text());
//             throw new Error('서버 응답이 실패했습니다.');
//         }
//         return response.text();
//     })
//     .then(apiKey => {
//         API_KEY = apiKey.trim();
//         console.log('API 키:');
//     })
//     .catch(error => {
//         console.error('API 키를 가져오는 중 오류:', error);
//     });


// real_address.js에서 먼저 API 키를 가져옴
// // 페이지 로드 시 API 키 가져오기
// fetchApiKey().then(() => {
//     console.log("API_KEY 준비 완료:");c
// });

// 팝업창 열기
function openIncomePopup() {
    console.log('openIncomePopup 함수 실행');
    const popup = document.getElementById('realIncomePopup');
    if (popup) {
        console.log('팝업 요소 찾음:', popup);
        popup.style.display = 'block'; // 팝업 보이기
        document.body.classList.add('popup-active');
        resetIncomeSelections();
    } else {
        console.error('realIncomePopup 요소를 찾을 수 없음');
    }
}

// 팝업창 닫기
function closeIncomePopup() {
    const popup = document.getElementById('realIncomePopup');
    popup.style.display = 'none';
    document.body.classList.remove('popup-active');
}

// 선택 초기화
function resetIncomeSelections() {
    document.getElementById('incomeCity').value = '';
    document.getElementById('incomeDistrict').innerHTML = '<option value="">선택하세요</option>';
}

// 구/군 목록 로드
function loadIncomeDistricts() {
    const citySelect = document.getElementById('incomeCity');
    const cityCode = citySelect.value;
    const cityName = citySelect.selectedOptions[0]?.text;
    if (!cityCode) return;

    const url = `http://apis.data.go.kr/1741000/StanReginCd/getStanReginCdList?serviceKey=${API_KEY}&pageNo=1&numOfRows=1000&type=json&locatadd_nm=${cityName}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const districtSelect = document.getElementById('incomeDistrict');
            districtSelect.innerHTML = '<option value="">선택하세요</option>';

            const districts = data.StanReginCd[1].row.filter(item => {
                return item.region_cd.startsWith(cityCode) &&
                    item.sgg_cd !== '000' &&
                    item.umd_cd === '000' &&
                    item.ri_cd === '00';
            });

            // 오름차순 정렬
            districts.sort((a, b) => a.locallow_nm.localeCompare(b.locallow_nm));

            districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.region_cd.slice(0, 5);
                option.textContent = district.locallow_nm;
                districtSelect.appendChild(option);
            });
        })
        .catch(error => console.error('구/군 목록 로드 중 오류 발생:', error));
}

function submitIncome() {
    // 선택값 가져오기
    const citySelect = document.getElementById('incomeCity');
    const districtSelect = document.getElementById('incomeDistrict');

    // 선택값 유효성 검사
    if (!citySelect.value || !districtSelect.value) {
        alert("모든 항목을 입력해주세요."); // 사용자 피드백
        return false; // 제출 방지
    }

    // 폼 데이터 생성
    const formData = new FormData();
    formData.append('cityCode', citySelect.value);
    formData.append('districtCode', districtSelect.value);
    formData.append('cityText', citySelect.options[citySelect.selectedIndex].text);
    formData.append('districtText', districtSelect.options[districtSelect.selectedIndex].text);

    // 서버로 데이터 전송
    fetch('/search-income', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(errorData.error || '처리 중 오류가 발생했습니다.');
                });
            }
            return response.json(); // JSON 형태의 응답 처리
        })
        .then(data => {
            // 리다이렉트 처리
            if (data.redirectUrl) {
                window.location.href = data.redirectUrl;
            } else {
                alert('리다이렉트 URL이 없습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });

    return false; // 기본 제출 동작 방지
}


