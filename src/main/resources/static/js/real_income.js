// 페이지 로드 시 API 키 가져오기
fetchApiKey().then(() => {
    console.log("API_KEY 준비 완료:");
});

// 팝업창 열기
function openIncomePopup() {
    const popup = document.getElementById('realIncomePopup');
    popup.style.display = 'block';
    document.body.classList.add('popup-active');
    resetIncomeSelections();
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
    // City와 District 선택 확인
    const citySelect = document.getElementById('incomeCity');
    const districtSelect = document.getElementById('incomeDistrict');
    const annualIncomeInput = document.getElementById('annualIncome'); // 연소득 입력 필드

    if (!citySelect.value || !districtSelect.value || !annualIncomeInput.value) {
        alert("모든 항목을 입력해주세요.");
        return false; // 폼 제출 방지
    }

    // Hidden Input 값 설정
    document.getElementById('hiddenCityCode').value = citySelect.value;
    document.getElementById('hiddenDistrictCode').value = districtSelect.value;
    document.getElementById('hiddenCityText').value = citySelect.options[citySelect.selectedIndex].text;
    document.getElementById('hiddenDistrictText').value = districtSelect.options[districtSelect.selectedIndex].text;
    document.getElementById('hiddenAnnualIncome').value = annualIncomeInput.value;

    // Submit the form
    document.getElementById('incomeForm').submit();
}

// Close popup
function closeIncomePopup() {
    const popup = document.getElementById('realIncomePopup');
    popup.style.display = 'none';
    document.body.classList.remove('popup-active');
}
