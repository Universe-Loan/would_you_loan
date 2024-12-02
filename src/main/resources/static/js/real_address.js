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
//         API_KEY = await response.text();
//         console.log('API 키를 성공적으로 가져왔습니다.');
//     } catch (error) {
//         console.error('API 키를 가져오는 중 오류 발생:', error);
//     }
// }
//
// // 페이지 로드 시 API 키 가져오기
// fetchApiKey();


// real_address.js에서 먼저 API 키를 가져옴
// 페이지 로드 시 API 키 가져오기
fetchApiKey().then(() => {
    console.log("API_KEY 준비 완료:");
});

// 팝업창 열기
function openInterestPopup() {
    const popup = document.getElementById('interestPropertyPopup');
    popup.style.display = 'block';

    // 팝업 중앙에 위치하도록 스타일 추가
    document.body.classList.add('popup-active');
    resetSelections();
}

// 팝업창 닫기
function closeInterestPopup() {
    const popup = document.getElementById('interestPropertyPopup');
    popup.style.display = 'none';

    // 팝업 활성화 상태 제거
    document.body.classList.remove('popup-active');
}

// 선택 초기화
function resetSelections() {
    document.getElementById('city').value = '';
    document.getElementById('district').innerHTML = '<option value="">선택하세요</option>';
    document.getElementById('neighborhood').innerHTML = '<option value="">선택하세요</option>';
    document.getElementById('apartment').innerHTML = '<option value="">선택하세요</option>';
}

// 구/군 목록 로드
function loadDistricts() {
    const citySelect = document.getElementById('city');
    const cityCode = citySelect.value;
    const cityName = citySelect.selectedOptions[0].text;
    if (!cityCode) return;

    const url = `http://apis.data.go.kr/1741000/StanReginCd/getStanReginCdList?serviceKey=${API_KEY}&pageNo=1&numOfRows=1000&type=json&locatadd_nm=${cityName}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const excludedCodes = ['41280', '41190', '41130', '41110', '41270', '41170', '41460'];

            const districts = data.StanReginCd[1].row.filter(item => {
                return item.region_cd.startsWith(cityCode) &&
                    item.sgg_cd !== '000' &&
                    item.umd_cd === '000' &&
                    item.ri_cd === '00' &&
                    !excludedCodes.includes(item.region_cd.slice(0, 5));
            });

            const districtSelect = document.getElementById('district');
            districtSelect.innerHTML = '<option value="">선택하세요</option>';

            districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.region_cd.slice(0, 5);

                let displayName = district.locallow_nm;
                const regionCode = district.region_cd.slice(0, 5);

                if (regionCode.startsWith('41') && regionCode !== '41111') {
                    const cityDistrict = data.StanReginCd[1].row.find(d =>
                        d.region_cd.startsWith(regionCode.slice(0, 4)) &&
                        d.sgg_cd !== '000' &&
                        d.umd_cd === '000' &&
                        d.ri_cd === '00'
                    );
                    if (cityDistrict && cityDistrict.locallow_nm !== displayName) {
                        displayName = `${cityDistrict.locallow_nm} ${displayName}`;
                    }
                }

                option.textContent = displayName;
                districtSelect.appendChild(option);
            });

        })
        .catch(error => console.error('Error:', error));
}

// 동 목록 로드
function loadNeighborhoods() {
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const cityName = citySelect.selectedOptions[0].text;
    const districtName = districtSelect.selectedOptions[0].text;
    const districtCode = districtSelect.value;

    if (!districtCode) return;

    const url = `http://apis.data.go.kr/1741000/StanReginCd/getStanReginCdList?serviceKey=${API_KEY}&pageNo=1&numOfRows=1000&type=json&locatadd_nm=${cityName} ${districtName}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const neighborhoods = data.StanReginCd[1].row.filter(item => item.umd_cd !== '000');
            const neighborhoodSelect = document.getElementById('neighborhood');
            neighborhoodSelect.innerHTML = '<option value="">선택하세요</option>';
            neighborhoods.forEach(neighborhood => {
                const option = document.createElement('option');
                option.value = neighborhood.region_cd;
                option.textContent = neighborhood.locallow_nm;
                neighborhoodSelect.appendChild(option);
            });

        })
        .catch(error => console.error('Error:', error));
}

