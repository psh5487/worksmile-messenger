// 이벤트 등록
document.querySelector('#connect').addEventListener('click', connect);
document.querySelector('#disconnect').addEventListener('click', disconnect);
document.querySelector('#subscribe').addEventListener('click', subscribeRoom);
document.querySelector('#unsubscribe').addEventListener('click', unsubscribeRoom);
document.querySelector('#sendMessage').addEventListener('click', sendTalkMessage);

let ruuid = '';
let sender = '';
let uname = '';
let cname = 'Smilegate Stove';
let pname = '인턴';
const accessToken = 'xyzxyz';
let stompClient = null;
let curChatRoom = null;
let chatRoomListRealTime = null;
setSubscribeRoom(false);

function connect () {
  sender = document.getElementById('sender').value;
  uname = sender + '본명';

  // const socket = new SockJS('http://localhost:8080/ws'); // local
  // const socket = new SockJS('http://52.198.41.19:8080/ws', { transports: ['websocket'] }); // dev
  const socket = new SockJS('http://35.72.40.112:80/ws', { transports: ['websocket'] }); // product
  console.log(socket);
  stompClient = Stomp.over(socket);

  // CONNECT
  stompClient.connect({ 'X-Auth-Token': accessToken, 'uid': sender }, function (data) {
    setConnected(true);
    console.log('Connected: ' + data);

    // SUBSCRIBE
    chatRoomListRealTime = stompClient.subscribe('/sub/msg/user/' + sender, function (data) {
      showChatRoomListRealTime(JSON.parse(data.body));
    }, { id: sender })
  });
}

function disconnect () {
  if (stompClient != null) {
    // UNSUBSCRIBE
    chatRoomListRealTime.unsubscribe();

    // DISCONNECT
    stompClient.disconnect(function () {}, { uid: sender });
  }
  setConnected(false);
  console.log('Disconnected');
}

function subscribeRoom () {
  ruuid = document.getElementById('roomId').value;
  setSubscribeRoom(true);

  if (stompClient != null) {
    // SUBSCRIBE
    curChatRoom = stompClient.subscribe('/sub/msg/room/' + ruuid, function (data) {
      showMessageOutput(JSON.parse(data.body));
    }, { id: sender + '/' + ruuid });

    // SEND
    sendMessage('ON');
  }
}

function unsubscribeRoom () {
  setSubscribeRoom(false);

  if (stompClient != null) {
    // UNSUBSCRIBE
    curChatRoom.unsubscribe();

    // SEND
    sendMessage('OFF');
  }
}

function sendTalkMessage () {
  sendMessage('TALK');
}

function sendMessage (type) {
  let content = '';

  if (type === 'TALK') {
    content = document.getElementById('text').value;
  } else if (type === 'OFF') {
    const contentObj = { last_read_idx: 5 }; // 현재 채팅방의 마지막 메시지 idx 넣을 것
    content = JSON.stringify(contentObj);
  } else if (type === 'ENTER') {
    const userList = { users: ['a', 'b', 'c'] };
    content = JSON.stringify(userList);
  }

  const newMessage = {
    type,
    midx: null,
    parent_id: 0,
    content,
    sender,
    uname,
    cname,
    pname,
    ruuid,
    device: 'web',
    created_at: '2021/01/26 14:08:31',
    deleted_at: ''
  };

  console.log(newMessage);

  // SEND
  stompClient.send('/pub/msg', {}, JSON.stringify(newMessage));
}

function setConnected (connected) {
  document.getElementById('connect').disabled = connected;
  document.getElementById('disconnect').disabled = !connected;
  document.getElementById('responseInChatRoomList').innerHTML = '';
}

function setSubscribeRoom (subscription) {
  document.getElementById('subscribe').disabled = subscription;
  document.getElementById('unsubscribe').disabled = !subscription;
  document.getElementById('chatRoomDiv').style.visibility = subscription ? 'visible' : 'hidden';
  document.getElementById('response').innerHTML = '';
}

function showChatRoomListRealTime (message) {
  console.log(message);
  const response = document.getElementById('responseInChatRoomList');
  const p = document.createElement('p');
  p.style.wordWrap = 'break-word';
  if (message.type !== 'ON' && message.type !== 'OFF') {
    p.appendChild(document.createTextNode(message.ruuid + ' : ' + message.sender + ' : ' + message.content));
  }
  response.appendChild(p);
}

function showMessageOutput (message) {
  console.log(message);
  const response = document.getElementById('response');
  const p = document.createElement('p');
  p.style.wordWrap = 'break-word';
  if (message.type !== 'ON' && message.type !== 'OFF') {
    p.appendChild(document.createTextNode(message.ruuid + ' : ' + message.sender + ' : ' + message.content));
  }
  response.appendChild(p);
}
