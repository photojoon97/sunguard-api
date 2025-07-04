// DOM이 완전히 로드된 후에 스크립트를 실행합니다.
document.addEventListener('DOMContentLoaded', function() {

    // 필요한 DOM 요소들을 미리 찾아둡니다.
    const departureInput = document.getElementById('departure');
    const departureIdInput = document.getElementById('departureId');
    const destinationInput = document.getElementById('destination');
    const destinationIdInput = document.getElementById('destinationId');
    const searchForm = document.getElementById('search-form');
    const bstopnmInput = document.getElementById('bstopnm-input');
    const searchResultContainer = document.querySelector('.list-group'); // 검색 결과 리스트
    const stopDetailsContainer = document.getElementById('stop-details'); // 정류장 상세 정보 컨테이너

    /**
     * 출발지를 설정하는 함수
     * @param {string} name - 정류장 이름
     * @param {string} id - 정류장 ID
     */
    function setDeparture(name, id) {
        departureInput.value = name;
        departureIdInput.value = id;
        alert(name + ' 정류장이 출발지로 설정되었습니다.');
    }

    /**
     * 목적지를 설정하는 함수
     * @param {string} name - 정류장 이름
     * @param {string} id - 정류장 ID
     */
    function setDestination(name, id) {
        destinationInput.value = name;
        destinationIdInput.value = id;
        alert(name + ' 정류장이 목적지로 설정되었습니다.');
    }

    /**
     * 현재 설정된 출발지/목적지 정보를 URL 파라미터에 추가하는 함수
     * @param {URLSearchParams} params - URL 파라미터 객체
     */
    function appendCurrentRouteState(params) {
        if (departureIdInput.value && departureInput.value) {
            params.set('departureId', departureIdInput.value);
            params.set('departureName', departureInput.value);
        }
        if (destinationIdInput.value && destinationInput.value) {
            params.set('destinationId', destinationIdInput.value);
            params.set('destinationName', destinationInput.value);
        }
    }

    // 정류장 검색 폼 제출 이벤트 처리
    if (searchForm) {
        searchForm.addEventListener('submit', (e) => {
            e.preventDefault(); // 폼의 기본 제출 동작을 막음
            const bstopnm = bstopnmInput.value;
            if (!bstopnm) {
                alert('정류장 이름을 입력해주세요.');
                return;
            }
            const params = new URLSearchParams({ bstopnm });
            appendCurrentRouteState(params);
            window.location.href = `/?${params.toString()}`;
        });
    }

    // "정보" 링크 클릭 이벤트 처리 (이벤트 위임)
    if (searchResultContainer) {
        searchResultContainer.addEventListener('click', (e) => {
            // 클릭된 요소가 'details-link' 클래스를 가진 a 태그인지 확인
            if (e.target && e.target.classList.contains('details-link')) {
                e.preventDefault(); // 링크의 기본 동작을 막음
                const link = e.target;
                const url = new URL(link.href);
                const params = url.searchParams;
                appendCurrentRouteState(params);
                window.location.href = `/?${params.toString()}`;
            }
        });
    }

    // "출발지로 설정", "목적지로 설정" 버튼 이벤트 처리 (이벤트 위임)
    if (stopDetailsContainer) {
        stopDetailsContainer.addEventListener('click', (e) => {
            const button = e.target;
            const stopName = button.dataset.stopName;
            const stopId = button.dataset.stopId;

            if (button.id === 'set-departure-btn') {
                setDeparture(stopName, stopId);
            } else if (button.id === 'set-destination-btn') {
                setDestination(stopName, stopId);
            }
        });
    }
});