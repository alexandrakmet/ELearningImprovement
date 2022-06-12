import {Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef} from '@angular/core';
import { TagFilter } from './tag-filter.model';
import { FBFilterComponent } from '../fb-filter-component.interface';
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {map, startWith} from "rxjs/operators";
import {FormControl} from "@angular/forms";
import {Observable} from "rxjs";

@Component({
  selector: 'app-tag-filter',
  templateUrl: './tag-filter.component.html'
})
export class TagFilterComponent implements OnInit, FBFilterComponent {

  @Input()
  public model: TagFilter

  @Output()
  public change: EventEmitter<string[]> = new EventEmitter();


  @ViewChild('tagInput') tagInput: ElementRef<HTMLInputElement>;

  tagCtrl = new FormControl();
  filteredTags: Observable<string[]>;

  constructor() {

    this.filteredTags = this.tagCtrl.valueChanges.pipe(
      startWith(null),
      map((tag: string | null) => tag ? this._filter(tag) : this.model.options.slice()));
  }

  ngOnInit() {
  }
/*
  public checkChanged(item: string, event: Event, checked: boolean): void {
    let index = this.model.filterValue.indexOf(item)
    if(checked) {
      if(index === -1)
         this.model.filterValue.push(item)
    } else {
      this.model.filterValue.splice(index, 1)
    }
    event.stopPropagation()
     this.change.emit(this.model.filterValue)
  }

  onApply(items: string[]) {
    this.model.filterValue = items
    this.change.emit(this.model.filterValue)
  }*/


  remove(tag: string): void {
    const index = this.model.filterValue.indexOf(tag);

    if (index >= 0) {
      this.model.filterValue.splice(index, 1);
      this.change.emit(this.model.filterValue)
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    if (!this.model.filterValue.includes(event.option.viewValue)) {
      if (this.model.options.indexOf(event.option.viewValue)>=0)
      {
        this.model.filterValue.push(event.option.viewValue);
        console.log("push " + event.option.viewValue);
        this.tagInput.nativeElement.value = '';
        this.tagCtrl.setValue(null);
        this.change.emit(this.model.filterValue)
      }
    }
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.model.options.filter(
      tag => tag.toLowerCase().indexOf(filterValue) === 0
      && !this.model.filterValue.includes(tag));
  }

}
