import {Component, OnDestroy, OnInit} from '@angular/core';
import {Users} from "../../core/models/gameResults";
import {Question} from "../../core/models/question";
import {Answer} from "../../core/models/answer";
import {ActivatedRoute, Router} from "@angular/router";
import {SecurityService} from "../../core/services/security.service";
import {PlayGameService} from "../../core/services/play-game.service";
import * as Stomp from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import {UserDto} from "../../core/models/userDto";
import {socket} from "../../../../environments/environment.prod";
import {EventType, WebsocketEvent} from "../../core/models/websocketEvent";
import {GameDto} from "../../core/models/gameDto";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit, OnDestroy {

  game: GameDto;
  question: Question;
  receivedQuestion: Question;
  players: String[] = [];
  private stompClient;
  public gameResults: Users;
  eventType = EventType;
  receivedEvent: WebsocketEvent;
  currQuestion: number;
  s: any;

  public currentUser: UserDto;
  public gameId: string = this.route.snapshot.paramMap.get('gameId');
  isWaiting: boolean;

  constructor(private route: ActivatedRoute,
              private redirect: Router,
              private securityService: SecurityService,
              private playGameService: PlayGameService) {
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(socket);
    this.stompClient = Stomp.Stomp.over(ws);
    let that = this;

    this.stompClient.connect({}, function () {
      that.s = that.stompClient.subscribe("/game/" + that.gameId + "/play", async (message) => {
        if (message.body) {
          that.receivedEvent = JSON.parse(message.body);
          if (that.receivedEvent.type == that.eventType.PLAYERS) {
            that.players = that.receivedEvent.players;
          }
          if (that.receivedEvent.type == that.eventType.QUESTION) {
            that.isWaiting = false;
            that.gameResults = null;
            that.receivedQuestion = that.receivedEvent.question;
            that.currQuestion = that.receivedEvent.currQuestion;
            if (that.question && that.receivedQuestion.id == that.question.id) that.question = that.receivedQuestion;
            else {
              if (that.question != null) localStorage.removeItem("endTime" + that.question.id);
              that.question = null;
              await sleep(500);
              that.question = that.receivedEvent.question;
            }
          }
          if (that.receivedEvent.type == that.eventType.RESULTS) {
            if (that.question != null) localStorage.removeItem("endTime" + that.question.id);
            that.isWaiting = false;
            that.question = null;
            that.gameResults = that.receivedEvent.gameResults;
            if (that.gameResults.isFinal) {
              localStorage.removeItem("playerId")
            }
          }
        }
      });
    }, this);
  }

  ngOnInit(): void {

    this.initializeWebSocketConnection();

    this.playGameService.getGame(this.gameId).subscribe(
      game => {
        this.game = game;
        this.isWaiting = true;
      }, err => {
        console.log(err);
        this.redirect.navigate(['home']);
      });

    let userId = this.securityService.getCurrentId();
    if (!userId) {
      userId = localStorage.getItem("playerId");
      if (!userId) this.connectUser(userId);
      else this.getCurrentData();
    } else {
      this.connectUser(userId);
    }
  }

  connectUser(userId: any) {
    this.playGameService.sendJoinedUser(userId ? userId : 0, this.gameId).subscribe(
      user => {
        this.currentUser = user;
        if (!this.securityService.getCurrentId() && !localStorage.getItem("playerId"))
          localStorage.setItem("playerId", String(this.currentUser.id));
        if (this.players.length == 0) this.players.push(this.currentUser.login);
        this.getCurrentData();

      }, err => {
        console.log(err);
        this.redirect.navigate(['home']);
      });
  }

  getCurrentData() {
    this.playGameService.getJoinedPlayers(this.gameId).subscribe(
      players => {
        if (players)
          this.players = players;
      }, err => {
        console.log(err);
        this.redirect.navigate(['home']);
      }
    );
    this.playGameService.getCurrentQuestion(this.gameId, this.currentUser.id).subscribe(
      question => {
        if (question) {
          this.isWaiting = false;
          this.gameResults = null;
          this.question = question;
        }
      }, err => {
        console.log(err);
        this.redirect.navigate(['home']);
      }
    );
  }

  sendAnswer(answer: Answer) {
    answer.gameId = this.gameId;
    answer.userId = this.currentUser.id;
    this.stompClient.send("/game/" + this.gameId + "/play", {}, JSON.stringify(answer));
  }

  startGame() {
    this.isWaiting = false;
    this.stompClient.send("/game/" + this.gameId + "/start", {}, "start game");
  }

  ngOnDestroy(): void {
    localStorage.removeItem("endTime" + this.question.id);
    localStorage.removeItem("playerId");
    this.s.unsubscribe();
    this.stompClient.disconnect();
  }

}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