// 아파트 단지 목록 로드
function loadApartments() {
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const neighborhoodSelect = document.getElementById('neighborhood');

    const cityCode = citySelect.value;
    const districtCode = districtSelect.value.slice(2);
    const neighborhoodCode = neighborhoodSelect.value.slice(5);

    const bjdCode = cityCode + districtCode + neighborhoodCode;

    const url = `http://apis.data.go.kr/1613000/AptListService2/getLegaldongAptList?serviceKey=${API_KEY}&pageNo=1&numOfRows=100&bjdCode=${bjdCode}`;

    fetch(url)
        .then(response => response.text())
        .then(str => (new window.DOMParser()).parseFromString(str, "text/xml"))
        .then(data => {
            const items = data.getElementsByTagName('item');
            const apartmentSelect = document.getElementById('apartment');
            apartmentSelect.innerHTML = '<option value="">선택하세요</option>';

            for (let item of items) {
                const option = document.createElement('option');
                option.value = item.getElementsByTagName('kaptCode')[0].textContent;
                option.textContent = item.getElementsByTagName('kaptName')[0].textContent;
                apartmentSelect.appendChild(option);
            }
        })
        .catch(error => console.error('Error:', error));
}

// 선택된 주소 입력
function submitInterestProperty() {
    const userId = globalUserId;
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const neighborhoodSelect = document.getElementById('neighborhood');
    const apartmentSelect = document.getElementById('apartment');

    if (!citySelect.value || !districtSelect.value || !neighborhoodSelect.value || !apartmentSelect.value) {
        alert('모든 항목을 선택해주세요.');
        console.log()
        return;
    }

    const city = citySelect.selectedOptions[0].text;
    const district = districtSelect.selectedOptions[0].text;
    const neighborhood = neighborhoodSelect.selectedOptions[0].text;
    const apartment = apartmentSelect.selectedOptions[0].text;

    const lawdCode = neighborhoodSelect.value;
    const kaptCode = apartmentSelect.value;

    const fullAddress = `${city} ${district} ${neighborhood} ${apartment}`;

    // Check where this function is called
    const isAptFindPage = document.body.classList.contains('apt-find');
    const isLoanPersonalInfoPage = document.body.classList.contains('loan-personal-info');

    if (isAptFindPage) {
        // Redirect to apartment report page
        if (!city || !district || !neighborhood || !apartment || !lawdCode) {
            alert('아파트 정보를 먼저 선택해주세요.');
            return;
        }

        const params = new URLSearchParams({
            city: city,
            district: district,
            neighborhood: neighborhood,
            apartment: apartment,
            lawdCode: lawdCode,
            kaptCode: kaptCode
        });

        const go_url = `/apt-report?${params.toString()}`;
        saveSearchRecord(apartment, go_url, userId, city, district)
        window.location.href = go_url;
    } else if (isLoanPersonalInfoPage) {
        // Update the address field and close the popup
        const addressButton = document.querySelector('button.form-control');
        if (addressButton) {
            addressButton.textContent = fullAddress;
        }

        closeInterestPopup();
    } else {
        alert('적합한 페이지에서만 동작합니다.');
    }
}

// 검색 기록 저장 함수 추가
function saveSearchRecord(apartName, go_url, userId, city, district) {
    fetch('/search-apt-record/save-search', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'userId': userId,
            'city': city,
            'district': district,
            'apartName': apartName,
            'url': go_url
    })
    })
        .then(response => {
            if (!response.ok) {
                console.error('Failed to save search record');
            }
        })
        .catch(error => console.error('Error saving search record:', error));
}
