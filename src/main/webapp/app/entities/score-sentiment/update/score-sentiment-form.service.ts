import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IScoreSentiment, NewScoreSentiment } from '../score-sentiment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScoreSentiment for edit and NewScoreSentimentFormGroupInput for create.
 */
type ScoreSentimentFormGroupInput = IScoreSentiment | PartialWithRequiredKeyOf<NewScoreSentiment>;

type ScoreSentimentFormDefaults = Pick<NewScoreSentiment, 'id'>;

type ScoreSentimentFormGroupContent = {
  id: FormControl<IScoreSentiment['id'] | NewScoreSentiment['id']>;
  ratioPositif: FormControl<IScoreSentiment['ratioPositif']>;
  ratioNegatif: FormControl<IScoreSentiment['ratioNegatif']>;
  ratioNeutre: FormControl<IScoreSentiment['ratioNeutre']>;
  scoreSentiment: FormControl<IScoreSentiment['scoreSentiment']>;
  influenceur: FormControl<IScoreSentiment['influenceur']>;
};

export type ScoreSentimentFormGroup = FormGroup<ScoreSentimentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScoreSentimentFormService {
  createScoreSentimentFormGroup(scoreSentiment?: ScoreSentimentFormGroupInput): ScoreSentimentFormGroup {
    const scoreSentimentRawValue = {
      ...this.getFormDefaults(),
      ...(scoreSentiment ?? { id: null }),
    };
    return new FormGroup<ScoreSentimentFormGroupContent>({
      id: new FormControl(
        { value: scoreSentimentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ratioPositif: new FormControl(scoreSentimentRawValue.ratioPositif),
      ratioNegatif: new FormControl(scoreSentimentRawValue.ratioNegatif),
      ratioNeutre: new FormControl(scoreSentimentRawValue.ratioNeutre),
      scoreSentiment: new FormControl(scoreSentimentRawValue.scoreSentiment),
      influenceur: new FormControl(scoreSentimentRawValue.influenceur),
    });
  }

  getScoreSentiment(form: ScoreSentimentFormGroup): IScoreSentiment | NewScoreSentiment {
    return form.getRawValue() as IScoreSentiment | NewScoreSentiment;
  }

  resetForm(form: ScoreSentimentFormGroup, scoreSentiment: ScoreSentimentFormGroupInput): void {
    const scoreSentimentRawValue = { ...this.getFormDefaults(), ...scoreSentiment };
    form.reset({
      ...scoreSentimentRawValue,
      id: { value: scoreSentimentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ScoreSentimentFormDefaults {
    return {
      id: null,
    };
  }
}
