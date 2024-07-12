document.addEventListener('DOMContentLoaded', function() {
    const serviceMessage = document.getElementById('service-message');
    const serviceItems = document.querySelectorAll('.service-select-box > div');

    serviceItems.forEach(item => {
        item.addEventListener('click', function() {
            const serviceName = this.querySelector('.service-name').textContent;

            // 텍스트 변경 및 애니메이션 적용
            serviceMessage.textContent = serviceName;
            serviceMessage.classList.add('selected');

            // 서비스 선택 박스 숨기기
            document.querySelector('.service-description').style.display = '';
            document.querySelector('.service-price').style.display = '';
            document.querySelector('.search-box').style.display = '';
            document.querySelector('.search-box-spacer').style.display = 'none';
        });
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const editableDiv = document.getElementById('search-box');

    // Enter 키 처리
    editableDiv.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            // 여기에 메시지 전송 로직 추가
            console.log('Message sent:', this.innerText);
            this.innerText = ''; // 입력 필드 비우기
        }
    });
});