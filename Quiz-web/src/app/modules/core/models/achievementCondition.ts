export class AchievementCondition {

  characteristicId: number;
  value: number;
  operator: ConditionOperator;

}

export class AchievementCharacteristic {

  id: number;
  name: string;

}

export enum ConditionOperator {
  LESS="<",
  EQUALS="=",
  GREATER=">"
}
