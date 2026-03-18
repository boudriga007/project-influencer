import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInfluenceur, NewInfluenceur } from '../influenceur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInfluenceur for edit and NewInfluenceurFormGroupInput for create.
 */
type InfluenceurFormGroupInput = IInfluenceur | PartialWithRequiredKeyOf<NewInfluenceur>;

type InfluenceurFormDefaults = Pick<NewInfluenceur, 'id'>;

type InfluenceurFormGroupContent = {
  id: FormControl<IInfluenceur['id'] | NewInfluenceur['id']>;
  nom: FormControl<IInfluenceur['nom']>;
  username: FormControl<IInfluenceur['username']>;
  photoUrl: FormControl<IInfluenceur['photoUrl']>;
  bio: FormControl<IInfluenceur['bio']>;
  plateforme: FormControl<IInfluenceur['plateforme']>;
  categorie: FormControl<IInfluenceur['categorie']>;
  scoreGlobal: FormControl<IInfluenceur['scoreGlobal']>;
};

export type InfluenceurFormGroup = FormGroup<InfluenceurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InfluenceurFormService {
  createInfluenceurFormGroup(influenceur?: InfluenceurFormGroupInput): InfluenceurFormGroup {
    const influenceurRawValue = {
      ...this.getFormDefaults(),
      ...(influenceur ?? { id: null }),
    };
    return new FormGroup<InfluenceurFormGroupContent>({
      id: new FormControl(
        { value: influenceurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(influenceurRawValue.nom, {
        validators: [Validators.required],
      }),
      username: new FormControl(influenceurRawValue.username),
      photoUrl: new FormControl(influenceurRawValue.photoUrl),
      bio: new FormControl(influenceurRawValue.bio),
      plateforme: new FormControl(influenceurRawValue.plateforme),
      categorie: new FormControl(influenceurRawValue.categorie),
      scoreGlobal: new FormControl(influenceurRawValue.scoreGlobal),
    });
  }

  getInfluenceur(form: InfluenceurFormGroup): IInfluenceur | NewInfluenceur {
    return form.getRawValue() as IInfluenceur | NewInfluenceur;
  }

  resetForm(form: InfluenceurFormGroup, influenceur: InfluenceurFormGroupInput): void {
    const influenceurRawValue = { ...this.getFormDefaults(), ...influenceur };
    form.reset({
      ...influenceurRawValue,
      id: { value: influenceurRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InfluenceurFormDefaults {
    return {
      id: null,
    };
  }
}
