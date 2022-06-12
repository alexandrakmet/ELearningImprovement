import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-spin-cat',
  templateUrl: './spin-cat.component.html',
  styleUrls: ['./spin-cat.component.css']
})
export class SpinCatComponent implements OnInit {

  @Input()
  public show: boolean;

  @Input()
  public text: string;

  constructor() {
  }

  ngOnInit(): void {
  }

}
