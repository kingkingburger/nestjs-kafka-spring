import http from 'k6/http';
import { sleep, check } from 'k6';

// 테스트 옵션 설정
export const options = {
  // vus: 가상 유저 수
  // 1명의 유저가 100번 반복하도록 설정
  vus: 1,

  // iterations: default 함수를 총 몇 번 실행할지 설정
  iterations: 100,
};

// 가상 유저가 실행할 메인 함수
export default function () {
  // 제공해주신 NestJS 컨트롤러에 따라 실제 URL 경로를 작성합니다.
  const url = 'http://192.168.0.192:3000/alarm/send';

  // GET 요청을 보냅니다.
  const res = http.get(url);

  console.log('res = ' , res);
  // (선택 사항) 응답이 성공적(status code 200)인지 확인합니다.
  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  // 각 요청 사이에 1초간 대기합니다. (서버에 과도한 부하를 주지 않기 위함)
  // 더 빠르게 보내고 싶다면 이 값을 줄이거나 sleep 함수를 제거할 수 있습니다.
  // sleep(0.1);
}