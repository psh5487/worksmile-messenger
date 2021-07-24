<h2>Entity 기반 DB 생성시 주의할 점</h2>

- src/main/resource/application.yml 에서 datasource를 Local 환경에 맞게 재설정
- 첫 시작에는 jpa.hibernate.ddl-auto: create로 설정 변경

<br/>
<h2>양방향 연관관계 맵핑 테스트</h2>
<h4> 주의사항</h4>

- Entity Class내 인스턴스 변수 간의 양방향 연관관계가 맺어져 있기 때문에 Entity Class로 반환하지 말 것 <br/>
- 반드시 Response하려는 Dto Class를 만들어서 변환시킨 뒤에 반환 <br/>
- Entity Class 내 인스턴수 변수끼리 연관관계의 Cycle이 형성되어 있으므로 Entity 타입으로 반환하면 무한루프로 인한 overflow 발생 <br/>
- Dto Class의 Builder 패턴을 통해 변환 <br/>

<br/>
<h2>jpa.hibernate.ddl-auto 옵션 상세</h2> 
<h4> create </h4>
  : 기존 table 존재하는 경우, table drop후 새로 create
  : DB가 없는 상황에서 설정

<h4> create-drop </h4> 
  : 앱 실행시 table create, 앱 종료시 table drop

<h4> update </h4>  
  : Entity 정보가 추가됐다면 table에도 추가

<h4> validate </h4>    
  : Entity 정보와 Table이 다른 경우, 에러 발생 후 애플리케이션 종료

<h4> none </h4>    
  : No Action