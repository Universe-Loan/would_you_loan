// API 키를 저장할 변수
let API_KEY = '';

// 백엔드에서 API 키를 가져오는 함수
async function fetchApiKey() {
    try {
        const response = await fetch('/api/key');
        if (!response.ok) {
            throw new Error('API 키를 가져오는데 실패했습니다.');
        }
        API_KEY = await response.text();
        console.log('API 키를 성공적으로 가져왔습니다.');
    } catch (error) {
        console.error('API 키를 가져오는 중 오류 발생:', error);
    }
}

// 페이지 로드 시 API 키 가져오기
fetchApiKey();
// 팝업창 열기
function openInterestPopup() {
    document.getElementById('interestPropertyPopup').style.display = 'block';
    resetSelections();
}

// 팝업창 닫기
function closeInterestPopup() {
    document.getElementById('interestPropertyPopup').style.display = 'none';
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
            const districts = data.StanReginCd[1].row.filter(item => {
                return item.region_cd.startsWith(cityCode) &&
                    item.sgg_cd !== '000' &&
                    item.umd_cd === '000' &&
                    item.ri_cd === '00';
            });

            const districtSelect = document.getElementById('district');
            districtSelect.innerHTML = '<option value="">선택하세요</option>';
            districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.region_cd.slice(0, 5);
                option.textContent = district.locallow_nm;
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
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const neighborhoodSelect = document.getElementById('neighborhood');
    const apartmentSelect = document.getElementById('apartment');

    if (!citySelect.value || !districtSelect.value || !neighborhoodSelect.value || !apartmentSelect.value) {
        alert('모든 항목을 선택해주세요.');
        return;
    }

    const city = citySelect.selectedOptions[0].text;
    const district = districtSelect.selectedOptions[0].text;
    const neighborhood = neighborhoodSelect.selectedOptions[0].text;
    const apartment = apartmentSelect.selectedOptions[0].text;
    const apartmentCode = apartmentSelect.value;

    const fullAddress = `${city} ${district} ${neighborhood} ${apartment}`;

    // 주소 입력 필드에 선택된 주소 설정
    document.querySelector('button.form-control').textContent = fullAddress;

    // 여기에 apartmentCode를 저장하는 로직을 추가하세요
    console.log('Selected Apartment Code:', apartmentCode);

    // 팝업 창 닫기
    closeInterestPopup();
}


// 사용자 검색기록 저장 및 다음 페이지로 넘어가기
function formatNumber(input) {
    // 숫자 이외의 문자 제거
    let value = input.value.replace(/[^\d]/g, '');

    // 천 단위 쉼표 추가
    value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    // 포맷된 값을 입력 필드에 설정
    input.value = value;
}

function submitLoanApplication() {
    // 모든 필수 입력 필드 확인
    const address = document.querySelector('button.form-control').textContent;
    const loanAmount = document.getElementById('loanAmount').value;
    const loanTerm = document.getElementById('loanTerm').value;
    const loanPurpose = document.querySelector('input[name="loanPurpose"]:checked')?.value;
    const firstTimeBuyer = document.querySelector('input[name="firstTimeBuyer"]:checked')?.value;
    const houseOwnership = document.querySelector('input[name="houseOwnership"]:checked')?.value;
    const occupation = document.querySelector('input[name="occupation"]:checked')?.value;
    const annualIncome = document.getElementById('annualIncome').value;
    const employmentDate = document.getElementById('employmentDate').value;

    // 모든 필드가 입력되었는지 확인
    if (
        !address ||
        address === '주소 검색하기' ||
        !loanAmount ||
        !loanTerm ||
        !loanPurpose ||
        !firstTimeBuyer ||
        !houseOwnership ||
        !occupation ||
        !annualIncome ||
        !employmentDate
    ) {
        alert('모든 항목을 입력해주세요.');
        return;
    }

    // 입력된 데이터를 객체로 만듦
    const loanApplicationData = {
        address,
        loanAmount,
        loanTerm,
        loanPurpose,
        firstTimeBuyer,
        houseOwnership,
        occupation,
        annualIncome,
        employmentDate
    };

    // 데이터를 서버에 저장하거나
    // 데이터를 로컬 스토리지에 저장
    localStorage.setItem('loanApplicationData', JSON.stringify(loanApplicationData));

    // 다음 페이지로 이동
    window.location.href = '/loan-list'; // 실제 다음 페이지 URL로 변경해야 합니다.
}