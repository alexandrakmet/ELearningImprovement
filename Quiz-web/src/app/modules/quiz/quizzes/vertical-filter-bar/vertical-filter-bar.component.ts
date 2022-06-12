import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  ViewChildren,
  QueryList,
  ElementRef,
  ChangeDetectorRef
} from '@angular/core';
import {FBFilter} from './fb-filter.interface';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {TagFilter} from "./tag-filter/tag-filter.model";
import {DateFilter} from "./date-filter/date-filter.model";

@Component({
  selector: 'app-vertical-filter-bar',
  templateUrl: './vertical-filter-bar.component.html',
  styleUrls: ['./vertical-filter-bar.component.css']
})
export class VerticalFilterBarComponent implements OnInit {

  @Input()
  public filters: FBFilter[]

  public filterResults: any = {}

  @ViewChildren('filterPanel')
  public filterPanels: QueryList<ElementRef>
  public filterCollapsed: boolean[] = []

  @Output()
  public filterResultsChange: EventEmitter<any> = new EventEmitter()

  constructor(
    private changeDetector: ChangeDetectorRef,
    private route: ActivatedRoute,
    private router: Router,
  ) {
  }

  ngOnInit() {
    //Update filter Results and filter Values based on the queryParams
    this.route.queryParamMap.subscribe((paramsMap: Params) => {
      this.filters.forEach(filterModel => {
        if (filterModel.identifier == 'date') {
          let minParamFilter = paramsMap.params['minDate']
          let maxParamFilter = paramsMap.params['maxDate']
          if (minParamFilter && maxParamFilter) {
            filterModel.filterValue = [];
            filterModel.filterValue.push(minParamFilter);
            filterModel.filterValue.push(maxParamFilter);
            console.log("add");
            (<DateFilter>filterModel).update();
          } else {
            filterModel.resetFilterValue()
            this.filterResults['minDate'] = undefined
            this.filterResults['maxDate'] = undefined
          }
        } else {
          let paramFilter = paramsMap.params[filterModel.identifier]
          if (paramFilter) {
            if (Array.isArray(filterModel.filterValue)) {
              if (!Array.isArray(paramFilter)) {
                filterModel.filterValue = []
                filterModel.filterValue.push(paramFilter)
              } else {
                filterModel.filterValue = paramFilter.slice()
              }
            } else {
              filterModel.filterValue = paramFilter
            }
            this.filterResults[filterModel.identifier] = filterModel.filterValue
          } else {
            filterModel.resetFilterValue()
            this.filterResults[filterModel.identifier] = undefined
          }
        }
      })
    })
    //Initialize filter Collapsed array
    this.filterCollapsed.fill(false, 0, this.filterCollapsed.length)
  }

  onFilterChanged(identifier: string, filterValue: any): void {

    for (let filter of this.filters) {
      if (filter.identifier == identifier && identifier == 'tag') {
        filterValue = (<[]>filterValue).filter(value => (<TagFilter>filter).options.includes(value))
      }
    }
    //If value empty, set to undefined
    if (filterValue != undefined && filterValue.length == 0) {
      filterValue = undefined
    }
    if (identifier == 'date') {
      if (filterValue != undefined) {
        //update date
        this.filterResults['minDate'] = filterValue[0];
        this.filterResults['maxDate'] = filterValue[1];
      } else {
        this.filterResults['minDate'] = undefined;
        this.filterResults['maxDate'] = undefined;
      }
    } else {
      //Update filterResults
      this.filterResults[identifier] = filterValue
    }


    //Update query params
    this.router.navigate(['/quizzes'], {queryParams: this.filterResults})
    //Emits filterResultChange event
    this.filterResultsChange.emit(this.filterResults)
  }

  onHideFilterClick(i: number) {
    this.filterCollapsed[i] = !this.filterCollapsed[i];
  }
}
